package bdd.example.testsuits;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm"}
        , glue = "bdd.example.behaviors"
        , features = "src/test/resources/features/"
        , tags = "@single and not @ignore")
public class SingleScenarioTestSuite {
}
