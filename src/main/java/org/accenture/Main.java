package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.accenture.entities.Deliver;
import org.accenture.entities.responses.*;
import org.accenture.requests.HttpRequests;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException {

        HttpRequests httpRequests = new HttpRequests();

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
        int currentFuel = 0;
        int consumedFuel = 0;
        int remainingFuel = 0;
        ZonedDateTime departureTime;
        ZonedDateTime arrivalTime;
        int price = 0;
        int units = 0;

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
            currentFuel = navigateResponse.getFuel().getCurrent();
            consumedFuel = navigateResponse.getFuel().getConsumed().getAmount();
            departureTime = navigateResponse.getNav().getRoute().getDepartureTime();
            arrivalTime = navigateResponse.getNav().getRoute().getArrival();
            remainingFuel = httpRequests.calculateFuel(currentFuel, consumedFuel);
            units = consumedFuel;
            System.out.println("Current fuel in ship: " + currentFuel);
            System.out.println("Total amount of fuel consumed: " + consumedFuel);
            System.out.println("Remaining fuel: " + remainingFuel);
            System.out.println("Departure time: " + departureTime);
            System.out.println("Arrival time: " + arrivalTime);

            System.out.println("Ship " + shipSymbol + " is currently in transit");
            Duration timeToArrive = httpRequests.waitingTime(departureTime, arrivalTime);
            Thread.sleep(timeToArrive.toMillis());

            System.out.println("Dock ship");
            NavigateShipResponse navigateShip = httpRequests.dockShip(token, shipSymbol);
            System.out.println("Ship successfully docked in " + navigateShip.getNav().getWaypointSymbol());

            System.out.println("Refuel ship");
            RefuelShipResponse refuelShipResponse = httpRequests.refuelShip(token, shipSymbol, units);
            price = refuelShipResponse.getTransaction().getPricePerUnit();
            currentFuel = refuelShipResponse.getFuel().getCurrent();
            System.out.println("Price per unit: " + price);
            System.out.println("Units of fuel purchased: " + units);
            System.out.println("Current fuel: " + currentFuel);


        } else {
            throw new RuntimeException("You must accept the contract before moving forward");
        }
    }
}