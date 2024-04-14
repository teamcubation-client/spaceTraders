package org.accenture;

import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.RequestBodyEntity;
import kong.unirest.Unirest;
import org.accenture.mocks.MockResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

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

            HttpRequestWithBody httpRequestWithBodyListWaypoints = setMockUnirest(MockResponses.responseListWaypointsInSystem, true);
            mockedStatic.when(() -> Unirest.get("/systems/{systemSymbol}/waypoints")).thenReturn(httpRequestWithBodyListWaypoints);

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
