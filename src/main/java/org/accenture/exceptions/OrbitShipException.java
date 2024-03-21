package org.accenture.exceptions;

public class OrbitShipException extends RuntimeException{

    public OrbitShipException(){
        super("Hubo un error al recuperar Orbit Ship");
    }
}
