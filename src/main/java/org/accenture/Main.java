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
        String token;
        String contractId;
        String tradeSymbol;
        int unitRequired;
        String destinationSymbol;
        String systemSymbol;
        String shipSymbol;

        RegisterNewAgentResponse registerNewAgentData = registerNewAgent();
        token               = registerNewAgentData.getToken();
        contractId          = registerNewAgentData.getContract().getId();
        tradeSymbol         = registerNewAgentData.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        unitRequired        = registerNewAgentData.getContract().getTerms().getDeliver()[0].getUnitsRequired();
        destinationSymbol   = registerNewAgentData.getContract().getTerms().getDeliver()[0].getDestinationSymbol();
        systemSymbol        = registerNewAgentData.getAgent().getHeadquarters().split("-")[0] + "-" + registerNewAgentData.getAgent().getHeadquarters().split("-")[1];
        shipSymbol          = registerNewAgentData.getShip().getSymbol();
        printVarRegisterNewAgent(token, contractId, tradeSymbol,unitRequired, destinationSymbol, systemSymbol,shipSymbol,true);


        /*
        AcceptContractResponse acceptContract;
        acceptContract = acceptContract(registerNewAgent.getToken(), registerNewAgent.getContract().getId());
        printDataAcceptContract(acceptContract);

         */
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
        return data;
    }
    private static void printVarRegisterNewAgent(   String tokenData,
                                                    String contractIdData,
                                                    String tradeSymbolData,
                                                    int unitRequiredData,
                                                    String destinationSymbolData,
                                                    String systemSymbolData,
                                                    String shipSymbolData,
                                                    boolean enable){
        if(enable) {
            System.out.print("token:           ");
            System.out.println(tokenData);

            System.out.println("contract");
            System.out.print("                 Id:                 ");
            System.out.println(contractIdData);
            System.out.print("                 Trade Symbol:       ");
            System.out.println(tradeSymbolData);
            System.out.print("                 Unit Required:      ");
            System.out.println(unitRequiredData);
            System.out.print("                 Destination Symbol: ");
            System.out.println(destinationSymbolData);

            //data.getAgent().getHeadquarters()): Ubicacion de donde esta, es un string de 3 componentes separados por "-": sector-sytem-ubicacion
            System.out.print("system symbol:   ");
            System.out.println(systemSymbolData);

            System.out.print("ship symbol:     ");
            System.out.println(shipSymbolData);
        }
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