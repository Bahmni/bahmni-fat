package org.bahmni.gauge.endtb.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.program.domain.PatientProgram;
import org.bahmni.gauge.common.registration.RegistrationFirstPage;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.bahmni.gauge.common.specs.ProgramManagementSpec;
import org.bahmni.gauge.endtb.program.domain.EndTBProgram;
import org.bahmni.gauge.endtb.registration.EndTBRegistrationFirstPage;
import org.bahmni.gauge.rest.BahmniRestClient;
import org.bahmni.gauge.util.TableTransformer;

import java.util.Map;
import java.util.Random;

public class EndTBProgramManagementSpec extends ProgramManagementSpec {


	@BeforeClassSteps
	public void waitForAppReady() {
		BahmniPage.waitForSpinner(DriverFactory.getDriver());
	}

	@Step("Click on treatment enrollment")
	public void clickOnTreatmentEnrollment() {
		EndTBRegistrationFirstPage registrationPage = (EndTBRegistrationFirstPage) PageFactory.get(RegistrationFirstPage.class);
		registrationPage.clickTreatmentRegistration();
		waitForAppReady();
	}

	@Step("Get Answers for <concept> concept")
	public void getAnswersForConcept(String concept) {
		Map<String, String> results = BahmniRestClient.get().getConceptAnswersForConceptName(concept);
	}

	@Step("Enroll patient to the treatment <table>")
	public void enrollPatientToTheTreatment(Table table) throws Exception {
		EndTBProgram program = TableTransformer.asEntity(table, EndTBProgram.class);
		program.setRegistrationNumber(String.valueOf(new Random().nextInt()));
		Patient patient = PageFactory.get(RegistrationFirstPage.class).getPatientFromSpecStore();

		PatientProgram patientProgram = new PatientProgram();
		patientProgram.setPatient(patient);
		patientProgram.setProgram(program);
		BahmniRestClient.get().enrollToProgram(patientProgram);
		programManagementPage.storeProgramInSpecStore(program);
		programManagementPage.storePatientProgramInSpecStore(patientProgram);
	}

	@Step("Register the patient to following treatment <programDetails>")
	public void enrollPatientToProgram(Table table) throws Exception {
		EndTBProgram treatment = TableTransformer.asEntity(table, EndTBProgram.class);
		treatment.setRegistrationNumber(String.valueOf(new Random().nextInt()));
		programManagementPage.storeProgramInSpecStore(treatment);
		programManagementPage.enrollPatientToProgram(treatment);

	}
}
