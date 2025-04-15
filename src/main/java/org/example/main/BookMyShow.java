package org.example.main;

import org.example.services.BookingService;

public class BookMyShow {
    public static void main(String[] args) {
        BookingService bookingService = BookingService.getInstance();
        bookingService.initialize();
        bookingService.startBookingSession();
    }
}
