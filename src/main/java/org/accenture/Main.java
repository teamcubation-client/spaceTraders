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
    private static String systemSymbol;

    public static void main(String[] args) throws JsonProcessingException {
        RegisterNewAgentResponse registerNewAgent;
        AcceptContractResponse acceptContract;

        registerNewAgent = registerNewAgent();
        Main.systemSymbol = registerNewAgent.getAgent().getHeadquarters().split("-")[0] + "-" + registerNewAgent.getAgent().getHeadquarters().split("-")[1];
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
        //System.out.println(data.getToken());

        return data;
    }

    private static void printDataRegisterNewAgent(RegisterNewAgentResponse data){
        System.out.print("token:           ");
        System.out.println(data.getToken());

        System.out.println("contract");
        System.out.print("                 Id:                 ");
        System.out.println(data.getContract().getId());
        System.out.print("                 Trade Symbol:       ");
        System.out.println(data.getContract().getTerms().getDeliver()[0].getTradeSymbol());
        System.out.print("                 Unit Required:      ");
        System.out.println(data.getContract().getTerms().getDeliver()[0].getUnitsRequired());
        System.out.print("                 Destination Symbol: ");
        System.out.println(data.getContract().getTerms().getDeliver()[0].getDestinationSymbol());

        //data.getAgent().getHeadquarters()): Ubicacion de donde esta, es un string de 3 componentes separados por "-": sector-sytem-ubicacion
        System.out.print("system symbol:   ");
        System.out.println(Main.systemSymbol);

        System.out.print("ship symbol:     ");
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
        System.out.print("AcceptContract:  ");
        System.out.println(data.getContract().isAccepted());
    }
}