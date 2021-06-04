package com.example.myapplication;

public class Staff{
    private int id;
    private byte[] img;
    private String name, password, phone, mail, date, gender, position;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.phone = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Staff(int id, byte[] img, String name, String password, String phone, String mail, String date, String gender, String position) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.mail = mail;
        this.date = date;
        this.gender = gender;
        this.position = position;
    }

    public Staff(byte[] img, String name, String password, String phone, String mail, String date, String gender, String position) {
        this.img = img;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.mail = mail;
        this.date = date;
        this.gender = gender;
        this.position = position;
    }

    public Staff(int id, byte[] img, String phone, String mail, String date, String gender, String position) {
        this.id = id;
        this.img = img;
        this.phone = phone;
        this.mail = mail;
        this.date = date;
        this.gender = gender;
        this.position = position;
    }
}
