package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.responses.*;

import java.time.ZonedDateTime;
import java.util.Arrays;

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

        String contractId = data.getContract().getId();
        System.out.println("Contract: " + contractId);


//        String destinationSymbol = data.getContract().getTerms().getDeliver()[0].getDestinationSymbol();
        String systemSymbol = data.getShip().getNav().getSystemSymbol();
        String shipSymbol = data.getShip().getSymbol();


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


//List Waypoints in System
        if(contractResponse.getContract().isAccepted()){
            HttpResponse<String> listWaypointsInSystem = Unirest.get("/systems/{systemSymbol}/waypoints")
                    .header("Accept", "application/json")
                    .routeParam("systemSymbol", systemSymbol)
                    .queryString("type", "ENGINEERED_ASTEROID")
                    .asString();

            ResponseBody bodyListWaypoints = mapper.readValue(listWaypointsInSystem.getBody(), ResponseBody.class);
            for (JsonNode waypoint: bodyListWaypoints.getData()){
                ListWaypointsResponse dataListWaypoints = mapper.convertValue(waypoint, ListWaypointsResponse.class);
                String waypointSymbol = dataListWaypoints.getSymbol();

                System.out.println("Waypoint Symbol: " + waypointSymbol);

                //orbit Ship
                HttpResponse<String> bodyOrbitShip = Unirest.post("/my/ships/{shipSymbol}/orbit")
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", token)
                        .routeParam("shipSymbol", shipSymbol)
                        .asString();

                ResponseBody dataOrbit = mapper.readValue(bodyOrbitShip.getBody(), ResponseBody.class);
                OrbitShipResponse orbitResponse = mapper.convertValue(dataOrbit.getData(), OrbitShipResponse.class);



                //navigate ship
                HttpResponse<String> navigateShip = Unirest.post("/my/ships/{shipSymbol}/navigate")
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Authorization", token)
                        .routeParam("shipSymbol", shipSymbol)
                        .body("{\n  \"waypointSymbol\": \"" + waypointSymbol +"\"\n}")
                        .asString();

                ResponseBody bodyNavigateShip = mapper.readValue(navigateShip.getBody(),ResponseBody.class);
                NavigateShipResponse navigateResponse = mapper.convertValue(bodyNavigateShip.getData(), NavigateShipResponse.class);

                int consumedFuel = navigateResponse.getFuel().getConsumed().getAmount();
                ZonedDateTime arrivalTime = navigateResponse.getNav().getRoute().getArrival();

                if (bodyNavigateShip.getError() != null){
                    System.out.println(bodyNavigateShip.getError().getMessage());
                }

                System.out.println("Consumed fuel: " +
                        consumedFuel + "\n" +
                        "Arrival time: " +
                        arrivalTime);
                }







            if (bodyListWaypoints.getError() != null){
                System.out.println(bodyListWaypoints.getError().getMessage());
            }

        }

        if (bodyContract.getError() != null) {
            System.out.println(bodyContract.getError().getMessage());
        }





            }


}