package de.unidue.inf.is.domain;

public final class Course {
    private short kid;
    private String name;
    private String description;
    private String passwort;
    private short freeplace;
    private short cid;

    public Course(short kid, String name, String description, String passwort, short freeplace, short cid) {
        this.kid = kid;
        this.name = name;
        this.description = description;
        this.passwort = passwort;
        this.freeplace = freeplace;
        this.cid = cid;
    }

    public Course(String name, String description, String passwort, short freeplace, short cid) {
        this.name = name;
        this.description = description;
        this.passwort = passwort;
        this.freeplace = freeplace;
        this.cid = cid;
    }

    public void setKid(short kid) {
        this.kid = kid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public void setFreeplace(short freeplace) {
        this.freeplace = freeplace;
    }

    public void setCid(short cid) {
        this.cid = cid;
    }

    public short getKid() {
        return kid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPasswort() {
        return passwort;
    }

    public short getFreeplace() {
        return freeplace;
    }

    public short getCid() {
        return cid;
    }
}
