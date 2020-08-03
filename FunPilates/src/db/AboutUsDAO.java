package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.sql.DataSource;

public class AboutUsDAO {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = "";
	
	// 커넥션 메소드
	private Connection getConn() {
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/funpilates");
			con = ds.getConnection();
		} catch (Exception e) {
			System.out.println("DB 접속 실패! : " + e);
		}
		return con;
	} // getConnection()
	
	// 자원해제 메소드
	public void closeResource(){
		try {
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
			if(con!=null) con.close();
		} catch (Exception e) {
			System.out.println("자원 해제 실패! : " + e);
		}
	} // closeResource()

	// CEO Message 정보(사진, 이름, 인삿말) 불오기
	public AboutUsBean getCeoMessage(){
		AboutUsBean cmBean = new AboutUsBean();
		try {
			getConn();
			
			sql="select* from ceomessage where ceoNo='1'";
			pstmt = con.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				cmBean.setCeoName(rs.getString("ceoName"));
				cmBean.setCeoImg(rs.getString("ceoImg"));
				cmBean.setCeoMessage(rs.getString("ceoMessage"));
			}
		} catch (Exception e) {
			System.out.println("getCeoMessage()내부에서 에러 발생" + e);
		} finally {
			closeResource();
		}
		return cmBean;
		
	} // getCeoMessage()

	// CEO Message 정보(사진, 이름, 인삿말) 저장 (=>이전 인사말 덮어 쓰기)
	public void updateCeoMessage(AboutUsBean cmBean){
		try {
			getConn();
			
			sql="update ceoMessage set ceoName=?, ceoImg=?, ceoMessage=? where ceoNo='1'";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, cmBean.getCeoName());
			pstmt.setString(2, cmBean.getCeoImg());
			pstmt.setString(3, cmBean.getCeoMessage());
				
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("insertCeoMessage()내부에서 에러 발생" + e);
		} finally {
			closeResource();
		}
		
	} // updateCeoMessage()
	
	// facilites 정보 가져오기
	public AboutUsBean getFacilites() {
		AboutUsBean fBean = new AboutUsBean();
		try {
			getConn();
			
			sql="select* from facilites where facilitesNo='1'";
			pstmt = con.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				fBean.setFacilitesImg1(rs.getString("facilitesImg1"));
				fBean.setFacilitesImg1cap(rs.getString("facilitesImg1cap"));
				fBean.setFacilitesImg2(rs.getString("facilitesImg2"));
				fBean.setFacilitesImg2cap(rs.getString("facilitesImg2cap"));
				fBean.setFacilitesImg3(rs.getString("facilitesImg3"));
				fBean.setFacilitesImg3cap(rs.getString("facilitesImg3cap"));
				fBean.setFacilitesExplain(rs.getString("facilitesExplain"));
			}
		} catch (Exception e) {
			System.out.println("getFacilites()내부에서 에러 발생" + e);
		} finally {
			closeResource();
		}
		return fBean;
	}

	// Facilites 정보(사진 설명) 저장 (=>이전 인사말 덮어 쓰기)
	public void updateFacilites(AboutUsBean fBean) {
		try {
			getConn();
			
			sql="update facilites set facilitesImg1=?, facilitesImg1cap=?, "
					+ "facilitesImg2=?, facilitesImg2cap=?, "
					+ "facilitesImg3=?, facilitesImg3cap=?, "
					+ "facilitesExplain=? "
				+ "where facilitesNo='1' ";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, fBean.getFacilitesImg1());
			pstmt.setString(2, fBean.getFacilitesImg1cap());
			pstmt.setString(3, fBean.getFacilitesImg2());
			pstmt.setString(4, fBean.getFacilitesImg2cap());
			pstmt.setString(5, fBean.getFacilitesImg3());
			pstmt.setString(6, fBean.getFacilitesImg3cap());
			pstmt.setString(7, fBean.getFacilitesExplain());
			
				
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateFacilites()내부에서 에러 발생" + e);
		} finally {
			closeResource();
		}
		
	} // updateFacilites()	
	
	
	// 회사 정보(이름, 연락처, 위치) 불러오기
	public AboutUsBean getCo_Info() {
		AboutUsBean cuBean = new AboutUsBean();
		try {
			getConn();
			
			sql = "select * from co_info";
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				cuBean.setCo_Name(rs.getString("co_Name"));
				cuBean.setCo_Tel(rs.getString("co_Tel"));
				cuBean.setCo_Fax(rs.getString("co_Fax"));
				cuBean.setCo_Email(rs.getString("co_Email"));
				cuBean.setCo_Postcode(rs.getInt("co_Postcode"));
				cuBean.setCo_RoadAddress(rs.getString("co_RoadAddress"));
				cuBean.setCo_DetailAddress(rs.getString("co_DetailAddress"));
			}
		} catch (Exception e) {
			System.out.println("getCo_Info()메소드 내부에서 SQL문 에러 발생");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		return cuBean;
	} // getCompanyInfo();

	// 회사 정보(이름, 연락처, 위치) 저장
	public void updateCo_Info(AboutUsBean co_Bean) {
		try {
			 getConn();
			
			 sql = "update co_info set co_Name=?, co_Tel=?, co_Fax=?, co_Email=?, "+
									  "co_Postcode=?, co_RoadAddress=?, co_DetailAddress=? "+
		 		   "where co_No='1'";
			
			 pstmt = con.prepareStatement(sql);
			
			 pstmt.setString(1,co_Bean.getCo_Name());
			 pstmt.setString(2, co_Bean.getCo_Tel());
			 pstmt.setString(3, co_Bean.getCo_Fax());
			 pstmt.setString(4, co_Bean.getCo_Email());
			 pstmt.setInt(5, co_Bean.getCo_Postcode());
			 pstmt.setString(6, co_Bean.getCo_RoadAddress());
			 pstmt.setString(7, co_Bean.getCo_DetailAddress());
				
			 pstmt.executeUpdate();
			 
		} catch (Exception e) {
			System.out.println("updateCo_Info()내부에서 에러 발생" + e);
		} finally {
			closeResource();
		}
		
	} // updateCo_Info()


	
}

