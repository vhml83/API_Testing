package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)

@CucumberOptions(
        plugin = {"html:target/reports/cucumber-html-report",
                "json:target/reports/cucumber.json",
                "pretty:target/reports/cucumber-html-pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "junit:target/reports/cucumber-results.xml",
                "pretty"},
        tags = "@CURRENT and not @ignore",
        features = {"src/test/resources/features/"},
        glue = {"com/cucumber/steps/"},
        monochrome = true
)

public class RunnerCustomReport {

    @AfterClass
    public static void writeExtentReport() {}
}
