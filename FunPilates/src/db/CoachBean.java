package db;

public class CoachBean {
	private String sel ;
	private int id;
	private String password;
	private int phoneNumber;
	
public String getSel() {
		return sel;
	}
	public void setSel(String sel) {
		this.sel = sel;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	// 	기본정보
	//////////////////////////////////////////////
	//  프로필
	
	private int coachNo;
	private String className;
	private	String position;
	private String coachName; 
	private String coachNickName;
	private String career; 
	private String pfImg; 
	private String bodyPfImg;
	
	public int getCoachNo() {
		return coachNo;
	}
	public void setCoachNo(int coachNo) {
		this.coachNo = coachNo;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
	public String getCoachNickName() {
		return coachNickName;
	}
	public void setCoachNickName(String coachNickName) {
		this.coachNickName = coachNickName;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getPfImg() {
		return pfImg;
	}
	public void setPfImg(String pfImg) {
		this.pfImg = pfImg;
	}
	public String getBodyPfImg() {
		return bodyPfImg;
	}
	public void setBodyPfImg(String bodyPfImg) {
		this.bodyPfImg = bodyPfImg;
	}
	

}