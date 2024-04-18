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
                        },
                        "accepted": false
                    },
                    "ship": {
                        "symbol": "string",
                        "nav": {
                            "waypointSymbol": "string",
                            "route": {
                                "departureTime": "2019-08-24T14:15:22Z",
                                "arrival": "2019-08-24T14:15:22Z"
                            },
                            "status": "IN_TRANSIT"
                        },
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
                  "symbol": "string"
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
                  "waypointSymbol": "string",
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