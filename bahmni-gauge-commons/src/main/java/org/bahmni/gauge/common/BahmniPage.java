package org.bahmni.gauge.common;

import com.thoughtworks.gauge.datastore.DataStore;
import com.thoughtworks.gauge.datastore.DataStoreFactory;
import org.bahmni.gauge.common.program.domain.Program;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BahmniPage {

	protected static String BASE_URL = System.getenv("BAHMNI_URL");

	public static final String PATIENT_KEY = "patient";
	public static final String PROGRAM_KEY = "program";

	public void storePatientInSpecStore(Patient value) {
		DataStore specStore = DataStoreFactory.getSpecDataStore();
		specStore.put(PATIENT_KEY, value);
	}

	public Patient getPatientFromSpecStore(){
		DataStore specStore = DataStoreFactory.getSpecDataStore();
		return (Patient) specStore.get(PATIENT_KEY);
	}

	public void storeProgramInSpecStore(Program program){
		DataStore specStore = DataStoreFactory.getSpecDataStore();
		specStore.put(PROGRAM_KEY, program);
	}

	public void waitForSpinner(WebDriver driver) {
		waitForElement(driver,ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#overlay")));
	}

	public Program getProgramFromSpecStore(){
		return (Program) DataStoreFactory.getSpecDataStore().get(PROGRAM_KEY);
	}

	public void acceptAlert(WebDriver driver) {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	public void handleExceptions(WebDriver driver){
		driver.findElement(By.cssSelector(".error-message-container .show-btn")).click();
	}

	public void validateSystemException(WebDriver driver){
		Assert.assertNotNull(driver.findElement(By.cssSelector(".error-message-container .show-btn")));
	}

	public void dismissAlert(WebDriver driver) {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	public void waitForAlertPopup(WebDriver driver) {
		waitForElement(driver,ExpectedConditions.alertIsPresent());
	}
	public void waitForElementOnPage(WebDriver driver, String element) {
		waitForElement(driver,ExpectedConditions.visibilityOfElementLocated(By.cssSelector(element)));
	}

	private void waitForElement(WebDriver driver, ExpectedCondition expectedCondition){
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(expectedCondition);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
