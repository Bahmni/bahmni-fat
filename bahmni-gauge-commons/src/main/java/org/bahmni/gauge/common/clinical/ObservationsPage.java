package org.bahmni.gauge.common.clinical;

import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;

import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.clinical.domain.ObservationForm;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.awt.*;
import java.io.IOException;
import java.util.List;

//import javafx.scene.control.Tab;

public class ObservationsPage extends BahmniPage {

    @FindBy(how = How.CSS, using = "#template-control-panel-button")
    public WebElement addFormbutton;

    @FindBy(how = How.CSS, using = "button[id*=\"chief_complaint_data_addmore_observation\"]")
    public WebElement addmore;

    @FindBy(how = How.CSS, using = "#dashboard-link")
    public WebElement dashboard;

    @FindBy(how = How.CSS, using = ".leaf-observation-node")  //.dashboard-content
    public List<WebElement> observation_nodes;

    @FindBy(how = How.CSS, using = ".template-control-panel")
    public WebElement templatePanel;

    @FindBy(how = How.CSS, using = ".concept-set-title")
    public List<WebElement> observationTemplates;

    @FindBy(how = How.CSS, using = ".save-consultation")
    public WebElement save;


    public void selectTemplate(String templateName) {
        clickTemplateButton();
        List<WebElement> allForms = templatePanel.findElements(By.tagName("button"));

        for (int i = 0; i < allForms.size(); i++) {
            String text = allForms.get(i).getText().replace(" ", "_"); //For debugging
            if (text.contains(templateName)) {
                allForms.get(i).click();
                break;
            }
        }
    }

    public WebElement expandObservationTemplate(String templateId) {
        WebElement template = driver.findElement(getSectionWithChildHavingId(templateId));
        WebElement downKey = waitForElementwithTimeOut(driver, ExpectedConditions.presenceOfElementLocated(By.cssSelector("#" + templateId + " h2 i.fa-caret-down:not(.ng-hide)")), 1);
        if (downKey != null) { //checking if down key exists
            return template;
        }

        WebElement expandArrow = driver.findElement(By.cssSelector("#" + templateId + " h2 i.fa-caret-right:not(.ng-hide)"));
        if (null != expandArrow) {
            expandArrow.click();
        }
        waitForSpinner();
        return template;
    }

    public void clickTemplateButton() {
        addFormbutton.click();
    }

    @Deprecated
    public void fillTemplateData(Table table, ObservationForm form) {

        WebElement observ_label = null;
        WebElement answer;
        List<TableRow> rows = table.getTableRows();
        List<String> columnNames = table.getColumnNames();

        for (int i = 0; i <= observation_nodes.size() - 1 && i <= columnNames.size() - 1; i++) {
            observ_label = observation_nodes.get(i).findElement(By.tagName("label"));
            if (observ_label.getText().contains(columnNames.get(i))) {
                answer = observation_nodes.get(i).findElement(By.cssSelector(".field-value"));
                if (answer.getText().length() != 0) {
                    if (answer.findElement(By.tagName("button")).isEnabled()) {
                        Point point = answer.findElement(By.tagName("button")).getLocation();
                        answer.findElement(By.tagName("button")).getLocation().move(point.getX(), point.getY());
                        answer.findElement(By.tagName("button")).click();
                    }
                } else {
                    if (answer.findElement(By.tagName("input")).isEnabled()) {
                        Point point = answer.findElement(By.tagName("input")).getLocation();
                        answer.findElement(By.tagName("input")).getLocation().move(point.getX(), point.getY());
                        answer.findElement(By.tagName("input")).sendKeys(rows.get(0).getCell(columnNames.get(i)));
                    }
                }

            }
        }
    }

    private By getSectionWithChildHavingId(String templateId) {
        return By.xpath("//div[contains(@class,\" section-grid\") and descendant::*[@id=\"" + templateId + "\"]]");
    }

    public void enterObservations(String template, Table data) {

        ObservationForm observationForm = new ObservationForm(expandObservationTemplate(template.replace(' ', '_')));
        observationForm.fillUp(data);
        save.click();
        storeObservationFormInSpecStore(observationForm);
    }

