package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.fabric.xmlrpc.base.Member;
import com.sun.xml.internal.bind.v2.runtime.Location;

import db.ClassBean;
import db.ClassDAO;
import db.MemberBean;
import db.MemberDAO;

@WebServlet("/MemberController.do")
public class MemberController extends HttpServlet {
	
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
			case "memberCheck" : 
			case "joinCheck" : reqCheck(req,resp);
			break ;	
			
			case "delMember" :
			case "delWaitingMember": reqDelMemberInfo(req, resp);
			break;	
				
			case "updateWaitList" : 
			case "join" : reqAddMember(req, resp);
			break;
			
			case "infoForReservation" :
			case "waitingMemberInfo":
			case "memberInfoToJoin" : 		
			case "memberInfoToEditByOneself" : 	
			case "memberInfoToEditByManager" : reqMemberInfo(req, resp);
			break;
		
			case "updateMemberInfoByOneself" : // 비번만 수정 가능
			case "updateMemberInfoByManager" : reqUpdateMemberInfo(req,resp);
			// 멤버쉽 갱신할 땐 어떡하지...?	
			break;
			
			case "login" : reqIdCheck(req, resp);
			break;
			
			case "logout" : reqLogOut(req, resp);
			break;
			
			
			
		   }
		
	} // select_ReqPro()
	
	// 회원 정보 저장 여부 체크 (대기리스타나 회원가입 전)	

	// 가입시 승인받은 번호 여부 체크
	private void reqCheck(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String check = req.getParameter("req");
		String phoneNumber=req.getParameter("phoneNumber");
		String sel = req.getParameter("sel");
		String idck="";
		
		MemberDAO mdao = new MemberDAO();
			idck = mdao.phNumberCheck(check, phoneNumber);
			
		PrintWriter out = resp.getWriter();		
			out.print(idck); 
			
	} // method()

	// waitlist 회원 정보 삭제
	private void reqDelMemberInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher dis =null;
		String purpose = req.getParameter("req");
		String id=req.getParameter("id");
		MemberDAO mdao = new MemberDAO();
		mdao.delMemberInfo(purpose, id);
		
		switch(purpose) {
			case "delWaitingMember" :
				dis = req.getRequestDispatcher("Main.jsp?center=management/ManageMember.jsp&title=Management");
			break;
			
			case "delMember" :
				dis = req.getRequestDispatcher("Main.jsp");
			break;
		}
		dis.forward(req, resp);

	}
	
	// 회원 가입 (+ 가입후 waitlist에서 삭제)
	// 관리자가 결제한 회원 정보를 가입 전 미리 waitlist 테이블에 저장 또는  회원이 가입시 회원 정보 저장
	private void reqAddMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String purpose = req.getParameter("req");
		
		MemberBean mBean = new MemberBean();
			mBean.setId(req.getParameter("id"));
			mBean.setSel(req.getParameter("sel"));
			mBean.setName(req.getParameter("name"));
			mBean.setPhoneNumber(req.getParameter("phoneNumber_front")+req.getParameter("phoneNumber"));
			mBean.setMembership( Integer.parseInt(req.getParameter("membership")));
			mBean.setBeginDate(req.getParameter("beginDate"));
			mBean.setExpDate(req.getParameter("expDate"));
			mBean.setLevel(req.getParameter("level"));
			mBean.setRemaining(mBean.getMembership());
			
			MemberDAO mdao = new MemberDAO();
			switch(purpose){
				case "updateWaitList" : 
					mdao.addMember(purpose, mBean);
					// ClassBean clBean = new ClassBean();
					// 조인을 하던지 예약할 때 필요함
				break ; 
				
				case "join" : 
					mBean.setPassword(req.getParameter("password"));
					mdao.addMember(purpose, mBean);
					mdao.delMemberInfo(purpose, mBean.getId());
				break;
			}
			
			PrintWriter out = resp.getWriter();
			out.print("<script> "
						+ "alert('완료!');"
						+ "location.href='Main.jsp';"
						+ "</script>");
	} // method()

	// 회원정보 불러들이기	
	// 가입 전 본인 정보 확인 또는 관리자나 회원이 회원 정보 수정을 위해 불러오기 (회원은 창을 보고 비번만 입력 후 가입 완료)
	private void reqMemberInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String purpose = req.getParameter("req");
		
		String id=req.getParameter("id");
		
		MemberDAO mdao = new MemberDAO();
		MemberBean mBean = new MemberBean();
			
		mBean = mdao.getMemberInfo(purpose, id);
			
		JSONObject totalObject = new JSONObject();
		
		// 본인정보수정할때는 전 페이지의 비밀번호를 수정페이지로 불러오면 되니까 필요 없나?
		JSONObject memberInfo	= new JSONObject();
			memberInfo.put("no", mBean.getNo());
			memberInfo.put("sel", mBean.getSel());
			memberInfo.put("id", mBean.getId());
			memberInfo.put("name", mBean.getName());
			memberInfo.put("phoneNumber", mBean.getPhoneNumber());
			memberInfo.put("membership", mBean.getMembership());
			memberInfo.put("beginDate", mBean.getBeginDate());
			memberInfo.put("expDate", mBean.getExpDate());
			memberInfo.put("level", mBean.getLevel());
			memberInfo.put("remaining", mBean.getRemaining());
			
			totalObject.put("memberInfo", memberInfo);
			String JsonInfo = totalObject.toJSONString();
				
			PrintWriter out = resp.getWriter();
			out.print(JsonInfo);
		}	
	
	// 관리자 또는 회원이 회원정보 수정  member에 수정 정보 저장
	private void reqUpdateMemberInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bywhom = req.getParameter("req");
		String from = req.getParameter("from");
		
		MemberBean mBean = new MemberBean();
			mBean.setSel(req.getParameter("sel"));
			mBean.setName(req.getParameter("name"));
			mBean.setBeginDate(req.getParameter("beginDate"));
			mBean.setExpDate(req.getParameter("expDate"));	
			mBean.setLevel(req.getParameter("level"));
			mBean.setMembership( Integer.parseInt(req.getParameter("membership")));
			
			if(!req.getParameter("newPhoneNumber").equals("")){
				mBean.setId(req.getParameter("newPhoneNumber"));
				mBean.setPhoneNumber("010"+req.getParameter("newPhoneNumber"));
				mBean.setOldId(req.getParameter("id"));
				mBean.setOldPhoneNumber(req.getParameter("phoneNumber"));
				
			} else {
				mBean.setPhoneNumber(req.getParameter("phoneNumber_front")+req.getParameter("phoneNumber"));
				mBean.setId(req.getParameter("id"));
			}
			
		

			
			if(!from.equals("waitlist")){
				mBean.setRemaining(Integer.parseInt(req.getParameter("remaining")));
			}	
			
			
		PrintWriter out = resp.getWriter();	
		MemberDAO mdao = new MemberDAO();
		switch(bywhom){
			case "updateMemberInfoByManager" : 
				mBean.setNo(Integer.parseInt(req.getParameter("no")));
				mdao.updateMember(bywhom, from, mBean);
				mdao.updateId(mBean);
				
				out.print("<script> "
						+ "alert('완료!');"
						+ "location.href='Main.jsp?center=management/ManageMember.jsp&title=Management';"
						+ "</script>");	
				// ClassBean clBean = new ClassBean();
				// 조인을 하던지 예약할 때 필요함
			break ; 
			
			case "updateMemberInfoByOneself" : 
				mBean.setNo(Integer.parseInt(req.getParameter("no")));
				mBean.setPassword(req.getParameter("password"));
				mdao.updateMember(bywhom, from, mBean);
				
				out.print("<script> "
						+ "alert('완료!');"
						+ "location.href='Main.jsp';"
						+ "</script>");	
			break;
		}
		
		

	} // method()	
	
	// 로그인시 아이디 체크를 위해 불러오기 
	private void reqIdCheck(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String id = req.getParameter("id");
		String password = req.getParameter("password");
		String idck="notUsable";
		
		MemberDAO mdao = new MemberDAO();
			idck = mdao.userCheck(id, password);

		switch (idck) {
			case "login" : 
				HttpSession session = req.getSession();
				String[] loginInfo = mdao.getCallName(id);
				session.setAttribute("id", id);
				session.setAttribute("sel", loginInfo[0]);
				session.setAttribute("name", loginInfo[1]);
				resp.sendRedirect("Main.jsp?title=Fun Pilates");
			break;
			
			case "passwordError" :
			case "idError" :
				req.setAttribute("userCheck", idck);
				RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=member/login.jsp&title=Log In");
				dis.forward(req,resp);
			break;	
		}

	} // reqIdCheck()
	
	// 로그 아웃
	private void reqLogOut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.getSession().invalidate();
		PrintWriter out = resp.getWriter();
		out.print("<script> "
				+ "alert('로그 아웃!');"
				+ "location.href='Main.jsp?title=Log Out';"
				+ "</script>");	
	}

}
