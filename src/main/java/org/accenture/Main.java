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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        String agentName = "TQ" + (int) (Math.random() * 1000000);

        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/register")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                .asString();

        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return;
        }
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);
        System.out.println(data.getToken());

        //Aceptar contrato
        response = Unirest.post("https://api.spacetraders.io/v2/my/contracts/{contractId}/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+data.getToken())
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
                .routeParam("contractId", data.getContract().getId())
                .asString();

        body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            System.out.println(body.getError().getMessage());
            return;
        }
        AcceptContractResponse dataAcceptContractResponse = mapper.convertValue(body.getData(), AcceptContractResponse.class);
        System.out.println(dataAcceptContractResponse.getContract().getId());

        String[] headquarter = dataAcceptContractResponse.getAgent().getHeadquarters().split("-");
        var headquarterPart = headquarter[0] + "-" + headquarter[1];

        response = Unirest.get("https://api.spacetraders.io/v2/systems/{systemSymbol}/waypoints")
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
}