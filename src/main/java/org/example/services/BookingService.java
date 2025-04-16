package org.example.services;

import org.example.controller.MovieController;
import org.example.controller.TheatreController;
import org.example.enums.City;
import org.example.model.movies.Movie;
import org.example.model.theatre.Show;
import org.example.model.theatre.Theatre;
import org.example.utility.BookingDataFactory;

import java.util.*;
import java.time.LocalDate;

/**
 * BookingService handles the booking workflow: city selection, movie selection, show selection,
 * seat booking, payment, and ticket generation.
 * Implements Singleton pattern to ensure only one instance exists.
 */
public class BookingService {

    private static BookingService instance;

    private final MovieController movieController;
    private final TheatreController theatreController;
    private final Scanner scanner;

    // Private constructor to enforce singleton pattern
    private BookingService() {
        movieController = new MovieController();
        theatreController = new TheatreController();
        scanner = new Scanner(System.in);
    }

    // Returns the singleton instance
    public static BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    // Initializes mock data using the factory
    public void initialize() {
        BookingDataFactory.createMovies(movieController);
        BookingDataFactory.createTheatres(movieController, theatreController);
    }

    // Starts the booking session loop
    public void startBookingSession() {
        printHeader("Welcome to BookMyShow");

        boolean continueBooking = true;

        while (continueBooking) {
            City userCity = selectCity();
            Movie selectedMovie = selectMovie(userCity);
            Show selectedShow = selectShow(userCity, selectedMovie);
            bookSeat(selectedShow);

            System.out.print("Do you want to book another ticket? (yes/no): ");
            String response = scanner.next().trim().toLowerCase();
            continueBooking = response.equals("yes");
        }

        printSuccess("Thank you for using BookMyShow! Have a great day!");
    }

    // Displays list of cities and returns user's selected city
    private City selectCity() {
        printSection("Select Your City");
        City[] cities = City.values();
        for (int i = 0; i < cities.length; i++) {
            System.out.println("   " + (i + 1) + ". " + cities[i]);
        }
        return cities[getUserChoice(1, cities.length) - 1];
    }

    // Displays movies in selected city and returns user's selected movie
    private Movie selectMovie(City city) {
        List<Movie> movies = movieController.getMoviesByCity(city);
        printSection("Available Movies in " + city);
        for (int i = 0; i < movies.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + movies.get(i).getMovieName());
        }
        return movies.get(getUserChoice(1, movies.size()) - 1);
    }

    // Displays all available shows for selected movie and returns user's selected show
    private Show selectShow(City city, Movie movie) {
        Map<Theatre, List<Show>> showsMap = theatreController.getAllShow(movie, city);
        List<Show> availableShows = new ArrayList<>();

        printSection("Available Shows for " + movie.getMovieName() + " in " + city);
        int index = 1;
        for (Map.Entry<Theatre, List<Show>> entry : showsMap.entrySet()) {
            Theatre theatre = entry.getKey();
            for (Show show : entry.getValue()) {
                System.out.println("   " + index + ". " + show.getShowStartTime() + " at " + theatre.getTheatreName());
                availableShows.add(show);
                index++;
            }
        }

        return availableShows.get(getUserChoice(1, availableShows.size()) - 1);
    }

    // Handles seat booking with validation and triggers payment + ticket generation
    private void bookSeat(Show show) {
        printSection("Select Your Seat (1-100)");
        int seatNumber = getUserChoice(1, 100);

        if (show.getBookedSeatIds().contains(seatNumber)) {
            System.out.println("Seat already booked! Please try another seat.");
            bookSeat(show); // Retry booking
        } else {
            show.getBookedSeatIds().add(seatNumber);

            PaymentService paymentService = new PaymentService();
            boolean paymentSuccess = paymentService.processPayment(250); // Fixed amount for demo

            if (paymentSuccess) {
                printSuccess(" Booking Successful! Enjoy your movie!");
                generateTicket(show, seatNumber);
            } else {
                System.out.println("Payment failed! Please try again.");
                show.getBookedSeatIds().remove(seatNumber); // Rollback
            }
        }
    }

    // Displays ticket confirmation details
    private void generateTicket(Show show, int seatNumber) {
        System.out.println("\n========================================");
        System.out.println("🎟️       MOVIE TICKET CONFIRMATION       🎟️");
        System.out.println("========================================");
        System.out.println("🎬 Movie: " + show.getMovie().getMovieName());
        System.out.println("⏰ Show Time: " + show.getShowStartTime() + ":00");
        System.out.println("💺 Seat Number: " + seatNumber);
        System.out.println("----------------------------------------");
        System.out.println("📅 Date: " + LocalDate.now());
        System.out.println("🆔 Booking ID: " + UUID.randomUUID());
        System.out.println("========================================");
        System.out.println("🎉 Enjoy your movie! 🍿 Have a great time!");
        System.out.println("========================================\n");
    }

    // Utility to get validated user input within given range
    private int getUserChoice(int min, int max) {
        int choice;
        do {
            System.out.print("👉 Enter choice (" + min + "-" + max + "): ");
            while (!scanner.hasNextInt()) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < min || choice > max);
        return choice;
    }

    // UI Utilities
    private void printHeader(String text) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("          " + text);
        System.out.println("══════════════════════════════════════════\n");
    }

    private void printSection(String text) {
        System.out.println("\n " + text);
        System.out.println("──────────────────────────────────────────");
    }

    private void printSuccess(String text) {
        System.out.println("\n " + text + "\n");
    }
}
