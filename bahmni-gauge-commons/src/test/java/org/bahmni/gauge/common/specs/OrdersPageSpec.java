package org.bahmni.gauge.common.specs;

import com.thoughtworks.gauge.BeforeClassSteps;
import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.bahmni.gauge.common.BahmniPage;
import org.bahmni.gauge.common.DriverFactory;
import org.bahmni.gauge.common.PageFactory;
import org.bahmni.gauge.common.clinical.OrdersPage;
import org.bahmni.gauge.common.clinical.domain.Order;
import org.bahmni.gauge.rest.BahmniRestClient;
import org.bahmni.gauge.util.TableTransformer;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class OrdersPageSpec {
    OrdersPage ordersPage;

    public OrdersPageSpec() {
        ordersPage = PageFactory.get(OrdersPage.class);
    }


    @Step("Expand <order> section on orders page")
    public void expandOrder(String order){
        ordersPage.clickOrder(order);
    }

    @Step("Select following orders <table>")
    public void selectOrders(Table table){
        List<Order> orders=TableTransformer.asEntityList(table,Order.class);
        ordersPage.selectorders(orders);
        ordersPage.getPatientFromSpecStore().setOrders(orders);
    }

    @Step("Unselect following orders <table>")
    public void unselectOrders(Table table){
        List<Order> orders=TableTransformer.asEntityList(table,Order.class);
        ordersPage.selectorders(orders);
        for(Order tableOrder:orders){
            for(Order order:ordersPage.getPatientFromSpecStore().getOrders()){
                if(order.getName().equals(tableOrder.getName())) {
                    ordersPage.getPatientFromSpecStore().getOrders().remove(order);
                    break;
                }
            }

        }

    }

    @Step("Add the following orders through API <table>")
    public void addOrdersAPI(Table table){

        List<Order> orders = TableTransformer.asEntityList(table,Order.class);
        ordersPage.addOrdersAPI(orders);

    }


    @Step("Delete the following orders <table>")
    public void deleteOrders(Table table){

        List<Order> orders = TableTransformer.asEntityList(table,Order.class);
        ordersPage.deleteOrders(orders);

    }

    @Step("Undo deletion of the following orders <table>")
    public void undoOrderDeletion(Table table){

        List<Order> orders = TableTransformer.asEntityList(table,Order.class);
        ordersPage.undoDelete(orders);

    }


    @Step("Enter notes to following <orderType> orders <table>")
    public void selectOrders(String orderType, Table table){
        List<Order> orders=TableTransformer.asEntityList(table,Order.class);
        for(TableRow row:table.getTableRows()){
            ordersPage.enterNotes(row.getCell("order"),row.getCell("note"));
        }
    }


    @Step("Verify order details on orders page")
    public void verifyOrders(){
        List<Order> orders=ordersPage.getPatientFromSpecStore().getOrders();
        ordersPage.verifyOrders(orders);

    }

    @Step("Verify notes are not editable for <orderType> orders")
    public void verifyNotesNotEditable(String orderType){
        ordersPage.verifyNotesNonEditable(orderType);

    }
}
