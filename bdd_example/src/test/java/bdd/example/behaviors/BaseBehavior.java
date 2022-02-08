package bdd.example.behaviors;

import bdd.example.api.BaseApi;
import bdd.example.utils.SettingsManager;
import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static bdd.example.utils.Utils.getCurrentSession;
import static org.hamcrest.CoreMatchers.*;

public class BaseBehavior {
    private static final SettingsManager settingsManager = SettingsManager.getInstance();
    private BaseApi baseApi = new BaseApi();


    @Before
    public void initialization() {
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

    @Given("^(?:(?:failed )'(.*)' )?((?:get )|(?:create )|(?:delete )|(?:update )|(?:download ))?order '([\\w\\d-]*)'?")
    public void workWithStore(String failed, String type, String name, String group, String fileName, String delimiter) {
        if (failed == null) {
            switch (type) {
                case "create ":

                    break;
                case "get ":

                    break;
                case "delete ":

                    break;
                case "update ":

            }
        } else {

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
}
