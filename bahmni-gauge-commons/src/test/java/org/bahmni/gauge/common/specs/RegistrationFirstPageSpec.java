package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.TestSpecException;
import org.bahmni.gauge.common.registration.RegistrationFirstPage;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.bahmni.gauge.rest.BahmniRestClient;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class RegistrationFirstPageSpec {

	private final WebDriver driver;

	private RegistrationFirstPage registrationFirstPage;

	public static final String PATIENT_KEY = "patient";

	public RegistrationFirstPageSpec() {
		this.driver = DriverFactory.getDriver();
		this.registrationFirstPage = PageFactory.getRegistrationFirstPage();
	}

	@BeforeClassSteps
	public void waitForAppReady() {
		new BahmniPage().waitForSpinner(driver);
	}

	@Step("On the new patient creation page")
	public void navigateToPatientCreatePage() {
		driver.get(RegistrationFirstPage.URL);
	}

	@Step("Create the following patient <table>")
	public void createPatients(Table table) {
		Patient patient = transformTableToPatient(table);
		RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.storePatientInSpecStore(patient);
		registrationFirstPage.registerPatient(patient);
	}

	@Step("Click on search patient link")
	public void navigateToPatientSearch() {
		//RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.navigateToSearchPage();
	}

	@Step("Validate that the patient edit page is opened for previously created patient")
	public void validateThePatientPageIsOpened() {
		//RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.verifyPatientWithIdentifierAndName();
	}

	@Step("Ensure that the patient edit page is opened for previously created patient")
	public void ensureThePatientPageIsOpened() {
		//RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.verifyPatientWithIdentifierAndName();
	}

	@Step("Start a visit <visit>")
	public void startPatientVisit(String visit) throws InterruptedException {
		//RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.startVisit(visit);
	}

	@Step("Select check to enter patient ID manually")
	public void selectCheckToEnterPatientID() {
		//RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.selectEnterPatientID();
	}

	@Step("Enter Visit Details Page")
	public void enterVisitDetailsPage() {
		//RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.enterVisitDetailsPage();
	}

	@Step("Create the following patient using api <table>")
	public void createPatientThroughAPI(Table table){
		Patient patient = transformTableToPatient(table);
		BahmniRestClient.get().createPatient(patient);
		RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		registrationFirstPage.storePatientInSpecStore(patient);
	}

	@Step("Verify the patient creation fails")
	public void verifyPatientCreationWithSameID() {
		new BahmniPage().validateSystemException(driver);
	}

	@Step("Delete the patient")
	public void deletePatient(){
		RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		String uuid = registrationFirstPage.getPatientFromSpecStore().getUuid();
		BahmniRestClient.get().retirePatient(uuid);
	}

	private Patient transformTableToPatient(Table table){
		List<TableRow> rows = table.getTableRows();
		List<String> columnNames = table.getColumnNames();

		if (rows.size() != 1) {
			throw new TestSpecException("Only one patient should be provided in the table");
		}

		RegistrationFirstPage registrationFirstPage = PageFactory.getRegistrationFirstPage();
		Patient patient = registrationFirstPage.transformTableRowToPatient(rows.get(0), columnNames);

		return patient;
	}
}
