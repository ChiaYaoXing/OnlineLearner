package de.unidue.inf.is.domain;

public class Rate {

    private short uid;
    private short sid;
    private short score;
    private String comment;

    public Rate(short uid, short sid, short score, String comment) {
        this.uid = uid;
        this.sid = sid;
        this.score = score;
        this.comment = comment;
    }

    public short getUid() {
        return uid;
    }

    public short getSid() {
        return sid;
    }

    public short getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}
