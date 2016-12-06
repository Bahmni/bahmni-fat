package org.bahmni.gauge.common.registration;

import org.bahmni.gauge.common.BahmniPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class RegistrationVisitDetailsPage extends BahmniPage{
	
	@FindBy(how= How.CSS, using = "#observation_1")
	public WebElement height;
	
	@FindBy(how= How.CSS, using = "#observation_2")
	public WebElement weight;
	
	@FindBy(how= How.CSS, using = "#observation_4")
	public WebElement fees;
	
	@FindBy(how= How.CSS, using = "#observation_5")
	public WebElement comments;
	
	@FindBy(how= How.CSS, using = ".confirm")
	public WebElement saveButton;
	
	@FindBy(how= How.CSS, using = ".fa-home")
	public WebElement homeButton;
	
	@FindBy(how= How.CSS, using = "button[ng-click='back()']")
	public WebElement backButton;

	@FindBy(how= How.CSS, using = "button.cancel")
	public WebElement closeVisit;
	
	@FindBy(how= How.CSS, using = ".fa-search")
	public WebElement searchLink;

	@FindBy(how= How.CSS, using = "#visitDisplayTable tr:nth-last-child(1) a")
	public WebElement openLastVisit;

	public void closeVisit() {
		closeVisit.click();
		waitForAlertPopup(driver);
		acceptAlert(driver);
	}
	public void tryCloseVisit(){
		closeVisit.click();
	}
	public void openLastVisit() {
		openLastVisit.click();
	}

	public String getDisplayControlText(String displayControlName) {
			waitForElementOnPage("[data-ng-switch-when*='"+displayControlName+"']");
			WebElement displayControl = findElement(By.cssSelector("[data-ng-switch-when*='"+displayControlName+"']"));
			waitForSpinner();
			return displayControl.getText().replace("\n", "");

	}
	public String getDisplayControlTextWithCaption(String displayControlCaption ){
		WebElement displayControl = findElement(By.xpath(".//h2[contains(text(),\""+displayControlCaption+"\")]/ancestor::*[contains(@class,\"dashboard-section\")][1]"));
		waitForSpinner();
		return displayControl.getText().replace("\n", "");

	}

	public void openTab(String tabCaption) {
		findElement(By.cssSelector("#addTabButton")).click();
		findElement(By.xpath(".//a[contains(text(),\""+tabCaption+"\")]")).click();
	}
}
