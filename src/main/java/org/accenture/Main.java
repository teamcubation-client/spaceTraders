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


public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        System.out.println("//INITIALIZING SYSTEM");
        System.out.println("//SYSTEM INITIALIZED");
        System.out.println("'STARFIELD AT HOME'\n");

        String agentUserName = "TQ" + (int) (Math.random() * 1000000);

        //REGISTER NEW AGENT
        System.out.println("//INITIALIZING REGISTRATION");
        HttpResponse<String> responseRegisterAgent = Unirest.post("https://api.spacetraders.io/v2/register")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentUserName + "\"\n}")
                .asString();


        ResponseBody bodyRegisterAgent = mapper.readValue(responseRegisterAgent.getBody(), ResponseBody.class);
        if (bodyRegisterAgent.getError() != null) {
            System.out.println(bodyRegisterAgent.getError().getMessage());
            return;
        }
        System.out.println("//REGISTRATION SUCCESSFUL");
        RegisterNewAgentResponse dataRegisterAgent = mapper.convertValue(bodyRegisterAgent.getData(), RegisterNewAgentResponse.class);

        String agentID = dataRegisterAgent.getAgent().getAccountId();
        String faction = dataRegisterAgent.getFaction().getName();
        String token = dataRegisterAgent.getToken();
        String contractID = dataRegisterAgent.getContract().getId();
        String contractTradeSymbol = dataRegisterAgent.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        int contractUnitsRequested = dataRegisterAgent.getContract().getTerms().getDeliver()[0].getUnitsRequired();
        String contractDestinationSymbol = dataRegisterAgent.getContract().getTerms().getDeliver()[0].getDestinationSymbol();
        String headquarters = dataRegisterAgent.getAgent().getHeadquarters();
        String[] arrOfStr = headquarters.split("-");
        String systemSymbol = arrOfStr[0]+"-"+arrOfStr[1];
        //String systemSymbol = dataRegisterAgent.getShip().getNav().getSystemSymbol();
        String shipSymbol = dataRegisterAgent.getShip().getSymbol();

        System.out.println("AGENT: "+agentID);
        System.out.println("HEADQUARTERS: "+headquarters);
        System.out.println("FACTION: "+faction);
        System.out.println("CONTRACT: "+contractID+"\n");
        System.out.println("MATERIAL REQUESTED: "+contractTradeSymbol);
        System.out.println("UNITS REQUESTED: "+contractUnitsRequested);
        System.out.println("CONTRACT DESTINATION: "+contractDestinationSymbol);
        System.out.println("SYSTEM SYMBOL: "+systemSymbol);
        System.out.println("SHIP SYMBOL: "+shipSymbol+"\n");


        //ACCEPT CONTRACT
        System.out.println("//VERIFYING CONTRACT");
        System.out.println("//VERIFICATION COMPLETED");
        System.out.println("//ACCEPTING CONTRACT");
        HttpResponse<String> responseAcceptContract = Unirest.post("https://api.spacetraders.io/v2/my/contracts/" + contractID + "/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        ResponseBody bodyAcceptContract = mapper.readValue(responseAcceptContract.getBody(), ResponseBody.class);
        if (bodyAcceptContract.getError() != null) {
            System.out.println(bodyAcceptContract.getError().getMessage());
            return;
        }

        AcceptContractResponse dataAcceptContract = mapper.convertValue(bodyAcceptContract.getData(), AcceptContractResponse.class);
        if (dataAcceptContract.getContract().isAccepted()){
            System.out.println("CONTRACT "+ dataAcceptContract.getContract().getId() +" ACCEPTED.\n");
        }


        //LIST FILTERED WAYPOINTS
        HttpResponse<String> responseListWaypoints = Unirest.get("https://api.spacetraders.io/v2/systems/{systemSymbol}/waypoints")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .queryString("type","ENGINEERED_ASTEROID")
                .routeParam("systemSymbol",systemSymbol)
                .asString();

        ResponseBody bodyListWaypoints = mapper.readValue(responseListWaypoints.getBody(), ResponseBody.class);
        if (bodyListWaypoints.getError() != null) {
            System.out.println(bodyListWaypoints.getError().getMessage());
            return;
        }

        System.out.println("//LOOKING FOR WAYPOINTS");
        System.out.println("//WAYPOINTS FOUND");
        System.out.println("//FILTERING WAYPOINTS BY 'ENGINEERED_ASTEROID'");
        System.out.println("//ENGINEERED_ASTEROID(S) FOUND");
        for(JsonNode waypoint : bodyListWaypoints.getData()){
            ListWaypointsResponse dataListWaypoints = mapper.convertValue(waypoint, ListWaypointsResponse.class);
            String asteroidType = dataListWaypoints.getType();
            String asteroidSymbol = dataListWaypoints.getSymbol();
            System.out.println("ASTEROID TYPE: "+asteroidType);
            System.out.println("ASTEROID SYMBOL: "+asteroidSymbol+"\n");
        }

        //TO DO STEP 4

    }
}