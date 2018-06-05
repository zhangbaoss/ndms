package nurteen.prometheus.pc.framework.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class JsonUtils {

    public static String toJSON(Object object) {
        try {
            return serializationObjectMapper().writeValueAsString(object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static <T> T fromJSON(String content, Class<T> type) {
        try {
            return deserializationObjectMapper().readValue(content, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T fromJSON(byte[] content, Class<T> type) {
        try {
            return deserializationObjectMapper().readValue(content, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateJSON(Object object, String json) {
        try {
            deserializationObjectMapper().readerForUpdating(object).readValue(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateJSON(Object object, byte[] content) {
        try {
            deserializationObjectMapper().readerForUpdating(object).readValue(content);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ObjectMapper serializationObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }
    private static ObjectMapper deserializationObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }
}
