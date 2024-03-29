package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;

@Getter
public class AcceptContractResponse {
    private Agent agent;
    private Contract contract;
}
