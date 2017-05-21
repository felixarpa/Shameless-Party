package felixarpa.shamelessapp.domain.model;

import java.util.Date;

import felixarpa.shamelessapp.utils.DateUtil;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Party extends RealmObject {


    // title to know about witch party are you talking about
    private String title;
    // limit hour to arrive home
    @PrimaryKey
    private long hour;
    // amount of money you are giving for each [minutes] minute you are late
    private float moneyAmount;
    // period of time between each finalPayment
    private int minutes;
    // NGO were your are giving
    private String ngo;
    // Latitude of your home
    private double latitude;
    // Longitude of your home
    private double longitude;
    // total of money you pay
    private float finalPayment;
    // the party has been canceled
    private boolean canceled;

    public static final double INVALID_LATLNG = 200.0;

    public static final double LATITUDE_MIN = -89.98155760646617;
    public static final double LATITUDE_MAX = 89.99346179538875;

    public static final double LONGITUDE_MIN = -180.0;
    public static final double LONGITUDE_MAX = 180.0;


    public Party() {}

    public Party(String title, long hour, float moneyAmount, int minutes, String ngo,
                 double latitude, double longitude) {
        this.title = title;
        this.hour = hour;
        this.moneyAmount = moneyAmount;
        this.minutes = minutes;
        this.ngo = ngo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.finalPayment = -1.0f;
        this.canceled = false;
    }



    public String getTitle() {
        return title;
    }

    public long getHour() {
        return hour;
    }

    public float getMoneyAmount() {
        return moneyAmount;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getNgo() {
        return ngo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getFinalPayment() {
        return finalPayment;
    }

    public boolean isCanceled() {
        return canceled;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }
    public void setHour(Date hour) {
        this.hour = hour.getTime();
    }

    public void setMoneyAmount(float moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setNgo(String ngo) {
        this.ngo = ngo;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setFinalPayment(float finalPayment) {
        this.finalPayment = finalPayment;
    }

    public void cancel() {
        finalPayment = 0.0f;
        this.canceled = true;
    }



    @Override
    public String toString() {

        String stringEnd = " to " + ngo;
        if (finalPayment < 0.0f) {
            stringEnd += ". You paid: " + finalPayment + "€.";
        }
        return "Party " + title
                + " at " + DateUtil.getHour(hour)
                + " giving " + moneyAmount + "€ every "
                + minutes + " minutes" + stringEnd;
    }


    public boolean isGoingOn() {
        return !canceled && finalPayment < 0.0f;
    }
}
