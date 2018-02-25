package google.search.StepDefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import static google.search.StepDefs.RestClient.response;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ElsevierStepDef {

    RestClient restClient = new RestClient();

    @Given("^I navigate to elsevier site$")
    public void test_no() {
        restClient.setRequestURL("http://www.cellpress.com");
        restClient.setContentType("application/json");
    }

    @When("^I make a GET request$")
    public void makeGETRequest() {
        // Write code here that turns the phrase above into concrete actions
       // throw new PendingException();
        restClient.performGetRequest();
    }

    @Then("^I get status code (\\d+)$")
    public void assertStatusCode(int arg1) {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
        assertThat(response.getStatusCode(), equalTo(arg1));
    }

}
