package com.epf.rentmanager.model;
public class Vehicle {

    private long id;
    private String constructor;
    private String modele;
    private int seats;
    private boolean reserved;

    public Vehicle(long id, String constructor, String modele, int seats) {
        this.id = id;
        this.constructor = constructor;
        this.modele = modele;
        this.seats = seats;
        this.reserved = false;
    }
    public Vehicle(long id, String constructor, String modele, int seats, boolean reserved) {
        this.id = id;
        this.constructor = constructor;
        this.modele = modele;
        this.seats = seats;
        this.reserved = reserved;
    }
    public Vehicle(String constructor, String modele, int seats) {
        this.constructor = constructor;
        this.modele = modele;
        this.seats = seats;
        this.reserved = false;
    }
    public Vehicle(String constructor, String modele, int seats, boolean reserved) {
        this.constructor = constructor;
        this.modele = modele;
        this.seats = seats;
        this.reserved = reserved;
    }
    public Vehicle() {
        this.id = 000;
        this.constructor = "None";
        this.modele = "None";
        this.seats = 0;
    }
    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", constructor='" + constructor + '\'' +
                ", modele='" + modele + '\'' +
                ", seats=" + seats +
                ", reserved=" + reserved +
                '}';
    }
    public long getId() { return id; }
    public String getConstructor() {
        return constructor;
    }
    public String getModele() { return modele; }
    public int getSeats() {
        return seats;
    }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }
}
