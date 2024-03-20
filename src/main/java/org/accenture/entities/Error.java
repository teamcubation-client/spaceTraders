package org.accenture.entities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

@Getter
public class Error {
    private String message;
    private String code;
    private JsonNode data;
}
