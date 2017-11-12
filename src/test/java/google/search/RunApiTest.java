package google.search;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by dbharti on 23/03/2017.
 */
@RunWith(Cucumber.class)
@cucumber.api.CucumberOptions(tags={"@test"}, features ="src/test/resources",strict=true, format = {"html:cucumber-html-reports", "json:cucumber-reports-with-handlebars/cucumber-report.json"})
public class RunApiTest {
}
