package org.accenture.entities;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Survey {
    private String signature;
    private String symbol;
    private Deposit[] deposits;
    private ZonedDateTime expiration;
    private Size size;

    private enum Size {
        SMALL, MODERATE, LARGE
    }
}
