package org.accenture.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.HashMap;
import java.util.Map;

public class HttpRequests {
    private Map<String, String> getContractsResponse = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoiU0hFUEVSRCIsInZlcnNpb24iOiJ2Mi4yLjAiLCJyZXNldF9kYXRlIjoiMjAyNC0wMy0xMCIsImlhdCI6MTcxMDQzNzg5OCwic3ViIjoiYWdlbnQtdG9rZW4ifQ.fxhJt-AuBZwn_gbMDj8Kuy96MqIP868eOw6fpzFv-Kr2Osj76Ax8vVT2sk3NowrP1V7k3l0Qrdz6P0DJn1tpBtxuiT3CF0GWOJa6SBpUueEEIuRVRpTqJeApvjkshhVlZ-Tmda3sZA45wS4bMq9QOTxmvF9xbUgn8XXiT2Y_qlq9RNh7r7AP8tZ5RbJRc1eW7V7BrJKD1FCGdNi24-JqlFBuSnj03_yP0nxy6CW2qUX_uMj0hiFka41zH7t5IqJREEWNe7Li36yif7L9aWHae7DtSs4F3QX4W3j29C1UppNYFVGO_tS_99Zg6izYrVGx2mXe00Bsjig10NcMrEPGqA";
    private String REGISTER_NEW_AGENT = "https://api.spacetraders.io/v2/register";
    private String LIST_CONTRACTS = "https://api.spacetraders.io/v2/my/contracts";
    private String ACCEPT_CONTRACT = "https://api.spacetraders.io/v2/my/contracts/cltrik96j09pts60cht8jnjqg/accept";
    private String LIST_WAYPOINTS_IN_SYSTEM = "https://api.spacetraders.io/v2/systems/X1-AF98/waypoints";
    private String LIST_SHIP = "https://api.spacetraders.io/v2/my/ships";
    private String GET_SHIP = "https://api.spacetraders.io/v2/my/ships/shipSymbol";


    public void registerNewAgent() {
        HttpResponse<String> response = Unirest.post(REGISTER_NEW_AGENT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"SHEPERD\"}")
                .asString();

        System.out.println(response.getBody());
    }

    public void getContracts() throws JsonProcessingException {
        HttpResponse<String> response = Unirest.get(LIST_CONTRACTS)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        getContractsResponse = objectMapper.readValue(response.getBody(), HashMap.class);
        System.out.println(getContractsResponse);
    }

    public void acceptContract() {
        HttpResponse<String> response = Unirest.post(ACCEPT_CONTRACT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        System.out.println(response.getBody());
    }

    public void listWaypointsInSystem() {
        HttpResponse<String> response = Unirest.get(LIST_WAYPOINTS_IN_SYSTEM)
                .header("Accept", "application/json")
                .queryString("type", "ENGINEERED_ASTEROID")
                .asString();

        System.out.println(response.getBody());
    }

    public void listShips() {
        HttpResponse<String> response = Unirest.get(LIST_SHIP)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        System.out.println(response.getBody());
    }

    public void getShip() {
        HttpResponse<String> response = Unirest.get(LIST_SHIP)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        System.out.println(response.getBody());
    }
}
