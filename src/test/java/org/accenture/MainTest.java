package org.accenture;

import com.fasterxml.jackson.core.JsonProcessingException;
import kong.unirest.*;
import org.accenture.mocks.MockResponses;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainTest {

    private enum RestMethods {
        POST, GET
    }

    private HttpRequest setMockUnirest(String responseBody, RestMethods method, boolean hasBody) {
        HttpResponse<String> httpResponse = mock(HttpResponse.class);
        if (method.equals(RestMethods.POST)) {
            HttpRequestWithBody httpRequestWithBody = mock(HttpRequestWithBody.class);
            RequestBodyEntity requestBodyEntity = mock(RequestBodyEntity.class);
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
        if (method.equals(RestMethods.GET)) {
            GetRequest getRequest = mock(GetRequest.class);
            when(getRequest.routeParam(anyString(), anyString())).thenReturn(getRequest);
            when(getRequest.queryString(anyMap())).thenReturn(getRequest);
            when(getRequest.header(anyString(), anyString())).thenReturn(getRequest);
            when(getRequest.asString()).thenReturn(httpResponse);
            when(httpResponse.getBody()).thenReturn(responseBody);
            return getRequest;
        }
        return null;
    }

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void whenApiError_ThenThrowExceptionError() {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            HttpRequest httpRequest = setMockUnirest(MockResponses.responseError, RestMethods.POST, true);
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequest);
            assertThrows(Error.class, () -> Main.main(new String[]{}));
            mockedStatic.verify(() -> Unirest.post("/register"));
        }
    }

    @Test
    public void whenRegisterNewAgent_ThenAcceptContract() throws IOException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequest httpRequestRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestRegisterNewAgent);
            HttpRequest httpRequestAcceptContract = setMockUnirest(MockResponses.responseError, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestAcceptContract);
            try {
                Main.main(new String[]{});
            } catch (Error e) {
                assertEquals("API Error", e.getMessage());
            }
            assertTrue(outputStreamCaptor.toString().contains("Token: 123"));
            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
        }
    }

    @Test
    @DisplayName("Test Accept Contract")
    public void whenAcceptContract_thenListWaypoints() throws JsonProcessingException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequest httpRequestRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestRegisterNewAgent);
            HttpRequest httpRequestAcceptContract = setMockUnirest(MockResponses.acceptContractResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestAcceptContract);
            HttpRequest httpRequestListViewpoints = setMockUnirest(MockResponses.responseError, RestMethods.GET, false);
            mockedStatic.when(() -> Unirest.get("/systems/{systemSymbol}/waypoints")).thenReturn(httpRequestListViewpoints);

            try {
                Main.main(new String[]{});
            } catch (Error e) {
                assertEquals("API Error", e.getMessage());
            }

            assertTrue(outputStreamCaptor.toString().contains("Token: 123"));
            assertTrue(outputStreamCaptor.toString().contains("Contract accepted"));
            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
        }
    }

    @Test
    @DisplayName("Test Contract not accepted")
    public void whenContractNotAccepted_thenThrowApiError() throws JsonProcessingException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequest httpRequestRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestRegisterNewAgent);
            HttpRequest httpRequestAcceptContract = setMockUnirest(MockResponses.NotAcceptContractResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestAcceptContract);

            try {
                Main.main(new String[]{});
            } catch (Error e) {
                assertEquals("Contract not accepted", e.getMessage());
            }

            mockedStatic.verify(() -> Unirest.post("/register"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
        }
    }

    @Test
    @DisplayName("Test List Waypoints in System")
    public void whenListWaypoint_thenNavigateShip() throws JsonProcessingException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequest httpRequestRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestRegisterNewAgent);
            HttpRequest httpRequestAcceptContract = setMockUnirest(MockResponses.acceptContractResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestAcceptContract);
            HttpRequest httpRequestListViewpoints = setMockUnirest(MockResponses.listWaypointResponse, RestMethods.GET, false);
            mockedStatic.when(() -> Unirest.get("/systems/{systemSymbol}/waypoints")).thenReturn(httpRequestListViewpoints);
            HttpRequest httpRequestOrbitShip = setMockUnirest(MockResponses.orbitShipResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/orbit")).thenReturn(httpRequestOrbitShip);
            HttpRequest httpRequestNavigateShip = setMockUnirest(MockResponses.responseError, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/navigate")).thenReturn(httpRequestNavigateShip);

            try {
                Main.main(new String[]{});
            } catch (Error e) {
                assertEquals("API Error", e.getMessage());
            }

            assertTrue(outputStreamCaptor.toString().contains("Asteroid symbol: "));
            assertTrue(outputStreamCaptor.toString().contains("Ship moved into orbit"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
            mockedStatic.verify(() -> Unirest.get("/systems/{systemSymbol}/waypoints"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/orbit"));
        }
    }

    @Test
    @DisplayName("Test Navigate Ship")
    public void whenShipNavigate_thenDockShip() throws JsonProcessingException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequest httpRequestRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestRegisterNewAgent);
            HttpRequest httpRequestAcceptContract = setMockUnirest(MockResponses.acceptContractResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestAcceptContract);
            HttpRequest httpRequestListViewpoints = setMockUnirest(MockResponses.listWaypointResponse, RestMethods.GET, false);
            mockedStatic.when(() -> Unirest.get("/systems/{systemSymbol}/waypoints")).thenReturn(httpRequestListViewpoints);
            HttpRequest httpRequestOrbitShip = setMockUnirest(MockResponses.orbitShipResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/orbit")).thenReturn(httpRequestOrbitShip);
            HttpRequest httpRequestNavigateShip = setMockUnirest(MockResponses.navigateShipResponse, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/navigate")).thenReturn(httpRequestNavigateShip);
            HttpRequest httpRequestDockShip = setMockUnirest(MockResponses.navigateShipResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/dock")).thenReturn(httpRequestDockShip);
            HttpRequest httpRequestRefuelShip = setMockUnirest(MockResponses.responseError, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/refuel")).thenReturn(httpRequestRefuelShip);

            try {
                Main.main(new String[]{});
            } catch (Error e) {
                assertEquals("API Error", e.getMessage());
            }

            assertTrue(outputStreamCaptor.toString().contains("Asteroid symbol: "));
            assertTrue(outputStreamCaptor.toString().contains("Ship moved into orbit"));
            assertTrue(outputStreamCaptor.toString().contains("Ship arrived at asteroid"));
            assertTrue(outputStreamCaptor.toString().contains("Ship docked at asteroid"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
            mockedStatic.verify(() -> Unirest.get("/systems/{systemSymbol}/waypoints"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/orbit"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/navigate"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/dock"));
        }
    }

    @Test
    @DisplayName("Test Refuel Ship")
    public void whenShipIsDocked_thenRefuelShip() throws JsonProcessingException, InterruptedException {
        try (MockedStatic<Unirest> mockedStatic = mockStatic(Unirest.class)) {
            mockedStatic.when(Unirest::config).thenCallRealMethod();
            HttpRequest httpRequestRegisterNewAgent = setMockUnirest(MockResponses.responseRegisterNewAgent, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/register")).thenReturn(httpRequestRegisterNewAgent);
            HttpRequest httpRequestAcceptContract = setMockUnirest(MockResponses.acceptContractResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/contracts/{contractId}/accept")).thenReturn(httpRequestAcceptContract);
            HttpRequest httpRequestListViewpoints = setMockUnirest(MockResponses.listWaypointResponse, RestMethods.GET, false);
            mockedStatic.when(() -> Unirest.get("/systems/{systemSymbol}/waypoints")).thenReturn(httpRequestListViewpoints);
            HttpRequest httpRequestOrbitShip = setMockUnirest(MockResponses.orbitShipResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/orbit")).thenReturn(httpRequestOrbitShip);
            HttpRequest httpRequestNavigateShip = setMockUnirest(MockResponses.navigateShipResponse, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/navigate")).thenReturn(httpRequestNavigateShip);
            HttpRequest httpRequestDockShip = setMockUnirest(MockResponses.navigateShipResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/dock")).thenReturn(httpRequestDockShip);
            HttpRequest httpRequestRefuelShip = setMockUnirest(MockResponses.refuelShipResponse, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/refuel")).thenReturn(httpRequestRefuelShip);
            httpRequestOrbitShip = setMockUnirest(MockResponses.orbitShipResponse, RestMethods.POST, false);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/orbit")).thenReturn(httpRequestOrbitShip);
            HttpRequest httpRequestCreateSurvey = setMockUnirest(MockResponses.responseError, RestMethods.POST, true);
            mockedStatic.when(() -> Unirest.post("/my/ships/{shipSymbol}/survey")).thenReturn(httpRequestCreateSurvey);

            try {
                Main.main(new String[]{});
            } catch (Error e) {
                assertEquals("API Error", e.getMessage());
            }

            assertTrue(outputStreamCaptor.toString().contains("Asteroid symbol: "));
            assertTrue(outputStreamCaptor.toString().contains("Ship moved into orbit"));
            assertTrue(outputStreamCaptor.toString().contains("Ship arrived at asteroid"));
            assertTrue(outputStreamCaptor.toString().contains("Ship docked at asteroid"));
            assertTrue(outputStreamCaptor.toString().contains("Ship refueled"));
            mockedStatic.verify(() -> Unirest.post("/my/contracts/{contractId}/accept"));
            mockedStatic.verify(() -> Unirest.get("/systems/{systemSymbol}/waypoints"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/orbit"), times(2));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/navigate"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/dock"));
            mockedStatic.verify(() -> Unirest.post("/my/ships/{shipSymbol}/refuel"));
        }
    }
}

