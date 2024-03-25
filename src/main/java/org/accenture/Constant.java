package org.accenture;

public class Constant {

    private Constant() {}

    public static final String REGISTER_NEW_AGENT = "https://api.spacetraders.io/v2/register";
    public static final String LIST_CONTRACTS = "https://api.spacetraders.io/v2/my/contracts";
    public static final String ACCEPT_CONTRACT = "https://api.spacetraders.io/v2/my/contracts/{contractId}/accept";
    public static final String LIST_WAYPOINTS_IN_SYSTEM = "https://api.spacetraders.io/v2/systems/{systemSymbol}/waypoints";
    public static final String LIST_SHIP = "https://api.spacetraders.io/v2/my/ships";
    public static final String GET_SHIP = "https://api.spacetraders.io/v2/my/ships/{shipSymbol}";
    public static final String ORBIT_SHIP = "https://api.spacetraders.io/v2/my/ships/{shipSymbol}/orbit";
    public static final String GET_CONTRACT = "https://api.spacetraders.io/v2/my/contracts/{contractId}";
    public static final String NAVIGATE_SHIP = "https://api.spacetraders.io/v2/my/ships/{shipSymbol}/navigate";

    
}
