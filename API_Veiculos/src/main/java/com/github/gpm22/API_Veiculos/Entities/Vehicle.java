package com.github.gpm22.API_Veiculos.Entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.util.Objects;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "vehicle_id")
    private long id;
    @Column(name = "vehicle_brand", nullable = false)
    private String brand;
    @Column(name = "vehicle_model", nullable = false)
    private String model;
    @Column(name = "vehicle_year", nullable = false)
    private String year;
    @Column(name = "vehicle_type", nullable = false)
    private String type;
    @Column(name = "vehicle_rotation_day")
    private int rotationDay;
    @Column(name = "vehicle_is_rotation_active")
    private Boolean isRotationActive;
    @Column(name = "vehicle_is_price")
    private String price;

    public Vehicle(){}

    public Vehicle(String brand, String model, String year, String type){
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRotationDay() {
        return rotationDay;
    }

    public void setRotationDay(int rotationDay) {
        this.rotationDay = rotationDay;
    }

    public Boolean getRotationActive() {
        return isRotationActive;
    }

    public void setRotationActive(Boolean rotationActive) {
        isRotationActive = rotationActive;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year='" + year + '\'' +
                ", type='" + type + '\'' +
                ", rotationDay=" + rotationDay +
                ", isRotationActive=" + isRotationActive +
                ", price='" + price + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return getId() == vehicle.getId() && getRotationDay() == vehicle.getRotationDay() && getBrand().equals(vehicle.getBrand()) && getModel().equals(vehicle.getModel()) && getYear().equals(vehicle.getYear()) && getType().equals(vehicle.getType()) && getPrice().equals(vehicle.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBrand(), getModel(), getYear(), getType(), getRotationDay(), getPrice());
    }
}
