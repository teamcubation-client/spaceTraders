package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.*;

@Getter
public class RegisterNewAgentResponse {
    private String token;
    private Agent agent;
    private Contract contract;
    private Faction faction;
    private Ship ship;
}
