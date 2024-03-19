package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;
import org.accenture.entities.Ship;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;
import org.accenture.requests.HttpRequests;

import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        HttpRequests httpRequests = new HttpRequests();

        RegisterNewAgentResponse newAgent = httpRequests.registerNewAgent("prueba");
        System.out.println("TOKEN" + " " + newAgent.getToken());
        String systemSymbol = newAgent.getAgent().getHeadquarters().substring(0, 7);
        System.out.println("systemSymbol " + systemSymbol);



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