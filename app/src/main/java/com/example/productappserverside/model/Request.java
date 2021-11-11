package com.example.productappserverside.model;

import java.util.List;

public class Request {
    private String Name;
    private String Address;
    private String Email;
    private String Total;
    private String Status;
    private List<Order> Products;

    public Request(String name, String address, String email, String total, List<Order> products) {
        Name = name;
        Address = address;
        Email = email;
        Total = total;
        Status = "0";
        Products = products;
    }
    public Request(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public List<Order> getProducts() {
        return Products;
    }

    public void setProducts(List<Order> products) {
        Products = products;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
