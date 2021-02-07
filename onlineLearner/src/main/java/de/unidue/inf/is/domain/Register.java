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

    public Register(short uid, short kid) {
        this.uid = uid;
        this.kid = kid;
        timestamp = null;
    }

    public void setUid(short uid) {
        this.uid = uid;
    }

    public void setKid(short kid) {
        this.kid = kid;
    }

    public void setTimestamp(Date timestamp) {
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
