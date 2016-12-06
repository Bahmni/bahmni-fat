package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.BacteriologyPage;
import org.bahmni.gauge.common.clinical.domain.Specimen;
import org.bahmni.gauge.util.TableTransformer;

import java.util.List;

/**
 * Created by atmaramn on 04/11/2016.
 */
public class BacteriologyPageSpec{

    BacteriologyPage bacteriologyPage;

    public BacteriologyPageSpec(){
        bacteriologyPage= PageFactory.get(BacteriologyPage.class);

    }

    @BeforeClassSteps
    public void waitForAppReady(){
        bacteriologyPage.waitForSpinner();
        bacteriologyPage= PageFactory.get(BacteriologyPage.class);
    }


    @Step("Add following bacteriology samples <table>")
    public void addBacteriologySample(Table table){
        List<Specimen> specimens=TableTransformer.asEntityList(table, Specimen.class);
        bacteriologyPage.addSamples(specimens);
        bacteriologyPage.getPatientFromSpecStore().setSpecimens(specimens);

    }

    @Step("Edit Sample with row number <rowNumber> as following <table>")
    public void editRow(int rowNumber,Table table){
        Specimen specimen=TableTransformer.asEntity(table, Specimen.class);
        BacteriologyPage bacteriologyPage= PageFactory.get(BacteriologyPage.class);
        bacteriologyPage.editSample(rowNumber,specimen);
        bacteriologyPage.getPatientFromSpecStore().getSpecimens().set(rowNumber-1,specimen);


    }

    @Step("Delete Sample with row number <rowNumber>")
    public void deleteRow(int rowNumber){
        BacteriologyPage bacteriologyPage= PageFactory.get(BacteriologyPage.class);
        bacteriologyPage.deleteSample(rowNumber);
        bacteriologyPage.getPatientFromSpecStore().getSpecimens().remove(rowNumber-1);


    }
}
