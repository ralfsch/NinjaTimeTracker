package models;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class BookingDto {

    @Size(min = 4)
    public String title;
    
//    @Size(min = 5)
    public String comment;
    
    @NotNull
    public Date date;
    @NotEmpty
    public String startTime;
    @NotEmpty
    public String endTime;
    
    public Long userId;
    public Long authorId;
    
    public BookingDto() {}

}
