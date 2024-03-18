package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Extraction {
    private String shipSymbol;
    private Yield yield;
}
