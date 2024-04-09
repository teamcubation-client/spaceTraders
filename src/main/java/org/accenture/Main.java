package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.accenture.entities.Deliver;
import org.accenture.entities.Survey;
import org.accenture.entities.responses.*;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, InterruptedException {
        Unirest.config().defaultBaseUrl("https://api.spacetraders.io/v2");
        Unirest.config().addDefaultHeader("Content-Type", "application/json");
        Unirest.config().addDefaultHeader("Accept", "application/json");

        String agentName = "TQ" + (int) (Math.random() * 1000000);
        HttpResponse<String> spacetradersResponse = Unirest.post("/register")
            .body("{\n  \"faction\": \"COSMIC\",\n  \"symbol\": \"" + agentName + "\"}")
            .asString();

        RegisterNewAgentResponse registerNewAgentData = deserializeObjectResponse(spacetradersResponse, RegisterNewAgentResponse.class);
        String token = registerNewAgentData.getToken();
        String contractId = registerNewAgentData.getContract().getId();
        Deliver contractDeliverable = Arrays.stream(registerNewAgentData.getContract().getTerms().getDeliver()).findFirst().get();
        String contractDeliverableTradeSymbol = contractDeliverable.getTradeSymbol();
        int contractDeliverableQuantity = contractDeliverable.getUnitsRequired();
        String contractDeliverableDestination = contractDeliverable.getDestinationSymbol();
        String[] splitHeadquarters = registerNewAgentData.getAgent().getHeadquarters().split("-");
        String systemSymbol = String.join("-", Arrays.copyOfRange(splitHeadquarters, 0, splitHeadquarters.length - 1));
        String shipSymbol = registerNewAgentData.getShip().getSymbol();

        System.out.println("Agent name: " + agentName);
        System.out.println("Token: " + token);
        Unirest.config().addDefaultHeader("Authorization", "Bearer " + token);

        spacetradersResponse = Unirest.post("/my/contracts/{contractId}/accept")
            .routeParam("contractId", contractId)
            .asString();

        AcceptContractResponse acceptContractResponse = deserializeObjectResponse(spacetradersResponse, AcceptContractResponse.class);
        if (acceptContractResponse.getContract().isAccepted()) {
            System.out.print("\n");
            System.out.println("Contract ID: " + contractId);
            System.out.println("Contract accepted");

            spacetradersResponse = Unirest.get("/systems/{systemSymbol}/waypoints")
                    .routeParam("systemSymbol", systemSymbol)
                    .queryString(Map.of("type", "ENGINEERED_ASTEROID", "traits", "COMMON_METAL_DEPOSITS"))
                    .asString();
            Stream<ListWaypointsResponse> listWaypointsResponse = deserializeArrayResponse(spacetradersResponse, ListWaypointsResponse.class);
            String asteroidSymbol = listWaypointsResponse.findFirst().get().getSymbol();

            System.out.print("\n");
            System.out.println("Asteroid symbol: " + asteroidSymbol);

            Unirest.post("/my/ships/{shipSymbol}/orbit")
                    .routeParam("shipSymbol", shipSymbol)
                    .asString();

            System.out.println("Ship moved into orbit");

            spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/navigate")
                    .routeParam("shipSymbol", shipSymbol)
                    .body("{\n  \"waypointSymbol\": \"" + asteroidSymbol + "\"\n}")
                    .asString();

            NavigateShipResponse navigateShipResponse = deserializeObjectResponse(spacetradersResponse, NavigateShipResponse.class);
            int consumedFuel = navigateShipResponse.getFuel().getConsumed().getAmount();
            long sleepTime = navigateShipResponse.getNav().getRoute().getDepartureTime().until(
                    navigateShipResponse.getNav().getRoute().getArrival(), ChronoUnit.MILLIS
            );
            long endTime = System.currentTimeMillis() + sleepTime;
            long logInterval = 1000;

            while (System.currentTimeMillis() < endTime) {
                long remainingTime = endTime - System.currentTimeMillis();
                System.out.print("\rShip in transit, remaining time: " + remainingTime / 1000 + " seconds");
                Thread.sleep(logInterval);
            }

            System.out.println("\rShip arrived at asteroid");

            Unirest.post("/my/ships/{shipSymbol}/dock")
                    .routeParam("shipSymbol", shipSymbol)
                    .asString();

            System.out.println("Ship docked at asteroid");

            spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/refuel")
                    .routeParam("shipSymbol", shipSymbol)
                    .body("{\n  \"units\": \"" + consumedFuel + "\",\n  \"fromCargo\": false\n}")
                    .asString();

            RefuelShipResponse refuelShipResponse = deserializeObjectResponse(spacetradersResponse, RefuelShipResponse.class);
            System.out.println("Ship refueled, total price: " + refuelShipResponse.getTransaction().getTotalPrice());

            Unirest.post("/my/ships/{shipSymbol}/orbit")
                    .routeParam("shipSymbol", shipSymbol)
                    .asString();

            System.out.println("Ship moved into orbit");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            int remainingDeliverableQuantity = contractDeliverableQuantity;
            int totalExtractedQuantity = 0;
            long surveyExpiryTime = 2;
            Survey matchingSurvey = null;
            int multipleJettison = 0;

            System.out.println("\nStarting extraction process");
            System.out.println("Looking for " + contractDeliverableQuantity + " " + contractDeliverableTradeSymbol);
            System.out.print("\n");

            while (remainingDeliverableQuantity > 0) {
                while (matchingSurvey == null || surveyExpiryTime <= 1) {
                    spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/survey")
                            .routeParam("shipSymbol", shipSymbol)
                            .asString();
                    CreateSurveyResponse createSurveyResponse = deserializeObjectResponse(spacetradersResponse, CreateSurveyResponse.class);
                    matchingSurvey = Arrays.stream(createSurveyResponse.getSurveys())
                            .filter(survey -> Arrays.stream(survey.getDeposits())
                                    .anyMatch(deposit -> deposit.getSymbol().equals(contractDeliverableTradeSymbol))
                            )
                            .findFirst()
                            .orElse(null);

                    if (matchingSurvey != null) {
                        surveyExpiryTime = ZonedDateTime.now().until(
                                matchingSurvey.getExpiration(), ChronoUnit.MINUTES
                        );
                    }
                    System.out.println("\rSurvey created");
                    sleepTime = createSurveyResponse.getCooldown().getTotalSeconds() * 1000L;
                    endTime = System.currentTimeMillis() + sleepTime;
                    while (System.currentTimeMillis() < endTime) {
                        long remainingTime = endTime - System.currentTimeMillis();
                        System.out.print("\rAfter survey cooldown, remaining time: " + remainingTime / 1000 + " seconds");
                        Thread.sleep(logInterval);
                    }
                }

                System.out.println();

                spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/extract/survey")
                        .routeParam("shipSymbol", shipSymbol)
                        .body(matchingSurvey)
                        .asString();

                ExtractResourceWithSurveyResponse extractResourceWithSurveyResponse = deserializeObjectResponse(spacetradersResponse, ExtractResourceWithSurveyResponse.class);
                String extractedResource = extractResourceWithSurveyResponse.getExtraction().getYield().getSymbol();
                int extractedQuantity = extractResourceWithSurveyResponse.getExtraction().getYield().getUnits();
                int cargoCapacity = extractResourceWithSurveyResponse.getCargo().getCapacity();
                int cargoUnits = extractResourceWithSurveyResponse.getCargo().getUnits();

                if (extractedResource.equals(contractDeliverableTradeSymbol)) {
                    if (multipleJettison > 0) {
                        System.out.println("Extracted " + extractedQuantity + " " + extractedResource);
                    } else {
                        System.out.print("\rExtracted " + extractedQuantity + " " + extractedResource);
                    }
                    multipleJettison = 0;
                    remainingDeliverableQuantity -= extractedQuantity;
                    totalExtractedQuantity += extractedQuantity;
                } else {
                    multipleJettison++;
                    if (multipleJettison > 1) {
                        System.out.print("\rExtracted other resource, jettisoning x" + multipleJettison);
                    } else {
                        System.out.print("\rExtracted other resource, jettisoning");
                    }
                    Unirest.post("/my/ships/{shipSymbol}/jettison")
                            .routeParam("shipSymbol", shipSymbol)
                            .body("{\n  \"symbol\": \"" + extractedResource + "\",\n  \"units\": " + extractedQuantity + "\n}")
                            .asString();
                }

                System.out.println();

                sleepTime = extractResourceWithSurveyResponse.getCooldown().getTotalSeconds() * 1000L;
                endTime = System.currentTimeMillis() + sleepTime;

                while (System.currentTimeMillis() < endTime) {
                    long remainingTime = endTime - System.currentTimeMillis();
                    System.out.print("\rAfter extraction cooldown, remaining time: " + remainingTime / 1000 + " seconds");
                    Thread.sleep(logInterval);
                }

                if (cargoCapacity - cargoUnits <= 0 || remainingDeliverableQuantity <= 0) {
                    spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/navigate")
                            .routeParam("shipSymbol", shipSymbol)
                            .body("{\n  \"waypointSymbol\": \"" + contractDeliverableDestination + "\"\n}")
                            .asString();

                    navigateShipResponse = deserializeObjectResponse(spacetradersResponse, NavigateShipResponse.class);
                    consumedFuel = navigateShipResponse.getFuel().getConsumed().getAmount();
                    sleepTime = navigateShipResponse.getNav().getRoute().getDepartureTime().until(
                            navigateShipResponse.getNav().getRoute().getArrival(), ChronoUnit.MILLIS
                    );
                    endTime = System.currentTimeMillis() + sleepTime;
                    while (System.currentTimeMillis() < endTime) {
                        long remainingTime = endTime - System.currentTimeMillis();
                        System.out.print("\rShip in transit, remaining time: " + remainingTime / 1000 + " seconds");
                        Thread.sleep(logInterval);
                    }

                    System.out.println("\rShip arrived at contract delivery destination");

                    Unirest.post("/my/ships/{shipSymbol}/dock")
                            .routeParam("shipSymbol", shipSymbol)
                            .asString();

                    System.out.println("Ship docked at contract delivery destination");

                    spacetradersResponse = Unirest.post("/my/contracts/{contractId}/deliver")
                            .routeParam("contractId", contractId)
                            .body("{\n  \"shipSymbol\": \"" + shipSymbol + "\",\n  \"tradeSymbol\": \"" + contractDeliverableTradeSymbol + "\",\n  \"units\": " + totalExtractedQuantity + "\n}")
                            .asString();

                    DeliverCargoToContractResponse deliverCargoToContractResponse = deserializeObjectResponse(spacetradersResponse, DeliverCargoToContractResponse.class);
                    int unitsFulfilled = Arrays.stream(deliverCargoToContractResponse.getContract().getTerms().getDeliver()).findFirst().get().getUnitsFulfilled();
                    System.out.println("Contract delivery, units fulfilled: " + unitsFulfilled);
                    totalExtractedQuantity = 0;

                    if (remainingDeliverableQuantity <= 0) {
                        System.out.println("Contract fulfilled");
                        spacetradersResponse = Unirest.post("/my/contracts/{contractId}/fulfill")
                            .routeParam("contractId", contractId)
                            .asString();
                        FulfillContractResponse fulfillContractResponse = deserializeObjectResponse(spacetradersResponse, FulfillContractResponse.class);
                        if (fulfillContractResponse.getContract().isFulfilled()) {
                            System.out.println("Contract fulfilled, payment received: " + fulfillContractResponse.getContract().getTerms().getPayment().getOnFulfilled());
                        }
                    } else {
                        System.out.println("Contract partially fulfilled");

                        spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/refuel")
                                .routeParam("shipSymbol", shipSymbol)
                                .body("{\n  \"units\": \"" + consumedFuel + "\",\n  \"fromCargo\": false\n}")
                                .asString();

                        refuelShipResponse = deserializeObjectResponse(spacetradersResponse, RefuelShipResponse.class);
                        System.out.println("Ship refueled, total price: " + refuelShipResponse.getTransaction().getTotalPrice());

                        Unirest.post("/my/ships/{shipSymbol}/orbit")
                                .routeParam("shipSymbol", shipSymbol)
                                .asString();

                        System.out.println("Ship moved into orbit");

                        spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/navigate")
                                .routeParam("shipSymbol", shipSymbol)
                                .body("{\n  \"waypointSymbol\": \"" + asteroidSymbol + "\"\n}")
                                .asString();

                        navigateShipResponse = deserializeObjectResponse(spacetradersResponse, NavigateShipResponse.class);
                        consumedFuel = navigateShipResponse.getFuel().getConsumed().getAmount();
                        sleepTime = navigateShipResponse.getNav().getRoute().getDepartureTime().until(
                                navigateShipResponse.getNav().getRoute().getArrival(), ChronoUnit.MILLIS
                        );
                        endTime = System.currentTimeMillis() + sleepTime;
                        while (System.currentTimeMillis() < endTime) {
                            long remainingTime = endTime - System.currentTimeMillis();
                            System.out.print("\rShip in transit, remaining time: " + remainingTime / 1000 + " seconds");
                            Thread.sleep(logInterval);
                        }

                        System.out.println("\rShip arrived at asteroid");
                        Unirest.post("/my/ships/{shipSymbol}/dock")
                                .routeParam("shipSymbol", shipSymbol)
                                .asString();

                        System.out.println("Ship docked at asteroid");

                        spacetradersResponse = Unirest.post("/my/ships/{shipSymbol}/refuel")
                                .routeParam("shipSymbol", shipSymbol)
                                .body("{\n  \"units\": \"" + consumedFuel + "\",\n  \"fromCargo\": false\n}")
                                .asString();

                        refuelShipResponse = deserializeObjectResponse(spacetradersResponse, RefuelShipResponse.class);
                        System.out.println("Ship refueled, total price: " + refuelShipResponse.getTransaction().getTotalPrice());

                        Unirest.post("/my/ships/{shipSymbol}/orbit")
                                .routeParam("shipSymbol", shipSymbol)
                                .asString();

                        System.out.println("Ship moved into orbit");
                    }
                }
                surveyExpiryTime = ZonedDateTime.now().until(
                        matchingSurvey.getExpiration(), ChronoUnit.MINUTES
                );
            }
        } else {
            throw new Error("Contract not accepted");
        }
    }

    public static <T> T deserializeObjectResponse(HttpResponse<String> response, Class<T> classType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            throw new Error(body.getError().getMessage());
        }
        return mapper.convertValue(body.getData(), classType);
    }

    public static <T> Stream<T> deserializeArrayResponse(HttpResponse<String> response, Class<T> classType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        ResponseBody body = mapper.readValue(response.getBody(), ResponseBody.class);
        if (body.getError() != null) {
            throw new Error(body.getError().getMessage());
        }
        return StreamSupport.stream(body.getData().spliterator(), false)
                .map(element -> mapper.convertValue(element, classType));
    }
}