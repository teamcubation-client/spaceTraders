package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Agent;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.ListWaypointsResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;
import org.apache.http.util.Asserts;

import javax.xml.crypto.Data;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;


public class Main {


    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        System.out.print("//INITIALIZING SYSTEM");
        sleep();
        System.out.println("//SYSTEM INITIALIZED");
        Thread.sleep(500);
        System.out.println("'STARFIELD AT HOME'\n");
        Thread.sleep(500);

        String agentUserName = "TQ" + (int) (Math.random() * 1000000);

        //REGISTER NEW AGENT
        System.out.print("//INITIALIZING REGISTRATION");
        sleep();
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
        Thread.sleep(500);
        RegisterNewAgentResponse dataRegisterAgent = mapper.convertValue(bodyRegisterAgent.getData(), RegisterNewAgentResponse.class);
        System.out.println("AGENT: "+dataRegisterAgent.getAgent().getAccountId());
        System.out.println("HEADQUARTERS: "+dataRegisterAgent.getAgent().getHeadquarters());
        System.out.println("FACTION: "+dataRegisterAgent.getFaction().getName()+"\n");
        Thread.sleep(500);

        String token = dataRegisterAgent.getToken();
        String contractID = dataRegisterAgent.getContract().getId();
        String contractTradeSymbol = dataRegisterAgent.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        String contractUnitRequested = dataRegisterAgent.getContract().getId();
        String contractDestinationSymbol = dataRegisterAgent.getContract().getId();
        String systemSymbol = dataRegisterAgent.getShip().getNav().getSystemSymbol();
        String shipSymbol = dataRegisterAgent.getShip().getSymbol();


        //ACCEPT CONTRACT
        System.out.print("//LOOKING FOR CONTRACT");
        sleep();
        System.out.println("//CONTRACT FOUND");
        Thread.sleep(500);
        System.out.print("//ACCEPTING CONTRACT");
        sleep();
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
        if (responseAcceptContract.getStatus() == 200){
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

        System.out.print("//LOOKING FOR WAYPOINTS.");
        sleep();
        System.out.println("//WAYPOINTS FOUND");
        Thread.sleep(500);
        System.out.print("//FILTERING WAYPOINTS BY 'ENGINEERED_ASTEROID'.");
        sleep();
        System.out.println("//ENGINEERED_ASTEROID(S) FOUND");
        Thread.sleep(500);
        for(JsonNode waypoint : bodyListWaypoints.getData()){
            ListWaypointsResponse dataListWaypoints = mapper.convertValue(waypoint, ListWaypointsResponse.class);
            System.out.println("ASTEROID TYPE: "+dataListWaypoints.getType());
            System.out.println("ASTEROID SYMBOL: "+dataListWaypoints.getSymbol()+"\n");
        }

        //TO DO STEP 4

    }

    public static void sleep() throws InterruptedException {
        Thread.sleep(200);
        System.out.print(".");
        Thread.sleep(200);
        System.out.print(".");
        Thread.sleep(200);
        System.out.println(".");
        Thread.sleep(500);
        System.out.print(".");
        Thread.sleep(200);
        System.out.print(".");
        Thread.sleep(200);
        System.out.print(".\r");
        Thread.sleep(200);
        System.out.print(" ");
        Thread.sleep(500);
        System.out.print("\r.");
        Thread.sleep(200);
        System.out.print(".");
        Thread.sleep(200);
        System.out.println(".\r");
        Thread.sleep(500);
    }
}