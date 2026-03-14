Feature: valid search

  Scenario: Going to Opencart and search an item
    Given I open chrome browser
    When I type https://devon.nl/ and press enter
    Then I can see page loaded with title "Home - Devon"
    Then I quit browser