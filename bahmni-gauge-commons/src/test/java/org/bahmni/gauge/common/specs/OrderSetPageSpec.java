package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.admin.OrderSetPage;
import org.bahmni.gauge.common.admin.domain.OrderSet;
import org.bahmni.gauge.common.admin.domain.OrderSetMember;
import org.bahmni.gauge.util.TableTransformer;

import java.util.ArrayList;

public class OrderSetPageSpec {
    OrderSetPage orderSetPage;

    public OrderSetPageSpec() {
        orderSetPage = PageFactory.get(OrderSetPage.class);
    }


    @Step("Create orderset and enter following orderset details <table>")
    public void fillOrderSetDetais(Table table) {
        orderSetPage.enterDetails(orderSetPage.transformTableToOrderSet(table));
    }

    @Step("Enter following orderset members <table>")
    public void fillOrderSetMembers(Table table) {
        int i = 0;
        for (OrderSetMember orderSetMember : orderSetPage.transformTableToOrderSetMembers(table)) {
            orderSetPage.enterMember(i, orderSetMember);

            new BahmniPage().getOrderSetInSpecStore().getOrderSetMembers().add(orderSetMember);

            i++;

        }
    }

    @Step("Save the orderset")
    public void saveOrderSet() {
        orderSetPage.clickSave();
        orderSetPage.waitForSpinner();
    }

    @Step("Click on back button on orderset page")
    public void back() {
        orderSetPage.back();
        orderSetPage.waitForSpinner();
    }

    public OrderSet createOrderSet(String namePrefix, String description, String operator) {
        ArrayList<String> headers = new ArrayList<>();
        headers.add("name");
        headers.add("description");
        headers.add("operator");
        Table table = new Table(headers);
        ArrayList<String> row = new ArrayList<>();
        row.add(namePrefix);
        row.add(description);
        row.add(operator);

        table.addRow(row);

        return TableTransformer.asEntity(table, OrderSet.class);
    }

    @Step("Create orderset <namePrefix>, description <description>, operator <operator> with following members using api <table>")
    public void createOrderSetUsingApi(String namePrefix, String description, String operator, Table orderSetMembers) {

        OrderSet orderSet = createOrderSet(namePrefix, description, operator);

        for (OrderSetMember orderSetMember : orderSetPage.transformTableToOrderSetMembers(orderSetMembers)) {
            orderSet.getOrderSetMembers().add(orderSetMember);
        }

        orderSetPage.createOrderSetUsingApi(orderSet);
        orderSetPage.storeOrderSetInSpecStore(orderSet);

    }

    @Step("Edit previous orderset as <namePrefix>, description <description>, operator <operator> with following details <table>")
    public void editOrderSet(String namePrefix, String description, String operator, Table orderSetMembers) {
        OrderSet orderSet = new BahmniPage().getOrderSetInSpecStore();

        TableTransformer.updateEntityProperty(orderSet, "name", namePrefix);
        TableTransformer.updateEntityProperty(orderSet, "description", description);
        TableTransformer.updateEntityProperty(orderSet, "operator", operator);

        orderSet.getOrderSetMembers().clear();


        for (OrderSetMember orderSetMember : orderSetPage.transformTableToOrderSetMembers(orderSetMembers)) {
            orderSet.getOrderSetMembers().add(orderSetMember);
        }

        orderSetPage.editOrderSet(orderSet);

    }

    @Step("Verify previous orderset")
    public void verifyOrderSet() {
        OrderSet orderSet = orderSetPage.getOrderSetInSpecStore();
        orderSetPage.verifyOrderSet(orderSet);

    }
}
