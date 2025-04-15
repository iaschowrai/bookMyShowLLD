package org.example.controller;

import org.example.enums.City;
import org.example.model.movies.Movie;
import org.example.model.theatre.Show;
import org.example.model.theatre.Theatre;

import java.util.*;

/**
 * Controller class to manage theatres across cities and retrieve show listings.
 */
public class TheaterController {

    // Maps each city to its list of theatres
    private final Map<City, List<Theatre>> cityWithTheatre;

    // List to store all theatres (regardless of city)
    private final List<Theatre> allTheatre;

    public TheaterController() {
        cityWithTheatre = new HashMap<>();
        allTheatre = new ArrayList<>();
    }

    /**
     * Adds a theatre to a city and the global theatre list.
     * Example: addTheatre(new Theatre("PVR", ...), City.DELHI);
     *
     * @param theatre Theatre object
     * @param city    City enum
     */
    public void addTheatre (Theatre theatre, City city){
        allTheatre.add(theatre);
        cityWithTheatre.computeIfAbsent(city, k-> new ArrayList<>()).add(theatre);
    }

    public Map<Theatre, List<Show>> getAllShow(Movie movie, City city){

        // get all theatre from the city
        Map<Theatre, List<Show>> theatreByShow = new HashMap<>();
        if (movie == null || city == null) return theatreByShow;

        List<Theatre> theatres = cityWithTheatre.getOrDefault(city, Collections.emptyList());
        // theatre = Delhi => PVR, INOX
        // Filter the theatres which run the movie

        for (Theatre theatre : theatres){
            List<Show> shows = theatre.getShows();
            if (shows == null || shows.isEmpty()) continue;

            List<Show> givenMovieShows = new ArrayList<>();

            for (Show show: shows){
                if (show.getMovie() != null && show.getMovie().getMovieId() == movie.getMovieId()){
                    givenMovieShows.add(show);
                }
            }
            if(!givenMovieShows.isEmpty()) {
                theatreByShow.put(theatre,givenMovieShows);
            }
        }
        return theatreByShow;
    }




}
