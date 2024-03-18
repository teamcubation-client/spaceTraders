package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.accenture.requests.HttpRequests;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        HttpRequests httpRequests = new HttpRequests();

        //httpRequests.registerNewAgent();
        //httpRequests.getContractsList();
        //httpRequests.acceptContract();
        //httpRequests.listWaypointsInSystem();
        //httpRequests.listShips();
        httpRequests.getShip("SHEPERD-1");
        //httpRequests.moveShipToOrbit();


        /*
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
            return;
        }
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);
        System.out.println(data.getToken());
         */
    }
}