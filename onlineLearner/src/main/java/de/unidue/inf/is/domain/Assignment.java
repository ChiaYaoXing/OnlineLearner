package de.unidue.inf.is.domain;

public final class Assignment {
    private short kid;
    private short aid;
    private String name;
    private String description;

    public Assignment(short kid, short aid, String name, String description) {
        this.kid = kid;
        this.aid = aid;
        this.name = name;
        this.description = description;
    }

    public Assignment(short kid, String name, String description) {
        this.kid = kid;
        this.name = name;
        this.description = description;
    }

    public short getKid() {
        return kid;
    }

    public short getAid() {
        return aid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
