package com.example.bookbazaar.Model;

public class SellerProducts {
    // write "Products" Node as in Firebase Database
    private String image, authorName, bookId, bookName, price, productState;

    // Empty constructor
    public SellerProducts() {

    }

    // Constructor to initialize all properties
    public SellerProducts(String image, String authorName, String bookId, String bookName, String price, String productState) {
        this.image = image;
        this.authorName = authorName;
        this.bookId = bookId;
        this.bookName = bookName;
        this.price = price;
        this.productState = productState;
    }

    // Getters and setters for all properties
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
