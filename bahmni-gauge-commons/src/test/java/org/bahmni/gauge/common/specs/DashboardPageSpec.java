package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.DashboardPage;
import org.bahmni.gauge.common.clinical.displaycontrol.ObsDisplayControl;
import org.bahmni.gauge.common.home.HomePage;
import org.bahmni.gauge.common.program.domain.PatientProgram;
import org.bahmni.gauge.common.program.domain.Program;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DashboardPageSpec {

	DashboardPage dashboardPage;

	public DashboardPageSpec() {
		dashboardPage=PageFactory.get(DashboardPage.class);

	}

	@BeforeClassSteps
	public void waitForAppReady() {

		dashboardPage.waitForSpinner();
		dashboardPage=PageFactory.get(DashboardPage.class);
	}

	@Step("Ensure that <id> Obs display control with title <title> has correct data <table>")
	public void validateContentInDisplayControl(String id, String title, Table table) {
		dashboardPage.waitForSpinner();

		WebElement displayControl = dashboardPage.findElementById(id);
		assertNotNull("The display control with id [" + id + "] not found", displayControl);

		ObsDisplayControl obsDisplayControl = new ObsDisplayControl(displayControl);
		assertEquals("The display control with title [" + title + "] is incorrect", title, obsDisplayControl.getTitle());

		Map<String, String> keyValues = obsDisplayControl.getKeyValues();
		List<String> columnNames = table.getColumnNames();

		for (String columnName : columnNames) {
			assertEquals("The column [" + columnName + "] has incorrect data for the patient ["+dashboardPage.getPatientFromSpecStore().getIdNumber()+"]",
					table.getTableRows().get(0).getCell(columnName), keyValues.get(columnName));
		}
	}

	@Step("Ensure that <id> Obs display control has the message <message>")
	public void validateNoContentInDisplayControl(String id, String message) {
		dashboardPage.waitForSpinner();

		WebElement displayControl = dashboardPage.findElementById(id);
		assertNotNull("The display control with id [" + id + "] not found", displayControl);

		ObsDisplayControl obsDisplayControl = new ObsDisplayControl(displayControl);
		assertEquals("The message when no data available is incorrect", obsDisplayControl.getNoRecordsFoundText(), message);

	}

	@Step("On the dashboard page")
	public void navigateToDashboardPage() {
		PatientProgram patientProgram = dashboardPage.getPatientProgramFromSpecStore();
		String url = String.format(DashboardPage.URL,patientProgram.getPatient().getUuid(),patientProgram.getPatientProgramUuid());
		dashboardPage.get(url);
	}

	@Step("Click on <name> dashboard")
	public void clickOnDashboard(String name) {
		dashboardPage.waitForSpinner();
		dashboardPage.selectDashboard(name);
		waitForAppReady();
	}

	@Step("Navigate to dashboard")
	public void navigateToHomePage() {
		dashboardPage.get(HomePage.URL);
	}

	@Step("Navigate to dashboard link")
	public void navigateToDashboardLink() {
		dashboardPage.findElement(By.id("dashboard-link")).click();
	}

	@Step("Navigate to adt dashboard link")
	public void navigateToAdtDashboardLink() {
		dashboardPage.addDashboardButton.click();
	}

	@Step("Navigate to adt dashboard")
	public void navigateToAdtDashboard() {
		dashboardPage.adtButton.click();
	}


	@Step("Navigate to consultation")
	public void goToConsultation(){
		waitForAppReady();
		dashboardPage.clickEnterData();
		waitForAppReady();
	}

	@Step("Ensure that the program is updated on patient program dashboard")
	public void verifyProgramUpdatedOnDashboard(){
		Program program=dashboardPage.getProgramFromSpecStore();
		dashboardPage.validateProgramsDisplayControl(program);
	}

	@Step("Verify details on dashboard <Programs> display control")
	public void selectDisplayControl(String name) {
		dashboardPage.selectDisplayControl(name);
	}

	@Step("Verify data on Obs display control")
	public void verifyObsDisplayControl(){
		dashboardPage.validateVitalsObservationDisplayControl();
	}

	@Step("Navigate to Program Management Page from dashboard page")
	public void navigateToProgramManagementPageFromDashboardPage(){
		DashboardPage dashboardPage = PageFactory.get(DashboardPage.class);
	}

	@Step("Verify Vitals display control is empty")
	public void verifyVitalDisplayControlEmpty(){
		dashboardPage.verifyNoVitals();
	}

	@Step("Verify following value in display controls <table>")
	public void verifyDisplayControlEmpty(Table table){
		for (int i=0; i <table.getTableRows().size();i++) {
			dashboardPage.verifyNoValuesDisplayControl(table.getColumnValues("displayControl").get(i),table.getColumnValues("values").get(i));
		}
	}

	@Step("Open the current visit")
	public void openCurrentVisit(){
		dashboardPage.openCurrentVisit();
	}
	@Step("Verify Consultation button is not present")
	public void verifyConsultationNotPresent(){
		waitForAppReady();
		Assert.assertFalse("Consultation button is present",dashboardPage.isEnterDataPresent());
		waitForAppReady();
	}

	@Step("Verify <visits> Active visits for patient")
	public void verifyVisitsCount(int visits){
		waitForAppReady();
		Assert.assertEquals("Total Number of visits don't match",visits,dashboardPage.getVisitsCount());
	}
}
