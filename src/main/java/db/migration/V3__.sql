-- the second script that will be run by Ninja's migration engine script
create table Booking (
    id bigint generated by default as identity,
    comment varchar(5000),
    postedAt timestamp,
    bookingDate date,
    startTime time,
    endTime time,
    duration time,
    title varchar(255),
    user_id bigint not null,
    primary key (id)
);

alter table Booking
add constraint FK_booking_user
foreign key (user_id)
references User;

create table Booking_userIds (
    Booking_id bigint not null,
    userIds bigint
);
   
alter table Booking_userIds 
add constraint FK_f9ivk719aqb0rqd8my08loev8 
foreign key (Booking_id) 
references Booking;