package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.DashboardPage;
import org.bahmni.gauge.common.clinical.ObservationsPage;
import org.bahmni.gauge.common.clinical.domain.DrugOrder;
import org.bahmni.gauge.common.clinical.domain.ObservationForm;
import org.bahmni.gauge.common.program.domain.PatientProgram;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.bahmni.gauge.rest.BahmniRestClient;
import org.bahmni.gauge.util.StringUtil;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservationSpec{
    ObservationsPage observationsPage;

    public ObservationSpec() {
        observationsPage = PageFactory.get(ObservationsPage.class);
    }

    @BeforeClassSteps
    public void waitForAppReady(){
        observationsPage.waitForSpinner();
        observationsPage = PageFactory.get(ObservationsPage.class);
    }

    @Step("Select the template <template> from on the observation page")
    public void clickOnTreatmentEnrollment(String template) throws InterruptedException {
        observationsPage.selectTemplate(template.replace(" ", "_"));
        waitForAppReady();
    }

    @Step("Enter values in <template> template <table>")
    public void enterTemplateValues(String template, Table table) throws InterruptedException {
        ObservationForm observationForm = null;
        if(template.toLowerCase().contains("obstetrics"))
            observationForm = new ObservationForm(observationsPage.findElement(By.cssSelector("#concept-set-3")));
        observationForm.fillUp(table);
        waitForAppReady();
    }

    @Step("Create a <formName> form with following data <table>")
    public void createForm(String formName, Table table) {
        Patient patient = new BahmniPage().getPatientFromSpecStore();
        PatientProgram patientProgram = new BahmniPage().getPatientProgramFromSpecStore();

        Map<String, Object> formAttributes = new HashMap<>();
        formAttributes.put("patientUuid", patient.getUuid());
        formAttributes.put("patientProgramUuid", patientProgram.getPatientProgramUuid());
        formAttributes.putAll(transformTableToMap(table));

        BahmniRestClient.get().createForm(formName + ".ftl", formAttributes);

    }

    private Map<String, String> transformTableToMap(Table table) {

        Map<String, String> formVariables = new HashMap<>();
        List<String> columnNames = table.getColumnNames();

        for (String columnName : columnNames) {
            formVariables.put(columnName, table.getTableRows().get(0).getCell(columnName));
        }

        return formVariables;
    }

    @Step("Verify observations recorded under <formName>")
    public void verifyObservationsOnDashboard(String formName) {
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        dashboardPage.validateObservationDisplayControl(formName);
    }

    @Step("Verify prescribed drugs on the dashboard page")
    public void verifyDrugsOnDashboard() {
        List<DrugOrder> drugOrder = (List<DrugOrder>) new BahmniPage().getDrugOrderFromSpecStore();
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        for (DrugOrder drug : drugOrder)
            dashboardPage.validateDrugOrderDisplayControl(drug, "All active TB Drugs");
    }

    @Step("Verify display control <displayControlId> on dashboard, has the following details <table>")
    public void verifyDisplayControlContent(String displayControlId,Table table) {
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        String displayControlText = dashboardPage.getDisplayControlText(displayControlId.replace(" ","-"));
        for (String drugOrder : table.getColumnValues("details")) {
            drugOrder = StringUtil.transformPatternToData(drugOrder);
            Assert.assertTrue(StringUtil.stringDoesNotExist(drugOrder, displayControlText),displayControlText.contains(drugOrder));
        }
    }

    @Step("Verify display control with Caption <controlCaption> on dashboard, has the following details <table>")
    public void verifyDisplayControlContentWithCaption(String controlCaption,Table table) {
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        String displayControlText = dashboardPage.getDisplayControlTextWithCaption(controlCaption);
        for (String drugOrder : table.getColumnValues("details")) {
            drugOrder = StringUtil.transformPatternToData(drugOrder);
            Assert.assertTrue(StringUtil.stringDoesNotExist(drugOrder, displayControlText),displayControlText.contains(drugOrder));
        }
    }

    @Step("Expand all sections from display control with Caption <controlText>")
    public void expandControl(String controlText){
        DashboardPage dashboardPage=PageFactory.get(DashboardPage.class);
        dashboardPage.expandControlWithCaption(controlText);
    }
    @Step("click  <displayControlId> on dashboard, and verify displayed dialog has the following details <table>")
    public void verifyDialogContent(String displayControlId,Table table) {
        DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
        dashboardPage.clickDisplayControlHeader(displayControlId);
        String displayControlText = dashboardPage.getDialogText(By.cssSelector(".ngdialog-content"));
        for (String drugOrder : table.getColumnValues("details")) {
            drugOrder = StringUtil.transformPatternToData(drugOrder);
            Assert.assertTrue(StringUtil.stringDoesNotExist(drugOrder, displayControlText),displayControlText.contains(drugOrder));
        }
        dashboardPage.closeDialog();
    }

    @Step("Verify the <template> concept set is <displayType>")
    public void verifyObservationFormContent(String template, String displayType) {
            DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
            if(displayType.toLowerCase().equals("not displayed"))
                Assert.assertFalse("Element "+template+" is displayed", dashboardPage.hasElement(By.cssSelector("#concept-set-4")));
            else
                Assert.assertTrue("Element "+template+" is not displayed", dashboardPage.hasElement(By.cssSelector("#concept-set-4")));
    }


    @Step("Close the app")
    public void closeApplication() {
        observationsPage.closeApp();
    }

    @Step("Fill <Vitals> template with following observation details <table>")
    public void enterObservations(String template,Table table){
        observationsPage.enterObservations(template,table);
//        storeObservationFormInSpecStore(observationForm);
    }

    @Step("Navigate back to program dashboard")
    public void navigateBackToDashboard(){
        observationsPage.navigateToDashboard();
        waitForAppReady();
    }
}
