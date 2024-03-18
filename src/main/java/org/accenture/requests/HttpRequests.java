package org.accenture.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.accenture.entities.Contract;
import org.accenture.entities.Ship;
import org.accenture.entities.responses.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.accenture.Constant.*;

@Log
public class HttpRequests {

    private ObjectMapper objectMapper = new ObjectMapper();

    private List<Contract> contracts = new ArrayList<>();

    public void registerNewAgent() {
        HttpResponse<String> response = Unirest.post(REGISTER_NEW_AGENT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"SHEPERD\"}")
                .asString();

        System.out.println(response.getBody());
    }

    public void getContractsList() throws JsonProcessingException {
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
            contracts = Stream.of(data).map(contract -> objectMapper.convertValue(node, Contract.class))
                    .collect(Collectors.toList());
        }

        System.out.println(contracts);

    }

    public void acceptContract() {
        HttpResponse<String> response = Unirest.post(ACCEPT_CONTRACT)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .asString();

        System.out.println(response.getBody());
    }

    public void listWaypointsInSystem() {
        HttpResponse<String> response = Unirest.get(LIST_WAYPOINTS_IN_SYSTEM)
                .header("Accept", "application/json")
                .queryString("type", "ENGINEERED_ASTEROID")
                .asString();

        System.out.println(response.getBody());
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

    /*
    public void moveShipToOrbit() throws JsonProcessingException {
        Ship ship = getShip();

        HttpResponse<String> response = Unirest.post(ORBIT_SHIP)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .routeParam("shipSymbol", ship.getSymbol())
                .asString();

        log.info(response.getBody());

    }

     */
}
