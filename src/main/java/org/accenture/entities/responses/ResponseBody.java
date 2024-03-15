package org.accenture.entities.responses;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.accenture.entities.Error;
import org.accenture.entities.Meta;

@Getter
public class ResponseBody {
    private JsonNode data;
    private Meta meta;
    private Error error;
}