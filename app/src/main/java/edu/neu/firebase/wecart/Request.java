package edu.neu.firebase.wecart;

import java.util.List;

import edu.neu.firebase.wecart.Order;

public class Request {
    private String name;
    private String total;
    private String status;
    private List<Order> products;

    public Request() {
    }

    public Request(String name, String total, List<Order> products) {
        this.name = name;
        this.total = total;
        this.products = products;
        this.status = "ordered";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getProducts() {
        return products;
    }

    public void setFoods(List<Order> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
