package com.codextech.kabir.bills_app.models;

public class Tenant {

    private String fname;
    private String lname;
    private String email;
    private String id;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
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

    public Tenant(String fname, String lname, String email, String id) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.id = id;
    }
}
