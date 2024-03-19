package org.accenture.exceptions;

public class ContractDeclinedException extends RuntimeException{

    public ContractDeclinedException(){
        super("Contract Declined");
    }
}
