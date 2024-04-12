package org.accenture.mocks;

import java.time.ZonedDateTime;

public class MockResponses {

    private static final ZonedDateTime expirationDate = ZonedDateTime.now().plusMinutes(1);
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
                        "symbol": "TQ41818",
                        "headquarters": "X1-QV57-A1"
                    },
                    "contract": {
                        "id": "123",
                        "terms": {
                            "deliver": [
                                {
                                    "tradeSymbol": "IRON_ORE",
                                    "destinationSymbol": "X1-QV57-H47",
                                    "unitsRequired": "59"
                                }
                            ]
                        }
                    },
                    "ship": {
                        "symbol": "TQ41818-1"
                    },
                    "token": "123"
                }
            }""";

    public static final String responseAcceptContract = """
            {
                "data": {
                    "contract": {
                        
                        "accepted": true
                        }
                        
                    }
                }
            }""";

    public final static String responseListWaypointsInSystem = """
            {
                "data": [
                    {
                        "symbol": "X1-QV57-B9",
                        "type": "PLANET",
                        "systemSymbol": "X1-QV57",
                        "x": 0,
                        "y": 0,
                        "orbitals": [
                            {
                                "symbol": "string"
                            }
                        ]
                    }
                ]
            }
            """;

    public final static String responseOrbitShipResponse = """
            {
              "data": {
                
              }
            }
            """;

    public final static String responseNavigateShip = """
            {
                "data": {
                    "fuel": {
                        "consumed": {
                            "amount": 0
                        }
                    },
                    "nav": {
                        "route": {
                            "departureTime": "2019-08-24T14:15:22Z",
                            "arrival": "2019-08-24T14:15:22Z"
                        }
                    }
                }
            }
            """;

    public final static String responseDockShip = """
            {
                "data": {
                   
                }
            }
           """;

    public final static String responseRefuelShip = """
            {
                "data": {
                    "agent": {},
                    "fuel": {},
                    "transaction": {
                    "totalPrice": 222255555
                }
                }
                
            }
            """;

    public static final String responseCreateSurvey = """
            {
              "data": {
                "cooldown": {
                  "shipSymbol": "string",
                  "totalSeconds": 0,
                  "remainingSeconds": 0,
                  "expiration": "2019-08-24T14:15:22Z"
                },
                "surveys": [
                  {
                    "signature": "string",
                    "symbol": "string",
                    "deposits": [
                      {
                        "symbol": "IRON_ORE"
                      }
                    ],
                    "expiration": \"""" + expirationDate + """ 
                    ",
                    "size": "SMALL"
                  }
                ]
              }
            }
            """;
    public static final String responseNotAcceptedContract = """
            {
                "data": {
                    "contract": {
                        
                        "accepted": false
                        }
                        
                    }
                }
            }""";

}