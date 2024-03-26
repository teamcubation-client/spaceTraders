package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Point;
import org.accenture.entities.responses.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.ZonedDateTime;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {
        String token;
        String contractId;
        String tradeSymbol;
        int unitRequired;
        String destinationSymbol;
        String systemSymbol;
        String shipSymbol;
        String asteroidSymbol;
        int consumedFuel;
        ZonedDateTime arrivalTime;


        RegisterNewAgentResponse registerNewAgentData = registerNewAgent();
        token = registerNewAgentData.getToken();
        contractId = registerNewAgentData.getContract().getId();
        tradeSymbol = registerNewAgentData.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        unitRequired = registerNewAgentData.getContract().getTerms().getDeliver()[0].getUnitsRequired();
        destinationSymbol = registerNewAgentData.getContract().getTerms().getDeliver()[0].getDestinationSymbol();
        systemSymbol = registerNewAgentData.getAgent().getHeadquarters().split("-")[0] + "-" + registerNewAgentData.getAgent().getHeadquarters().split("-")[1];
        shipSymbol = registerNewAgentData.getShip().getSymbol();
        printVarRegisterNewAgent(token, contractId, tradeSymbol, unitRequired, destinationSymbol, systemSymbol, shipSymbol, true);

        if(acceptContract(token, contractId)){
            List<ListWaypointsResponse> listWaypointsInSystemData = listWaypointsInSystem(systemSymbol);
            asteroidSymbol = listWaypointsInSystemData.get(0).getSymbol();
            printDataListWaypoints(asteroidSymbol);

            if(orbitShip(token, shipSymbol)){
                System.out.println("Ship in Orbit");
                NavigateShipResponse navigateShipData = navigateShip(token, shipSymbol, asteroidSymbol);
                consumedFuel = navigateShipData.getFuel().getConsumed().getAmount();
                arrivalTime = navigateShipData.getNav().getRoute().getArrival();
                printDatanavigateShip(consumedFuel, arrivalTime);
            }
        }
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
    private static boolean acceptContract(String token, String contractId) throws JsonProcessingException{
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
            return false;
        }
        AcceptContractResponse data = mapper.convertValue(body.getData(), AcceptContractResponse.class);

        System.out.print("AcceptContract:  ");
        System.out.println(data.getContract().isAccepted());
        return data.getContract().isAccepted();
    }

    private static List<ListWaypointsResponse> listWaypointsInSystem(String systemSymbol) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/systems/" + systemSymbol + "/waypoints?type=ENGINEERED_ASTEROID")
                .header("Accept", "application/json")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }
        List<ListWaypointsResponse> data = new ArrayList<ListWaypointsResponse>();
        for(JsonNode waypoint: body.getData() ){
            data.add(mapper.convertValue(waypoint, ListWaypointsResponse.class));
        }

        return data;
    }

    private static void printDataListWaypoints(String asteroidSymbolData){
        System.out.print("Asteroid Symbol: ");
        System.out.println(asteroidSymbolData);
    }

    private static boolean orbitShip(String token, String shipSymbol) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/ships/" + shipSymbol + "/orbit")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return false;
        }
        return true;
    }

    private static NavigateShipResponse navigateShip(String token, String shipSymbol, String asteroidSymbol) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/ships/" + shipSymbol + "/navigate")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .body("{\n  \"waypointSymbol\": \"" + asteroidSymbol + "\"\n}")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }

        NavigateShipResponse data = mapper.convertValue(body.getData(), NavigateShipResponse.class);
        return data;
    }

    private static void printDatanavigateShip(int consumedFuelData, ZonedDateTime arrivalTimeData ){
        System.out.print("Consumed fuel:   ");
        System.out.println(consumedFuelData);

        System.out.print("Arrival time:    ");
        System.out.println(arrivalTimeData);
    }


}