package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ClassDAO {
		
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

		// 요일 변환 함수
		public String getDateDay(String date, String dateType) throws Exception {
			 
		    String day = "" ;
		     
		    SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
		    java.util.Date nDate = dateFormat.parse(date) ;
		     
		    Calendar cal = Calendar.getInstance() ;
		    cal.setTime(nDate);
		     
		    int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

		    switch(dayNum){
		        case 2: day = "MON";
		        break ;
		        case 3: day = "TUE";
		        break ;
		        case 4: day = "WED";
		        break ;
		        case 5: day = "THR";
		        break ;
		        case 6: day = "FRI";
		        break ;
		        case 7: day = "SAT";
		        break ;   
		    }

		    return day ;
		}	
		
		// 예약 가능한 시간 구하기
		public ArrayList<String> availableTime(ClassBean cBean, int max, String notAvailableT, String availableT, String availableP, String alreadyResv){
		
				String morningTime = "";
				String dayTime =  "";
				String nightTime =  "";
				
				ArrayList<String> timeList = new ArrayList<String>();
				
			try {			
				cBean.setReservDay(getDateDay(cBean.getReservDate(), "yyyy-mm-dd"));
				getConn();
				
				// 지정한 요일의 타임 스케줄 빼오기
				sql = "select classTime from classtimetable where level=? and classRoom=? and day=? order by classNo";
				
				pstmt=con.prepareStatement(sql);
				
					pstmt.setString(1, cBean.getLevel());
					pstmt.setString(2, cBean.getReservClass());
					pstmt.setString(3, cBean.getReservDay());
				
				rs=pstmt.executeQuery();
				
				while(rs.next()){
					timeList.add(rs.getString("classTime"));
				}
					
				morningTime = timeList.get(0);
				dayTime = timeList.get(1);
				nightTime = timeList.get(2);
				
				cBean.setMorningTime(morningTime);
				cBean.setDayTime(dayTime);
				cBean.setNightTime(nightTime);
				
				// 정원이 초과하는지 여부 확인
				sql = "select count(CASE WHEN (level=? and reservClass=? and reservDate=? and reservTime=?) THEN 1 END) as count from reservation" ;
				pstmt=con.prepareStatement(sql);

				pstmt.setString(1, cBean.getLevel());
				pstmt.setString(2, cBean.getReservClass());
				pstmt.setString(3, cBean.getReservDate());
						
					// 오전 수업
					if(!timeList.get(0).equals(morningTime+alreadyResv)){		
					pstmt.setString(4, morningTime);
							
					rs=pstmt.executeQuery();
						
					if(rs.next()){
							if(rs.getInt("count")==max){
								timeList.set(0, morningTime+notAvailableT);	
							} else { 
								timeList.set(0, morningTime+availableT+availableP+(max-rs.getInt("count")));}
						} 
					}
						
					// 오후 수업	
					if(timeList.get(1)!=dayTime+notAvailableT){
						pstmt.setString(4, dayTime);
							
						rs=pstmt.executeQuery();
					
						if(rs.next()){
							if(rs.getInt("count")==max){
								timeList.set(1, dayTime+notAvailableT);	
							} else { timeList.set(1, dayTime+availableT+availableP+(max-rs.getInt("count")));}
						}
					}
						
					// 저녁 수업	
					if(timeList.get(2)!=nightTime+notAvailableT){
						pstmt.setString(4, nightTime);
								
						rs=pstmt.executeQuery();
							
						if(rs.next()){
							if(rs.getInt("count")==max){
								timeList.set(2, nightTime+notAvailableT);	
							} else { timeList.set(2, nightTime+availableT+availableP+(max-rs.getInt("count")));}
						} 							
					}
					
					// 예약 혰는지 확인
					sql = "select * from reservation where level=? and reservClass=? and reservDate=? and id=?" ;
					
					pstmt=con.prepareStatement(sql);
					
					pstmt.setString(1, cBean.getLevel());
					pstmt.setString(2, cBean.getReservClass());
					pstmt.setString(3, cBean.getReservDate());
					pstmt.setString(4, cBean.getId());
					
					rs=pstmt.executeQuery();

					if(rs.next()){
						rs.previous();
						while(rs.next()){
							if(rs.getString("reservTime").equals(morningTime)){
								timeList.set(0, morningTime+alreadyResv);
							} else if(rs.getString("reservTime").equals(dayTime)){
								timeList.set(1, dayTime+alreadyResv);
							} else if(rs.getString("reservTime").equals(nightTime)){
								
								timeList.set(2, nightTime+alreadyResv);
							} 	
						}
					
					} 
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeResource();
			}
			return timeList;
		} // method()

		// 예약 하기(예약 테이블에 정보 저장)
		public int reservation(ClassBean cBean) {
			try {
				getConn();

				sql = "insert into reservation values(null, ?, ?, ?, ?, ?, ?)" ;

				pstmt = con.prepareStatement(sql);
					
				pstmt.setString(1, cBean.getLevel());
				pstmt.setString(2, cBean.getReservClass());
				pstmt.setString(3, cBean.getReservDate());
				pstmt.setString(4, cBean.getReservDay());
				pstmt.setString(5, cBean.getReservTime());
				pstmt.setString(6, cBean.getId());
				
				pstmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeResource();
			} 
			return 1;

		} //method()

		// 예약 내열 뽑기
		public List<ClassBean> getList(String from, String id) {
			
			List<ClassBean> reservList = new ArrayList<ClassBean>();

			try{
			
				getConn();
				
				switch(from) {
					case "reservationList" :
						sql = "select * from reservation where id=? order by reservDate desc, reservTime";
					break;
					
					case "attendancRecord" :
						sql = "select * from attendance where id=? order by classDate desc, classTime desc";
					break;
				}	

				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, id);
				
				rs=pstmt.executeQuery();
				
				switch(from) {
					case "reservationList" :
						while(rs.next()){
							ClassBean cBean = new ClassBean();
							
							cBean.setLevel(rs.getString("level"));
							cBean.setReservClass(rs.getString("reservClass"));
							cBean.setReservDate(rs.getString("reservDate"));
							cBean.setReservDay(rs.getString("reservDay"));
							cBean.setReservTime(rs.getString("reservTime"));
							cBean.setId(rs.getString("id"));
							
							reservList.add(cBean);
							
						}
					break;
					
					case "attendancRecord" :
						while(rs.next()){
							ClassBean cBean = new ClassBean();
							
							cBean.setLevel(rs.getString("level"));
							cBean.setReservClass(rs.getString("classRoom"));
							cBean.setReservDate(rs.getString("classDate"));
							cBean.setReservDay(rs.getString("classDay"));
							cBean.setReservTime(rs.getString("classTime"));
							cBean.setId(rs.getString("id"));
							
							reservList.add(cBean);
							
						}
					break;
				}	
				
				
				
			
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeResource();
			} 
			return reservList;
		}

		// 예약 삭제
		public void delReservation(ClassBean cBean) {
			try{
				
				getConn();
				
				sql = "delete from reservation where level=? and reservClass=? and reservDate=? and reservTime=? and id=?";
				
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, cBean.getLevel());
				pstmt.setString(2, cBean.getReservClass());
				pstmt.setString(3, cBean.getReservDate());
				pstmt.setString(4, cBean.getReservTime());
				pstmt.setString(5, cBean.getId());
				
				pstmt.executeUpdate();
					
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeResource();
			} 			
		}

		public ArrayList<ClassBean> getReservedMember(ClassBean cBean) {
			String morningTime = "";
			String dayTime =  "";
			String nightTime =  "";
			String reservTime = "";
			
			ArrayList<ClassBean> memberList = new ArrayList<ClassBean>();
			ArrayList<String> timeList = new ArrayList<>();
			
			try {			
				cBean.setReservDay(getDateDay(cBean.getReservDate(), "yyyy-mm-dd"));
				getConn();
				// 지정한 요일의 타임 스케줄 빼오기
				sql = "select classTime from classtimetable where level=? and classRoom=? and day=? order by classNo";
				
				pstmt=con.prepareStatement(sql);
				
					pstmt.setString(1, cBean.getLevel());
					pstmt.setString(2, cBean.getReservClass());
					pstmt.setString(3, cBean.getReservDay());
				
				rs=pstmt.executeQuery();
				
				while(rs.next()){
					timeList.add(rs.getString("classTime"));
				}

				morningTime = timeList.get(0);
				dayTime = timeList.get(1);
				nightTime = timeList.get(2);
				
				for(String t : timeList){
					if(cBean.getReservTime().equals("morningTime")){
						reservTime=morningTime;
					} else if(cBean.getReservTime().equals("dayTime")){
						reservTime=dayTime;
					} else if(cBean.getReservTime().equals("nightTime")){
						reservTime=nightTime;
					} else {
						reservTime=cBean.getReservTime();
					}
				}
				cBean.setReservTime(reservTime);
				cBean.setMorningTime(morningTime);
				cBean.setDayTime(dayTime);
				cBean.setNightTime(nightTime);
				
				sql=
				"select m.id id, m.name name, r.level level, r.reservClass reservClass, r.reservDate reservDate, r.reservTime reservTime " +
				"from member m join reservation r " +
				"on m.id=r.id " +
				"where r.level=? and r.reservClass=? and r.reservDate=? and r.reservTime=? ";
				
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, cBean.getLevel());
				pstmt.setString(2, cBean.getReservClass());
				pstmt.setString(3, cBean.getReservDate());
				pstmt.setString(4, reservTime);

				rs=pstmt.executeQuery();
				
				if(rs.next()){
					rs.previous();
					while (rs.next()){
						ClassBean mBean = new ClassBean();
						mBean.setId(rs.getString("id"));
						mBean.setName(rs.getString("name"));
						mBean.setReservTime(rs.getString("reservTime"));
						memberList.add(mBean);
					}
				} else {
					cBean.setId("none");
					memberList.add(cBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeResource();
			} 			
			return memberList;
		}

		public void insertAttendRecord(ClassBean cBean) {
			try{
				getConn();
				
				sql = "insert into attendance values(null,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, cBean.getLevel());
				pstmt.setString(2, cBean.getReservClass());
				pstmt.setString(3, cBean.getReservDate());
				pstmt.setString(4,  cBean.getReservDay());
				pstmt.setString(5,  cBean.getReservTime());
				pstmt.setString(6, cBean.getId());
				
				pstmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeResource();
			}
			
		}

}
