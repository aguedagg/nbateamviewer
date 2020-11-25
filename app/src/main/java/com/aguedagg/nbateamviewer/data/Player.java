package com.aguedagg.nbateamviewer.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("position")
    private String position;
    @SerializedName("number")
    private String number;

    protected Player(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        position = in.readString();
        number = in.readString();
    }

    public Player(String name, String last, String position, String number) {
        this.firstName = name;
        this.lastName = last;
        this.position = position;
        this.number = number;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.position);
        dest.writeString(this.number);
    }

    public void setFirstName(String name) { this.firstName = name; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

