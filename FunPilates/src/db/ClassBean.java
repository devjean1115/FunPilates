package db;

public class ClassBean {
	private String id;
	private String name;
	private String level;
	private String reservClass;
	private String reservDate;
	private String reservDay;
	private String reservTime;
	private String morningTime;
	private String dayTime;
	private String nightTime;

	private int attendance;
	private int remaining;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReservTime() {
		return reservTime;
	}
	public void setReservTime(String reservTime) {
		this.reservTime = reservTime;
	}	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getReservClass() {
		return reservClass;
	}
	public void setReservClass(String reservClass) {
		this.reservClass = reservClass;
	}
	public String getReservDate() {
		return reservDate;
	}
	public void setReservDate(String reservDate) {
		this.reservDate = reservDate;
	}
	public String getReservDay() {
		return reservDay;
	}
	public String getDayTime() {
		return dayTime;
	}
	public void setDayTime(String dayTime) {
		this.dayTime = dayTime;
	}
	public String getMorningTime() {
		return morningTime;
	}
	public void setMorningTime(String morningTime) {
		this.morningTime = morningTime;
	}
	public String getNightTime() {
		return nightTime;
	}
	public void setNightTime(String nightTime) {
		this.nightTime = nightTime;
	}
	public void setReservDay(String reservDay) {
		this.reservDay = reservDay;
	}
	public int getAttendance() {
		return attendance;
	}
	public void setAttendance(int attendance) {
		this.attendance = attendance;
	}
	public int getRemaining() {
		return remaining;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}
	

	
	
	
}
