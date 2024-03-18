package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Error {
    private String message;
    private String code;
    private String data;
}
