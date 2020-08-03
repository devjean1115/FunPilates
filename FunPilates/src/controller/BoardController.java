package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import db.BoardBean;
import db.BoardDAO;
import db.MemberBean;
import db.MemberDAO;

@WebServlet("/BoardController.do")
public class BoardController extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		select_ReqPro(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=utf-8");
		select_ReqPro(req, resp);
	} 
	
	// 클라이언트의 어떤 방식의 요청이든 처리하겠다. 어떤 전달인자를 받느냐에 따라!
	protected void select_ReqPro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{	
		
		switch (req.getParameter("req")) {
			case "myContents" : reqMycontents(req, resp);
			break;
			
			case "qna" : reqBoard(req, resp); 
			break;	
			
			case "delA" :
			case "updateA" :
			case "updateQ" :
			case "addQ" : reqAddOrUpdateContent(req, resp); 
			break;
			
			case "qNaContentToEdit" :
			case "qNaContent" : reqContent(req,resp);
			break;
			
			case "delQ" : reqDelContent(req,resp);
			break;
			
			
		}
	}

	private void reqMycontents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String id = req.getParameter("id");
		
			BoardDAO bdao = new BoardDAO();
			
			String type = req.getParameter("req");
			//DB에 저장되어 있는 전체 글의 개수를 조회 하기 위해 
			//QnADAO의 getBoardCount()메소드를 호출함
			int count = bdao.getMyContentCount(type, id);
			int pageSize =5;
			
			String pageNum=req.getParameter("pageNum");
			
			if(pageNum == null){
				pageNum = "1";
			}
			
			int currentPage = Integer.parseInt(pageNum);
			
			//각페이지 마다 첫번째로 보여질 시작 글번호 구하기
			//(현재 보여질 페이지번호  - 1) * 한페이지당 보여줄 글개수 5
			int startRow = (currentPage - 1) * pageSize;
			
			//board테이블에 존재하는 모든 글을 조회하여 저장할 용도의 ArrayList배열객체를 담을 변수 선언
			List<BoardBean> list = null;
			
			if(count > 0){
				//board테이블에 존재하는 글을 검색해서 가져옴
				//				getBoardList(시작글번호,한페이지당보여줄글수)
				list = bdao.getMyContentsList(id, startRow,pageSize);  
			}	
			///////////////////////////////////////////////////////////////////////	
				int pageCount = 0;
				int endPage = 0;
				int startPage =0;
				int pageBlock =0;
			
				if(count>0){//DB에 글이 저장되어 있다면			
						pageCount = count/pageSize+(count%pageSize==0?0:1);
						//하나의 화면(하나의 블럭)에 보여줄 페이지수 설정
						pageBlock = 2;

						//시작페이지 번호 구하기
						// 1 ~ 10 => 1 , 11~20 => 11 , 21~30=>21
						startPage = ((currentPage/pageBlock)
										-(currentPage%pageBlock==0?1:0))*pageBlock+1;
						
						//끝페이지 번호 구하기
						// 1 ~ 10 =>10,  11~20 => 20
						//시작페이지번호 + 현재블럭에 보여줄 페이지수 -1
						endPage = startPage+pageBlock-1;
						
						//끝페이지번호가 전체페이지수보다 클떄??
						if(endPage > pageCount){
							//끝페이지번호를 전체페이지수로 저장
							endPage = pageCount;
						}		
			}
			
			req.setAttribute("qnaBoard", list);
			req.setAttribute("startRow", startRow);
			req.setAttribute("currentPage", currentPage);
			req.setAttribute("pageSize", pageSize);
			req.setAttribute("pageNum", pageNum);
			req.setAttribute("count", count);
			req.setAttribute("startPage", startPage);
			req.setAttribute("endPage",endPage);
			req.setAttribute("pageBlock", pageBlock);
			
			RequestDispatcher dis= req.getRequestDispatcher("Main.jsp?center=board/MyContents.jsp?pageNum="+pageNum+"&title=MyQ List");
			dis.forward(req,resp);
				
		
	}

	// 게시판 목록 가져오기
	private void reqBoard(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		BoardDAO bdao = new BoardDAO();
		
		String type = req.getParameter("req");
		//DB에 저장되어 있는 전체 글의 개수를 조회 하기 위해 
		//QnADAO의 getBoardCount()메소드를 호출함
		int count = bdao.getContentCount(type);
		int pageSize =5;
		
		String pageNum=req.getParameter("pageNum");
		
		if(pageNum == null){
			pageNum = "1";
		}
		
		int currentPage = Integer.parseInt(pageNum);
		
		//각페이지 마다 첫번째로 보여질 시작 글번호 구하기
		//(현재 보여질 페이지번호  - 1) * 한페이지당 보여줄 글개수 5
		int startRow = (currentPage - 1) * pageSize;
		
		//board테이블에 존재하는 모든 글을 조회하여 저장할 용도의 ArrayList배열객체를 담을 변수 선언
		List<BoardBean> list = null;
		
		if(count > 0){
			//board테이블에 존재하는 글을 검색해서 가져옴
			//				getBoardList(시작글번호,한페이지당보여줄글수)
			list = bdao.getBoardList(type,startRow,pageSize);  
		}	
		///////////////////////////////////////////////////////////////////////	
			int pageCount = 0;
			int endPage = 0;
			int startPage =0;
			int pageBlock =0;
		
			if(count>0){//DB에 글이 저장되어 있다면			
					pageCount = count/pageSize+(count%pageSize==0?0:1);
					//하나의 화면(하나의 블럭)에 보여줄 페이지수 설정
					pageBlock = 2;

					//시작페이지 번호 구하기
					// 1 ~ 10 => 1 , 11~20 => 11 , 21~30=>21
					startPage = ((currentPage/pageBlock)
									-(currentPage%pageBlock==0?1:0))*pageBlock+1;
					
					//끝페이지 번호 구하기
					// 1 ~ 10 =>10,  11~20 => 20
					//시작페이지번호 + 현재블럭에 보여줄 페이지수 -1
					endPage = startPage+pageBlock-1;
					
					//끝페이지번호가 전체페이지수보다 클떄??
					if(endPage > pageCount){
						//끝페이지번호를 전체페이지수로 저장
						endPage = pageCount;
					}		
		}
		
		req.setAttribute("qnaBoard", list);
		req.setAttribute("pageCount", pageCount);
		req.setAttribute("startRow", startRow);
		req.setAttribute("currentPage", currentPage);
		req.setAttribute("pageSize", pageSize);
		req.setAttribute("pageNum", pageNum);
		req.setAttribute("count", count);
		req.setAttribute("startPage", startPage);
		req.setAttribute("endPage",endPage);
		req.setAttribute("pageBlock", pageBlock);
		
		RequestDispatcher dis= req.getRequestDispatcher("Main.jsp?center=board/QnA.jsp?pageNum="+pageNum+"&title=QnA List");
		dis.forward(req,resp);
	}		

	// DB 글 추가 또는 수정 또는 댓글 추가
	private void reqAddOrUpdateContent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type=req.getParameter("req");
		
		BoardBean bBean = new BoardBean();
		bBean.setStatus(req.getParameter("status"));
		bBean.setId(req.getParameter("id"));
		bBean.setPassword(req.getParameter("password"));
		bBean.setSubject(req.getParameter("subject"));
		bBean.setContent(req.getParameter("content"));
		System.out.println("상태"+bBean.getContent());
		bBean.setDate(new Timestamp( System.currentTimeMillis()));
		System.out.println("상태"+bBean.getDate());
		bBean.setIp(req.getRemoteAddr());	
		
		BoardDAO bdao = new BoardDAO();
		switch(type) {
			case "delA" : 
				bBean.setStatus("답변대기");
			case "updateQ" : 
				bBean.setNum(Integer.parseInt(req.getParameter("num")));
				bdao.updateContent(type, bBean);
			break;
			
			case "updateA" :
				bBean.setNum(Integer.parseInt(req.getParameter("num")));
				bBean.setStatus("답변완료");
				bBean.setAnswer(req.getParameter("answer"));
				bBean.setAnswerDate(new Timestamp( System.currentTimeMillis()));
				bdao.updateContent(type, bBean);
			break;
			
			case "addQ" :
				bdao.insertContent(type, bBean);
			break;
		}
		
		RequestDispatcher dis = req.getRequestDispatcher("BoardController.do?req=qna");
		dis.forward(req,resp);
	}

	// DB 글 내용+댓글 불러오기
	private void reqContent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String type = req.getParameter("req");		
		int num=Integer.parseInt(req.getParameter("num"));
		
		BoardDAO bdao = new BoardDAO();
		
		BoardBean bBean = bdao.getContent(type,num);
		
		req.setAttribute("bBean", bBean);
		
		RequestDispatcher dis = null;
		switch (type) {
			case "qNaContent" :
				dis = req.getRequestDispatcher("Main.jsp?center=board/content.jsp&title=QnA Content"); 
			break;
			
			case "qNaContentToEdit" :
				dis = req.getRequestDispatcher("Main.jsp?center=board/write.jsp&title=Content Edit"); 
			break;	
		}
			dis.forward(req,resp);
		
	} 	

	// DB 글 삭제
	private void reqDelContent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type = req.getParameter("req");
		int num = Integer.parseInt(req.getParameter("num"));
		
		BoardDAO bdao = new BoardDAO();
			bdao.delContent(type, num);
			
		RequestDispatcher dis = req.getRequestDispatcher("BoardController.do?req=qna");
		dis.forward(req,resp);	
	}	
	
	
	
}
