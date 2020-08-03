package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	
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

	// 관리자가 멤버리스트에 멤버존재 여부 또는 회원이 가입 전 대기 리스트에 존재 여부 확인 
	public String phNumberCheck(String check, String phoneNumber) {		
			String result="";
		try {
			getConn();
			
			 sql = "select * from member where phoneNumber=?";
			 
			 pstmt  = con.prepareStatement(sql);
			 pstmt.setString(1, phoneNumber);
			
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()){
				 	result = "oldMember";
			 } else {
				 	result = "waitingMember";
			 		  sql = "select * from waitlist where phoneNumber=?";
			 		  pstmt  = con.prepareStatement(sql);
			 		  pstmt.setString(1, phoneNumber);
			 				
			 		  rs = pstmt.executeQuery();
			 			
			 		 if(!rs.next()){
			 			 if (check.equals("joinCheck")) {
			 			  			result = "notpermitted";
			 			} else {
			 				result ="new";	
			 			}
			 			 
			 		 } 

			 }
		} catch (Exception e) {
			System.out.println("phNumberCheck메소드 내부에서 예외 발생 : " + e);
		}
		return result; 		
	
	} // method()
		
	// 관리가자가 waitlist에 추가 저장 또는 회원 가입으로 member에 추가 저장 
	public void addMember(String purpose, MemberBean mBean) {
		try{
			getConn();
		
			switch(purpose) {
				case "updateWaitList" :
					sql="insert into waitlist values(null, ?, ?, ?, ?, ?, ?, ?, ?)";
			
					pstmt = con.prepareStatement(sql);
				
					pstmt.setString(1, mBean.getSel());
					pstmt.setString(2, mBean.getId());
					pstmt.setString(3, mBean.getName());
					pstmt.setString(4, mBean.getPhoneNumber());
					pstmt.setInt(5, mBean.getMembership());
					pstmt.setString(6, mBean.getBeginDate());
					pstmt.setString(7, mBean.getExpDate());
					pstmt.setString(8, mBean.getLevel());
					break;
			
					case "join" :	
					sql="insert into member values(null,?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					
					pstmt = con.prepareStatement(sql);
					
					pstmt.setString(1, mBean.getSel());
					pstmt.setString(2, mBean.getId());
					pstmt.setString(3, mBean.getPassword());
					pstmt.setString(4, mBean.getName());
					pstmt.setString(5, mBean.getPhoneNumber());
					pstmt.setInt(6, mBean.getMembership());
					pstmt.setString(7, mBean.getBeginDate());
					pstmt.setString(8, mBean.getExpDate());
					pstmt.setString(9, mBean.getLevel());
					pstmt.setInt(10, mBean.getRemaining());
					
					break;
					
			}
			
			pstmt.executeUpdate();
		
		} catch (Exception e) {
			System.out.println("addMember메소드 내부에서 예외 발생 : " + e);
		}
		
	} // method()
		
	// 가입 전 본인 정보 확인 또는 관리자나 회원이 회원 정보 수정을 위해 불러오기 
	public MemberBean getMemberInfo(String purpose, String id) {
		MemberBean mBean = new MemberBean();
		
		try {
			getConn();
			 
			 switch(purpose) {
			 	case "waitingMemberInfo":
			 	case "memberInfoToJoin" :
			 		  sql = "select * from waitlist where id=?";
				break;
				 
			 	case "infoForReservation" :
			 	case "memberInfoToEditByManager" :
			 	case "memberInfoToEditByOneself" : // 비번이 있어야 들어감을 주의하자 수정 해야 할 듯?
			 		  sql = "select * from member where id=?";
				break;
			 }
			 
			 pstmt  = con.prepareStatement(sql);
			
			 pstmt.setString(1, id);
			
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()){
				 switch(purpose) {		
				 	case "infoForReservation" :
				 	case "memberInfoToEditByManager" :
				 	case "memberInfoToEditByOneself" : 
				 		 mBean.setRemaining(Integer.parseInt(rs.getString("remaining")));
					break;
				 }
				 mBean.setNo(rs.getInt("no"));
				 mBean.setSel(rs.getString("sel"));
				 mBean.setId(rs.getString("id"));
				 mBean.setName(rs.getString("name"));
				 mBean.setPhoneNumber(rs.getString("phoneNumber"));
				 mBean.setMembership(Integer.parseInt(rs.getString("membership")));
				 mBean.setBeginDate(rs.getString("beginDate"));
				 mBean.setExpDate(rs.getString("expDate"));
				 mBean.setLevel(rs.getString("level"));
				 
			 } 
			 
		} catch (Exception e) {
			System.out.println("getMemberInfo메소드 내부에서 예외 발생 : " + e);
		}
		return mBean; 		
	} // method()
			
	// 가입 후 가입 대기 리스트에서 가입한 회원 정보 삭제
	public void delMemberInfo(String purpose, String id) {
		try{
			getConn();
		
			switch(purpose) {
				
				case "join":
				case "delWaitingMember":
					sql="delete from waitlist where id=?";
				break;
				
				case "delMember":
					sql="delete from member where id=?";
				break;			
			}
			
		
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
	
			pstmt.executeUpdate();
		
		} catch (Exception e) {
			System.out.println("delMemberInfo메소드 내부에서 예외 발생 : " + e);
		}	
		
	} // method()

	// 수정한 회원 정보 저장
	public void updateMember(String bywhom, String from, MemberBean mBean) {
		try{
			getConn();
			switch(from) {
				case "member" :
					sql = "update member set sel=?, name=?, phoneNumber=?, "+
							   "membership=?, beginDate=?, expDate=?, level=?, "+
							   "id=?, remaining=? "+
							   "where no=?";
				break;
				case "waitlist" :
					sql = "update waitlist set sel=?, name=?, phoneNumber=?, "+
								   "membership=?, beginDate=?, expDate=?, level=?, "+
								   "id=? "+
								   "where no=?";
				break;
			}
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, mBean.getSel());
			pstmt.setString(2, mBean.getName());
			pstmt.setString(3, mBean.getPhoneNumber());
			pstmt.setInt(4, mBean.getMembership());
			pstmt.setString(5, mBean.getBeginDate());
			pstmt.setString(6, mBean.getExpDate());
			pstmt.setString(7, mBean.getLevel());
			pstmt.setString(8, mBean.getId());
			
			switch(from) {
				case "member" :
					pstmt.setInt(9, mBean.getRemaining());
					pstmt.setInt(10, mBean.getNo());
				break;
				case "waitlist" :
					pstmt.setInt(9, mBean.getNo());
				break;
			}
			
			pstmt.executeUpdate();
			
			switch(bywhom) {
				case "updateMemberInfoByOneself" : 
					
					sql = "update member set password=?"+
					     "where no=?";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, mBean.getPassword());
					pstmt.setInt(2, mBean.getNo());
					
					pstmt.executeUpdate();
				break;
			}
			
		} catch (Exception e) {
			System.out.println("updateMember메소드 내부에서 예외 발생 : " + e);
		}	
	} // addMember()
	
	// 로그인시 아이디, 비번 확인
	// 로그인을 위한 로그인 정보 체크
	public String userCheck(String id, String password) {
		String check = "error";  
		
		try {
			 getConn();
			 
			 sql = "select * from member where id=?";
		
			 pstmt  = con.prepareStatement(sql);
			
			 pstmt.setString(1, id);
			
			 rs = pstmt.executeQuery();
			
			 if(rs.next()){	 
			 	if(password.equals(rs.getString("password"))){
			 		check = "login"; //아이디 맞음 , 비밀번호 맞음
			 	} else {	
			 		check = "passwordError"; //아이디 맞음 , 비밀번호 틀림
			 	}
			 } else {//입력한 아이디에 해당하는 레코드가 검색되지 않는다면
			 		check = "idError";
			 }
			 
		} catch (Exception e) {
			System.out.println("userCheck메소드 내부에서 예외 발생 : " + e);
		} finally {
			closeResource();
		}
			return check;
			
	} // method()

	// 로그인시 띄울 이름 가져오기
	public String[] getCallName(String id) {
		String name="";
		String sel="";
		String[] loginInfo= new String[2];
		try {
			 getConn();
			 
			 sql = "select name, sel from member where id=?";
		
			 pstmt  = con.prepareStatement(sql);
			
			 pstmt.setString(1, id);
			
			 rs = pstmt.executeQuery();
			 
			 if(rs.next()){
				 sel=rs.getString("sel");
				 name=rs.getString("name");
			 }
			loginInfo[0]=sel; loginInfo[1]=name;
		} catch (Exception e) {
			System.out.println("userCheck메소드 내부에서 예외 발생 : " + e);
		} finally {
			closeResource();
		}
			return loginInfo;
			
	} // method()

	public void updateRemaining(String id) {
		int remaining=0;
		try{
			getConn();
			
			sql="select remaining from member where id=?";
			
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				remaining = rs.getInt("remaining");
			}
			
			remaining-=1;
			
			sql = "update member set remaining=? where id=?";
			
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, remaining);
			pstmt.setString(2, id);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("updateRemaining메소드 내부에서 예외 발생 : " + e);
		} finally {
			closeResource();
		}
	}

	// id가 바꼈을 경우 관련 db id값 전부 바꿈
	public void updateId(MemberBean mBean) {
		try {
			
				if(!mBean.getOldId().equals("")){
				// 출석부
				sql="update attendance set id=? where id=?";
				
				pstmt=con.prepareStatement(sql);
				
				pstmt.setString(1, mBean.getId());
				pstmt.setString(2, mBean.getOldId());
				
				pstmt.executeUpdate();
				
				// 게시판
				sql="update qna set id=? where id=?";
				
				pstmt=con.prepareStatement(sql);
				
				pstmt.setString(1, mBean.getId());
				pstmt.setString(2, mBean.getOldId());
				
				pstmt.executeUpdate();
				
				// reservation
				sql="update reservation set id=? where id=?";
				
				pstmt=con.prepareStatement(sql);
				
				pstmt.setString(1, mBean.getId());
				pstmt.setString(2, mBean.getOldId());
				
				pstmt.executeUpdate();
				
				// waitList
				sql="update reservation set id=? where id=?";
				
				pstmt=con.prepareStatement(sql);
				
				pstmt.setString(1, mBean.getId());
				pstmt.setString(2, mBean.getOldId());
				
				pstmt.executeUpdate();
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			closeResource();
		}
		
	}





	
	
	
}