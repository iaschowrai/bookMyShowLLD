package org.example.controller;

import org.example.enums.City;
import org.example.model.movies.Movie;

import javax.print.DocFlavor;
import java.util.*;

public class MovieController {

    private final Map<City, List<Movie>> cityWithMovies;
    private final Map<String, Movie> movieByName; // New: for faster lookups by name
    private final Set<Movie> allMovies;

    public MovieController(){
        cityWithMovies = new HashMap<>();
        movieByName = new HashMap<>();
        allMovies = new HashSet<>();
    }
    // Add Movie
    public void addMovie(Movie movie, City city){
        allMovies.add(movie);
        movieByName.put(movie.getMovieName(), movie); // map for fast lookup


        // this logic in Java 8+
        // cityWithMovies.computeIfAbsent(city, k -> new ArrayList<>()).add(movie);

        List<Movie> movies = cityWithMovies.getOrDefault(city,new ArrayList<>());
        movies.add(movie);
        cityWithMovies.put(city, movies);
    }

    // Get movie by name in O(1)
    public Movie getMovieByName(String movieName){
        return movieByName.get(movieName); // fast and null-safe

//        for (Movie movie :allMovies){
//            if((movie.getMovieName().equals(movieName))){
//                return movie;
//            }
//        }
//        return null;
    }
    // ✅ Get all movies by city
    public List<Movie> getMoviesByCity(City city){
        return cityWithMovies.get(city);
    }


    public boolean updateMovie(String movieName, Movie updatedMovie) {
        Movie exisitingMovie  = movieByName.get(movieName);
        if (exisitingMovie != null) {
            // Replace in allMovies list
            allMovies.remove(exisitingMovie);
            allMovies.add(updatedMovie);


            // Replace in City Mapping
            for (Map.Entry<City, List<Movie>> entry : cityWithMovies.entrySet()) {
                List<Movie> movieList = entry.getValue();
                if (movieList.remove(exisitingMovie)) {
                    movieList.add(updatedMovie);
                    break;
                }
            }

            //update movieByName
            movieByName.remove(movieName);
            movieByName.put(updatedMovie.getMovieName(), updatedMovie);
            return true;
        }
        return false;
    }

    public boolean removeMovie(String movieName){
        Movie movieToRemove = movieByName.remove(movieName);
        if (movieToRemove != null){
            allMovies.remove(movieToRemove);

            //remove from city list
            for (List<Movie> movies : cityWithMovies.values()){
                movies.remove(movieToRemove);
            }
            return true;
        }
        return false;
    }

    // Get all movies
    public List<Movie> getAllMovies(){
        return new ArrayList<>(allMovies);
    }
}
