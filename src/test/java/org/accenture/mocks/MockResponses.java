package org.accenture.mocks;

public class MockResponses {
    public static final String responseError = """
            {
              "error": {
                "message": "API Error",
                "code": "500"
              }
            }""";
    public static final String responseRegisterNewAgent = """
            {
              "data": {
                      "agent": {
                          "headquarters": "string"
                      },
                      "contract": {
                          "id": "123",
                          "terms": {
                              "deliver": [
                                  {
                                      "tradeSymbol": "string",
                                      "destinationSymbol": "string",
                                      "unitsRequired": 0
                                  }
                              ]
                          }
                      },
                      "ship": {
                          "symbol": "string"
                      },
                      "token": "123"
                  }
            }
            """;


    public static final String responseContractNotAccepted = """
            {
                "data": {
                        "contract": {
                            "accepted": false
                        }
                }
            }""";
    public static final String responseContractAccepted = """
            {
                "data": {
                        "contract": {
                            "accepted": true
                        }
                }
            }""";
    public static final String responselistWayPoints = """
            {
                "data": 
                        [
                            {
                                "symbol": "X1-ZY69-DC5X"
                            }
                        ]
                        
                
            }""";

    public static final String responseNavigateShip = """
             {
                "data": {
                    "nav": {
                        "route": {
                            "arrival": "2024-04-12T02:19:41.374Z",
                                    "departureTime": "2024-04-12T02:18:48.374Z"
                        }
                    },
                    "fuel": {
                        "consumed": {
                            "amount": 45
                        }
                    }
                }
             }""";
    public static final String emptyResponse = """
            {
                "data": {
                        }
                }
            }""";

    public static final String refuelShipResponse = """
    {
        "data": {
            "transaction": {
                "totalPrice": 72
            }
        }
    }""";

}