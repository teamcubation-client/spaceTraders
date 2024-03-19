package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.AllResponses;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

//import static org.accenture.entities.responses.AllResponses.agentEndpoint;
import static org.accenture.entities.responses.AllResponses.acceptContract;
import static org.accenture.entities.responses.AllResponses.registerEndpoint;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        RegisterNewAgentResponse registerNewAgentResponse = registerEndpoint();
        System.out.println("REGISTER NEW AGENT: " + registerNewAgentResponse);
        String token = registerNewAgentResponse.getToken();
        Contract contract = registerNewAgentResponse.getContract();
        String tradeSymbol = registerNewAgentResponse.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        String shipSymbol = registerNewAgentResponse.getShip().getSymbol();
        String systemSymbol = registerNewAgentResponse.getAgent().getHeadquarters();
        systemSymbol = systemSymbol.substring(0, systemSymbol.length() - 3);

        System.out.println("System symbol: " + systemSymbol);

        boolean checkAccept = acceptContract(token);
        System.out.println("ACCEPT CONTRACT: " + checkAccept);

    }
}