@test
Feature: Client API Collections - Specific format

    Scenario: get call
        Given I navigate to google search
        When I send the GET request
        Then I expect the response code 200

    @test
    Scenario: post call
        Given I navigate to fake api post call
        When I send the data in the body
        When I send the POST request
        Then I expect the response code 301
        And response body should contain '"userId": 500'
        And response body should contain 'userId'
        And response body should contain '500'
        And response should contain below items:
            | userID | code |
            | 500    | 201  |


    Scenario: Validate json data in json response
        Given I have the json response
        Then I should see the below data in response:
            | type  | BMW   |
            | color | Black |
            | type  | FIAT  |
            | color | Red   |


    Scenario: Validate xml data in xml response
        Given I have the xml response
        Then I should see the below xml data in response:
            | name | HSV Maloo |