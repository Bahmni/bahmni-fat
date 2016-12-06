package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.apache.commons.beanutils.BeanUtils;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.TreatmentPage;
import org.bahmni.gauge.common.clinical.domain.DrugOrder;
import org.bahmni.gauge.common.program.ProgramManagementPage;
import org.bahmni.gauge.common.program.domain.PatientProgram;
import org.bahmni.gauge.common.registration.RegistrationFirstPage;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.bahmni.gauge.rest.BahmniRestClient;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.bahmni.gauge.common.specs.BaseSpec.setDateTime;
import static org.bahmni.gauge.common.specs.BaseSpec.stringDoesNotExist;

public class TreatmentPageSpec {

    private TreatmentPage treatmentPage;

    public TreatmentPageSpec() {
        treatmentPage = PageFactory.get(TreatmentPage.class);
    }

    @BeforeClassSteps
    public void waitForAppReady() {
        treatmentPage.waitForSpinner();
        treatmentPage = PageFactory.get(TreatmentPage.class);
    }


    @Step("Create drug order <table>")
    public void createDrugOrder(Table table) {
        List<DrugOrder> drugOrders = transformTableToDrugOrder(table);
        for (DrugOrder drugOrder : drugOrders) {
            treatmentPage.createDrugOrder(drugOrder);
        }
    }

    @Step("Edit previous drug order <table>")
    public void editDrugOrder(Table table) {
        int i = 0;
        List<DrugOrder> drugOrders = transformTableToDrugOrder(table);
        for (DrugOrder drugOrder : drugOrders) {
            treatmentPage.editDrugOrderbuttons.get(i++).click();
            treatmentPage.createDrugOrder(drugOrder);
        }
    }

    @Step("Add <orderset> drug order set")
    public void createDrugOrder(String orderset) {
        treatmentPage.addOrderSet(orderset, 0);
    }

    @Step("Add <orderset> drug order set with previous date")
    public void createDrugOrderPrevious(String orderset) {
        treatmentPage.addOrderSet(orderset, -1);
    }

    @Step("Stop the following drugs <table>")
    public void stopDrug(Table table) {
        for (String drugName : table.getColumnValues("details"))
            treatmentPage.stopDrugOrder(drugName);
    }

    @Step("Verify display control <displayTabName> on medications tab, has the following details <table>")
    public void verifyDisplayControlContent(String displayTabName, Table table) {
        String displayControlText = treatmentPage.getDisplayControlText(displayTabName);
        for (String drugOrder : table.getColumnValues("details")) {
            drugOrder = setDateTime(drugOrder);
            Assert.assertTrue(stringDoesNotExist(drugOrder) + "Actual string : " + displayControlText, displayControlText.contains(drugOrder));
        }
    }

    @Step("Create the following drug order using API <table>")
    public void createDrugOrderUsingAPI(Table table) {
        List<DrugOrder> drugOrders = transformTableToDrugOrder(table);
        Patient patient = treatmentPage.getPatientFromSpecStore();
        PatientProgram patientProgram = treatmentPage.getPatientProgramFromSpecStore();

        for (DrugOrder drugOrder : drugOrders) {
            drugOrder.setDrugUuid(BahmniRestClient.get().getUuidOfDrug(drugOrder.getDrugName()));
            drugOrder.setPatientUuid(patient.getUuid());
            drugOrder.setProgramUuid(patientProgram.getPatientProgramUuid());
        }
        BahmniRestClient.get().createDrugOrders(drugOrders);
        new BahmniPage().storeDrugOrderInSpecStore(drugOrders);
    }

    @Step("Refill previous drug order as following <table>")
    public void refillDrugOrder(Table table) {
        int i = 0;
        List<DrugOrder> drugOrders = transformTableToDrugOrder(table);
        for (DrugOrder drugOrder : drugOrders) {
            treatmentPage.refillDrugOrder(drugOrder, i++);

        }
    }

    private List<DrugOrder> transformTableToDrugOrder(Table table) {
        List<DrugOrder> drugOrders = new ArrayList<>();

        List<TableRow> rows = table.getTableRows();
        List<String> columnNames = table.getColumnNames();

        DrugOrder drugOrder;

        for (TableRow row : rows) {
            drugOrder = new DrugOrder();
            for (String columnName : columnNames) {

                try {
                    BeanUtils.getProperty(drugOrder, columnName);
                    BeanUtils.setProperty(drugOrder, columnName, row.getCell(columnName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
            drugOrders.add(drugOrder);
        }
        return drugOrders;
    }
}
