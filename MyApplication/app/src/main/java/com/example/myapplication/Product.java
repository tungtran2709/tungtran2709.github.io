package com.example.myapplication;

public class Product {
    private int id;
    private byte[] img;
    private String type, name;
    private int count;
    private String price, date, des;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Product(byte[] img, String type, String name, int count, String price, String date, String des) {
        this.img = img;
        this.type = type;
        this.name = name;
        this.count = count;
        this.price = price;
        this.date = date;
        this.des = des;
    }

    public Product(int id, byte[] img, String type, String name, int count, String price, String date, String des) {
        this.id = id;
        this.img = img;
        this.type = type;
        this.name = name;
        this.count = count;
        this.price = price;
        this.date = date;
        this.des = des;
    }
}
