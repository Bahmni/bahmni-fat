package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import freemarker.cache.StrongCacheStorage;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.DispositionPage;

public class DispositionPageSpec {
    private DispositionPage dispositionPage;

    @BeforeClassSteps
    public void waitForAppReady(){
        dispositionPage = PageFactory.get(DispositionPage.class);
        dispositionPage.waitForSpinner();}

    @Step("Select <Disposition Type> disposition")
    public void captureDataForDisposition(String dispositionType){
        dispositionPage.captureDataForDisposition(dispositionType);
    }

    @Step("Select <Disposition Type> disposition with notes <notes>")
    public void captureDataForDisposition(String dispositionType, String notes){
        dispositionPage.captureDataForDisposition(dispositionType,notes);
    }
}
