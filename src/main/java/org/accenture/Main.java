package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Agent;
import org.accenture.entities.responses.AcceptContractResponse;
import org.accenture.entities.responses.RegisterNewAgentResponse;
import org.accenture.entities.responses.ResponseBody;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;


public class Main {


    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        System.out.print("//INITIALIZING SYSTEM.");
        sleep();
        System.out.println("//SYSTEM INITIALIZED");
        Thread.sleep(500);
        System.out.println("'STARFIELD AT HOME'\n");
        Thread.sleep(500);

        String agentUserName = "TQ" + (int) (Math.random() * 1000000);

        //REGISTER NEW AGENT
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
        RegisterNewAgentResponse dataRegisterAgent = mapper.convertValue(bodyRegisterAgent.getData(), RegisterNewAgentResponse.class);
        System.out.print("//INITIALIZING REGISTRATION.");
        sleep();
        System.out.println("//REGISTRATION SUCCESSFUL");
        Thread.sleep(500);
        System.out.println("Agent: "+dataRegisterAgent.getAgent().getAccountId());
        System.out.println("Headquarters: "+dataRegisterAgent.getAgent().getHeadquarters());
        System.out.println("Faction: "+dataRegisterAgent.getFaction().getName()+"\n");
        Thread.sleep(500);


        //GET CONTRACTS

        String contractID = dataRegisterAgent.getContract().getId();
        String token = dataRegisterAgent.getToken();

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

        System.out.print("//LOOKING FOR CONTRACT.");
        sleep();
        System.out.println("//CONTRACT FOUND");
        Thread.sleep(500);
        System.out.print("//ACCEPTING CONTRACT.");
        sleep();
        System.out.println("Contract "+ dataAcceptContract.getContract().getId() +" accepted.\n");



    }

    public static void sleep() throws InterruptedException {
        Thread.sleep(200);
        System.out.print(".");
        Thread.sleep(200);
        System.out.println(".");
        Thread.sleep(500);
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
        System.out.println(".");
        Thread.sleep(500);
    }
}