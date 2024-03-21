package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.accenture.entities.Contract;
import org.accenture.entities.Nav;
import org.accenture.entities.responses.AllResponses;
import org.accenture.entities.responses.RegisterNewAgentResponse;

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
        System.out.println("ACCEPT CONTRACT: " + acceptEndpoint(token, contract.getId()));

        System.out.println("LIST WAYPOINTS IN SYSTEM: " + waypointEndpoint(systemSymbol));

        Nav nav = AllResponses.orbitEndpoint(token, shipSymbol);
        System.out.println(nav.getWaypointSymbol());
    }
}