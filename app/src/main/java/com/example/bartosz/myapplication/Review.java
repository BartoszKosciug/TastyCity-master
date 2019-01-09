package com.example.bartosz.myapplication;

public class Review {
    private String author_name;
    private String text;
    private String relative_time_description;

    public Review(){

    }

    public Review(String author_name, String text, String relative_time_description){
        this.text = text;
        this.author_name = author_name;
        this.relative_time_description = relative_time_description;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public String getRelative_time_description() {
        return relative_time_description;
    }

    public String getText() {
        return text;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setRelative_time_description(String relative_time_description) {
        this.relative_time_description = relative_time_description;
    }

    public void setText(String text) {
        this.text = text;
    }
}
