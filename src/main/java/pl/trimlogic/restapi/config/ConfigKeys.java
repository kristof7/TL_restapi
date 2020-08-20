package pl.trimlogic.restapi.config;

public class ConfigKeys {

    public static final String CE_URI = "http://192.168.1.140:9080/wsi/FNCEWS40MTOM";
    public static final String OBJECT_STORE_NAME = "ArchNSPR";
    public static final String STANZA_NAME = "FileNetP8WSI";

    public static class Filenet {
        static final String PREFIX = "filenet.ce";
        public static final String URL = PREFIX + ".url";
        public static final String STANZA_NAME = PREFIX + ".stanzaName";
    }


    public static class Systems {
        public static final String PREFIX = "filenet.systems";

        public static class Attributes {
            public static String ENABLED = "enabled";
            public static String NAME = "name";
            public static String OBJECT_STORE_NAME = "objectStore";
            public static String DOCUMENTS = "documents";

        }
    }
}