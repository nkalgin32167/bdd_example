package bdd.example.behaviors;

import bdd.example.api.BaseApi;
import bdd.example.utils.SettingsManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bdd.example.utils.Utils.getCurrentSession;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class BaseBehavior {
    private static final SettingsManager settingsManager = SettingsManager.getInstance();
    private BaseApi baseApi = new BaseApi();


    @Before
    public void initialization(Scenario scenario) {
        List<String> tags = (List<String>) scenario.getSourceTagNames();
        System.out.println(tags);
        if (System.getProperty("baseURI") != null) {
            getCurrentSession().put("baseURI", System.getProperty("baseURI"));
        } else {
            getCurrentSession().put("baseURI", SettingsManager.getBaseURI());
        }
    }

    @After
    public void finalization() {

    }


    @Given("^(?:(?:failed )'(.*)' )?((?:get )|(?:create )|(?:delete )|(?:update ))?pet$")
    public void workWithPet(String failed, String type, DataTable table) {
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        Map<String, Integer> ids = new HashMap<>();
        Map<String, Integer> finalIds = (Map<String, Integer>) getCurrentSession().get("ids");
        switch (type) {
            case "create ":
                for (Map<String, String> row : rows) {
                    JsonObject body = new JsonObject();
                    body.addProperty("name", row.get("name"));
                    JsonObject category = new JsonObject();
                    category.addProperty("name", row.get("category"));
                    body.add("category", category);
                    Response response = baseApi.getResponseFromPOST(body, "pet");
                    if (failed == null) {
                        response.then().assertThat().statusCode(200);
                    } else {
                        response.then().assertThat().statusCode(anyOf(not(200), not(201)));
                    }
                    ids.put(response.body().jsonPath().get("name"), response.body().jsonPath().get("id"));
                    getCurrentSession().put("ids", ids);
                }
                break;
            case "get ":
                rows.stream().map(s -> s.get("name")).forEach(s -> {
                    Response response = baseApi.getResponseFromGET("pet/" + finalIds.get(s));
                    if (failed == null) {
                        response.then().assertThat().statusCode(200);
                    } else {
                        response.then().assertThat().statusCode(anyOf(not(200), not(201)))
                                .and().assertThat().body(containsString(failed));
                    }
                });
                break;
            case "delete ":
                rows.stream().map(s -> s.get("name")).forEach(s -> {
                    Response response = baseApi.getResponseFromDELETE("pet/" + finalIds.get(s));
                    if (failed == null) {
                        response.then().assertThat().statusCode(200);
                    } else {
                        response.then().assertThat().statusCode(anyOf(not(200), not(201)));
                    }
                });
                break;
            case "update ":
        }
    }


    @Given("we have an empty pet shop")
    public void weHaveAnEmptyPetShop() {
        Response response = baseApi.getResponseFromGET("pet/findByStatus?status=available");
        response.body().jsonPath().getList("id").forEach(s -> {
            baseApi.getResponseFromDELETE("pet/" + s);
        });
    }

    @Then("we bought {string} named {string}")
    public void weBoughtDogNamedSnafu(String category, String name) {
        JsonObject body = new JsonObject();
        body.addProperty("name", name);
        JsonObject cat = new JsonObject();
        cat.addProperty("name", category);
        body.add("category", cat);
        Response response = baseApi.getResponseFromPOST(body, "pet");
        response.then().assertThat().statusCode(200);
        Map<String, Integer> ids = new HashMap<>();
        ids.put("name", response.body().jsonPath().get("id"));
        getCurrentSession().put("ids", ids);
    }

    @And("sell it to customer")
    public void sellItToCustomer() {
        Map<String, Integer> finalIds = (Map<String, Integer>) getCurrentSession().get("ids");
        finalIds.entrySet().forEach(s -> {
            baseApi.getResponseFromDELETE("pet/" + s.getValue())
                    .then().assertThat().statusCode(200);
            baseApi.getResponseFromGET("pet/" + s)
                    .then().assertThat().statusCode(404);
        });

    }

    @Given("^create api element with body$")
    public void createApiElementWithBody(String StringBody) {
        JsonObject body = new Gson().fromJson(StringBody, JsonObject.class);
        Response response = baseApi.getResponseFromPOST(body, "pet");
        response.then().assertThat().statusCode(200);
        getCurrentSession().put("latestResponse", response);
    }

    @Then("^response body should be like$")
    public void responseBodyShouldBeLike(String StringBody) {
        Response response = (Response) getCurrentSession().get("latestResponse");
        String expected = StringBody
                .replaceAll("[\\s]*", "").replaceAll("\\n", "");
        String actual = response.body().prettyPrint()
                .replaceAll("[\\s]*", "").replaceAll("\\n", "");
        Pattern pattern = Pattern.compile(Pattern.quote(expected));
        Matcher matcher = pattern.matcher(actual);
        assertThat(
                String.format("Expected line like: '%s', actual is '%s'", expected, actual),
                matcher.matches(), Matchers.is(true));
    }
}
