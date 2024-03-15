package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;
import org.accenture.entities.Faction;
import org.accenture.entities.Ship;

@Getter
public class RegisterNewAgentResponse {
    private String token;
    private Agent agent;
    private Contract contract;
    private Faction faction;
    private Ship ship;
}
