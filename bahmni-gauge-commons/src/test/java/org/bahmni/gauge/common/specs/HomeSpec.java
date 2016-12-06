package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.PatientListingPage;
import org.bahmni.gauge.common.home.HomePage;
import org.bahmni.gauge.data.StoreHelper;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class HomeSpec {
    private HomePage homePage;


    public HomeSpec() {
        homePage = PageFactory.get(HomePage.class);
    }

    @BeforeClassSteps
    public void waitForAppReady() {
        homePage.waitForSpinner();
        homePage = PageFactory.get(HomePage.class);
    }

    @Step("Click on registration app")
    public void goToRegistrationPage() {
        homePage.clickRegistrationApp();
        homePage.waitForSpinner();
    }

    @Step("Click on programs app")
    public void goToProgramsPage() {
        homePage.clickProgramsApp();
    }

    @Step("Click on clinical app")
    public void goToClinicalPage() {
        homePage.clickClinicalApp();
    }

    @Step("Click on inpatient app")
    public void goToInpatientPage() {
        homePage.clickInpatientApp();
        waitForAppReady();
        PatientListingPage patientListingPage=PageFactory.get(PatientListingPage.class);
        if(StoreHelper.getAppGlobal().getBedCount()==0) {
            StoreHelper.getAppGlobal().setBedCount(patientListingPage.getAvailableBedCount());
        }
    }

    @Step("Click on admin app")
    public void goToAdminPage() {
        homePage.clickAdminApp();
    }

    @Step("Click on orders app")
    public void goToOrdersPage() {
        homePage.clickOrdersApp();
    }

    @Step("Click on radiology upload app")
    public void goToRadiologyUploadPage() {
        homePage.clickRadiologyUploadApp();
    }


    @Step("Click on <apps> app")
    public void goToApp(String app) {
        switch (app.toLowerCase()) {
            case "registration":
                homePage.clickRegistrationApp();
                break;

            case "programs":
                homePage.clickProgramsApp();
                break;

            case "clinical":
                homePage.clickClinicalApp();
                break;

            case "inpatient":
                homePage.clickInpatientApp();
                break;

            case "radiology upload":
                homePage.clickRadiologyUploadApp();
                break;

            case "patient documents":
                Gauge.writeMessage(app + " not implemented");
                break;

            case "admin":
                homePage.clickAdminApp();
                break;

            case "reports":
                Gauge.writeMessage(app + " not implemented");
                break;

            case "orders":
                homePage.clickOrdersApp();
                break;

            case "implementor interface":
                Gauge.writeMessage(app + " not implemented");
                break;

            default:
                Gauge.writeMessage(app + " not implemented");
                break;
        }
    }


    @Step("Logout the user")
    public void logout() {
        homePage.logout();
    }

    @Step("Verify Login Page")
    public void validateLogin() {
        Assert.assertTrue(homePage.registration.isDisplayed());
        Assert.assertTrue(homePage.admin.isDisplayed());
        Assert.assertTrue(homePage.exports.isDisplayed());
    }

}
