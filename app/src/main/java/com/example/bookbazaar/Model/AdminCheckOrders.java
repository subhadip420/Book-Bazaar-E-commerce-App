package com.example.bookbazaar.Model;

public class AdminCheckOrders {
    // Properties representing order attributes in "Orders" Node as in Firebase Database
    private String orderId, State, address, city, date, name, phone, pin, time, totalAmount;

    // Generate empty constructor
    public AdminCheckOrders() {
    }

    // Generate constructor with parameter

    public AdminCheckOrders(String orderId, String state, String address, String city, String date, String name, String phone, String pin, String time, String totalAmount) {
        this.orderId = orderId;
        State = state;
        this.address = address;
        this.city = city;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.pin = pin;
        this.time = time;
        this.totalAmount = totalAmount;
    }


    // Getters and setters for all properties


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}///end class
