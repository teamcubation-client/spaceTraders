package org.accenture.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Meta {
    private int total;
    private int page;
    private int limit;
}
