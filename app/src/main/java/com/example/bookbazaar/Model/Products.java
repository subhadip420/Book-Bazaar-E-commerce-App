package com.example.bookbazaar.Model;

public class Products {

    // Properties representing product attributes in "Products" Node as in Firebase Database
    private String authorName, bookId, bookName, category, date, description, price, image, time, productState, sellerAddress, sellerEmail, sellerName, sellerPhone, sid;

    // Empty constructor
    public Products() {

    }

    // Constructor to initialize all properties
    public Products(String authorName, String bookId, String bookName, String category, String date, String description, String price, String image, String time, String productState, String sellerAddress, String sellerEmail, String sellerName, String sellerPhone, String sid) {
        this.authorName = authorName;
        this.bookId = bookId;
        this.bookName = bookName;
        this.category = category;
        this.date = date;
        this.description = description;
        this.price = price;
        this.image = image;
        this.time = time;
        this.productState = productState;
        this.sellerAddress = sellerAddress;
        this.sellerEmail = sellerEmail;
        this.sellerName = sellerName;
        this.sellerPhone = sellerPhone;
        this.sid = sid;
    }


//    public Products(String authorName, String bookId, String bookName, String category, String date, String description, String price, String image, String time) {
//        this.authorName = authorName;
//        this.bookId = bookId;
//        this.bookName = bookName;
//        this.category = category;
//        this.date = date;
//        this.description = description;
//        this.price = price;
//        this.image = image;
//        this.time = time;
//    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

}