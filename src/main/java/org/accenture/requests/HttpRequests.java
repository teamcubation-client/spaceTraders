package org.accenture.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.java.Log;
import org.accenture.entities.Contract;
import org.accenture.entities.Ship;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.accenture.Constant.*;

@Log
public class HttpRequests {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Contract> contracts = new ArrayList<>();

    public RegisterNewAgentResponse registerNewAgent(String agentName) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.post(REGISTER_NEW_AGENT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                .asString();

        ResponseBody body = objectMapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
        }

        log.info("Agent " + body.getData());

        return objectMapper.convertValue(body.getData(), RegisterNewAgentResponse.class);

    }

    public List<Contract> getContractsList() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get(LIST_CONTRACTS)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        ResponseBody data = objectMapper.readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        for (JsonNode node : data.getData()) {
            Contract contract = objectMapper.convertValue(node, Contract.class);
            contracts.add(contract);
        }

        log.info("Contract list: " + contracts);
        return contracts;
    }

    public Contract getContract(String contractId) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get(GET_CONTRACT)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .routeParam("contractId", contractId)
                .asString();

        ResponseBody data = objectMapper.readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }
        Contract contract = objectMapper.convertValue(data.getData(), Contract.class);

        log.info("Contract " + contract);
        return contract;
    }

    public void acceptContract(String contractId) {

        HttpResponse<String> response = Unirest.post(ACCEPT_CONTRACT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .routeParam("contractId", contractId)
                .asString();

        System.out.println(response.getBody());
    }

    public void listWaypointsInSystem(String systemSymbol) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get(LIST_WAYPOINTS_IN_SYSTEM)
                .header("Accept", "application/json")
                .routeParam("systemSymbol", systemSymbol)
                .queryString("type", "ENGINEERED_ASTEROID")
                .asString();

        ResponseBody responseBody = objectMapper.readValue(response.getBody(), ResponseBody.class);
        if (responseBody.getError() != null) {
            System.out.println(responseBody.getError().getMessage());
        }

        log.info(responseBody.getData().toString());
    }

    public List<Ship> listShips() throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        List<Ship> ships = new ArrayList<>();

        HttpResponse<String> response = Unirest.get(LIST_SHIP)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        ResponseBody data = objectMapper.readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        for (JsonNode node : data.getData()) {
            Ship ship = objectMapper.convertValue(node, Ship.class);
            ships.add(ship);
        }
        log.info("Ship list: " + ships);
        return ships;
    }

    public Ship getShip(String shipSymbol) throws JsonProcessingException {

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        HttpResponse<String> response = Unirest.get(GET_SHIP)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .routeParam("shipSymbol", shipSymbol)
                .asString();

        ResponseBody data = objectMapper.readValue(response.getBody(), ResponseBody.class);
        if (data.getError() != null) {
            System.out.println(data.getError().getMessage());
        }

        Ship ship = objectMapper.convertValue(data.getData(), Ship.class);
        log.info("Ship data: " + ship);

        return ship;
    }

    public void moveShipToOrbit(String shipSymbol) {

        HttpResponse<String> response = Unirest.post(ORBIT_SHIP)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .routeParam("shipSymbol", shipSymbol)
                .asString();

        log.info(response.getBody());

    }

}
