package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

public class TelegramSetupRunnable implements Runnable {

    private final String endpoint;
    private final String token;
    private final WSClient ws;

    private final String webhookEndpoint;

    public TelegramSetupRunnable(Config config, WSClient ws) {
        this.ws = ws;
        this.endpoint = config.getString("telegram.url");
        this.token = config.getString("telegram.token");
        this.webhookEndpoint = config.getString("telegram.webhookEndpoint");
    }

    @Override
    public void run() {

        // Setup Webhook

        JsonNode body = Json.newObject()
                .put("url", webhookEndpoint)
                .put("max_connections", 1);

        ws.url(endpoint + token + "/setWebhook")
                .post(body)
                .thenApply(this::webhookResponse);

    }

    private Boolean webhookResponse(WSResponse wsResponse) {



        return true;
    }
}
