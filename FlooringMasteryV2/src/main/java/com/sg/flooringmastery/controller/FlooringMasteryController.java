/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringMasteryPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.ProductType;
import com.sg.flooringmastery.dto.State;
import com.sg.flooringmastery.service.FlooringMasteryServiceLayer;
import com.sg.flooringmastery.ui.FlooringMasteryView;
import java.util.List;

/**
 *
 * @author apprentice
 */
public class FlooringMasteryController {

    private final FlooringMasteryServiceLayer service;
    private FlooringMasteryView view;
    private List<Order> controllerList;

    public FlooringMasteryController(FlooringMasteryServiceLayer service, FlooringMasteryView view) {
        this.service = service;
        this.view = view;
    }

    public void run() {

        boolean keepGoing = true;
        int menuSelection = 0;

        try {
            while (keepGoing) {

                String mode = service.readConfig();
                menuSelection = getMenuSelection();
                service.loadAllOrders();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addAnOrder();
                        break;
                    case 3:
                        editAnOrder();
                        break;
                    case 4:
                        removeAnOrder();
                        break;
                    case 5:
                        saveWork(mode);
                        break;
                    case 6:
                        keepGoing = false;
                        saveWork(mode);
                        break;
                    default:
                        unknownCommand();

                }

            }
        } catch (FlooringMasteryPersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }
        exitMessage();
    }

    public int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    public void displayOrders() {
        view.displayAllOrdersByDateBanner();
        List<Order> orderList = service.getAllOrders();
        view.displayOrdersOfDate(orderList);

    }

    public void addAnOrder() throws FlooringMasteryPersistenceException {
        view.createOrderBanner();
        boolean hasErrors = false;

        List<State> stateOptions = service.getAllStates();
        List<ProductType> productTypeOptions = service.getAllProductTypes();

        Order currentOrder = view.getNewOrderInfo(stateOptions, productTypeOptions);
        Order calculatedOrder = service.calculateOrderCosts(currentOrder);
        boolean saveData = view.printOrderInfo(calculatedOrder);
        if (saveData) {
            controllerList = service.addOrder(calculatedOrder);

        }
    }

    public void editAnOrder() {
        List<Order> orderList = service.getAllOrders();
        Order orderToModify = view.getOrderToModify(orderList);

        Order modifiedOrder = view.getModifiedOrderInfo(orderToModify);

        boolean commitEditOrder = view.printOrderInfo(modifiedOrder);
        if (commitEditOrder) {
            controllerList = service.editOrder(modifiedOrder);
        }
    }

    public void removeAnOrder() {
        List<Order> orderList = service.getAllOrders();
        Order orderToRemove = view.getOrderToRemove(orderList);
        boolean commitRemoveOrder = view.printOrderInfo(orderToRemove);

        if (commitRemoveOrder) {
            controllerList = service.removeOrder(orderToRemove);
        }
    }

    public void saveWork(String mode) {
        if (mode.equalsIgnoreCase("TRN")) {
            view.displayErrorMessage("Unable to save order due to training configuration.");
        } else {
            try {
                service.saveOrder(controllerList);
            } catch (FlooringMasteryPersistenceException e) {
                view.displayErrorMessage("Unable to save order.");
            }
        }
    }

    public void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    public void exitMessage() {
        view.exitMessage();
    }

}


// Week 5 - SShandonay 
