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
                  "accountId": "string",
                  "symbol": "string",
                  "headquarters": "string",
                  "credits": 0,
                  "startingFaction": "string",
                  "shipCount": 0
                },
                "contract": {
                  "id": "123",
                  "terms": {
                    "deadline": "2019-08-24T14:15:22Z",
                    "payment": {
                      "onAccepted": 0,
                      "onFulfilled": 0
                    },
                    "deliver": [
                      {
                        "tradeSymbol": "string",
                        "destinationSymbol": "string",
                        "unitsRequired": 0,
                        "unitsFulfilled": 0
                      }
                    ]
                  },
                  "accepted": false,
                  "fulfilled": false,
                  "expiration": "2019-08-24T14:15:22Z",
                  "deadlineToAccept": "2019-08-24T14:15:22Z"
                },
                "faction": {
                  "symbol": "COSMIC",
                  "name": "string",
                  "description": "string",
                  "headquarters": "string",
                  "traits": [
                    {
                      "symbol": "BUREAUCRATIC",
                      "name": "string",
                      "description": "string"
                    }
                  ],
                  "isRecruiting": true
                },
                "ship": {
                  "symbol": "string",
                  "registration": {
                    "name": "string",
                    "factionSymbol": "string",
                    "role": "FABRICATOR"
                  },
                  "nav": {
                    "systemSymbol": "string",
                    "waypointSymbol": "string",
                    "route": {
                      "destination": {
                        "symbol": "string",
                        "type": "PLANET",
                        "systemSymbol": "string",
                        "x": 0,
                        "y": 0
                      },
                      "origin": {
                        "symbol": "string",
                        "type": "PLANET",
                        "systemSymbol": "string",
                        "x": 0,
                        "y": 0
                      },
                      "departureTime": "2019-08-24T14:15:22Z",
                      "arrival": "2019-08-24T14:15:22Z"
                    },
                    "status": "IN_TRANSIT",
                    "flightMode": "CRUISE"
                  },
                  "crew": {
                    "current": 0,
                    "required": 0,
                    "capacity": 0,
                    "rotation": "STRICT",
                    "morale": 0,
                    "wages": 0
                  },
                  "frame": {
                    "symbol": "FRAME_PROBE",
                    "name": "string",
                    "description": "string",
                    "condition": 0,
                    "integrity": 0,
                    "moduleSlots": 0,
                    "mountingPoints": 0,
                    "fuelCapacity": 0,
                    "requirements": {
                      "power": 0,
                      "crew": 0,
                      "slots": 0
                    }
                  },
                  "reactor": {
                    "symbol": "REACTOR_SOLAR_I",
                    "name": "string",
                    "description": "string",
                    "condition": 0,
                    "integrity": 0,
                    "powerOutput": 1,
                    "requirements": {
                      "power": 0,
                      "crew": 0,
                      "slots": 0
                    }
                  },
                  "engine": {
                    "symbol": "ENGINE_IMPULSE_DRIVE_I",
                    "name": "string",
                    "description": "string",
                    "condition": 0,
                    "integrity": 0,
                    "speed": 1,
                    "requirements": {
                      "power": 0,
                      "crew": 0,
                      "slots": 0
                    }
                  },
                  "cooldown": {
                    "shipSymbol": "string",
                    "totalSeconds": 0,
                    "remainingSeconds": 0,
                    "expiration": "2019-08-24T14:15:22Z"
                  },
                  "modules": [
                    {
                      "symbol": "MODULE_MINERAL_PROCESSOR_I",
                      "capacity": 0,
                      "range": 0,
                      "name": "string",
                      "description": "string",
                      "requirements": {
                        "power": 0,
                        "crew": 0,
                        "slots": 0
                      }
                    }
                  ],
                  "mounts": [
                    {
                      "symbol": "MOUNT_GAS_SIPHON_I",
                      "name": "string",
                      "description": "string",
                      "strength": 0,
                      "deposits": [
                        "QUARTZ_SAND"
                      ],
                      "requirements": {
                        "power": 0,
                        "crew": 0,
                        "slots": 0
                      }
                    }
                  ],
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
                  },
                  "fuel": {
                    "current": 0,
                    "capacity": 0,
                    "consumed": {
                      "amount": 0,
                      "timestamp": "2019-08-24T14:15:22Z"
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
}