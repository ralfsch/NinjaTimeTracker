package models;

import java.util.Date;

import javax.validation.constraints.Size;

import org.joda.time.LocalTime;

public class BookingDto {

    @Size(min = 5)
    public String title;
    
//    @Size(min = 5)
    public String comment;
    
    public Date date;
    public String startTime;
    public String endTime;
    
    public Long userId;
    public Long authorId;
    
    public BookingDto() {}

}
