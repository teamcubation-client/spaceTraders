package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Payment {
    private int onAccepted;
    private int onFulfilled;
}
