package bdd.example.api;

import com.google.gson.JsonObject;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static bdd.example.utils.Utils.getCurrentSession;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;


public class BaseApi {
    private static Logger LOGGER = LoggerFactory.getLogger(BaseApi.class);

    public BaseApi() {

    }

    public Response getResponseFromPOST(JsonObject body, String goal) {
//        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .log().all()
//                .auth().oauth2(token)
//                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post(getCurrentSession().get("baseURI") + goal);
        LOGGER.info(response::statusLine);
        LOGGER.info(response::prettyPrint);
        return response;
    }

    public Response getResponseFromGET(String goal) {
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .log().all()
//                .auth().oauth2(token)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .get(getCurrentSession().get("baseURI") + goal);
        LOGGER.info(response::statusLine);
        LOGGER.info(response::prettyPrint);
        return response;
    }

    public Response getResponseFromPUT(JsonObject body, String goal) {
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .log().all()
//                .auth().oauth2(token)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .put(getCurrentSession().get("baseURI") + goal);
        LOGGER.info(response::statusLine);
        LOGGER.info(response::prettyPrint);
        return response;
    }

    public Response getResponseFromDELETE(String goal) {
        RestAssured.useRelaxedHTTPSValidation();
        Response response = given()
                .log().all()
//                .auth().oauth2(token)
                .config(config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL)))
                .when()
                .delete(getCurrentSession().get("baseURI") + goal);
        LOGGER.info(response::statusLine);
        LOGGER.info(response::prettyPrint);
        return response;
    }
}
