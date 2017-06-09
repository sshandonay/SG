package com.sg.flooringmastery.dao;

import static com.sg.flooringmastery.dao.FlooringMasteryOrderDaoImpl.DELIMITER;
import com.sg.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author apprentice
 */
public class FlooringMasteryOrderDaoImpl implements FlooringMasteryOrderDao {

    private static Map<Integer, Order> floorOrders = new HashMap<>();
    public static final String ORDERS_FOLDER = "Orders/";
    public static final String DELIMITER = ",";
    private Integer tempOrderNum = -1;

    @Override
    public List<Order> addOrder(Order order) {
        order.setOrderNum(tempOrderNum);
        floorOrders.put(tempOrderNum, order);
        tempOrderNum--;
        List<Order> updatedOrderList = getAllOrders();
        return updatedOrderList;
    }

    @Override
    public List<Order> editOrder(Order order) {
        floorOrders.put(order.getOrderNum(), order);
        List<Order> updatedOrderList = getAllOrders();
        return updatedOrderList;
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(floorOrders.values());
    }

    @Override
    public List<Order> removeOrder(Order order) {
        floorOrders.remove(order.getOrderNum(), order);
        List<Order> orderList = this.getAllOrders();
        return orderList;
    }

    @Override
    public int openOrderDao() throws FlooringMasteryPersistenceException {
        Scanner scanner;
        int counter = 0;

        File dir = new File(ORDERS_FOLDER);
        File[] files = dir.listFiles();

        for (File file : files) {

            try {
                scanner = new Scanner(new BufferedReader(new FileReader(file)));
            } catch (FileNotFoundException e) {
                throw new FlooringMasteryPersistenceException("Could not load Flooring Orders", e);
            }
            String currentLine;
            String[] currentTokens;
            String fileNameDate = file.getName().substring(7, 15);
            LocalDate fileDate = LocalDate.parse(fileNameDate, DateTimeFormatter.ofPattern("MMddyyyy"));

            while (scanner.hasNextLine()) {

                currentLine = scanner.nextLine();

                currentTokens = currentLine.split(DELIMITER);

                Order currentOrder = new Order();

                currentOrder.setOrderNum(Integer.parseInt(currentTokens[0]));
                currentOrder.setCustomerName(currentTokens[1]);
                currentOrder.setState(currentTokens[2]);
                currentOrder.setTaxRate(new BigDecimal(currentTokens[3]));
                currentOrder.setProductType(currentTokens[4]);
                currentOrder.setArea(new BigDecimal(currentTokens[5]));
                currentOrder.setCostPerSquareFoot(new BigDecimal(currentTokens[6]));
                currentOrder.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[7]));
                currentOrder.setMaterialCost(new BigDecimal(currentTokens[8]));
                currentOrder.setLaborCost(new BigDecimal(currentTokens[9]));
                currentOrder.setTax(new BigDecimal(currentTokens[10]));
                currentOrder.setTotal(new BigDecimal(currentTokens[11]));

                currentOrder.setOrderDate(fileDate);

                floorOrders.put(currentOrder.getOrderNum(), currentOrder);
                if (currentOrder.getOrderNum() > counter) {
                    counter = currentOrder.getOrderNum();
                }
            }
            scanner.close();
        }
        return counter;

    }

    public void saveOrderDao(List<Order> orderList) throws FlooringMasteryPersistenceException {
        int nextId = orderList.size() + 1;
        int tempOrderId;
        for (Order o : orderList) {
            if (o.getOrderNum() < 0) {
                tempOrderId = o.getOrderNum();
                floorOrders.remove(tempOrderId);
                o.setOrderNum(nextId);
                nextId++;
            }
        }

        writeFloorOrder(orderList);
    }

    private void writeFloorOrder(List<Order> orderList) throws FlooringMasteryPersistenceException {
        PrintWriter out;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

        File dir = new File(ORDERS_FOLDER);
        File[] files = dir.listFiles();

        for (File removeFile : files) {
            removeFile.delete();
        }

        for (Order currentOrder : orderList) {
            String formattedDate = currentOrder.getOrderDate().format(formatter);

            try {
                out = new PrintWriter(new FileWriter(ORDERS_FOLDER + "Orders_" + formattedDate + ".txt", true));
            } catch (IOException e) {
                throw new FlooringMasteryPersistenceException(
                        "Could not save floor order data", e);
            }

            out.println(currentOrder.getOrderNum() + DELIMITER
                    + currentOrder.getCustomerName() + DELIMITER
                    + currentOrder.getState() + DELIMITER
                    + currentOrder.getTaxRate() + DELIMITER
                    + currentOrder.getProductType() + DELIMITER
                    + currentOrder.getArea() + DELIMITER
                    + currentOrder.getCostPerSquareFoot() + DELIMITER
                    + currentOrder.getLaborCostPerSquareFoot() + DELIMITER
                    + currentOrder.getMaterialCost() + DELIMITER
                    + currentOrder.getLaborCost() + DELIMITER
                    + currentOrder.getTax() + DELIMITER
                    + currentOrder.getTotal() + DELIMITER);

            out.flush();
        }

    }

}


// Week 5 - SShandonay 
