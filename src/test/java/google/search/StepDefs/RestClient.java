package google.search.StepDefs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.element.NodeChildren;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;
import cucumber.api.DataTable;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.jayway.restassured.path.xml.XmlPath;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.xml.XmlPath.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by dbharti on 23/03/2017.
 */

public class RestClient {

    private static String requestURL = "";
    private static Map<String, String> requestHeaders = new HashMap<String, String>();
    private static String requestBody = "";
    protected static Response response;
    private static String responseString = "";
    private static String jsonCarArray = "";
    private static String xmlString = "";


    private RequestSpecification giveRestAssured() {
        return given().log().all().headers(requestHeaders);
    }


    protected Response performGetRequest() {
        response = giveRestAssured().expect().get(requestURL);
        responseString = response.asString();
        return response;
    }

    private ResponseSpecification prepareRequest() {
        return giveRestAssured().body(requestBody).expect();
    }


    public void setRequestURL(String url) {
        requestURL = url;
    }

    public void setRequestBody(String body) {

        requestBody = body;
    }

    public String getResponseAsString() {
        return responseString;
    }

    public void assertStatusCode(int statusCode) {
        assertThat(response.getStatusCode(), equalTo(statusCode));
        //assertThat(String.format("call to::: %s \n ::Response:: \n %s", requestURL, getResponseAsString()), response.getStatusCode(), equalTo(statusCode));
    }

    public void assertResponseBody(String responseExpectedString) {
        assertThat(String.format("call to::: %s \n ::Response:: \n %s", requestURL, getResponseAsString()), getResponseAsString(), containsString(responseExpectedString));
    }

    public void assertResponse(Map<String, String> data) {
        assertThat("usual string", containsString(data.get("userID")));
        assertThat(String.format("test1", getResponseAsString()), getResponseAsString(), containsString(data.get("userId")));
    }

    public void setRequestHeader(String key, String value) {
        requestHeaders.put(key, value);
    }

    public void setContentType(String contentType) {
        requestHeaders.put("Accept", contentType);
        requestHeaders.put("Content-Type", contentType);

    }

    public void performPostRequest() {
        response = given().log().all().headers(requestHeaders).body(requestBody)
                .expect().post(requestURL);

        responseString = response.asString();

    }

    public String setXmlStringInVariable() {
        xmlString = "<records>\n" +
                "      <car name='HSV Maloo' make='Holden' year='2006'>\n" +
                "        <country>Australia</country>\n" +
                "        <record type='speed'>Pickup Truck with speed of 271kph</record>\n" +
                "      </car>\n" +
                "      <car name='P50' make='Peel' year='1962'>\n" +
                "        <country>Isle of Man</country>\n" +
                "        <record type='size'>Street-Legal Car at 99cm wide, 59kg</record>\n" +
                "      </car>\n" +
                "      <car name='Royals' make='Bugatti' year='1931'>\n" +
                "        <country>France</country>\n" +
                "        <record type='price'>Most Valuable Car at $15 million</record>\n" +
                "      </car>\n" +
                "</records>";

        return xmlString.toString();
    }

    public void assertXmlResponse(Map<String, String> responseDetail) {

        String xmlString = setXmlStringInVariable();
        final List<String> list = from(xmlString).getList("records.car.country.list()", String.class);
        final List<String> list1 = from(xmlString).getList("records.car.record.list()", String.class);
        final List<String> list2 = from(xmlString).getList("records.car[0..2].@name", String.class);
        final List<String> list3 = from(xmlString).getList("records.car[0..2].@make", String.class);
        final List<String> list4 = from(xmlString).getList("records.car[0..2].@year", String.class);

        XmlPath path = new XmlPath(xmlString);

        responseDetail.entrySet().forEach(entry -> {
            String actualValues = path.getString("records.car.@name");
            System.out.println("actual => " + actualValues);
            String expectedValue = entry.getValue();
            System.out.println("expected =>" + expectedValue);
            assertThat("asserting car names", actualValues, containsString(expectedValue.toString()));
        });

        System.out.println("===>" + list);
        System.out.println("===>" + list1);
        System.out.println("===>" + list2);
        System.out.println("===>" + list3);
        System.out.println("===>" + list4);

        // XmlPath path = new XmlPath(xmlString);
        System.out.println("print values => " + path.getString("records.car.name"));

        final String name = from(xmlString).getString("records.car[0].@name");
        assertThat(name, equalTo("HSV Maloo"));


        final NodeChildren categories = new XmlPath(xmlString).get("records.car");
        assertThat(categories.size(), equalTo(3));
        // XmlPath.from(responseString).setRoot("records").get("car");
    }


    public String setJsonStringInVariable() {
        jsonCarArray = "{\"car\": [{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]}";
        return jsonCarArray.toString();
    }

    public void assertJsonResponse(Map<String, String> table) throws IOException {
        String jsonString = setJsonStringInVariable();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonString);

        JsonPath path = new JsonPath(jsonString).setRoot("car");
        System.out.println("========>" + path.getString("type"));
        assertTrue("BMW does not exist", path.getString("type").contains("BMW"));


//       JsonPath jsonPath = new JsonPath(restClient.getResponseAsString());
//
//        String concurrencyToken = jsonPath.getString("homeAddress.concurrencyToken");
//        String objectKey = jsonPath.getString("homeAddress.objectKey");
//        concurrencyToken = jsonPath.getString("correspondenceAddresses[0].concurrencyToken"); //In case of array
//        objectKey = jsonPath.getString("correspondenceAddresses[0].objectKey");


        table.entrySet().forEach(entry -> {
            List<String> actualValues = node.findValuesAsText(entry.getKey());
            String expectedValue = entry.getValue();

            assertTrue("Field " + entry.getKey() + ", Expected " + expectedValue + ", Actual Values" + actualValues, actualValues.stream().anyMatch(actualValue -> actualValue.equalsIgnoreCase(expectedValue.trim())));

        });
    }


}
