package org.example.model.theatre;

import org.example.enums.City;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheatreFactory {
    public static Theatre createTheatre(int theatreId, String name, City city, List<Show> shows){
        Theatre theatre = new Theatre();
        theatre.setTheatreId(theatreId);
        theatre.setTheatreName(name);
        theatre.setCity(city);
        theatre.setShows(shows);
        theatre.setScreens(createScreen());

        return theatre;
    }

    private static List<Screen> createScreen() {
        Screen screen = new Screen();
        screen.setScreenId(1);
        screen.setSeats(generateSeats(100));
        return Arrays.asList(screen);
    }

    private static List<Seat> generateSeats(int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < totalSeats; i++) {
            Seat seat = new Seat();
            seats.add(seat);
        }
        return seats;
    }
}
