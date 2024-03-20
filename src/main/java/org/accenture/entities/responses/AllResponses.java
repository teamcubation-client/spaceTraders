package org.accenture.entities.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.*;

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


    /*
    public static Trait.Symbol waypointsResponse(String systemSymbol) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/systems/" + systemSymbol + "waypoints?type=ENGINEERED_ASTEROID")
                .header("Accept", "application/json")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }
        ListWaypointsResponse waypoints = mapper.convertValue(body.getData(), ListWaypointsResponse.class);
        Trait trait = new Trait();
        for(Trait t : waypoints.getTraits()) {
            if(t.getSymbol().name() == "ASTEROID") {
                trait = t;
            }
        }
        return trait.getSymbol();
    }*/
}
