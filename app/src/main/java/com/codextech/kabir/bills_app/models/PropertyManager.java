package com.codextech.kabir.bills_app.models;

public class PropertyManager {

    private String name;
    private String lname;
    private String email;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PropertyManager(String name, String lname, String email, String id) {
        this.name = name;
        this.lname = lname;
        this.email = email;
        this.id = id;
    }
}
