package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.google.common.collect.Lists;

@Entity
public class Booking {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String title;
    @Column(name = "bookingDate")
    private Date date;
    private Date startTime;
    private Date endTime;
    private Date postedAt;
    private Date duration;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="USER_ID", nullable=false)
    private User user;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="AUTHOR_ID", nullable=false)
    private User author;
    
    @Column(length = 5000) //init with VARCHAR(1000)
    private String comment;
    
    @ElementCollection(fetch=FetchType.EAGER)
    private List<Long> userIds;

	public User getUser() {
		return this.user;
	}
 
	public void setUser(User user) {
		this.user = user;
	}
 
	public User getAuthor() {
		return this.author;
	}
 
	public void setAuthor(User author) {
		this.author = author;
	}
 
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public long getFirstUserId(){
		return getUserIds().get(0); 
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getDuration() {
		return duration;
	}
	
	private Date convertTimeStringToDate(String time)
	{
        SimpleDateFormat myDateFormat = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        try {
        	return myDateFormat.parse(time);
        }
        catch(ParseException pe)
        {
        	return null;
        }
		
	}
	
	private Date calculateDuration()
	{
		DateTime start = new DateTime(startTime);
		DateTime end = new DateTime(endTime);
		Period period = new Period(start, end);
		
		PeriodFormatter hoursAndMinutes = new PeriodFormatterBuilder()
		     .printZeroAlways()
		     .appendHours()
		     .appendSeparator(":")
		     .appendMinutes()
		     .toFormatter();
		String dur = hoursAndMinutes.print(period);	
		return convertTimeStringToDate(dur);
	}

	public Booking() {}
    
    public Booking(User author, User user, String title, String comment, Date date, String startTime, String endTime) {
        this.userIds = Lists.newArrayList(user.getId());
        this.title = title;
        this.comment = comment;
        this.date = date;
        this.startTime = convertTimeStringToDate(startTime);
        this.endTime = convertTimeStringToDate(endTime);
        
        this.duration = calculateDuration();
        
        this.user = user;

        this.author = author;
        this.postedAt = new Date();
    }
 
}