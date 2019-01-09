package com.example.bartosz.myapplication;

public class Place {

    private String place_id;
    private String name;
    private String distance;
    private String open;
    private String rating;
    private String address;
    private String uri;

    public Place(){

    }

    public Place(String place_id, String name, String distance, String open, String rating, String address, String uri){
        this.place_id = place_id;
        this.name = name;
        this.distance = distance;
        this.open = open;
        this.rating = rating;
        this.address = address;
        this.uri = uri;
    }

    public String getPlace_id(){
        return place_id;
    }

    public String getName(){
        return name;
    }

    public String getDistane(){
        return distance;
    }

    public String getOpen(){
        return open;
    }

    public String getRating(){
        return rating;
    }

    public String getUri(){ return uri; }

    public String getAddress(){
        return address;
    }

    public void setPlace_id(String place_id){
        this.place_id = place_id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDistance(String distance){
        this.distance = distance;
    }

    public void setOpen(String open){
        this.open = open;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setUri(String uri){ this.uri = uri; }

}
