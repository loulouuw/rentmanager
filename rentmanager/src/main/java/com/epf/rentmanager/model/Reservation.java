package com.epf.rentmanager.model;
import com.epf.rentmanager.service.VehicleService;

import java.time.LocalDate;

public class Reservation {
    private long id;
    private Client client;
    private Vehicle vehicle;
    private LocalDate beginning;
    private LocalDate ending;

    public Reservation(long id, Client client, Vehicle vehicle, LocalDate beginning, LocalDate ending) {
        this.id = id;
        this.client = client;
        this.vehicle = vehicle;
        this.beginning = beginning;
        this.ending = ending;
    }

    public Reservation(Client client, Vehicle vehicle, LocalDate beginning, LocalDate ending) {
        this.client = client;
        this.vehicle = vehicle;
        this.beginning = beginning;
        this.ending = ending;
    }

    public Reservation() {
        this.id = 000;
        this.client = new Client();
        this.vehicle = new Vehicle();
        this.beginning = LocalDate.now();
        this.ending = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + client +
                ", vehicle=" + vehicle +
                ", beginning=" + beginning +
                ", ending=" + ending +
                '}';
    }

    public long getId() { return id; }
    public Client getClient() {
        return client;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public LocalDate getBeginning() {
        return beginning;
    }
    public LocalDate getEnding() {
        return ending;
    }
    public boolean isReserved(){
        return (LocalDate.now().isAfter(this.beginning) && LocalDate.now().isBefore(this.ending));
    }
}
