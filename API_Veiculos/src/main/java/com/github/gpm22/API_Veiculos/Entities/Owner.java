package com.github.gpm22.API_Veiculos.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "owner_id")
    private long id;
    @Column(name = "owner_name", nullable = false)
    private String name;
    @Column(name = "owner_email", nullable = false, unique = true)
    private String email;
    @Column(name = "owner_cpf", nullable = false, unique = true)
    private String cpf;
    @Column(name = "owner_birthdate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "owners_vehicles", joinColumns = @JoinColumn(name = "owner_id"), inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
    private Set<Vehicle> vehicles;

    public Owner(){}

    public Owner(String name, String email, String cpf, Date birthDate){
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.birthDate = birthDate;
    }

    public void updateInfoWith(Owner updatedOwner){
        this.setCpf(updatedOwner.getCpf());
        this.setBirthDate(updatedOwner.getBirthDate());
        this.setEmail(updatedOwner.getEmail());
        this.setName(updatedOwner.getName());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        this.getVehicles().add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        if(!this.getVehicles().contains(vehicle)){
            throw new IllegalArgumentException("O usuário de cpf " + this.getCpf() + " não possui o veículo de id: " + vehicle.getId());
        }

        this.getVehicles().remove(vehicle);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
