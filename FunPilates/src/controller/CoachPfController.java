package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import db.CoachBean;
import db.CoachDAO;

@WebServlet("/CoachPfController.do")
public class CoachPfController extends HttpServlet{

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
	private void select_ReqPro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		switch (req.getParameter("req")){ 
			case "coachPfList" : reqCoachPfList(req, resp);
			break;
				
			case "coachPfToEdit" : reqCoachPf(req, resp);
			break;
			  
			case "addOrupdateCoachPf" : reqAddOrUpdateCoachPf(req, resp);
			break;
			
			case "coachPfToDel" : reqDelCoachPf(req, resp);
			break;
			
			default:
			break;
		}
	
	}

	// Coaches 페이지로 보낼 코치 프로필 리스트 정보
	protected void reqCoachPfList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			
		CoachDAO cdao = new CoachDAO();
		CoachBean cbean = new CoachBean();
			
		Vector<CoachBean> v = cdao.getCoachPfList();
			
		req.setAttribute("coachPfList", v);
		
		RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=aboutus/AboutUs.jsp?subcenter=coaches&title=Coaches");
		dis.forward(req,resp);
			
	} // reqCoachPfList()

	// CoachesEdit 페이지로 보낼선택된  코치(1인) 프로필 정보
	private void reqCoachPf(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
					
		int coachNo=Integer.parseInt(req.getParameter("coachNo"));
		CoachDAO cdao = new CoachDAO();
		CoachBean cBean = cdao.getCoachPf(coachNo);
				
		req.setAttribute("coachPf", cBean);
				
		RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=aboutus/CoachPfEdit.jsp&title=Coach's Profile Edit");
		dis.forward(req,resp);
	
	} // reqCoachPf()

	// 코치 프로필 수정 또는 추가 하여 DB에 update
	private void reqAddOrUpdateCoachPf(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			 // 업로드 할 실제 서버의 경로 얻기
			 // metadata 아래 실제 폴더 주소는 톰캣이 불러올 수 없는 듯하다
			 // 서버에서 Serve modules without publishing을 체크해주자 
			 // 체크해주기 전에는 톰캣을 중지시켜놓고 체크해야 저장됨
			 String realFolder = getServletContext().getRealPath("images/coaches");
			 System.out.println(realFolder);
			 // 업로드 할 수 있는 최대 파일 용량 1000MB
			 int max = 1000*1024*1024;
					 	
			 // 실제 파일 업로드를 담당하는 MultipartRequest객체 생성시 업로드 할 정보를 전달 하여
			 // 다중 파일 업로드를 진행함				 	
			 MultipartRequest multi = 
					 new MultipartRequest(req, realFolder, max, "UTF-8", new DefaultFileRenamePolicy());
					 	
			 // 입력했던 텍스트 값들 얻기			
			 String className = "pilates";
			 String position = "필라테스 코치";
			 String coachName = multi.getParameter("coachName"); 
			 
			 String coachNickName ="";
			 if(multi.getParameter("coachNickName").length()==0){
				 coachNickName = multi.getParameter("coachName");
			 } else {
				 coachNickName = multi.getParameter("coachNickName");
			 }
			 
			 String career = multi.getParameter("career"); 
			 System.out.println("들어온나마"+ multi.getParameter("pfImg"));
			 String pfImg = multi.getParameter("pfImg"); // 업로드 시 프로필 사진명
			 System.out.println(pfImg);
			 
			 String pfImg_origin = multi.getParameter("pfImg");  // 원본 프로필 사진명
			 System.out.println("니는 왜 안돼는데"+pfImg);
					
			 // 파일 업로드 당시 선택했던 <input>태그들의 name 속성값들을 모두 
			 // Enumeration 반복기 객체에 담아 반환
			 Enumeration e = multi.getFileNames();
			 while(e.hasMoreElements()){
				 String filename = (String) e.nextElement();
				 if(multi.getFilesystemName(filename)!=null){
						pfImg = multi.getFilesystemName(filename);
						pfImg_origin = multi.getOriginalFileName(filename);
				 } // null 값이면 기존 이미지 사용 
			 } // while문	
					
			 System.out.println("pfimg_upload: " + pfImg);
			 // deleteFiles(realFolder, ceoImg_uploadname);	
					
			 CoachBean cBean = new CoachBean();
			 	cBean.setClassName(className);
				cBean.setPosition(position);
				cBean.setCoachName(coachName);
				cBean.setCoachNickName(coachNickName);
				cBean.setCareer(career);
				cBean.setPfImg(pfImg);
								
			 CoachDAO cdao = new CoachDAO();
			 
			// coachNo 전달값 존재 여부에 따라 추가인지 수정인지 결정 
			if(multi.getParameter("coachNo").equals("add")){
				cdao.addCoachPf(cBean);
			 } else {
				 int coachNo=Integer.parseInt(multi.getParameter("coachNo"));
				 cBean.setCoachNo(coachNo);
				 cdao.updateCoachPf(cBean);
			 }
	
			RequestDispatcher dis = req.getRequestDispatcher("CoachPfController.do?req=coachPfList");
			dis.forward(req,resp);
	
	} // reqUpdateCoachPf()	
	
	// 선택된 코치 프로필 DB에서 삭제
	private void reqDelCoachPf(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
					
			int coachno=Integer.parseInt(req.getParameter("coachNo"));
			CoachDAO cdao = new CoachDAO();
			cdao.delCoachPf(coachno);
					
			RequestDispatcher dis = req.getRequestDispatcher("CoachPfController.do?req=coachPfList");
			dis.forward(req,resp);
		
	} // reqDelCoachPf()		

}