package org.accenture.entities.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.*;

import java.util.Objects;

public class AllResponses {

    public static RegisterNewAgentResponse registerEndpoint() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        String agentName = "TQ" + (int) (Math.random() * 1000000);

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/register")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);

        return data;
    }

    public static Agent agentEndpoint(String token) throws JsonProcessingException {
        Agent agent = new Agent();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/my/agent")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+token)
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }
        Agent data = mapper.convertValue(body.getData(), Agent.class);

        return agent;
    }


    public static Boolean acceptContract(String token, String contractId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/contracts/" + contractId + "/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }
        AcceptContractResponse acceptContractResponse = mapper.convertValue(body.getData(), AcceptContractResponse.class);
        Contract contract = acceptContractResponse.getContract();

        return contract.isAccepted();
    }

    public static String waypointsResponse(String systemSymbol) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/systems/" + systemSymbol + "/waypoints?type=ENGINEERED_ASTEROID")
                .header("Accept", "application/json")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }
        String asteroidSymbol = "";
        for (JsonNode jn : body.getData()) {
            ListWaypointsResponse waypoints = mapper.convertValue(jn, ListWaypointsResponse.class);
                    asteroidSymbol = waypoints.getSymbol();
        }
        return asteroidSymbol;
    }

    public static Nav dockEndpoint(String shipSymbol, String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/ships/" + shipSymbol + "/dock")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+token)
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        Nav nav = mapper.convertValue(body.getData(), Nav.class);

        return nav;
    }

    public static int refuelEndpoint(String token, String shipSymbol) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/ships/" + shipSymbol + "/refuel")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+token)
                .body("{\n  \"units\": \"100\",\n  \"fromCargo\": false\n}")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        RefuelShipResponse refuelShipResponse = mapper.convertValue(body.getData(), RefuelShipResponse.class);

        Transaction transaction = refuelShipResponse.getTransaction();
        return transaction.getTotalPrice();
    }
}

