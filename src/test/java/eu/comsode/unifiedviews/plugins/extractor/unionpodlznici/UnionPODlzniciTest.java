package eu.comsode.unifiedviews.plugins.extractor.unionpodlznici;

import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;
import cz.cuni.mff.xrg.odcs.dpu.test.TestEnvironment;
import eu.unifiedviews.dataunit.files.WritableFilesDataUnit;
import eu.unifiedviews.helpers.dpu.test.config.ConfigurationBuilder;

public class UnionPODlzniciTest {

	@Test
	public void execute() throws Exception {

		// Prepare config.
		UnionPODlzniciConfig_V1 config = new UnionPODlzniciConfig_V1();

		UnionPODlznici dpu = new UnionPODlznici();
		// Prepare DPU.
		dpu.configure((new ConfigurationBuilder()).setDpuConfiguration(config)
				.toString());

		// Prepare test environment.
		TestEnvironment environment = new TestEnvironment();

        // Prepare data unit.
        WritableFilesDataUnit filesOutput = environment
                .createFilesOutput("fileOutput");
		try {
			// Run.
			environment.run(dpu);
		} finally {
			// Release resources.
			environment.release();
		}
        assertTrue(readCSV());
	}

    private boolean readCSV() {
        final String fileName = "union_po_dlznici.csv";
        final int COLUMNS = 7;
        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(fileName));
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length != COLUMNS) {
                    reader.close();
                    return false;
                }
            }
            reader.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
