package org.accenture.mocks;

public class MockResponses {
    public static final String errorResponse = """
            {
              "error": {
                "message": "API Error",
                "code": "500"
              }
            }""";
    public static final String registerNewAgentResponse = """
            {
                "data": {
                    "agent": {
                        "headquarters": "X1-HK21-MP5"
                    },
                    "contract": {
                        "id": "123",
                        "terms": {
                            "deliver": [
                                {
                                    "tradeSymbol": "COPPER_ORE",
                                    "destinationSymbol": "X1-AR15-QZ8",
                                    "unitsRequired": 55
                                }
                            ]
                        }
                    },
                    "ship": {
                        "symbol": "MILLENNIUM FALCON",
                        "fuel": {
                            "consumed": {
                                "amount": 0
                            }
                        }
                    },
                    "token": "123"
                }
            }""";

    public static final String acceptContractResponse = """
            {
               "data":{
                  "contract":{
                     "accepted":true
                  }
               }
            }
            """;

    public static final String notAcceptContractResponse = """
            {
               "data":{
                  "contract":{
                     "accepted":false
                  }
               }
            }
            """;

    public static final String listWaypointResponse = """
            {
              "data": [
                {
                  "symbol": "X1-JK42-XM4"
                }
              ]
            }
            """;

    public static final String orbitShipResponse = """
            {
              "data": {
                
            }
            """;

    public static final String navigateShipResponse = """
            {
              "data": {
                "fuel": {
                  "consumed": {
                    "amount": 0
                  }
                },
                "nav": {
                  "waypointSymbol": "X1-JK42-XM4",
                  "route": {
                    "departureTime": "2019-08-24T14:15:22Z",
                    "arrival": "2019-08-24T14:15:22Z"
                  },
                  "status": "IN_TRANSIT"
                }
              }
            }
            """;

    public static final String dockShipResponse = """
            {
              "data": {
                
            }
            """;

    public static final String refuelShipResponse = """
            {
                "data": {
                    "fuel": {
                        "consumed": {
                            "amount": 0
                        }
                    },
                    "transaction": {
                        "totalPrice": 0
                    }
                }
            }
            """;
}