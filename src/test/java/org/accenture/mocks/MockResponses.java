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
}