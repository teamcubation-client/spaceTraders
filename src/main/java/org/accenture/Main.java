package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.ListWaypointsResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;
import org.accenture.exceptions.ContractDeclinedException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        String agentName = "TQ" + (int) (Math.random() * 1000000);

        //New agent
        RegisterNewAgentResponse data = getRegisterNewAgentResponse(mapper, agentName);
        if (data == null) return;
        ResponseBody body;

        //Accept contract
        AcceptContractResponse dataAcceptContractResponse = getAcceptContractResponse(mapper, agentName, data);
        if (dataAcceptContractResponse == null) return;
        if(!dataAcceptContractResponse.getContract().isAccepted()){
            throw new ContractDeclinedException();
        }

        String[] headquarter = dataAcceptContractResponse.getAgent().getHeadquarters().split("-");
        var headquarterPart = headquarter[0] + "-" + headquarter[1];

        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/systems/{systemSymbol}/waypoints")
                .routeParam("systemSymbol", headquarterPart)
                .queryString("type", "ENGINEERED_ASTEROID")
                .asString();

        body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return;
        }
        JsonNode nodes = body.getData();
        List<ListWaypointsResponse> listOfWayPoints = new ArrayList();
        for(JsonNode jnode: nodes){
            ListWaypointsResponse listWaypointsResponse = mapper.convertValue(jnode,ListWaypointsResponse.class);
            listOfWayPoints.add(listWaypointsResponse);
        }
        System.out.println("Primer Lista de traits - tamaño: "+listOfWayPoints.get(0).getTraits().length);

    }

    private static AcceptContractResponse getAcceptContractResponse(ObjectMapper mapper, String agentName, RegisterNewAgentResponse data) throws JsonProcessingException {
        ResponseBody body;
        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/contracts/{contractId}/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+ data.getToken())
                .routeParam("contractId", data.getContract().getId())
                .asString();

        body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }
        AcceptContractResponse dataAcceptContractResponse = mapper.convertValue(body.getData(), AcceptContractResponse.class);
        System.out.println("Contract accepted: " + dataAcceptContractResponse.getContract().isAccepted());
        return dataAcceptContractResponse;
    }

    private static RegisterNewAgentResponse getRegisterNewAgentResponse(ObjectMapper mapper, String agentName) throws JsonProcessingException {
        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/register")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return null;
        }
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);
        System.out.println("Token: "+data.getToken());
        System.out.println("Contract Id:"+data.getContract().getId());
        System.out.println("Trade Symbol:"+data.getContract().getTerms().getDeliver()[0].getTradeSymbol());
        System.out.println("Unit Requires:"+data.getContract().getTerms().getDeliver()[0].getUnitsRequired());
        System.out.println("Destination Symbol:"+data.getContract().getTerms().getDeliver()[0].getDestinationSymbol());
        System.out.println("Ship Symbol:"+data.getShip().getSymbol());
        return data;
    }
}