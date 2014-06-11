package com.example.nelsonmerc;

public class Item {
    private String id;
    private String title;
    private String picture;
    private Double price;
    private String sellerId;

    public Item(){
    }

    public Item(String id, String title, String picture, Double price){
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.price = price;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}

