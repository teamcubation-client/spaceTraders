package org.accenture.entities.responses;

import lombok.Getter;
import lombok.Setter;
import org.accenture.entities.Agent;
import org.accenture.entities.Contract;

@Getter
@Setter
public class AcceptContractResponse {
    private Agent agent;
    private Contract contract;
}
