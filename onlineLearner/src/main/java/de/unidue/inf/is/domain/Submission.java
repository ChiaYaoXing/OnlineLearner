package de.unidue.inf.is.domain;

public final class Submission {
    private short sid;
    private String text;




    public void setSid(short sid) {
        this.sid = sid;
    }


    public short getsid() {
        return sid;
    }


    public Submission(String text) {
        this.text = text;
    }

    public Submission(short sid, String text) {
        this.sid = sid;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
