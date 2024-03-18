package org.accenture;
import kong.unirest.HttpResponse;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import org.accenture.entities.Agent;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    String agentUserName;
    String contractID;
    static String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoiQURSSUFOVFEiLCJ2ZXJzaW9uIjoidjIuMi4wIiwicmVzZXRfZGF0ZSI6IjIwMjQtMDMtMTAiLCJpYXQiOjE3MTAzNjAxNjIsInN1YiI6ImFnZW50LXRva2VuIn0.kib6gpO8nOnNprB7m5v1t886FIde2f8gaF8Q5CN_pV3wY_GQLbDP2Rjk0R37CCf1Hq59GVLYpw6HdMGKdSSZir1FADzLoGLsDvKBmCYJibeIyJ2NDLInwcX3Zy_eVMUgSGRlqWI0cIVidHd185c-IQjrFZLHeHA0FjIjSVBbNo-5eUgJZjhEszA1dp9A0UgxV2-onPPIfFlGFARvlOJxR2Oaq3FKLjrAOeaywZ5uZD-ZbUwmQcrdWnC6yPqFlYEo62WvuorzS0QtJBa-z4X_iemlqXuqlJbjS6r99i9Z-9MBVK7jpept3wQgg_AHjbcq8an1dCCaL2h8DJz5sMpqvA";

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Starfield at home:");

        //GET AGENT INFORMATION
        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/my/agent")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        System.out.println(response.getBody());

        String json = response.getBody();
        //Agent data = new ObjectMapper().readValue(response.getBody(), HashMap.class);

    }

    public void registerAgent() {
        //REGISTER NEW AGENT
        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/register")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentUserName + "\"\n}")
                .asString();

        System.out.printf(response.getBody());
    }

    /*

     */
    public void getLisContract() {
        //GET CONTRACTS
        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/my/contracts")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();

        System.out.printf(response.getBody());
    }

    /*
    public void getSpecificContract(){
        //GET CONTRACT
        HttpResponse<String> response = Unirest.get("https://api.spacetraders.io/v2/my/contracts/"+contractID)
            .header("Accept", "application/json")
            .header("Authorization", "Bearer "+token)
            .asString();

        System.out.printf(response.getBody());
    }
    */
    public void acceptContract() {
        //ACCEPT CONTRACT
        HttpResponse<String> response = Unirest.post("https://api.spacetraders.io/v2/my/contracts/" + contractID + "/accept")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .asString();
    }

}