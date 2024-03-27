package org.accenture.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.java.Log;
import org.accenture.Mapper;
import org.accenture.entities.Contract;
import org.accenture.entities.Ship;
import org.accenture.entities.responses.*;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.accenture.Constant.*;

@Log
public class HttpRequests {
    private List<Contract> contracts = new ArrayList<>();

    private Mapper mapper = new Mapper();

    String token = "";

    public RegisterNewAgentResponse registerNewAgent(String agentName) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.post(REGISTER_NEW_AGENT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                .asString();

        ResponseBody body = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        log.info("Agent " + body.getData());

        return mapper.getMapper().convertValue(body.getData(), RegisterNewAgentResponse.class);

    }

    public List<Contract> getContractsList(String token) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.get(LIST_CONTRACTS)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        ResponseBody data = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        for (JsonNode node : data.getData()) {
            Contract contract = mapper.getMapper().convertValue(node, Contract.class);
            contracts.add(contract);
        }

        log.info("Contract list: " + contracts);
        return contracts;
    }

    public Contract getContract(String token, String contractId) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.get(GET_CONTRACT)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .routeParam("contractId", contractId)
                .asString();

        ResponseBody data = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }
        Contract contract = mapper.getMapper().convertValue(data.getData(), Contract.class);

        log.info("Contract " + contract);
        return contract;
    }

    public AcceptContractResponse acceptContract(String token, String contractId) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.post(ACCEPT_CONTRACT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .routeParam("contractId", contractId)
                .asString();

        ResponseBody data = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        return mapper.getMapper().convertValue(data.getData(), AcceptContractResponse.class);
    }

    public List<ListWaypointsResponse> listWaypointsInSystem(String systemSymbol) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.get(LIST_WAYPOINTS_IN_SYSTEM)
                .header("Accept", "application/json")
                .routeParam("systemSymbol", systemSymbol)
                .queryString("type", "ENGINEERED_ASTEROID")
                .asString();

        ResponseBody responseBody = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (responseBody.getError() != null) {
            System.out.println(responseBody.getError().getMessage());
        }

        List<ListWaypointsResponse> responses = new ArrayList<>();

        for (JsonNode node : responseBody.getData()) {
            ListWaypointsResponse waypointsResponse = mapper.getMapper()
                    .convertValue(node, ListWaypointsResponse.class);
            responses.add(waypointsResponse);
        }

        return responses;
    }

    public List<Ship> listShips(String token) throws JsonProcessingException {

        List<Ship> ships = new ArrayList<>();

        HttpResponse<String> response = Unirest.get(LIST_SHIP)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        ResponseBody data = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        for (JsonNode node : data.getData()) {
            Ship ship = mapper.getMapper().convertValue(node, Ship.class);
            ships.add(ship);
        }
        log.info("Ship list: " + ships);
        return ships;
    }

    public Ship getShip(String token, String shipSymbol) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.get(GET_SHIP)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .routeParam("shipSymbol", shipSymbol)
                .asString();

        ResponseBody data = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        Ship ship = mapper.getMapper().convertValue(data.getData(), Ship.class);
        log.info("Ship data: " + ship);

        return ship;
    }

    public NavigateShipResponse moveShipToOrbit(String token, String shipSymbol) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.post(ORBIT_SHIP)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .routeParam("shipSymbol", shipSymbol)
                .asString();

        ResponseBody body = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        return mapper.getMapper().convertValue(body.getData(), NavigateShipResponse.class);

    }

    public NavigateShipResponse navigateShip(String token, String shipSymbol, String waypointSymbol) throws JsonProcessingException {

            HttpResponse<String> response = Unirest.post(NAVIGATE_SHIP)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .routeParam("shipSymbol", shipSymbol)
                    .body("{\n  \"waypointSymbol\": \"" + waypointSymbol + "\"\n}")
                    .asString();

            ResponseBody body = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
            if (body.getError() != null) {
                System.out.println(body.getError().getMessage());
            }

            NavigateShipResponse navigateShipResponse = mapper.getMapper()
                    .convertValue(body.getData(), NavigateShipResponse.class);

        log.info("Navigate ship response: " + navigateShipResponse);
        return navigateShipResponse;

    }

    public NavigateShipResponse dockShip(String token, String shipSymbol) throws JsonProcessingException {

        HttpResponse<String> response = Unirest.post(DOCK_SHIP)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .routeParam("shipSymbol", shipSymbol)
                .asString();

        ResponseBody body = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        return mapper.getMapper().convertValue(body.getData(), NavigateShipResponse.class);
    }

    public RefuelShipResponse refuelShip(String token, String shipSymbol, int units) throws JsonProcessingException {
        HttpResponse<String> response = Unirest.post(REFUEL_SHIP)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .routeParam("shipSymbol", shipSymbol)
                .body("{\n  \"units\": \"" + units + "\",\n  \"fromCargo\": false\n}")
                .asString();

        ResponseBody body = mapper.getMapper().readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        return mapper.getMapper().convertValue(body.getData(), RefuelShipResponse.class);


    }

    public int calculateFuel(int currentFuel, int consumedFuel) {
        return currentFuel - consumedFuel;
    }

    public Duration waitingTime(ZonedDateTime departureTime, ZonedDateTime arrivalTime) {
        return Duration.between(departureTime, arrivalTime);
    }
}
