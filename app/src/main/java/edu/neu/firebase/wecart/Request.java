package edu.neu.firebase.wecart;

import java.util.List;

import edu.neu.firebase.wecart.Order;

public class Request {
    private String name;
    private String total;
    private String status;
    private String storeName;
    private int storeId;
    private List<Order> products;

    public Request() {
    }

    public Request(String name, String total, List<Order> products, String storeName, int storeId) {
        this.name = name;
        this.total = total;
        this.products = products;
        this.status = "ordered";
        this.storeName = storeName;
        this.storeId = storeId;
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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }
}