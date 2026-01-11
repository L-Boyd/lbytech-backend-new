package com.lbytech.lbytech_backend_new.es;

public interface EsConstant {

    String CREATE_INDEX_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"fileName\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"content\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"file_url\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"thumbCount\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"createTime\": {\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"updateTime\": {\n" +
            "        \"type\": \"date\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
