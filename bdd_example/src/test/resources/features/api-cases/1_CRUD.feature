@ready
Feature: 1. Pet Shop


  Scenario: 1.1 Simple API - narative
    Given we have an empty pet shop
    Then we bought 'dog' named 'Snafu'
    And sell it to customer


  Scenario Outline: 1.1 Simple API - narative
    Given we have an empty pet shop
    Then we bought '<category>' named '<name>'
    And sell it to customer
  Examples:
    | name    | category |
    | Snafu   | dog      |
    | Bakster | cat      |
    | Teresia | snail    |
    | Emile   | dog      |
    | Kaynie  | cat      |
    | Devola  | snail    |
    | Popola  | snail    |


  Scenario: 1.2 Simple API - Datatables
    Given create pet
      | name    | category |
      | Snafu   | dog      |
      | Bakster | cat      |
      | Teresia | snail    |
      | Emile   | dog      |
      | Kaynie  | cat      |
      | Devola  | snail    |
      | Popola  | snail    |

    Then get pet
      | name    | category |
      | Snafu   | dog      |
      | Bakster | cat      |
      | Teresia | snail    |

    When delete pet
      | name  |
      | Snafu |

    Then failed 'Pet not found' get pet
      | name  |
      | Snafu |


