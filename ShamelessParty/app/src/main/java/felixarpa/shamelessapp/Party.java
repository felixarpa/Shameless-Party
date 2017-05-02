package felixarpa.shamelessapp;

import java.util.Date;

import io.realm.RealmObject;

public class Party extends RealmObject {

    private String title;
    private Date hour;
    private float moneyAmount;
    private int minutes;
    private NonGovernmentalOrganization ngo;

    public Party() {
    }

    public Party(String title, Date hour, float moneyAmount, int minutes, NonGovernmentalOrganization ngo) {
        this.title = title;
        this.hour = hour;
        this.moneyAmount = moneyAmount;
        this.minutes = minutes;
        this.ngo = ngo;
    }

    public String getTitle() {
        return title;
    }

    public Date getHour() {
        return hour;
    }

    public float getMoneyAmount() {
        return moneyAmount;
    }

    public int getMinutes() {
        return minutes;
    }

    public NonGovernmentalOrganization getNgo() {
        return ngo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHour(Date hour) {
        this.hour = hour;
    }

    public void setMoneyAmount(float moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setNgo(NonGovernmentalOrganization ngo) {
        this.ngo = ngo;
    }

    @Override
    public String toString() {
        return "Party " + title
                + " at " + DateUtil.getHour(hour)
                + " giving " + moneyAmount + "€ every "
                + minutes +" minutes to "
                + ngo.toString();
    }
}
