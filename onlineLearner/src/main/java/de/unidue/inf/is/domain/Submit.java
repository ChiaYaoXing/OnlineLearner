package de.unidue.inf.is.domain;

public final class Submit {
    private short uid;
    private short kid;
    private short aid;
    private short sid;

    public short getUid() {
        return uid;
    }

    public short getKid() {
        return kid;
    }

    public short getAid() {
        return aid;
    }

    public short getSid() {
        return sid;
    }

    public Submit(short uid, short kid, short aid, short sid) {
        this.uid = uid;
        this.kid = kid;
        this.aid = aid;
        this.sid = sid;
    }
}
