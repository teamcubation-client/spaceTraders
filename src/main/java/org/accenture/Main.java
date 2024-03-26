package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Contract;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.AllResponses;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

//import static org.accenture.entities.responses.AllResponses.agentEndpoint;
import static org.accenture.entities.responses.AllResponses.*;


public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        RegisterNewAgentResponse registerNewAgentResponse = registerEndpoint();
        System.out.println("REGISTER NEW AGENT: " + registerNewAgentResponse);
        String token = registerNewAgentResponse.getToken();
        Contract contract = registerNewAgentResponse.getContract();
        String tradeSymbol = registerNewAgentResponse.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        String shipSymbol = registerNewAgentResponse.getShip().getSymbol();
        String systemSymbol = registerNewAgentResponse.getAgent().getHeadquarters();

        System.out.println(systemSymbol);
        systemSymbol = systemSymbol.substring(0, systemSymbol.lastIndexOf('-'));

        System.out.println(systemSymbol);
        if(acceptContract(token, contract.getId())) {
            System.out.println("CONTRACT ACCEPTED");

            String waypointSymbol = waypointsResponse(systemSymbol);
            System.out.println("LIST WAYPOINTS IN SYSTEM: " + waypointSymbol);

            //System.out.println("DOCK SHIP: " + AllResponses.dockEndpoint(shipSymbol, token));

            System.out.println("SHIP STATUS: " + orbitEndpoint(token, shipSymbol));

            navigateEndpoint(token, shipSymbol, waypointSymbol);
        }

    }
}