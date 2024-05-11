package com.example.bookbazaar.Model;

public class UploadPDF {

    private String pdfId;
    private String pdfTitle;
    private String pdfAuthor;
    private String pdfCategory;
    private String pdfDescription;
    private String pdfUrl; // URL to download the PDF from Firebase Storage

    // Default constructor required for calls to DataSnapshot.getValue(UploadPDF.class)

    public UploadPDF()
    {
    }

    public UploadPDF(String pdfId, String pdfTitle, String pdfAuthor, String pdfCategory, String pdfDescription, String pdfUrl) {
        this.pdfId = pdfId;
        this.pdfTitle = pdfTitle;
        this.pdfAuthor = pdfAuthor;
        this.pdfCategory = pdfCategory;
        this.pdfDescription = pdfDescription;
        this.pdfUrl = pdfUrl;
    }

    public String getPdfId() {
        return pdfId;
    }

    public void setPdfId(String pdfId) {
        this.pdfId = pdfId;
    }

    public String getPdfTitle() {
        return pdfTitle;
    }

    public void setPdfTitle(String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public String getPdfAuthor() {
        return pdfAuthor;
    }

    public void setPdfAuthor(String pdfAuthor) {
        this.pdfAuthor = pdfAuthor;
    }

    public String getPdfCategory() {
        return pdfCategory;
    }

    public void setPdfCategory(String pdfCategory) {
        this.pdfCategory = pdfCategory;
    }

    public String getPdfDescription() {
        return pdfDescription;
    }

    public void setPdfDescription(String pdfDescription) {
        this.pdfDescription = pdfDescription;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
