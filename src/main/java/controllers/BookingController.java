/**
 * Copyright (C) 2015 Ralf Schindelasch.
 */

package controllers;

import models.Booking;
import models.BookingDto;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.SecureFilter;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import dao.BookingDao;
import etc.LoggedInUser;

@Singleton
public class BookingController {
    
    @Inject
    BookingDao bookingDao;

    ///////////////////////////////////////////////////////////////////////////
    // Show time booking
    ///////////////////////////////////////////////////////////////////////////
    public Result bookingShow(@PathParam("id") Long id) {

        Booking booking = null;

        if (id != null) {

            booking = bookingDao.getBooking(id);

        }

        Result result = Results.html();
        result.render("booking", booking);
        result.render("user", booking.getUser());
        return result;

    }
    
    ///////////////////////////////////////////////////////////////////////////
    // Create new time booking
    ///////////////////////////////////////////////////////////////////////////
    @FilterWith(SecureFilter.class)
    public Result bookingNew() {

        return Results.html();

    }

    @FilterWith(SecureFilter.class)
    public Result bookingNewPost(@LoggedInUser String username,
                                 Context context,
                                 @JSR303Validation BookingDto bookingDto,
                                 Validation validation) {

        if (validation.hasViolations()) {

            context.getFlashScope().error("Please correct field.");
            context.getFlashScope().put("title", bookingDto.title);
            context.getFlashScope().put("comment", bookingDto.comment);

            return Results.redirect("/booking/new");

        } else {
            
            bookingDao.postBooking(username, bookingDto);
            
            context.getFlashScope().success("New booking created.");
            
            return Results.redirect("/");

        }

    }

}
