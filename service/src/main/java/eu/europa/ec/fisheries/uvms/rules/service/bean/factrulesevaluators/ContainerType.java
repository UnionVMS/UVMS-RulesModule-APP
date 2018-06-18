package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators;

public enum ContainerType {

    FA_REPORT("faReport","ec.europa.eu.faResport"),
    FA_QUERY("faQuery","ec.europa.eu.faQuery"),
    FA_RESPONSE("faResponse","ec.europa.eu.faResponse"),
    SALES("sales","ec.europa.eu.sales");


    private String packageName;
    private String containerName;

    ContainerType(String containerName, String packageName){
        this.containerName = containerName;
        this.packageName  = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getContainerName() {
        return containerName;
    }
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
}
