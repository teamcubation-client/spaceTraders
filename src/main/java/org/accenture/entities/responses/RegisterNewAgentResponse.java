package org.accenture.entities.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;
import org.accenture.entities.Faction;
import org.accenture.entities.Ship;

@Getter
@Setter
@ToString
public class RegisterNewAgentResponse {
    private String token;
    private Agent agent;
    private Contract contract;
    private Faction faction;
    private Ship ship;
}
