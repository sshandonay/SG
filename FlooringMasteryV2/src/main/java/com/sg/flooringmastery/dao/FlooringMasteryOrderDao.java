/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import java.util.List;

/**
 *
 * @author apprentice
 */
public interface FlooringMasteryOrderDao {

    public List<Order> addOrder(Order order);

    public List<Order> editOrder(Order order);

    public List<Order> getAllOrders();

    public List<Order> removeOrder(Order order);

    public int openOrderDao() throws FlooringMasteryPersistenceException;

    public void saveOrderDao(List<Order> orderList) throws FlooringMasteryPersistenceException;
}


// Week 5 - SShandonay 