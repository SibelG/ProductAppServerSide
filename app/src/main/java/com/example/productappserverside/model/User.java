package com.example.productappserverside.model;


import android.widget.EditText;

public class User {
    private String name;
    private String pass;
    private String email;
    private String isStaff;
    private String secureCode;

    public User(String name, String pass, String email,String isStaff) {
        this.name = name;
        this.pass = pass;
        this.email=email;
        this.isStaff="false";
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

