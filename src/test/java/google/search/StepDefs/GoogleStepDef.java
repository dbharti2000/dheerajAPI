package google.search.StepDefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


import java.io.IOException;
import java.util.Map;

import static google.search.StepDefs.RestClient.*;

/**
 * Created by dbharti on 23/03/2017.
 */
public class GoogleStepDef {

    RestClient restClient = new RestClient();


    @Given("^I navigate to google search$")
    public void navigate_to_google_serach() throws IOException {
        restClient.setRequestURL("http://google.co.uk");
        restClient.setContentType("application/json");
    }



    @Given("^I navigate to fake api post call$")
    public void navigate_to_fake_api() {
        restClient.setRequestURL("http://jsonplaceholder.typicode.com/posts");
        restClient.setContentType("application/json");
    }

    @Given("^I have the json response$")
    public void i_have_json() {
        restClient.setJsonStringInVariable();
    }

    @Then("^I should see the below data in response:$")
    public void assertJsonResponse(Map<String, String> table) throws IOException {
        restClient.assertJsonResponse(table);
    }

    @Then("^I should see the below xml data in response:$")
    public void assertXmlResponse(Map<String, String> table) throws IOException {
        restClient.assertXmlResponse(table);
    }

    @Given("^I have the xml response$")
    public void i_have_xml() {
        restClient.setXmlStringInVariable();
    }

    public void send_the_get_request() {
        restClient.performGetRequest();
    }

    @Then("^I expect the response code (.*)$")
    public void getCode(int statusCode) {
        restClient.assertStatusCode(statusCode);
    }

    @Then("^response body should contain '(.*)'$")
    public void response_should_contain_string(String responseExpectedString) {
        restClient.assertResponseBody(responseExpectedString);
    }

    @When("^I send the POST request$")
    public void send_the_post_request() {
        restClient.performPostRequest();
    }

    @When("^I send the data in the body$")
    public void send_data() {
        restClient.setRequestBody("{\"title\":\"TestTitle\",\"body\":\"TestBody\",\"userId\":500}");
    }

    @Then("^response should contain below items:$")
    public void response_contains(DataTable dataTable) {
        for (Map<String, String> userData : dataTable.asMaps(String.class, String.class)) {
            restClient.assertResponse(userData);
        }

    }
}
