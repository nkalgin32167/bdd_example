@ready
Feature: 1. Pet Shop Narratives

  Background:
    Given we have an empty pet shop


  Scenario: 1.1 Pet Shop Narratives - Simplest
    Then we bought 'dog' named 'Snafu'
    And sell it to customer


  Scenario Outline: 1.2.1 Pet Shop Narratives - Using Examples
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


  Scenario Outline: 1.2.2 Pet Shop Narratives - Using Examples bis
    When we bought '<category>' named '<name>'
    And sell it to customer
    @foo
    Examples:
      | name  | category |
      | Snafu | dog      |
    @bar @single
    Examples:
      | name   | category |
      | Popola | snail    |
    @snafu
    Examples:
      | name   | category |
      | Devola | snail    |


  Scenario: 1.3 Pet Shop Narratives - Datatables & Behavior variables
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