    public void navigateToDashboard() {
        dashboard.click();

    }

    public void addMoreObservation() {
        addmore.click();

    }

    int rowCount;

    public void addChiefComplaints(String template, Table data) {
        ObservationForm observationForm = new ObservationForm(expandObservationTemplate(template.replace(' ', '_')));
        List<TableRow> rows = data.getTableRows();
        List<String> columnNames = data.getColumnNames();
        rowCount = 1;
        String value;
        for (TableRow row : rows) {
            value = row.getCell(columnNames.get(0));
            addmore.click();
            WebElement chiefComplaints = driver.findElement(By.xpath(("(.//*[contains(@id,'observation_')])[" + rowCount + "]")));
            chiefComplaints.sendKeys(value);
            driver.findElement(By.xpath("(.//*[contains(@id,'observation_')])[" + rowCount + "]/../div/button")).click();
            rowCount++;


        }
        save.click();
    }


    public void removeChiefComplaints(String template, Table data) {
        ObservationForm observationForm = new ObservationForm(expandObservationTemplate(template.replace(' ', '_')));
        List<TableRow> rows = data.getTableRows();
        int rowSize = driver.findElements(By.xpath("(. //*[contains(@class,'concept-name ng-pristine ng-untouched ng-valid ui-autocomplete-input')])")).size();
        List<String> columnNames = data.getColumnNames();
        String value;
        int rowCount = 1;
        for (TableRow row : rows) {
            value = row.getCell(columnNames.get(0));
            for (rowCount = 1; rowCount <= rowSize; rowCount++) {
                WebElement element = driver.findElement(By.xpath(("(.//*[contains(@id,'observation_')])[" + rowCount + "]")));
                if (value.equals(element.getAttribute("value")) && (rowCount != 1)) {
                    driver.findElement(By.xpath(("(.//*[contains(@id,'removeClonedObs')])[" + rowCount + "]"))).click();
                }
            }
        }
        save.click();
    }

    public void uploadConsultaionImageAndAddComment(String template, Table table) throws AWTException, IOException, InterruptedException {
        ObservationForm observationForm = new ObservationForm(expandObservationTemplate(template.replace(' ', '_')));
        List<TableRow> rows = table.getTableRows();
        int rowCount = 1;
        for (TableRow row : rows) {
            String imageName = row.getCell("Image");
            waitForSpinner();
            scrollToBottom();
            driver.findElement(By.xpath("(//label[contains(@class,'placeholder btn')])[" + rowCount + "]")).click();
            Thread.sleep(3000);
            uploadImage(imageName);
            waitForSpinner();
            driver.findElement(By.xpath("(//legend/strong[contains(text(),'Images')]/../../..//button[@toggle='observation.showComment'])[" + rowCount + "]")).click();
            driver.findElement(By.xpath("(//textarea[contains(@class,'consultation-img-comments')])[" + rowCount + "]")).sendKeys(row.getCell("Comment"));
            driver.findElement(By.xpath(("(.//*[contains(@id,'image_addmore_observation_')])"))).click();
            rowCount++;
        }
        save.click();
        waitForSpinner();
    }

    public void removeFirstConsultationImage()
    {
        driver.findElement(By.xpath("(//button[@class='row-remover'])[1]")).click();
        save.click();
        waitForSpinner();
    }
    public void removeConsultationImage(String template, int data) {
        ObservationForm observationForm = new ObservationForm(expandObservationTemplate(template.replace(' ', '_')));
        int totalImages = driver.findElements(By.xpath("(//*[contains(@class,'has-image')])")).size();
        String value = new String();
        int rowCount = 1;
        while (rowCount<=data) {

            for (rowCount = 1; rowCount <= totalImages; rowCount++) {
                WebElement element = driver.findElement(By.xpath(("(.//*[contains(@id,'observation_')])[" + rowCount + "]")));
                if (value.equals(element.getAttribute("value")) && (rowCount != 1)) {
                    driver.findElement(By.xpath(("(.//*[contains(@id,'removeClonedObs')])[" + rowCount + "]"))).click();
                }
            }
        }
        save.click();
    }
}
