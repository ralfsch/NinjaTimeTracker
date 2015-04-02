/**
 * Copyright (C) 2015 Ralf Schindelasch.
 */

package controllers;

import java.util.Iterator;

import models.Booking;
import models.BookingDto;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.SecureFilter;
import ninja.params.PathParam;
import ninja.validation.FieldViolation;
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

            String errorMsg = "Please correct field(s):";
            if(validation.hasBeanViolations())
            {
                FieldViolation violation;
                for ( Iterator<FieldViolation> i = validation.getBeanViolations().iterator(); i.hasNext(); )
                {
                	violation=i.next();
                	errorMsg = errorMsg + "\n" + violation.field + " (" + violation.constraintViolation.getMessageKey() + ")";
                	System.out.println("\n#*#*#*#*# violation: " + violation.field);
                	System.out.println("      default Message: " + violation.constraintViolation.getDefaultMessage());
                	System.out.println("           MessageKey: " + violation.constraintViolation.getMessageKey());
                	System.out.println("             FieldKey: " + violation.constraintViolation.getFieldKey());
                	System.out.println("        MessageParams: " + violation.constraintViolation.getMessageParams());
                }
            }
            context.getFlashScope().error(errorMsg);
            context.getFlashScope().put("title", bookingDto.title);
            context.getFlashScope().put("comment", bookingDto.comment);
            context.getFlashScope().put("startTime", bookingDto.startTime);
            context.getFlashScope().put("endTime", bookingDto.endTime);
            context.getFlashScope().put("date", bookingDto.date);

            return Results.redirect("/booking/new");

        } else {
            
            bookingDao.postBooking(username, bookingDto);
            
            context.getFlashScope().success("New booking created.");
            
            return Results.redirect("/");

        }

    }

}
