package de.unidue.inf.is.domain;

import java.util.Date;

public final class Register {
    private short uid;
    private short kid;
    private Date timestamp;

    public Register(short uid, short kid, Date timestamp) {
        this.uid = uid;
        this.kid = kid;
        this.timestamp = timestamp;
    }

    public short getUid() {
        return uid;
    }

    public short getKid() {
        return kid;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
