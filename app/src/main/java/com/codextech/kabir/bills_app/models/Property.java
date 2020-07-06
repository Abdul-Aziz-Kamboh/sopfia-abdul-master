package com.codextech.kabir.bills_app.models;

public class Property {

    private String pname;
    private String id;

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Property(String pname, String id) {
        this.pname = pname;
        this.id = id;
    }
}
