package com.example.bookbazaar.Model;

public class Cart {
    // All properties corresponding to nodes in the Cart list in Firebase
    private String bookId, bookName, bookAuthorName, price, discount, quantity;

    // Empty constructor
    public Cart() {
    }

    // Constructor with all parameters
    public Cart(String bookId, String bookName, String bookAuthorName, String price, String discount, String quantity) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
    }

    // Getters and setters for all properties
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthorName() {
        return bookAuthorName;
    }

    public void setBookAuthorName(String bookAuthorName) {
        this.bookAuthorName = bookAuthorName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
