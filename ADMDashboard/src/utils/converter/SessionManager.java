package utils.converter;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.UserService;

public class SessionManager {
	
	public static User beginSession(HttpServletRequest request, HttpServletResponse response){

		HttpSession session = request.getSession();
		User user;
		String email = request.getParameter(User.COL_EMAIL);
		String logoURL = request.getParameter("logoURL");
		// check if valid
		if(email == null || logoURL == null)
			return null;
		
		// get user
		user = UserService.searchUser(email);
		if(user == null)
			return null;
		
		// save important details to session
		session.setAttribute(User.COL_IDNUMBER, user.getUserID());
		session.setAttribute("logoURL", logoURL);
		session.setAttribute(User.COL_ORGCODE, user.getOrgcode());
		
		// save cookies
		
		response.addCookie(new Cookie(User.COL_IDNUMBER, user.getUserID() + ""));		// add cookie to list of cookies
		response.addCookie(new Cookie("logoURL", logoURL)); 
		response.addCookie(new Cookie(User.COL_ORGCODE, user.getOrgcode())); 		
		
		return user;
	}
	
	public static void endSession(HttpServletRequest request, HttpServletResponse response){
		Cookie[] cookies;
		HttpSession session = request.getSession();
		
		// invalidate session
		session.invalidate();
		
		// get cookies
		cookies = request.getCookies();

		// kill cookies
		if (cookies != null) {
			for (Cookie c : cookies) {
				c.setValue("");
				c.setPath("/");
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
	}
	
	// to get individual attributes
	public static Object getAttribute(HttpServletRequest request, String name){
		return request.getSession().getAttribute(name);
	}
	
	// get all atributes
	public static Enumeration<String> getAttributeNames(HttpServletRequest request){
		return request.getSession().getAttributeNames();
	}
	
	// to add attributes
	public static void setAttribute(HttpServletRequest request, String name, Object attribute){
		request.getSession().setAttribute(name, attribute);
	}
	
}

