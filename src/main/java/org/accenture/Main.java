package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        String agentName = "TQ" + (int) (Math.random() * 1000000);

//registerNewAgent
        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/register")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                    .asString();


        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);
        System.out.println("Agent " + agentName + " registered");
        String token = "Bearer" + data.getToken();
        String contractId = String.valueOf(data.getContract().getId());
        String shipSymbol = String.valueOf(data.getShip().getSymbol());

        //acceptContract
        HttpResponse<String> acceptContract = Unirest.post("https://api.spacetraders.io/v2/my/contracts/" + contractId + "/accept")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", token)
                    .asString();

        ResponseBody bodyContract = mapper.readValue(acceptContract.getBody(),ResponseBody.class);
        AcceptContractResponse contractResponse = mapper.convertValue(bodyContract.getData(), AcceptContractResponse.class);
        System.out.println("Contract " + contractId + " accepted");


        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return;
        }





            }


}