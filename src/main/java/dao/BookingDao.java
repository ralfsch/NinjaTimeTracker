/**
 * Copyright (C) 2015 Ralf Schindelasch
 */

package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import models.Booking;
import models.BookingDto;
import models.BookingsDto;
import models.User;
import ninja.jpa.UnitOfWork;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class BookingDao {
   
    @Inject
    Provider<EntityManager> entityManagerProvider;
    
    @UnitOfWork
    public BookingsDto getAllBookings() {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        TypedQuery<Booking> query= entityManager.createQuery("SELECT x FROM Booking x", Booking.class);
        List<Booking> bookings = query.getResultList();        

        BookingsDto bookingsDto = new BookingsDto();
        bookingsDto.bookings = bookings;
        
        return bookingsDto;
        
    }
    
    @UnitOfWork
    public Booking getFirstBookingForFrontPage() {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM Booking x ORDER BY x.postedAt DESC");
        Booking booking = (Booking) q.setMaxResults(1).getSingleResult();      
        
        return booking;
        
        
    }
    
    @UnitOfWork
    public List<Booking> getOlderBookingsForFrontPage() {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM Booking x ORDER BY x.postedAt DESC");
        List<Booking> bookings = (List<Booking>) q.setFirstResult(1).setMaxResults(10).getResultList();        
        
        return bookings;
        
        
    }
    
    @UnitOfWork
    public List<Booking> getAllBookingsForFrontPage() {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM Booking x ORDER BY x.postedAt DESC");
        List<Booking> bookings = (List<Booking>) q.setMaxResults(10).getResultList();        
        
        return bookings;
        
        
    }
    
    @UnitOfWork
    public Booking getBooking(Long id) {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query q = entityManager.createQuery("SELECT x FROM Booking x WHERE x.id = :idParam");
        Booking booking = (Booking) q.setParameter("idParam", id).getSingleResult();        
        
        return booking;
        
        
    }
    
    /**
     * Returns false if user cannot be found in database.
     */
    @Transactional
    public boolean postBooking(String username, BookingDto bookingDto) {
        
        EntityManager entityManager = entityManagerProvider.get();
        
        Query query = entityManager.createQuery("SELECT x FROM User x WHERE username = :usernameParam");
        User user = (User) query.setParameter("usernameParam", username).getSingleResult();
        
        if (user == null) {
            return false;
        }
        
        Booking booking = new Booking(user, bookingDto.title, bookingDto.comment, bookingDto.date, bookingDto.startTime, bookingDto.endTime);
        System.out.println("New Booking to create: \n   Title=" + bookingDto.title + "\n   Date=" + bookingDto.date + "\n   startTime=" + bookingDto.startTime + "\n   endTime=" + bookingDto.endTime +"\n   username=" + user.getUsername() +"\n   fullname=" + user.getFullname() +"\n");
        entityManager.persist(booking);
        
        return true;
        
    }

}
