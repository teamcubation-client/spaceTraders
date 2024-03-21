package org.accenture.entities;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Error {
    private String message;
    private String code;
    private JsonNode data;
}
