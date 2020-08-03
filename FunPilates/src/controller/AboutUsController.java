package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import db.AboutUsBean;
import db.AboutUsDAO;

@WebServlet("/AboutUsController.do")
public class AboutUsController extends HttpServlet{
	
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
			case "ceoMessage" : 
			case "ceoMessageToEdit" : reqCeoMessage(req, resp);
			break;
			
			case "updateCeoMessage" : reqUpdateCeoMessage(req, resp);
			break;
		 
			case "facilites"		:
			case "facilitesToEdit"   : reqFacilites(req, resp);
			break;
			
			case "updateFacilites" : reqUpdateFacilites(req, resp);
			break;
		 
			case "co_Info" :
			case "co_InfoToEdit" : reqCo_Info(req, resp);
			break;
		 
			case "updateCo_Info" : reqUpdateCo_Info(req, resp);
			break;
			
		}
	}
	
	// CeoMessage로 보낼 인삿말 정보
	protected void reqCeoMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			RequestDispatcher dis = null;
			AboutUsDAO cdao = new AboutUsDAO();
			AboutUsBean cmBean = cdao.getCeoMessage();
			
			req.setAttribute("ceoMessage", cmBean);
		
			// AboutUs(CeoMessage)페이지로 보냄
			if(req.getParameter("req").equals("ceoMessage")){
				dis = req.getRequestDispatcher("Main.jsp?center=aboutus/AboutUs.jsp?subcenter=ceoMessage&title=CEO Message");
							
			// ceoMessageEdit페이지로 보냄
			} else if (req.getParameter("req").equals("ceoMessageToEdit")){
				dis = req.getRequestDispatcher("Main.jsp?center=aboutus/CeoMessageEdit.jsp&title=CEO Message Edit");
				
			}
			dis.forward(req,resp);
	
	} //reqCeoMessage()
	
	// 업로드 여부에 따라 기존 폴더에 남아있는 이미지 파일 삭제
	private void deleteFiles(String realFolder, String ceoImg_upload) {
		// TODO Auto-generated method stub
		
	}	

	// 수정된 CEO Message 저장
	protected void reqUpdateCeoMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

			// 업로드 할 실제 서버의 경로 얻기
			// metadata 아래 실제 폴더 주소는 톰캣이 불러올 수 없는 듯하다
			// 서버에서 Serve modules without publishing을 체크해주자 
			// 체크해주기 전에는 톰캣을 중지시켜놓고 체크해야 저장됨
			String realFolder = getServletContext().getRealPath("images/ceo");
			System.out.println(realFolder);
			// 업로드 할 수 있는 최대 파일 용량 1000MB
			int max = 1000*1024*1024;
				 	
			// 실제 파일 업로드를 담당하는 MultipartRequest객체 생성시 업로드 할 정보를 전달 하여
			// 다중 파일 업로드를 진행함
				 	
			MultipartRequest multi = 
			new MultipartRequest(req, realFolder, max, "UTF-8", new DefaultFileRenamePolicy());
				 	
			// 입력했던 텍스트 값들 얻기
			String ceoName = multi.getParameter("ceoName");
			String ceoMessage = multi.getParameter("ceoMessage").replace("\r\n","<br/>"); 
			String ceoImg = multi.getParameter("ceoImg"); // 기존에 저장된 파일명
			String ceoImg_origin = multi.getParameter("ceoImg"); // 기존에 저장된 파일명
			
			System.out.println("넘어온 값 : "+ceoImg);
			
			// 파일 업로드 당시 선택했던 <input>태그들의 name 속성값들을 모두 
			// Enumeration 반복기 객체에 담아 반환
			Enumeration e =	multi.getFileNames();
			while(e.hasMoreElements()){
			String filename = (String) e.nextElement();
				if(multi.getFilesystemName(filename)!=null){
					ceoImg = multi.getFilesystemName(filename); // 업로드 시 파일명
					ceoImg_origin = multi.getOriginalFileName(filename); // 원본 파일명
				}
			}	
			
			System.out.println("uploadname: " + ceoImg);
			System.out.println("ceoImg_orgin: " + ceoImg_origin);
			
			// deleteFiles(realFolder, ceoImg_upload);	
			
			AboutUsBean cmBean = new AboutUsBean();
			cmBean.setCeoName(ceoName);
			cmBean.setCeoMessage(ceoMessage);
			cmBean.setCeoImg(ceoImg);
			
			AboutUsDAO cmdao = new AboutUsDAO();
			cmdao.updateCeoMessage(cmBean);
				
			RequestDispatcher dis = req.getRequestDispatcher("AboutUsController.do?req=ceoMessage");
			dis.forward(req,resp);
	
	} // reqUpdateCeoMessage()	

	// Facilites로 보낼 정보
	private void reqFacilites(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		RequestDispatcher dis = null;
		AboutUsDAO fdao = new AboutUsDAO();
		AboutUsBean fBean = fdao.getFacilites();
		req.setAttribute("facilites", fBean);
		
		// // AboutUs(facilites)페이지로 보냄
		if(req.getParameter("req").equals("facilites")){
			dis = req.getRequestDispatcher("Main.jsp?center=aboutus/AboutUs.jsp?subcenter=facilites&title=Facilites");		
		
			// facilitesEdit페이지로 보냄
		} else if (req.getParameter("req").equals("facilitesToEdit")){
			dis = req.getRequestDispatcher("Main.jsp?center=aboutus/FacilitesEdit.jsp&title=Facilites Edit");
		}
			dis.forward(req,resp);
		
	}	
	
	private void reqUpdateFacilites(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// 업로드 할 실제 서버의 경로 얻기
					// metadata 아래 실제 폴더 주소는 톰캣이 불러올 수 없는 듯하다
					// 서버에서 Serve modules without publishing을 체크해주자 
					// 체크해주기 전에는 톰캣을 중지시켜놓고 체크해야 저장됨
					String realFolder = getServletContext().getRealPath("images/facilites");
					System.out.println(realFolder);
					// 업로드 할 수 있는 최대 파일 용량 1000MB
					int max = 1000*1024*1024;
						 	
					// 실제 파일 업로드를 담당하는 MultipartRequest객체 생성시 업로드 할 정보를 전달 하여
					// 다중 파일 업로드를 진행함
						 	
					MultipartRequest multi = 
					new MultipartRequest(req, realFolder, max, "UTF-8", new DefaultFileRenamePolicy());
						 	
					// 입력했던 텍스트 값들 얻기
					String facilitesImg1 = multi.getParameter("facilitesImg1");
					System.out.println(facilitesImg1);
					String facilitesImg1_origin = multi.getParameter("facilitesImg1");
					String facilitesImg1cap = multi.getParameter("facilitesImg1cap");
					
					String facilitesImg2 = multi.getParameter("facilitesImg2");
					String facilitesImg2_origin = multi.getParameter("facilitesImg2");
					String facilitesImg2cap = multi.getParameter("facilitesImg2cap");
					
					String facilitesImg3 = multi.getParameter("facilitesImg3");
					String facilitesImg3_origin = multi.getParameter("facilitesImg3");
					String facilitesImg3cap = multi.getParameter("facilitesImg3cap");
					
					String facilitesExplain = multi.getParameter("facilitesExplain").replace("\r\n","<br/>"); 
					
					
					System.out.println("넘어온 값 : "+facilitesImg1+","+facilitesImg2+","+facilitesImg3);
					
					// 파일 업로드 당시 선택했던 <input>태그들의 name 속성값들을 모두 
					// Enumeration 반복기 객체에 담아 반환
					Enumeration e =	multi.getFileNames();
					while(e.hasMoreElements()){
					String filename = (String) e.nextElement();
						if(multi.getFilesystemName(filename)!=null){
							switch(filename){
								case "updateFacilitesImg1" :
									facilitesImg1 = multi.getFilesystemName(filename); // 업로드 시 파일명
									facilitesImg1_origin = multi.getOriginalFileName(filename); // 원본 파일명
								break;
								case "updateFacilitesImg2" :
									facilitesImg2 = multi.getFilesystemName(filename); // 업로드 시 파일명
									facilitesImg2_origin = multi.getOriginalFileName(filename); // 원본 파일명
									break;
								case "updateFacilitesImg3" :
									facilitesImg3 = multi.getFilesystemName(filename); // 업로드 시 파일명
									facilitesImg3_origin = multi.getOriginalFileName(filename); // 원본 파일명
									break;
							}		
						}
					}	
					
					System.out.println("uploadname: " +facilitesImg1+","+facilitesImg2+","+facilitesImg3);
					// deleteFiles(realFolder, ceoImg_upload);	
					
					AboutUsBean fBean = new AboutUsBean();
					fBean.setFacilitesImg1(facilitesImg1);
					fBean.setFacilitesImg1cap(facilitesImg1cap);
					fBean.setFacilitesImg2(facilitesImg2);
					fBean.setFacilitesImg2cap(facilitesImg2cap);
					fBean.setFacilitesImg3(facilitesImg3);
					fBean.setFacilitesImg3cap(facilitesImg3cap);
					fBean.setFacilitesExplain(facilitesExplain);
					
					AboutUsDAO cmdao = new AboutUsDAO();
					cmdao.updateFacilites(fBean);
						
					RequestDispatcher dis = req.getRequestDispatcher("AboutUsController.do?req=facilites");
					dis.forward(req,resp);
		
	}		
	
	// ContactUs나 ContactUsEdit로 보낼 위치 정보
	protected void reqCo_Info(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		AboutUsDAO cdao = new AboutUsDAO();
		AboutUsBean cuBean = cdao.getCo_Info();
		
		req.setAttribute("co_Info", cuBean);
		
		System.out.println();
		
		// // AboutUs(contactUs)페이지로 보냄
		if(req.getParameter("req").equals("co_Info")){
			RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=aboutus/AboutUs.jsp?subcenter=contactUs&title=Contact Us");
			dis.forward(req,resp);
		
		// CoInfoUpdate페이지로 보냄
		} else if (req.getParameter("req").equals("co_InfoToEdit")){
			RequestDispatcher dis = req.getRequestDispatcher("Main.jsp?center=aboutus/ContactUsEdit.jsp&title=Contact Us Edit");
			dis.forward(req,resp);
		}
	
	} // reqContactUsLocation()

	// 수정된 회사 정보 저장
	private void reqUpdateCo_Info(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		 					 	
		
		// 입력했던 텍스트 값들 얻기
		String co_Name = req.getParameter("co_Name"); 
		String co_Tel = req.getParameter("co_Tel");
		String co_Fax = req.getParameter("co_Fax");
		String co_Email = req.getParameter("co_Email");	
		int co_Postcode = Integer.parseInt(req.getParameter("co_Postcode"));
		String co_RoadAddress = req.getParameter("co_RoadAddress");
		String co_DetailAddress = req.getParameter("co_DetailAddress");
					
		AboutUsBean co_Bean = new AboutUsBean();
		co_Bean.setCo_Name(co_Name);
		co_Bean.setCo_Tel(co_Tel);
		co_Bean.setCo_Fax(co_Fax);
		co_Bean.setCo_Email(co_Email);
		co_Bean.setCo_Postcode(co_Postcode);
		co_Bean.setCo_RoadAddress(co_RoadAddress);
		co_Bean.setCo_DetailAddress(co_DetailAddress);
			
		AboutUsDAO co_dao = new AboutUsDAO();
		co_dao.updateCo_Info(co_Bean);
				
		RequestDispatcher dis = req.getRequestDispatcher("AboutUsController.do?req=co_Info");
		dis.forward(req,resp);
	
	}	// requestUpdateCompanyLocation()

	
}
