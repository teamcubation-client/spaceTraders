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
            }""";

    public static final String responseAcceptContract = """
        {
            "data": {
                "contract": {
                    "accepted": true
                }
            }
        }""";

    public static final String responseListWaypoints = """
            {
              "data": [
                {
                  "symbol": "AABBCC"
                }
              ]
            }""";

    public static final String responseOrbitShip = """
        {
            "data": {
            }
        }  """;

    public static final String responseNavigateShip = """
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
              } """;

    public static final String responseDockShip = """
        {
            "data": {
            }
        }  """;

    public static final String responseRefuelShip = """
            {
                "data": {
                  "transaction": {
                    "totalPrice": 0
                  }
                }
            } """;


}