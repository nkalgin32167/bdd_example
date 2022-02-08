@ready
Feature: 2. Pet Shop Dedicated API

  Scenario: 2.1 Pet Shop Dedicated API - Doc Strings
    Given we have an empty pet shop
    Given create api element with body
    """
    {
      "name": "Snafu",
      "category": {
          "name": "dog"
      }
    }
    """
    Then response body should be like
    """
    {
    "id": \d*,
    "category": {
        "id": \d*,
        "name": "dog"
    },
    "name": "Snafu",
    "photoUrls": [

    ],
    "tags": [

    ]
    }
    """