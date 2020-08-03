package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

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

	// Board테이블에 저장된 전체 글 개수를 조회해서 제공하는 메소드
	public int getContentCount(String type){
		// board테이블에 저장되어 있는 조회한 글 개수를 저장할 변수
		int count = 0;
		try {
			// 커넥션 풀로부터 커넥션 얻기(DB와의 연결)
			getConn();
			
			// SQL문 : 전체 글 개수 조회
			switch (type) {
				case "qna" : 
					sql = "select count(*) from qna";
				break;		
			} 
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 전체 글개수 조회 후 반환
			
			// 전체 글 개수가 조회 된다면
			if(rs.next()){
				count = rs.getInt(1);
			} 
			
		} catch (Exception e) {
			System.out.println("getBoardCount()메소드 내부에서 에러발생 : " + e);
		} finally{
			closeResource();
		}
		return count; // 조회한 글 개수 리턴
	} // getBoardCount();
	
	// Board테이블에 저장된 전체 글 개수를 조회해서 제공하는 메소드
	public int getMyContentCount(String type, String id){
		// board테이블에 저장되어 있는 조회한 글 개수를 저장할 변수
		int count = 0;
		try {
			// 커넥션 풀로부터 커넥션 얻기(DB와의 연결)
			getConn();
			
				sql = "select count(*) from qna where id=?";
						
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery(); // 전체 글개수 조회 후 반환
			
			// 전체 글 개수가 조회 된다면
			if(rs.next()){
				count = rs.getInt(1);
			} 
			
		} catch (Exception e) {
			System.out.println("getBoardCount()메소드 내부에서 에러발생 : " + e);
		} finally{
			closeResource();
		}
		return count; // 조회한 글 개수 리턴
	} // getBoardCount();

	// 전체 글목록 가져오기
	public List<BoardBean> getBoardList(String type, int startRow, int pageSize) {
			
		// board테이블로부터 검색한 글정보들을 
		// 각각 한 줄 단위로 BoardBean객체에 저장 후~
		// BoardBean객체들을 각각 ArrayList배열에 추가로 저장 하기 위한 용도
		List<BoardBean> boardList = new ArrayList<BoardBean>();
		
		try {
			// DB연결
			getConn();
			
			// SQL문 만들기
			switch(type){
				case "qna" :
					sql = "select* from qna order by num desc limit ?,?";
				break;
			}
				
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, pageSize);
			
			rs = pstmt.executeQuery();
				
			while(rs.next()){
				// 검색한 글 목록 중 ... 한 줄 단위의 데이터를 저장할 용도로 객체 생성
				BoardBean bBean = new BoardBean();
				// rs => BoardBean 객체의 각변수에 저장
				bBean.setNum(rs.getInt("num"));
				bBean.setStatus(rs.getString("status"));
				bBean.setId(rs.getString("id"));
				bBean.setPassword(rs.getString("password"));
				bBean.setSubject(rs.getString("subject"));
				bBean.setContent(rs.getString("content"));
				bBean.setDate(rs.getTimestamp("date"));
				bBean.setIp(rs.getString("ip"));
				bBean.setAnswer(rs.getString("answer"));
				bBean.setAnswer("answerDate");

				// BoardBean 객체 => ArrayList 배열에 추가
				boardList.add(bBean);
					
			} // while 반복문 끝 	
				
		} catch (Exception e) {
			System.out.println("getBoardList 내부에서 예외 발생 : "+e);
		}finally {
			closeResource();
		}
			return boardList; 
			
	} // method()

	// 글 추가
	public void insertContent(String type, BoardBean bBean) {
		try {
			getConn();

			switch(type) {	
				case "addQ" :
					sql = "insert into qna "
						+"values(null,?,?,?,?,?,?,?,?,?)";
				break;
			}
				
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, bBean.getStatus());
			pstmt.setString(2, bBean.getId());
			pstmt.setString(3, bBean.getPassword());
			pstmt.setString(4, bBean.getSubject());
			pstmt.setString(5, bBean.getContent());
			pstmt.setTimestamp(6, bBean.getDate()); //주글을 추가한 날짜및 시간 
			pstmt.setString(7, bBean.getIp());//주글(새글)을 추가한 사람의  IP주소 
			pstmt.setString(8, "");
			pstmt.setTimestamp(9, bBean.getDate());
				
			pstmt.executeUpdate();
				
		} catch (Exception e) {
			System.out.println("insertBoard메소드 내부에서 예외발생 : " + e);
		} finally {
			//자원해제
			closeResource();
		}	
	} // method()

	// 글 불러오기	
	public BoardBean getContent(String type, int num) {
		BoardBean bBean = new BoardBean();
		try {
			 getConn();
			 
			 switch (type) {
			 
			 	case "qNaContentToEdit" :
			 	case "qNaContent" :
			 		sql = "select * from qna where num=?"; 
			 	break;
			 }
				 
			 pstmt = con.prepareStatement(sql);
			 
			 pstmt.setInt(1, num);
				 
			 rs = pstmt.executeQuery();
				 
			 if(rs.next()){
				 bBean.setNum(num);
				 bBean.setStatus(rs.getString("status"));
				 bBean.setId(rs.getString("id"));
				 bBean.setPassword(rs.getString("password"));
				 bBean.setSubject(rs.getString("subject"));
				 bBean.setContent(rs.getString("content").replace("\r\n","<br/>"));
				 bBean.setDate(rs.getTimestamp("date"));
				 bBean.setAnswer(rs.getString("answer").replace("\r\n","<br/>"));
				 bBean.setAnswerDate(rs.getTimestamp("answerDate"));
			 }
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource();
		}
			
		return bBean;
	}
	
	// 글 수정
	public void updateContent(String type, BoardBean bBean) {
		try{
			getConn();
			switch (type){
				case "updateQ" :
					sql = "update qna set id=?, password=?, subject=?, content=?, date=?, ip=?"
							+ " where num=?";

					pstmt = con.prepareStatement(sql);
					pstmt.setString(1,bBean.getId());
					pstmt.setString(2, bBean.getPassword());
					pstmt.setString(3, bBean.getSubject());
					pstmt.setString(4, bBean.getContent());
					pstmt.setTimestamp(5, bBean.getDate()); //주글을 추가한 날짜및 시간 
					pstmt.setString(6, bBean.getIp());//주글(새글)을 추가한 사람의  IP주소 
					pstmt.setInt(7, bBean.getNum());	
					
				break;
				
				case "updateA" :
					sql = "update qna set status=?, answer=?, answerDate=?"
							+ " where num=?";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, bBean.getStatus());
					pstmt.setString(2, bBean.getAnswer());
					pstmt.setTimestamp(3, bBean.getAnswerDate());
					pstmt.setInt(4, bBean.getNum());
				break; 	
			
				case "delA" :
					sql = "update qna set status=?, answer='' where num=?";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, bBean.getStatus());
					pstmt.setInt(2, bBean.getNum());
				break; 	
					
			}
			pstmt.executeUpdate();
						
		} catch (Exception e) {
			System.out.println("updateContent메소드 내부에서 예외발생 : " + e);
			e.printStackTrace();
		} finally {
			//자원해제
			closeResource();
		}	
			
	} //method

	// 글 삭제	
	public void delContent(String type, int num) {
		try {
			  getConn();
			  
			  switch(type){
			  	case "delQnAContent" :
			  		sql = "delete from qna where num=?";
			  	break;
				  
			  }  
			  
			  pstmt=con.prepareStatement(sql);
			  
			  pstmt.setInt(1,num);
				  
			  pstmt.executeUpdate();	  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource();
		}
			
	} //method()

	public List<BoardBean> getMyContentsList(String id, int startRow, int pageSize) {
			
			// board테이블로부터 검색한 글정보들을 
			// 각각 한 줄 단위로 BoardBean객체에 저장 후~
			// BoardBean객체들을 각각 ArrayList배열에 추가로 저장 하기 위한 용도
			List<BoardBean> boardList = new ArrayList<BoardBean>();
			
			try {
				// DB연결
				getConn();
				
				// SQL문 만들기
						sql = "select* from qna where id=? order by num desc limit ?,?";
					
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setInt(2, startRow);
				pstmt.setInt(3, pageSize);
				
				rs = pstmt.executeQuery();
					
				while(rs.next()){
					// 검색한 글 목록 중 ... 한 줄 단위의 데이터를 저장할 용도로 객체 생성
					BoardBean bBean = new BoardBean();
					// rs => BoardBean 객체의 각변수에 저장
					bBean.setNum(rs.getInt("num"));
					bBean.setStatus(rs.getString("status"));
					bBean.setId(rs.getString("id"));
					bBean.setPassword(rs.getString("password"));
					bBean.setSubject(rs.getString("subject"));
					bBean.setContent(rs.getString("content"));
					bBean.setDate(rs.getTimestamp("date"));
					bBean.setIp(rs.getString("ip"));
					bBean.setAnswer(rs.getString("answer"));
					bBean.setAnswer("answerDate");

					// BoardBean 객체 => ArrayList 배열에 추가
					boardList.add(bBean);
						
				} // while 반복문 끝 	
					
			} catch (Exception e) {
				System.out.println("getBoardList 내부에서 예외 발생 : "+e);
			}finally {
				closeResource();
			}
				return boardList; 
				
		} // method()

		
}
