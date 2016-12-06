package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.admin.AdminPage;
import org.bahmni.gauge.common.home.HomePage;
import org.openqa.selenium.WebDriver;

/**
 * Created by atmaramn on 05/10/2016.
 */
public class AdminSpec  {
    private AdminPage adminPage;

    public AdminSpec(){
        adminPage = PageFactory.get(AdminPage.class);
    }

    @BeforeClassSteps
    public void waitForAppReady(){
        adminPage.waitForSpinner();
        adminPage = PageFactory.get(AdminPage.class);
    }

    @Step("Click on order set app")
    public void goToOrderSetPage(){
        adminPage.clickOrderSet();
        waitForAppReady();
    }
}
