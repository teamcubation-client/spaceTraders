package org.accenture;

import kong.unirest.*;
import org.accenture.mocks.MockResponses;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
}
