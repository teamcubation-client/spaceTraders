package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import kong.unirest.*;
import org.accenture.mocks.MockResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainTest {

    private static String consoleOutput;

    private HttpRequestWithBody setMockUnirest(String responseBody, boolean hasBody) {
        RequestBodyEntity requestBodyEntity = mock(RequestBodyEntity.class);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        HttpRequestWithBody httpRequestWithBody = mock(HttpRequestWithBody.class);
        when(httpRequestWithBody.routeParam(anyString(), anyString())).thenReturn(httpRequestWithBody);
        when(httpRequestWithBody.header(anyString(), anyString())).thenReturn(httpRequestWithBody);
        if (hasBody) {
            when(httpRequestWithBody.body(anyString())).thenReturn(requestBodyEntity);
            when(requestBodyEntity.asString()).thenReturn(httpResponse);
        } else {
            when(httpRequestWithBody.asString()).thenReturn(httpResponse);
        }
        when(httpResponse.getBody()).thenReturn(responseBody);
        return httpRequestWithBody;
    }

    private GetRequest setMockUnirestByGetRequest(String responseBody, boolean hasBody) {
        RequestBodyEntity requestBodyEntity = mock(RequestBodyEntity.class);
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        GetRequest getRequest = mock(GetRequest.class);
        Map<String,String> stringMap = mock(Map.class);
        when(getRequest.routeParam(anyString(), anyString())).thenReturn(getRequest);
        when(getRequest.queryString(anyMap())).thenReturn(getRequest);
        when(getRequest.header(anyString(), anyString())).thenReturn(getRequest);
        when(getRequest.asString()).thenReturn(httpResponse);

        when(httpResponse.getBody()).thenReturn(responseBody);
        return getRequest;
    }

    @BeforeEach
    void setUp() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArrayOutputStream);
        System.setOut(out);
        consoleOutput = byteArrayOutputStream.toString(Charset.defaultCharset());
        out.close();
    }

    @Test
    void whenApiError_ThenThrowExceptionError() {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            HttpRequestWithBody httpRequestWithBody = setMockUnirest(MockResponses.responseError, true);
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestWithBody);
            assertThrows(Error.class, () -> Main.main(new String[]{}));
            mockedStatic.verify(() -> Unirest.post("/register"));
        }
    }

    @Test
    public void whenRegisterNewAgent_ThenAcceptContract() throws IOException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequestWithBody httpRequestWithBodyRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestWithBodyRegisterNewAgent);
            HttpRequestWithBody httpRequestWithBodyAcceptContract = setMockUnirest(MockResponses.responseError, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestWithBodyAcceptContract);

            try {
                Main.main(new String[]{});
                assertTrue(consoleOutput.contains("Token: 123"));
            } catch (Error e) {
                assertEquals("API Error", e.getMessage());
            }
            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
        }
    }

    @Test
    public void whenTheContractIsnotAccepted_ThenShowsMessageContractNotAccepted() throws IOException, InterruptedException{
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequestWithBody httpRequestWithBodyRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestWithBodyRegisterNewAgent);
            HttpRequestWithBody httpRequestWithBodyAcceptContract = setMockUnirest(MockResponses.responseContractNotAccepted, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestWithBodyAcceptContract);

            try {
                Main.main(new String[]{});
                assertTrue(consoleOutput.contains("accepted: false"));
            } catch (Error e) {
                assertEquals("Contract not accepted", e.getMessage());
            }
            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
        }

    }

    @Test
    public void whenTheContrac() throws IOException, InterruptedException{
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequestWithBody httpRequestWithBodyRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestWithBodyRegisterNewAgent);
            HttpRequestWithBody httpRequestWithoutBodyAcceptContract = setMockUnirest(MockResponses.responseContractNotAccepted, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestWithoutBodyAcceptContract);

            try {
                Main.main(new String[]{});
                assertTrue(consoleOutput.contains("accepted: false"));
            } catch (Error e) {
                assertEquals("Contract not accepted", e.getMessage());
            }
            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
        }

    }

    @Test
    public void whenValidateOrbitShip_thenTheCorrectStatusIsInOrbit() throws IOException, InterruptedException{
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequestWithBody httpRequestWithBodyRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestWithBodyRegisterNewAgent);
            HttpRequestWithBody httpRequestWithoutBodyAcceptContract = setMockUnirest(MockResponses.responseContractAccepted, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestWithoutBodyAcceptContract);

            GetRequest httpRequestWithoutBodylistWaypoints = setMockUnirestByGetRequest(MockResponses.responselistWayPoints, false);
            mockedStatic.when(() -> Unirest.get("/systems/{systemSymbol}/waypoints")).thenReturn(httpRequestWithoutBodylistWaypoints);

            HttpRequestWithBody httpRequestWithBodyNavigateShip= setMockUnirest(MockResponses.responseNavigateShip, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/navigate")).thenReturn(httpRequestWithBodyNavigateShip);

            HttpRequestWithBody httpRequestWithBodyShipSymbol=  setMockUnirest(MockResponses.emptyResponse, true);;
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/dock")).thenReturn(httpRequestWithBodyShipSymbol);

            HttpRequestWithBody httpRequestWithBodyRefuelShipl=  setMockUnirest(MockResponses.refuelShipResponse, true);;
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/refuel")).thenReturn(httpRequestWithBodyRefuelShipl);

            HttpRequestWithBody httpRequestWithBodyValidateOrbitShip = setMockUnirest(MockResponses.responseValidateOrbitShip, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/orbit")).thenReturn(httpRequestWithBodyValidateOrbitShip);

            try {
                Main.main(new String[]{});
                assertTrue(consoleOutput.contains("Ship refueled, total price: IN_ORBIT"));
            } catch (Error e) {
                assertEquals("Contract not accepted", e.getMessage());
            }
            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
            mockedStatic.verify(() -> Unirest.get("/systems/{systemSymbol}/waypoints"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/navigate"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/dock"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/refuel"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/orbit"));
        }

    }
     /*private static void validateOrbitShip(ObjectMapper mapper, RegisterNewAgentResponse data) throws JsonProcessingException {
        ResponseBody body;
        HttpResponse<String> responseOrbitShip = Unirest.post("https://api.spacetraders.io/v2" +
                        "/my/ships/{shipSymbol}/orbit")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+ data.getToken())
                .routeParam("shipSymbol", data.getShip().getSymbol())
                .asString();
        body = mapper.readValue(responseOrbitShip.getBody(), ResponseBody.class);
        if (mapper.readValue(responseOrbitShip.getBody(), ResponseBody.class).getError() != null) {
            System.out.println(body.getError().getMessage());
            throw new OrbitShipException();
        }
    }*/

}
