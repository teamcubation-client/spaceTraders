package org.accenture.mocks;

import java.time.ZonedDateTime;

public class MockResponses {
    private static ZonedDateTime expiration = ZonedDateTime.now().plusMinutes(3);

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
                                    "unitsRequired": 10
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

    public static final String createSurveyResponse = """
            {
              "data": {
                "cooldown": {
                  "totalSeconds": 5
                },
                "surveys": [
                  {
                    "deposits": [
                      {
                        "symbol": "COPPER_ORE"
                      }
                    ],
                    "expiration": \"""" + expiration + """
                     ",
                    "size": "MODERATE"
                  }
                ]
              }
            }
            """;

    public static final String jettisonCargoResponse = """
            {
              "data": {
                
            }
            """;

    public static final String extractResourceWithSurveyResponse = """
            {
                "data": {
                    "cooldown": {
                        "shipSymbol": "string",
                        "totalSeconds": 0,
                        "expiration": "2019-08-24T14:15:22Z"
                    },
                    "extraction": {
                        "shipSymbol": "string",
                        "yield": {
                            "symbol": "COPPER_ORE",
                            "units": 5
                        }
                    },
                    "cargo": {
                        "capacity": 0,
                        "units": 0,
                        "inventory": [
                            {
                                "symbol": "PRECIOUS_STONES",
                                "name": "string",
                                "description": "string",
                                "units": 1
                            }
                        ]
                    }
                }
            }
            """;

    public static final String deliverCargoToContract = """
            {
                "data": {
                    "contract": {
                        "id": "string",
                        "terms": {
                            "deadline": "2019-08-24T14:15:22Z",
                            "deliver": [
                                {
                                    "tradeSymbol": "COPPER_ORE",
                                    "destinationSymbol": "X1-AR15-QZ8",
                                    "unitsRequired": 10,
                                    "unitsFulfilled": 0
                                }
                            ]
                        },
                        "expiration": "2019-08-24T14:15:22Z",
                        "deadlineToAccept": "2019-08-24T14:15:22Z"
                    },
                    "cargo": {
                        "capacity": 0,
                        "units": 0,
                        "inventory": [
                            {
                                "symbol": "PRECIOUS_STONES",
                                "name": "string",
                                "description": "string",
                                "units": 1
                            }
                        ]
                    }
                }
            }
            """;

    public static final String fulfillContractResponse = """
            {
              "data": {
                "agent": {
                 
                },
                "contract": {
                  "id": "string",
                  "terms": {
                    "deadline": "2019-08-24T14:15:22Z",
                    "payment": {
                     
                    },
                    "deliver": [
                      {
                        "tradeSymbol": "COPPER_ORE",
                        "destinationSymbol": "X1-AR15-QZ8",
                        "unitsRequired": 10
                      }
                    ]
                  },
                  "fulfilled": true,
                  "expiration": "2019-08-24T14:15:22Z",
                  "deadlineToAccept": "2019-08-24T14:15:22Z"
                }
              }
            }
           \s""";

    public static final String extractWrongResourceWithSurvey = """
            {
                "data": {
                    "cooldown": {
                        "shipSymbol": "string",
                        "totalSeconds": 0,
                        "expiration": "2019-08-24T14:15:22Z"
                    },
                    "extraction": {
                        "shipSymbol": "string",
                        "yield": {
                            "symbol": "PRECIOUS_STONES",
                            "units": 5
                        }
                    },
                    "cargo": {
                        "capacity": 0,
                        "units": 0,
                        "inventory": [
                            {
                                "symbol": "PRECIOUS_STONES",
                                "name": "string",
                                "description": "string",
                                "units": 1
                            }
                        ]
                    }
                }
            }
            """;
}