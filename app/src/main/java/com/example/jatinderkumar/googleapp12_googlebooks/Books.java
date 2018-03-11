package com.example.jatinderkumar.googleapp12_googlebooks;

/**
 * Created by Jatinder Kumar on 10-03-2018.
 */

public class Books {
    private String title;
    private String author;
    private String publisher;
    private String url;

    public Books(String title, String author, String publisher,String url) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getUrl() {
        return url;
    }
}
