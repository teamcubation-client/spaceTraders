package org.accenture.mocks;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MockResponses {

    private static final String zoneTime = String.valueOf(ZonedDateTime.now());
    private static final String zoneTimePost = String.valueOf(ZonedDateTime.now().toLocalDateTime().plus(2, ChronoUnit.SECONDS)
                                                    .atZone(ZonedDateTime.now().getZone()));
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
                                      "tradeSymbol": "ALUMINUM_ORE",
                                      "destinationSymbol": "string",
                                      "unitsRequired": 40
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
                           "arrival": \" """  +zoneTime+ """
                         ","departureTime": \" """ +zoneTimePost+ """
                           "             
                       }
                   },
                   "fuel": {
                       "consumed": {
                           "amount": 45
                       }
                    }
               }
            }
            """;

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

    public static final String createSurveyResponse = """
            {
                 "data": {
                     "cooldown": {
                         "totalSeconds": "1"
                                      
                     },
                     
                     "surveys": [
                         {
                             "deposits": [
                                 {
                                     "symbol": "ALUMINUM_ORE"
                                 }
                             ],
                             "expiration": \" """ +zoneTimePost+ """
                                            "
                         }
                     ]
                 }
            }
            """;

}