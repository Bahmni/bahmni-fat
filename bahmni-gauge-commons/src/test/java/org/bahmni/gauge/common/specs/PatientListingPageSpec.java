package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.PatientListingPage;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.junit.Assert;

public class PatientListingPageSpec {

    PatientListingPage patientListing;

    public PatientListingPageSpec() {
        patientListing = PageFactory.get(PatientListingPage.class);
    }

    @BeforeClassSteps
    public void waitForAppReady(){

        patientListing.waitForSpinner();
        patientListing = PageFactory.get(PatientListingPage.class);

    }

    @Step("Select existing patient from patient listing page under tab <tab>")
    public void selectPatientFromTab(String tab) {
        Patient patient = patientListing.getPatientFromSpecStore();
        patientListing.searchSelectPatientFromTab(patient.getIdentifier(),tab);
        //patientListing.searchSelectPatientFromTab("BAH253047",tab);
    }

    @Step("Select patient <patient> in tab <tab>")
    public void selectPatient(String patientID , String tab){
        patientListing.searchSelectPatientFromTab(patientID,tab);
    }

    @Step("Verify previous patient is not listed on patient listing page under tab <tab>")
    public void verifyPatientPresent(String tab){
        Patient patient = patientListing.getPatientFromSpecStore();
        Assert.assertFalse(patientListing.isPatientListedOnTab(patient.getIdentifier(),tab));

    }
}
