package zs.library.externalApis;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import zs.library.exceptions.InvalidEmailException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.util.Timeout;
import org.json.JSONObject;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class CliqApiManager {
    
    private static final String OAUTH_URL = "https://accounts.zoho.com/oauth/v2/token";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static CliqApiManager instance = null;
    
    private static final String CLIENT_ID = "1000.S94KKZEDIZ3WKGIBSX3SS5YJPU3MEM";
    private static final String CLIENT_SECRET = "1bb61c8a937970c23598ab4d504bd8ce741ffa309d";
    private static final String REFRESH_TOKEN = "1000.ff760ab0ec189efaf24576dafed393a1.b140b975ddeccd72ff9cc7225b97022c";
    private static final String REDIRECT_URI = "http://localhost:8080";

    private CliqApiManager() { }

    public static CliqApiManager getInstance() {
        if (instance == null) {
            instance = new CliqApiManager();
        }
        return instance;
    }

    public String getAccessToken() throws IOException, ParseException, InterruptedException {
    	 String requestUrl = "https://accounts.zoho.com/oauth/v2/token";

         String requestBody = "grant_type=refresh_token"
                 + "&scope=ZohoCliq.Webhooks.CREATE"
                 + "&client_id=1000.S94KKZEDIZ3WKGIBSX3SS5YJPU3MEM"
                 + "&client_secret=1bb61c8a937970c23598ab4d504bd8ce741ffa309d"
                 + "&redirect_uri=" + URLEncoder.encode("http://localhost:8080", StandardCharsets.UTF_8)
                 + "&refresh_token=1000.55df90d2a9db1033c0d0a6827ac783dd.5ae18937394edc2009f698c694960145";

         HttpRequest request = HttpRequest.newBuilder()
                 .uri(URI.create(requestUrl))
                 .header("Content-Type", "application/x-www-form-urlencoded") // Important for form data
                 .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                 .build();

         HttpClient client = HttpClient.newHttpClient();
         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() == 200) {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(response.body());
            return jsonNode.get("access_token").asText();
        } else {
            throw new RuntimeException("Failed to get access token: " + response.body());
        }
    }

    public boolean sendMessageToZohoCliq(String userMail, String message) throws IOException, ParseException, InvalidEmailException, InterruptedException {
        String accessToken = getAccessToken();
        return sendMessageToZohoCliq(accessToken, userMail, message);
    }

    public boolean sendMessageToZohoCliq(String accessToken, String userMail, String message) throws IOException, ParseException, InvalidEmailException {
        String url = "https://cliq.zoho.com/api/v2/bots/zlib/message";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Authorization", "Zoho-oauthtoken " + accessToken);
            post.setHeader("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("text", message);
            json.put("userids", userMail);
            json.put("sync_message", true);
           
            post.setEntity(new StringEntity(json.toString(), ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse httpResponse = httpClient.execute(post)) {
                int responseCode = httpResponse.getCode();
                if (responseCode == 200 || responseCode == 204) {
                    return true;
                } else if (responseCode == 400) {
                    throw new InvalidEmailException("Invalid Email.");
                } else {
                    System.out.println("Failed to send message. Response Code: " + responseCode);
                    return false;
                }
            }
        }
    }
}
