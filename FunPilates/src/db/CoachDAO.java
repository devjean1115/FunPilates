package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class CoachDAO {
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = "";
	
	// 커넥션 메소드
	private Connection getConn(){
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/funpilates");
			con = ds.getConnection();
		} catch (Exception e) {
			System.out.println("DB 접속 실패! : ");
			e.printStackTrace();
		}
		return con;
	} // getConnection()
	
	// 자원해제 메소드
	public void closeResource(){
		try {
			if(rs!=null)rs.close();
			if(pstmt!=null)pstmt.close();
			if(con!=null)con.close();
		} catch (Exception e) {
			System.out.println("자원 해제 실패! : ");
			e.printStackTrace();
		}
	} // closeResource()
	
	// 코치진 프로필 리스트 벡터타입으로 가져오기
	public Vector<CoachBean> getCoachPfList(){
		Vector<CoachBean> v = new Vector<CoachBean>();
		try {
			getConn();
			
			sql = "select * from coach order by position";
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				CoachBean cbean = new CoachBean();
				cbean.setCoachNo(Integer.parseInt(rs.getString("coachNo")));
				cbean.setClassName(rs.getString("className"));
				cbean.setPosition(rs.getString("position"));
				cbean.setCoachName(rs.getString("coachName"));
				cbean.setCoachNickName(rs.getString("coachNickName"));
				cbean.setCareer(rs.getString("career"));
				cbean.setPfImg(rs.getString("pfImg"));
				
				v.add(cbean);
			}
		} catch (Exception e) {
			System.out.println("getCoachList()메소드 내부에서 SQL문 에러 발생");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		return v;
	} // getCoachPfList()

	// 코치 프로필 추가 저장
	public void addCoachPf(CoachBean coachBean){
		try {
			getConn();
			
			sql= "insert into coach " +
					          "values(null,?,?,?,?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, coachBean.getClassName());
			pstmt.setString(2, coachBean.getPosition());
			pstmt.setString(3, coachBean.getCoachName());
			pstmt.setString(4, coachBean.getCoachNickName());
			pstmt.setString(5, coachBean.getCareer());
			pstmt.setString(6, coachBean.getPfImg());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("addCoachPf메소드 내부에서 SQL문 에러 발생");
			e.printStackTrace();
		} finally {
			closeResource();
		}
	} // updateCoachPf()	
	
	// 수정할 코치(1인) 프로필 가져오기
	public CoachBean getCoachPf(int coachNo) {
		CoachBean cbean = new CoachBean();
		try {
			getConn();
			
			sql = "select * from coach where coachno=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, coachNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				cbean.setCoachNo(Integer.parseInt(rs.getString("coachNo")));
				cbean.setClassName(rs.getString("className"));
				cbean.setPosition(rs.getString("position"));
				cbean.setCoachName(rs.getString("coachName"));
				cbean.setCoachNickName(rs.getString("coachNickName"));
				cbean.setCareer(rs.getString("career"));
				cbean.setPfImg(rs.getString("pfImg"));
			}
		} catch (Exception e) {
			System.out.println("getCoachPf()메소드 내부에서 SQL문 에러 발생");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		return cbean;
	} // getCoachPf()

	// 수정된 코치 프로필 저장
	public void updateCoachPf(CoachBean coachBean){
		try {
			getConn();
			
			sql= "update coach set className=?, position=?, coachName=?, "+
					"coachNickName=?, career=?, pfImg=? " +
				     "where coachNo=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, coachBean.getClassName());
			pstmt.setString(2, coachBean.getPosition());
			pstmt.setString(3, coachBean.getCoachName());
			pstmt.setString(4, coachBean.getCoachNickName());
			pstmt.setString(5, coachBean.getCareer());
			pstmt.setString(6, coachBean.getPfImg());
			pstmt.setInt(7, coachBean.getCoachNo());
			
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateCoachPf메소드 내부에서 SQL문 에러 발생");
			e.printStackTrace();
		} finally {
			closeResource();
		}
	} // updateCoachPf()

	// 선택한 코치 프로필 삭제
	public void delCoachPf(int coachNo) {
		try {
			 getConn();
			 
			 sql="delete from coach where coachNo=?";
			 
			 pstmt=con.prepareStatement(sql);
			 pstmt.setInt(1, coachNo);
			 
			 pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("delCoachPf()메소드 내부에서 SQL문 에러 발생");
			e.printStackTrace();
		} finally {
			closeResource();
		}
	} // delCoachPf()

}
