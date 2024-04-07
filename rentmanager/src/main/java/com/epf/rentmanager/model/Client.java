package com.epf.rentmanager.model;
import java.time.LocalDate;

public class Client {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate bdate;

    public Client(long id, String firstname, String lastname, String email, LocalDate bdate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.bdate = bdate;
    }

    public Client(String firstname, String lastname, String email, LocalDate bdate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.bdate = bdate;
    }

    public Client() {
        this.id = 000;
        this.firstname = "None";
        this.lastname = "None";
        this.email = "None";
        this.bdate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", bdate=" + bdate +
                '}';
    }

    public long getId() { return id; }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getEmail() {
        return email;
    }
    public LocalDate getBdate() {
        return bdate;
    }
}
