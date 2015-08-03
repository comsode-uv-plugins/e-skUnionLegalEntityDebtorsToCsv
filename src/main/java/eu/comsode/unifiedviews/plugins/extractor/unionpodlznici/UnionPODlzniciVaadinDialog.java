package eu.comsode.unifiedviews.plugins.extractor.unionpodlznici;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

public class UnionPODlzniciVaadinDialog extends AbstractDialog<UnionPODlzniciConfig_V1> {

    /**
     * 
     */
    private static final long serialVersionUID = -1539890500190668002L;

    public UnionPODlzniciVaadinDialog() {
        super(UnionPODlznici.class);
    }

    @Override
    protected void buildDialogLayout() {
    }

    @Override
    protected void setConfiguration(UnionPODlzniciConfig_V1 c) throws DPUConfigException {
    }

    @Override
    protected UnionPODlzniciConfig_V1 getConfiguration() throws DPUConfigException {

        final UnionPODlzniciConfig_V1 cnf = new UnionPODlzniciConfig_V1();
        return cnf;
    }

}
