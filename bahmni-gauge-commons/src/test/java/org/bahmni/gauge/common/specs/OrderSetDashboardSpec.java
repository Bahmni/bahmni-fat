package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.Step;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.admin.OrderSetDashboardPage;
import org.junit.Assert;

/**
 * Created by atmaramn on 05/10/2016.
 */
public class OrderSetDashboardSpec {
    private OrderSetDashboardPage orderSetDashboardPage;

    public OrderSetDashboardSpec(){
        orderSetDashboardPage= PageFactory.get(OrderSetDashboardPage.class);

    }

    @Step("Click on create new button on orderset dashboard")
    public void clickCreateNew(){
        orderSetDashboardPage.clickCreateNewButton();
        orderSetDashboardPage.waitForSpinner();
    }

    @Step("Verify orderset created")
    public void verifyOrdersetCreated(){
        Assert.assertTrue("Order Set not created",orderSetDashboardPage.ordersetExists(new BahmniPage().getOrderSetInSpecStore()));
    }

    @Step("Click on previous orderset")
    public void clickPreviousOrderSet(){
        orderSetDashboardPage.clickOrderSet(new BahmniPage().getOrderSetInSpecStore());
        orderSetDashboardPage.waitForSpinner();
    }

    @Step("delete previous orderset")
    public void deleteOrderSet(){
        orderSetDashboardPage.deleteOrderSet(new BahmniPage().getOrderSetInSpecStore());
        orderSetDashboardPage.waitForSpinner();

    }

    @Step("verify previous orderset deleted")
    public void verifyOrderSetDeleted(){
        Assert.assertFalse("Order Set not deleted",orderSetDashboardPage.ordersetExists(new BahmniPage().getOrderSetInSpecStore()));
    }

}
