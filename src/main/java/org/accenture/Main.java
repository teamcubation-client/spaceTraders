package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Contract;
import org.accenture.entities.responses.*;

//import static org.accenture.entities.responses.AllResponses.agentEndpoint;
import java.time.ZonedDateTime;

import static org.accenture.entities.responses.AllResponses.*;


public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        RegisterNewAgentResponse registerNewAgentResponse = registerEndpoint();
        System.out.println("REGISTER NEW AGENT: " + registerNewAgentResponse);
        String token = registerNewAgentResponse.getToken();
        Contract contract = registerNewAgentResponse.getContract();
        String shipSymbol = registerNewAgentResponse.getShip().getSymbol();
        String systemSymbol = registerNewAgentResponse.getAgent().getHeadquarters();

        System.out.println(systemSymbol);
        systemSymbol = systemSymbol.substring(0, systemSymbol.lastIndexOf('-'));

        System.out.println(systemSymbol);
        if(acceptContract(token, contract.getId())) {
            System.out.println("CONTRACT ACCEPTED");

            String waypointSymbol = waypointsResponse(systemSymbol);
            System.out.println("LIST WAYPOINTS IN SYSTEM: " + waypointSymbol);

            System.out.println("SHIP STATUS: " + orbitEndpoint(token, shipSymbol));

            NavigateShipResponse navigateShipResponse = navigateEndpoint(token, shipSymbol, waypointSymbol);
            ZonedDateTime arrivalTime = navigateShipResponse.getNav().getRoute().getArrival();
            System.out.println("ARRIVAL TIME: " + arrivalTime.toString());

            String shipStatus = String.valueOf(navigateShipResponse.getNav().getStatus());
            int consumed = navigateShipResponse.getFuel().getConsumed().getAmount();
            String arrival = String.valueOf(navigateShipResponse.getNav().getRoute().getArrival());
            System.out.println("CONSUMED FUEL: "+ consumed + ", ARRIVAL TIME: " + arrival);

            int fuelToLoad = navigateShipResponse.getFuel().getConsumed().getAmount();
            //System.out.println("SHIP STATUS: " + getShipStatusEndpoint(token, shipSymbol));
            System.out.println("TOTAL PRICE: " + refuelEndpoint(token, shipSymbol, fuelToLoad, arrivalTime));

        }

    }
}