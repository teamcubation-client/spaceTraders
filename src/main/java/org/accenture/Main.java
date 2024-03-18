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
        RegisterNewAgentResponse registerNewAgent;
        AcceptContractResponse acceptContract;

        registerNewAgent = registerNewAgent();
        printDataRegisterNewAgent(registerNewAgent);

        acceptContract = acceptContract(registerNewAgent.getToken(), registerNewAgent.getContract().getId());
        printDataAcceptContract(acceptContract);
    }


    private static RegisterNewAgentResponse registerNewAgent() throws JsonProcessingException{
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
            return null;
        }
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);
        System.out.println(data.getToken());

        return data;
    }

    private static void printDataRegisterNewAgent(RegisterNewAgentResponse data){
        System.out.println("token");
        System.out.println(data.getToken());

        System.out.println("contract");
        System.out.println(data.getContract().getId());
        System.out.println(data.getContract().getFactionSymbol());
        System.out.println(data.getContract().getType());
        System.out.println(data.getContract().getTerms());

        System.out.println("system symbol");
        System.out.println(data.getAgent().getSymbol());

        System.out.println("ship symbol");
        System.out.println(data.getShip().getSymbol());
    }
    private static AcceptContractResponse acceptContract(String token, String contractId) throws JsonProcessingException{
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
            return null;
        }
        AcceptContractResponse data = mapper.convertValue(body.getData(), AcceptContractResponse.class);
        return data;
    }
    private static void printDataAcceptContract(AcceptContractResponse data){
        System.out.print("AcceptContract: ");
        System.out.println(data.getContract().isAccepted());
    }
}