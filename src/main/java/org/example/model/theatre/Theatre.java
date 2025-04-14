package org.example.model.theatre;

import org.example.enums.City;

import java.util.ArrayList;
import java.util.List;

public class Theatre {
    private int theatreId;
    private String theatreName;
    private String address;
    private City city;
    List<Screen> screens = new ArrayList<>();
    List<Show> shows = new ArrayList<>();

    public Theatre(int theatreId, String theatreName, String address, City city, List<Screen> screens, List<Show> shows) {
        this.theatreId = theatreId;
        this.theatreName = theatreName;
        this.address = address;
        this.city = city;
        this.screens = screens;
        this.shows = shows;
    }

    public Theatre() {
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

}
