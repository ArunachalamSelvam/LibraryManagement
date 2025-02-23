package zs.library.utils;

import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtDecoder {
    public static String decodeJwt(String jwt) throws JsonProcessingException {
        // Split JWT into parts
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT format");
        }

        // Decode Base64 URL Safe
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert payload JSON string to Object
        JsonNode jsonNode = objectMapper.readTree(payloadJson);

        // Convert JSON to formatted string
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }
}