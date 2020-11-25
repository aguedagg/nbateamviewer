package com.aguedagg.nbateamviewer.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Team implements Parcelable {

    @SerializedName("full_name")
    private String fullName;
    @SerializedName("wins")
    private String wins;
    @SerializedName("losses")
    private String losses;
    @SerializedName("players")
    private List<Player> players;

    public Team() {}

    protected Team(Parcel in) {
        fullName = in.readString();
        wins = in.readString();
        losses = in.readString();
        this.players = new ArrayList<Player>();
        in.readTypedList(players, Player.CREATOR);
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getLosses() {
        return losses;
    }

    public void setLosses(String loses) {
        this.losses = loses;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.wins);
        dest.writeString(this.losses);
        dest.writeTypedList(this.players);
    }

    public static Comparator<Team> TeamWinsComparator = new Comparator<Team>() {

        @Override
        public int compare(Team team1, Team team2) {
            String wins1 = team1.getWins();
            String wins2 = team2.getWins();

            return wins2.compareTo(wins1);
        }
    };

    public static Comparator<Team> TeamLossesComparator = new Comparator<Team>() {

        @Override
        public int compare(Team team1, Team team2) {
            String losses1 = team1.getLosses();
            String losses2 = team2.getLosses();

            return losses2.compareTo(losses1);
        }
    };

    public static Comparator<Team> TeamAZComparator = new Comparator<Team>() {

        @Override
        public int compare(Team team1, Team team2) {
            String name1 = team1.getFullName();
            String name2 = team2.getFullName();

            return name1.compareTo(name2);
        }
    };
}

