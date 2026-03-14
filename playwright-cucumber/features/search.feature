@chrome
@firefox
@webkit
Feature: valid search

  Scenario: Going to Devon website and verify page loads
    Given I open chrome browser
    When I type "https://devon.nl/" and press enter
    Then I can see page loaded with title "Home - Devon"
    Then I quit browser

  Scenario: Search functionality test
    Given I open chrome browser
    When I type "https://devon.nl/" and press enter
    Then I can see page loaded with title "Home - Devon"
    And I type "test" in search box And I click search button
    Then I can see search results with title "Home - Devon"
    Then I quit browser
