package de.unidue.inf.is.domain;

public final class User {

    private String name;
    private String email;
    private short uid;


    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, short uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(short uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public short getUid(){
        return uid;
    }

}