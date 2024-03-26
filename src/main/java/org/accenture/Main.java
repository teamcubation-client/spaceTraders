package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Contract;
import org.accenture.entities.Terms;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.ListWaypointsResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        //REGISTER NEW AGENT (STEP 1)
        System.out.println("RUNNING REGISTRATION PROCESS...");
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
        System.out.println("The user has been successfully registered under name: "+agentName+"\n");
        RegisterNewAgentResponse data = mapper.convertValue(body.getData(), RegisterNewAgentResponse.class);

        String agentToken = data.getToken();
        String contractId = data.getContract().getId();
        String contractTradeSymbol = data.getContract().getTerms().getDeliver()[0].getTradeSymbol();
        int contractUnitsRequired = data.getContract().getTerms().getDeliver()[0].getUnitsRequired();
        String contractDestinationSymbol = data.getContract().getTerms().getDeliver()[0].getDestinationSymbol();
        String systemSymbol = data.getShip().getNav().getSystemSymbol();
        String shipSymbol = data.getShip().getSymbol();

        //ACCEPT A CONTRACT (STEP 2)
        System.out.println("ACCEPTING CONTRACT...");
        HttpResponse<String> responseAccContract = Unirest.post("https://api.spacetraders.io/v2/my/contracts/"+contractId+"/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+agentToken)
                .asString();

        ResponseBody bodyAccContract = mapper.readValue(responseAccContract.getBody(), ResponseBody.class);
        if (bodyAccContract.getError() != null) {
            System.out.println(bodyAccContract.getError().getMessage());
            return;
        }
        //IF THE CONTRACT IS ACCEPTED IT WILL LIST ASTEROID WAYPOINTS
        AcceptContractResponse dataAccContract = mapper.convertValue(bodyAccContract.getData(), AcceptContractResponse.class);
        if(dataAccContract.getContract().isAccepted()){
            System.out.println("The contract ID: "+contractId+" has been accepted successfully.\n");
            System.out.println("LISTING ENGINEERED ASTEROID WAYPOINTS...");

            HttpResponse<String> responseListWaypoints = Unirest.get("https://api.spacetraders.io/v2/systems/"+systemSymbol+"/waypoints?type=ENGINEERED_ASTEROID")
                    .header("Accept", "application/json")
                    .asString();

            ResponseBody bodyListWaypoints = mapper.readValue(responseListWaypoints.getBody(), ResponseBody.class);
            if (bodyListWaypoints.getError() != null) {
                System.out.println(bodyListWaypoints.getError().getMessage());
                return;
            }

            for (JsonNode waypoint : bodyListWaypoints.getData()){
                ListWaypointsResponse dataListWaypoints = mapper.convertValue(waypoint, ListWaypointsResponse.class);
                System.out.println("The Asteroid Symbol is: "+dataListWaypoints.getSymbol());
            }
        }
    }
}