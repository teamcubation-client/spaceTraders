package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.responses.*;
import org.accenture.exceptions.ContractDeclinedException;
import org.accenture.exceptions.OrbitShipException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        String agentName = "TQ" + (int) (Math.random() * 1000000);

        //New agent
        RegisterNewAgentResponse data = getRegisterNewAgentResponse(mapper, agentName);
        if (data == null) return;
        ResponseBody body;

        //Accept contract
        AcceptContractResponse dataAcceptContractResponse = getAcceptContractResponse(mapper, agentName, data);
        if (dataAcceptContractResponse == null) return;
        if(!dataAcceptContractResponse.getContract().isAccepted()){
            throw new ContractDeclinedException();
        }

        String[] headquarter = dataAcceptContractResponse.getAgent().getHeadquarters().split("-");
        var headquarterPart = headquarter[0] + "-" + headquarter[1];

        //Get ListWayPoints
        List<ListWaypointsResponse> listOfWayPoints = getListWaypointsResponses(mapper, headquarterPart);
        if (listOfWayPoints == null) return;

        //Orbit ship & Navigate Ship
        validateOrbitShip(mapper, data);
        NavigateShipResponse navigateShipResponse = navigateShip(mapper, data, listOfWayPoints);


    }

    private static NavigateShipResponse navigateShip(ObjectMapper mapper, RegisterNewAgentResponse data, List<ListWaypointsResponse> listOfWayPoints) throws JsonProcessingException {
        ResponseBody body;
        HttpResponse<String> responsee = Unirest.post("https://api.spacetraders.io/v2" +
                        "/my/ships/{shipSymbol}/navigate")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+ data.getToken())
                .routeParam("shipSymbol", data.getShip().getSymbol())
                .body("{\n \"waypointSymbol\": \"" + listOfWayPoints.get(0).getSymbol() + "\"}")
                .asString();


        body = mapper.readValue(responsee.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }
        NavigateShipResponse navigateShipResponse = mapper.convertValue(body.getData(), NavigateShipResponse.class);
        System.out.println("-- NAVIGATE SHIP --");
        System.out.println("Consumed Fuel: " + navigateShipResponse.getFuel().getConsumed().getAmount());
        System.out.println("Arrival Time: " + navigateShipResponse.getNav().getRoute().getArrival());
        return navigateShipResponse;
    }

    private static void validateOrbitShip(ObjectMapper mapper, RegisterNewAgentResponse data) throws JsonProcessingException {
        ResponseBody body;
        HttpResponse<String> responseOrbitShip = Unirest.post("https://api.spacetraders.io/v2" +
                        "/my/ships/{shipSymbol}/orbit")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+ data.getToken())
                .routeParam("shipSymbol", data.getShip().getSymbol())
                .asString();
        body = mapper.readValue(responseOrbitShip.getBody(), ResponseBody.class);
        if (mapper.readValue(responseOrbitShip.getBody(), ResponseBody.class).getError() != null) {
            System.out.println(body.getError().getMessage());
            throw new OrbitShipException();
        }
    }

    private static List<ListWaypointsResponse> getListWaypointsResponses(ObjectMapper mapper, String headquarterPart) throws JsonProcessingException {
        ResponseBody body;
        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/systems/{systemSymbol}/waypoints")
                .routeParam("systemSymbol", headquarterPart)
                .queryString("type", "ENGINEERED_ASTEROID")
                .asString();

        body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }
        JsonNode nodes = body.getData();
        List<ListWaypointsResponse> listOfWayPoints = new ArrayList();
        for(JsonNode jnode: nodes){
            ListWaypointsResponse listWaypointsResponse = mapper.convertValue(jnode,ListWaypointsResponse.class);
            listOfWayPoints.add(listWaypointsResponse);
        }
        System.out.println("Asteroid symbol: "+listOfWayPoints.get(0).getSymbol());
        return listOfWayPoints;
    }

    private static AcceptContractResponse getAcceptContractResponse(ObjectMapper mapper, String agentName, RegisterNewAgentResponse data) throws JsonProcessingException {
        ResponseBody body;
        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/contracts/{contractId}/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+ data.getToken())
                .routeParam("contractId", data.getContract().getId())
                .asString();

        body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }
        AcceptContractResponse dataAcceptContractResponse = mapper.convertValue(body.getData(), AcceptContractResponse.class);
        System.out.println("Contract accepted: " + dataAcceptContractResponse.getContract().isAccepted());
        return dataAcceptContractResponse;
    }

    private static RegisterNewAgentResponse getRegisterNewAgentResponse(ObjectMapper mapper, String agentName) throws JsonProcessingException {
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
        System.out.println("Token: "+data.getToken());
        System.out.println("Contract Id:"+data.getContract().getId());
        System.out.println("Trade Symbol:"+data.getContract().getTerms().getDeliver()[0].getTradeSymbol());
        System.out.println("Unit Requires:"+data.getContract().getTerms().getDeliver()[0].getUnitsRequired());
        System.out.println("Destination Symbol:"+data.getContract().getTerms().getDeliver()[0].getDestinationSymbol());
        System.out.println("System Symbol:"+data.getShip().getNav().getSystemSymbol());
        System.out.println("Ship Symbol:"+data.getShip().getSymbol());
        return data;
    }
}