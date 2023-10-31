package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderIds = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartner = new HashMap<>();
    HashMap<String, String> orderPartnerPair = new HashMap<>();


    public void addOrder(Order order) {
        if(!orderIds.containsKey(order.getId()))
            orderIds.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        if(!deliveryPartner.containsKey(partnerId))
            deliveryPartner.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(!orderPartnerPair.containsKey(orderId))
            orderPartnerPair.put(orderId, partnerId);

        deliveryPartner.get(partnerId).setNumberOfOrders(deliveryPartner.get(partnerId).getNumberOfOrders() + 1);
    }

    public Order getOrderById(String orderId) {
        if(orderIds.containsKey(orderId))
            return orderIds.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if(deliveryPartner.containsKey(partnerId))
            return deliveryPartner.get(partnerId);
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if(deliveryPartner.containsKey(partnerId))
            return deliveryPartner.get(partnerId).getNumberOfOrders();
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        ArrayList<String> ordersList = new ArrayList<>();
        for(Map.Entry<String, String> pair: orderPartnerPair.entrySet())
            if(pair.getValue().equals(partnerId))
                ordersList.add(pair.getKey());

        return ordersList;
    }

    public List<String> getAllOrders() {
        ArrayList<String> orders = new ArrayList<>();
        for(String order: orderIds.keySet())
            orders.add(order);

        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderIds.size() - orderPartnerPair.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int count = 0;
        for(Map.Entry<String, String> pair: orderPartnerPair.entrySet()) {
            if (pair.getValue().equals(partnerId))
                if (orderIds.get(pair.getKey()).getDeliveryTime() > Integer.parseInt((time)))
                    count += 1;
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time = Integer.MIN_VALUE;
        for(Map.Entry<String, String> pair: orderPartnerPair.entrySet()) {
            if(pair.getValue().equals(partnerId))
                time = Math.max(time, orderIds.get(pair.getKey()).getDeliveryTime());
        }

        return String.valueOf(time);
    }

    public void deletePartnerById(String partnerId) {
        for(Map.Entry<String, String> pair: orderPartnerPair.entrySet())
            if(pair.getValue().equals(partnerId))
                orderPartnerPair.remove(pair.getKey());

        deliveryPartner.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderPartnerPair.remove(orderId);
        orderIds.remove(orderId);
    }
}
