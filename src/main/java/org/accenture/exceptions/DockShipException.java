package org.accenture.exceptions;

public class DockShipException extends RuntimeException{

    public DockShipException(){
        super("Ocurrio un error al Dockear Ship");
    }
}
