package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Agent;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

import static org.accenture.entities.responses.AllResponses.agentEndpoint;
import static org.accenture.entities.responses.AllResponses.registerEndpoint;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        RegisterNewAgentResponse registerNewAgentResponse = registerEndpoint();
        System.out.println(registerNewAgentResponse);
        String token = registerNewAgentResponse.getToken();

        Agent agentResult = agentEndpoint(token);
        System.out.println(agentResult);
    }
}