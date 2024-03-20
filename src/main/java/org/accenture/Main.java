package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.accenture.entities.Deliver;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.requests.HttpRequests;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
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
        Boolean isContractAccepted = false;

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
        AcceptContractResponse acceptContractResponse = httpRequests.acceptContract(contractId);
        isContractAccepted = acceptContractResponse.getContract().isAccepted();

        if (isContractAccepted.equals(true)) {
            
        }
    }
}