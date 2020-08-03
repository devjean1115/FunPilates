package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Request;

import db.BoardDAO;
import db.ClassBean;
import db.ClassDAO;
import db.MemberDAO;

@WebServlet("/ClassController.do")
public class ClassController extends HttpServlet {
	
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

			case "availableTime" : reqAvailableTime(req, resp);
			break;
			
			case "reservation" : reqReservation(req, resp);
			break;
			
			case "attendancRecord" :
			case "reservationList" : reqList(req, resp);
			break;

			case "delReservation" : reqDelReservation(req, resp);
			break;
			
			case "reservMemberToCheckAttendance" : reqReservedMember(req, resp);
			break;
			
			case "checkAttendance" : reqcheckAttendance(req, resp);
			break;
					
		}
		
	} // select_ReqPro()

	// 출석 처리 (출석 명단에 삽입 > 예약 명단 삭제 > 회원권 수정)
	private void reqcheckAttendance(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClassBean cBean = new ClassBean();
		cBean.setLevel(req.getParameter("level"));
		cBean.setReservClass(req.getParameter("reservClass"));
		cBean.setReservDate(req.getParameter("reservDate"));
		cBean.setReservDay(req.getParameter("reservDay"));
		cBean.setReservTime(req.getParameter("reservTime"));
		cBean.setId(req.getParameter("id"));
		
		ClassDAO cdao = new ClassDAO();
		cdao.insertAttendRecord(cBean);
	
		cdao.delReservation(cBean);
		
		MemberDAO mdao = new MemberDAO();
		mdao.updateRemaining(cBean.getId());
		
		reqReservedMember(req, resp, cBean);
	}

	// 한명씩 출첵 이후 남은 인원이 그대로 화면이 보이게 세팅 
	private void reqReservedMember(HttpServletRequest req, HttpServletResponse resp, ClassBean cBean) throws IOException, ServletException {	
		ClassBean reBean = new ClassBean();
		reBean.setLevel(cBean.getLevel());
		reBean.setReservClass(cBean.getReservClass());
		reBean.setReservDate(cBean.getReservDate());
		reBean.setReservTime(cBean.getReservTime());
		
		ClassDAO cdao = new ClassDAO();
		ArrayList<ClassBean> mList = cdao.getReservedMember(reBean);
		
		PrintWriter out = resp.getWriter();
		if(mList.get(0).getId().equals("none")){
			out.print("<script> "
					+ "alert('예약자가 없습니다.');"
					+ "location.href='Main.jsp?center=management/AttendanceInfo.jsp&title=Check Attendance';"
					+ "</script>");	
		} else {
			req.setAttribute("cBean" ,reBean);
			req.setAttribute("mList", mList);
			RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=management/CheckAttendance.jsp&title=Check Attendance");
			dis.forward(req,resp);	
		}
		
	}

	// 출석체크를 위한 지정한 날짜에 예약한 회원 리스트
	private void reqReservedMember(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClassBean cBean = new ClassBean();
		cBean.setLevel(req.getParameter("level"));
		cBean.setReservClass(req.getParameter("reservClass"));
		cBean.setReservDate(req.getParameter("reservDate"));
		cBean.setReservTime(req.getParameter("reservTime"));
		
		ClassDAO cdao = new ClassDAO();
		ArrayList<ClassBean> mList = cdao.getReservedMember(cBean);
		
		PrintWriter out = resp.getWriter();
		if(mList.get(0).getId().equals("none")){
			out.print("<script> "
					+ "alert('예약자가 없습니다.');"
					+ "location.href='Main.jsp?center=management/AttendanceInfo.jsp&title=Check Attendance';"
					+ "</script>");	
		} else {
			req.setAttribute("cBean" ,cBean);
			req.setAttribute("mList", mList);
			RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=management/CheckAttendance.jsp&title=Check Attendance");
			dis.forward(req,resp);	
		}

		
	}

	// 예약 삭제(출석 체크 후 또는 회원이 직접 삭제)
	private void reqDelReservation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String from = req.getParameter("from");
		
		ClassBean cBean = new ClassBean();
		
		cBean.setLevel(req.getParameter("level"));
		cBean.setReservClass(req.getParameter("reservClass"));
		cBean.setReservDate(req.getParameter("reservDate"));
		cBean.setReservTime(req.getParameter("reservTime"));
		cBean.setId(req.getParameter("id"));
		
		ClassDAO cdao = new ClassDAO();
			cdao.delReservation(cBean);
			
		RequestDispatcher dis = null;
		
		if(from.equals("checkAttendance")){
			reqReservedMember(req, resp, cBean);
		} else {
			dis=req.getRequestDispatcher("ClassController.do?req=reservationList");
			dis.forward(req,resp);	
		}
			
	}

	// 예약 내역 불러오기
	private void reqList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String from = req.getParameter("req");
		String id = req.getParameter("id");
		ClassDAO cdao = new ClassDAO();
		
		List<ClassBean> list = cdao.getList(from, id);
		RequestDispatcher dis = null;
		switch(from) {
			case "reservationList" :
				req.setAttribute("reservList", list);
				dis = req.getRequestDispatcher("Main.jsp?center=member/ReservationList.jsp&title=Reservation List");
			break;
			
			case "attendancRecord" :
				req.setAttribute("attendanceCnt", list.size());
				req.setAttribute("attendanceList", list);
				dis = req.getRequestDispatcher("Main.jsp?center=member/AttendanceRecord.jsp&title=Attendance Record");
			break;
		}
		
		dis.forward(req, resp);
	}

	// 예약 하기
	private void reqReservation(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		ClassBean cBean = new ClassBean();
		
		cBean.setLevel(req.getParameter("level"));
		cBean.setReservClass(req.getParameter("reservClass"));
		cBean.setReservDate(req.getParameter("reservDate"));
		cBean.setReservDay(req.getParameter("reservDay"));
		cBean.setReservTime(req.getParameter("reservTime"));
		cBean.setId(req.getParameter("id"));
		
		ClassDAO cdao = new ClassDAO();
		
		int result=cdao.reservation(cBean);
		
		RequestDispatcher dis = req.getRequestDispatcher("ClassController.do?req=reservationList");
		dis.forward(req,resp);	
	}

	// 에약 가능한 시간 구하기	
	private void reqAvailableTime(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClassBean cBean = new ClassBean();
		int max=3;
		String notAvailableT = "<br>정원 초과! 예약 불가!";
		String availableT = "예약 가능!";
		String availableP = "<br>남은 인원 : ";
		String alreadyResv = "<br>이미 예약 하셨습니다.";
		
		cBean.setLevel(req.getParameter("level"));
		cBean.setReservClass(req.getParameter("reservClass"));
		cBean.setReservDate(req.getParameter("reservDate"));
		cBean.setId(req.getParameter("id"));
				
		ClassDAO cdao = new ClassDAO();
		ArrayList<String> atList = cdao.availableTime(cBean, max, notAvailableT, availableT, availableP, alreadyResv);
		
		ArrayList<String> reservTimeList = new ArrayList<>();
		reservTimeList.add(cBean.getMorningTime());
		reservTimeList.add(cBean.getDayTime());
		reservTimeList.add(cBean.getNightTime());
		
		req.setAttribute("availableT", availableT);
		req.setAttribute("reservTimeList", reservTimeList);
		req.setAttribute("atList", atList);
		req.setAttribute("cBean", cBean);
		
		RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=member/Reservation.jsp?title=Reservation List");
		dis.forward(req, resp);
	
	}
	
	
	
	
	
	
}
;