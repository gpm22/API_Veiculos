package com.github.gpm22.API_Veiculos.Entities;

import javax.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    private long id;
    private String brand;
    private String model;
    private String year;
    private String type;
    private int rotationDay;
    private Boolean isRotationActive;
    private String price;

    public Vehicle(){

    }

    public Vehicle(String brand, String model, String year, String type){
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.type = type;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "brand", nullable = false)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(name = "model", nullable = false)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "year", nullable = false)
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Column(name = "type", nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "rotationDay")
    public int getRotationDay() {
        return rotationDay;
    }

    public void setRotationDay(int rotationDay) {
        this.rotationDay = rotationDay;
    }

    @Column(name = "rotationActive")
    public Boolean getRotationActive() {
        return isRotationActive;
    }

    public void setRotationActive(Boolean rotationActive) {
        isRotationActive = rotationActive;
    }

    @Column(name = "price")
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
