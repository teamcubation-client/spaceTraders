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
import static org.accenture.entities.responses.AllResponses.registerEndpoint;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        RegisterNewAgentResponse registerNewAgentResponse = registerEndpoint();
        //System.out.println(registerNewAgentResponse);
        String token = registerNewAgentResponse.getToken();
        Contract contract = registerNewAgentResponse.getContract();
        String contractId = registerNewAgentResponse.getContract().getId();
        System.out.println("Token: " + token);
        System.out.println("Contract: " + contract);
        System.out.println("Ship symbol: " + registerNewAgentResponse.getShip().getSymbol());
        System.out.println("System symbol:" + registerNewAgentResponse.getShip().getNav().getSystemSymbol());
        System.out.println("Agent headquarter: " + registerNewAgentResponse.getAgent().getHeadquarters());

        AcceptContractResponse acceptContractResponse = AllResponses.acceptContract(token);
        Contract responseContract = acceptContractResponse.getContract();
        boolean checkAccepted = responseContract.isAccepted();
        System.out.println(checkAccepted);

    }
}