package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.accenture.entities.Deliver;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.ListWaypointsResponse;
import org.accenture.entities.responses.NavigateShipResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.requests.HttpRequests;

import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {

        HttpRequests httpRequests = new HttpRequests();
        /*

        String agentName = "AG" + (int) (Math.random() * 1000000);

        System.out.println("Register new Agent");
        RegisterNewAgentResponse newAgent = httpRequests.registerNewAgent(agentName);
        String systemSymbol = newAgent.getAgent().getHeadquarters().substring(0, 7);
        String contractId = newAgent.getContract().getId();
        String token = newAgent.getToken();
        String tradeSymbol = "";
        int unitsRequired = 0;
        String destinationSymbol = "";
        String shipSymbol = newAgent.getShip().getSymbol();
        Boolean isContractAccepted;
        String waypointSymbol = "";
        String shipStatus = "";

        Deliver deliver[] = newAgent.getContract().getTerms().getDeliver();
        for (Deliver deliver1 : deliver) {
            tradeSymbol = deliver1.getTradeSymbol();
            unitsRequired = deliver1.getUnitsRequired();
            destinationSymbol = deliver1.getDestinationSymbol();
        }

        System.out.println("token " + token);
        System.out.println("systemSymbol " + systemSymbol);
        System.out.println("contractId: " + contractId);
        System.out.println("tradeSymbol " + tradeSymbol);
        System.out.println("unitsRequired " + unitsRequired);
        System.out.println("destinationSymbol " + destinationSymbol);
        System.out.println("shipSymbols " + shipSymbol);

        System.out.println("Accept contract");
        if (newAgent.getContract().isAccepted()) {
            throw new RuntimeException("Contract with id " + contractId + " was already accepted");
        } else {
            AcceptContractResponse acceptContractResponse = httpRequests.acceptContract(token, contractId);
            isContractAccepted = acceptContractResponse.getContract().isAccepted();
            System.out.println("Contract accepted: " + isContractAccepted);
        }


        if (isContractAccepted.equals(true)) {
            System.out.println("List waypoints in System");
            List<ListWaypointsResponse> waypointsResponses = httpRequests.listWaypointsInSystem(systemSymbol);
            waypointSymbol = waypointsResponses.get(0).getSymbol();
            System.out.println("Asteroid waypointSymbol " + waypointSymbol);

            System.out.println("Navigate ship to orbit");
            NavigateShipResponse navigateShipResponse = httpRequests.moveShipToOrbit(token, shipSymbol);
            shipStatus = String.valueOf(navigateShipResponse.getNav().getStatus());
            System.out.println(shipStatus);

            System.out.println("Navigate ship to a waypoint");
            NavigateShipResponse navigateResponse = httpRequests.navigateShip(token, shipSymbol, waypointSymbol);

        } else {
            throw new RuntimeException("You must accept the contract before moving forward");
        }

         */

        int currentFuel = 0;
        int consumedFuel = 0;
        double remainingFuel = 0;

        System.out.println("Navigate ship to a waypoint");
        NavigateShipResponse navigateResponse = httpRequests.navigateShip("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoiQUc5NzU0ODUiLCJ2ZXJzaW9uIjoidjIuMi4wIiwicmVzZXRfZGF0ZSI6IjIwMjQtMDMtMjQiLCJpYXQiOjE3MTEzODA4OTEsInN1YiI6ImFnZW50LXRva2VuIn0.KOO3LCENCFrqfkoDJigYAcd26ci8l8vGuknlyMGl52x8b6_bBaGdTaAa1I_onPZW801SJgk9JubPXC4eOH6vPjtH7LwXO5U7Mh5lZMna7gXtgsA5wKwPdvp5P_J57uzXV0ID0RT63Lzjsf4RRom0M4bApEwmNyZfGp82spAACvVK4fWpTQipbpgBcikqxN10H683rtCk0NJ3x0itsIyxlEIdN66D1PczMPolQEimIiduS6_W1CpLRut_2tWTr-uuQinw5g9QD13Lnl4b-H9UD6JnEXis1x17LYT48aD-vaUrIxZKBAX9jiV4Yg_fmdQiT9UWQN8swVzNIwL1G0zxdw"
                , "AG975485-1", "X1-PK40-BC5E");

        currentFuel = navigateResponse.getFuel().getCurrent();
        consumedFuel = navigateResponse.getFuel().getConsumed().getAmount();
        remainingFuel = httpRequests.calculateFuel(currentFuel, consumedFuel);
        System.out.println("Current fuel in ship: " + currentFuel);
        System.out.println("Total amount of fuel consumed: " + consumedFuel);
        System.out.println("Remaining fuel: " + remainingFuel);
    }
}