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

        String agentName = "Cami" + (int) (Math.random() * 1000000);

        Unirest.config().defaultBaseUrl("https://api.spacetraders.io/v2");

//registerNewAgent
        HttpResponse<String> response = Unirest.post("/register")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                    .asString();


        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);

        System.out.println("Agent " + agentName + " registered");
        String token = "Bearer " + data.getToken();

        String contractId = String.valueOf(data.getContract().getId());
        System.out.println("Contract: " + contractId);

//        String tradeSymbol = data.getContract().getTerms().getDeliver()[0].getTradeSymbol();
//        String destinationSymbol = data.getContract().getTerms().getDeliver()[0].getDestinationSymbol();
//        int unitRequired = data.getContract().getTerms().getDeliver()[0].getUnitsRequired();
//        String shipSymbol = String.valueOf(data.getShip().getSymbol());
//        String systemSymbol = String.valueOf(data.getShip().getNav().getSystemSymbol());

//        System.out.println("Token: " + token +"\n" +
//                "Contract ID: " + contractId + "\n"+
//                "Trade Symbol: "+ tradeSymbol + "\n" +
//                "Unit Required: " + unitRequired+ "\n" +
//                "Destination Symbol: "+ destinationSymbol + "\n"+
//                "System Symbol: " + systemSymbol + "\n" +
//                "Ship Symbol: " + shipSymbol);

        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return;
        }



        //acceptContract
        HttpResponse<String> acceptContract = Unirest.post( "/my/contracts/{contractId}/accept")
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", token)
                    .routeParam("contractId", contractId)
                    .asString();

        ResponseBody bodyContract = mapper.readValue(acceptContract.getBody(),ResponseBody.class);
        AcceptContractResponse contractResponse = mapper.convertValue(bodyContract.getData(), AcceptContractResponse.class);

        if(contractResponse.getContract().isAccepted()){
            System.out.println("Contract " + contractId + " accepted");
        }

        if (body.getError() != null) {
            System.out.println(bodyContract.getError().getMessage());
        }
//

//        HttpResponse<String> listWaypointsInSystem = Unirest.get("/systems/systemSymbol/waypoints")
//                .header("Accept", "application/json")
//                .routeParam("systemSymbol", systemSymbol)
//                .asString();


//
//            }
//
//            public void orbitShip(String token, String shipSymbol){
//                HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/ships/{shipSymbol}/orbit")
//                        .header("Content-Type", "application/json")
//                        .header("Accept", "application/json")
//                        .header("Authorization", token)
//                        .routeParam("shipSymbol", shipSymbol)
//                        .asString();
//
//
//
            }


}