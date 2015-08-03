package eu.comsode.unifiedviews.plugins.extractor.unionpodlznici;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.comsode.unifiedviews.plugins.extractor.service.Tabula;
import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.DataUnitException;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;

@DPU.AsExtractor
public class UnionPODlznici extends AbstractDpu<UnionPODlzniciConfig_V1> {
	private static final Logger LOG = LoggerFactory
			.getLogger(UnionPODlznici.class);

	private final static String INPUT_URL = "http://www.union.sk/zoznam-neplaticov-pravnicke-osoby";

	@DataUnit.AsOutput(name = "fileOutput")
	public WritableFilesDataUnit fileOutput;

	public UnionPODlznici() {
		super(UnionPODlzniciVaadinDialog.class, ConfigHistory
				.noHistory(UnionPODlzniciConfig_V1.class));
	}

	@Override
	protected void innerExecute() throws DPUException {
		File outputDirectory = null;
		String outputVirtualPath = "union_po_dlznici.csv";
		File outputFile = null;
		ctx.getExecMasterContext().getDpuContext().getWorkingDir();
		try {
			outputDirectory = new File(URI.create(fileOutput
					.getBaseFileURIString()));
			outputFile = File.createTempFile("____",
					FilenameUtils.getExtension(outputVirtualPath),
					outputDirectory);
		} catch (DataUnitException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

			HttpGet httpGet = new HttpGet(INPUT_URL);
			httpGet.setHeader("Accept", "*/*");
			httpGet.setHeader("Host", "www.union.sk");
			httpGet.setHeader("User-Agent", "curl/7.33.0");

			CloseableHttpResponse response1 = httpclient.execute(httpGet);

			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("GET response status line: %s",
						response1.getStatusLine()));
			}
			String response = null;
			int responseCode = response1.getStatusLine().getStatusCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				LOG.error("GET request not worked");
				throw new Exception("GET request not worked");
			}
			HttpEntity entity = null;
			try {
				entity = response1.getEntity();
				response = EntityUtils.toString(entity, "UTF-8");
			} finally {
				EntityUtils.consumeQuietly(entity);
				response1.close();
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Server response:\n%s", response));
			}
			Document rootDoc = Jsoup.parse(response);
			for (Element el : rootDoc.select("a[href]")) {
				if (el.attr("href").endsWith("Zoznam_dlznikov_PO")) {
					URL url = new URL(INPUT_URL);
					URIBuilder linkBuilder = new URIBuilder();
					linkBuilder.setScheme(url.getProtocol())
							.setHost(url.getHost()).setPath(el.attr("href"));
					HttpGet httpget = new HttpGet(linkBuilder.build());
					response1 = httpclient.execute(httpget);
					entity = response1.getEntity();
					if (entity != null) {
						InputStream inputStream = entity.getContent();
                        FileUtils.copyInputStreamToFile(inputStream, outputFile);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		new Tabula().runTabula(outputFile.getAbsolutePath());
	}
}