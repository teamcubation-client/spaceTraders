package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;

@Getter
public class FulfillContractResponse {
    private Agent agent;
    private Contract contract;
}
