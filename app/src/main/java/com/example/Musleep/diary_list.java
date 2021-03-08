package com.example.Musleep;

public class diary_list {
    private String date;
    private long wakeup;
    private long coffee;
    private long nap;
    private long wine;
    private long drug;
    private long sport;

    private diary_list(){}
    private diary_list(long coffee,long nap){
        this.date = date;
        this.wakeup = wakeup;
        this.coffee = coffee;
        this.nap = nap;
        this.wine = wine;
        this.drug = drug;
        this.sport = sport;

    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getWakeup() {
        return wakeup;
    }

    public void setWakeup(long wakeup) {
        this.wakeup = wakeup;
    }

    public long getCoffee() {
        return coffee;
    }

    public void setCoffee(long coffee) {
        this.coffee = coffee;
    }

    public long getNap() {
        return nap;
    }

    public void setNap(long nap) {
        this.nap = nap;
    }

    public long getWine() {
        return wine;
    }

    public void setWine(long wine) { this.wine = wine; }

    public long getDrug() {
        return drug;
    }

    public void setDrug(long drug) { this.drug = drug; }

    public long getSport() { return sport; }

    public void setSport(long sport) { this.sport = sport; }
}

