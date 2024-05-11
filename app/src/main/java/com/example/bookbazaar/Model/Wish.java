package com.example.bookbazaar.Model;

public class Wish
{
    // all node as in Cart List in firebase
    String bookId, bookName, bookAuthorName, price;

    // create empty constructor
    public Wish()
    {
    }

    // generate constructor with all parameter
    public Wish(String bookId, String bookName, String bookAuthorName, String price)
    {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthorName = bookAuthorName;
        this.price = price;
    }

    // generate getter and setter for all


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
}
