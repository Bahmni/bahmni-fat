package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.registration.RegistrationSearch;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationSearchSpec {

	RegistrationSearch registrationSearch;
	public RegistrationSearchSpec() {
		registrationSearch = PageFactory.get(RegistrationSearch.class);
	}

	@BeforeClassSteps
	public void waitForAppReady(){
		registrationSearch.waitForSpinner();
		registrationSearch = PageFactory.get(RegistrationSearch.class);

	}

	@Step("Click on create new patient link")
	public void clickOnCreateNew() {
		registrationSearch.clickCreateNew();
	}

	@Step({"Click on search patient link","Navigate to Registration search page"})
	public void clickOnSearch() {
		registrationSearch.clickSearch();
	}

	@Step("Search patient with prefix <prefix> identifier <identifier>")
	public void clickOnSearch(String prefix, String identifier) {
		registrationSearch.searchByIdentifier(prefix, identifier);
	}

	@Step("Search patient with name <name>")
	public void searchPatientByName(String name){
		registrationSearch.searchByName(name);
	}

	@Step("Search previously created patient with exact identifier")
	public void searchPreviousCreatedPatientWithIdentifier() {
		Patient recentlyCreatedPatient = registrationSearch.getPatientFromSpecStore();
		waitForAppReady();
		registrationSearch.searchByExactIdentifier(recentlyCreatedPatient.getPrefix(), recentlyCreatedPatient.getIdNumber());

	}

	@Step("Search previously created patient with name")
	public void searchPreviouslyCreatedPatientWithName(){
		Patient recentlyCreatedPatient = registrationSearch.getPatientFromSpecStore();
		registrationSearch.searchByName(recentlyCreatedPatient.getFirstName());
	}

	@Step("Search previously created patient with gram panchayat")
	public void searchPreviouslyCreatedPatientWithGramPancjayat(){
		Patient recentlyCreatedPatient = registrationSearch.getPatientFromSpecStore();
		registrationSearch.searchByGramPanchayat(recentlyCreatedPatient.getGramPanchayat());
	}

	@Step("Select the patient from the search results")
	public void ensureThatTheSearchResultsAreShown(){
		registrationSearch.getFirstResult();
	}

	@Step("Validate that the search results are displayed")
	public void validateSearchResults() {
			registrationSearch.verifySearchResults();
	}

	@Step("Verify search result contains <searchText> in column <column>")
	public void validateSearchResults(String searchText, String column) {
		registrationSearch.verifySearchResults(searchText,column);
	}
	@Step("Verify previous patient is listed in search result")
	public void validatePreviousPatientSearchResults() {
		registrationSearch.verifySearchResults(new BahmniPage().getPatientFromSpecStore());
	}
	@Step("Click on previous patient link from search results")
	public void clickOnPreviousPatientLink(){
		registrationSearch.findElement(By.xpath(".//a[contains(text(),\""+registrationSearch.getPatientFromSpecStore().getIdentifier()+"\")]")).click();
	}
}
