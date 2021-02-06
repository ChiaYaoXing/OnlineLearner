package de.unidue.inf.is.domain;

public final class Submission1 {
    private short sid;
    private String text;
    private Assignment assignment;
    private Rate rate;

    public Submission1(String text, Assignment assignment, Rate rate) {
        this.text = text;
        this.assignment = assignment;
        this.rate = rate;
    }


    public void setSid(short sid) {
        this.sid = sid;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Submission1(short sid, String text, Assignment assignment, Rate rate) {
        this.sid = sid;
        this.text = text;
        this.assignment = assignment;
        this.rate = rate;
    }

    public Submission1(short sid, String text, Assignment assignment) {
        this.sid = sid;
        this.text = text;
        this.assignment = assignment;

    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public short getsid() {
        return sid;
    }

    public Submission1(String text, Assignment assignment) {
        this.text = text;
        this.assignment = assignment;
    }

    public Submission1(String text) {
        this.text = text;
    }

    public Submission1(short sid, String text) {
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
