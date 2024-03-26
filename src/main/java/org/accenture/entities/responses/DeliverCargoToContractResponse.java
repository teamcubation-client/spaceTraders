package org.accenture.entities.responses;

import lombok.Getter;
import org.accenture.entities.Cargo;
import org.accenture.entities.Contract;

@Getter
public class DeliverCargoToContractResponse {
    private Contract contract;
    private Cargo cargo;
}
