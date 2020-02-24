package com.bridgelabz.usermanagements.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.bridgelabz.usermanagements.model.Message;
import com.bridgelabz.usermanagements.model.User;
import com.bridgelabz.usermanagements.service.ServiceImplemation;

/**
 * Servlet implementation class RegisterServlets this is login servltes
 * 
 * @author vikash kumar
 * @date: 04/04/2020
 * @version: 1.1
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final Logger LOGGER = Logger.getLogger(LoginServlet.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.debug("Login servlets ");
		String userEmail = request.getParameter("email");
		String userPassword = request.getParameter("password");
		LOGGER.debug(userEmail + " " + userPassword);

		ServiceImplemation service = new ServiceImplemation();
		User user = service.loginByUserEmailAndPassword(userEmail, userPassword);

		try {
			if (user == null) {
				// not valid or password incorrect
				Message msg = new Message("Invalid Details! try with another", "error", "alert-danger");
				LOGGER.warn("Invalide password");
				response.sendRedirect("login_page.jsp");
				HttpSession session = request.getSession();
				session.setAttribute("msg", msg);

			} else {
				HttpSession s = request.getSession();
				s.setAttribute("currentUser", user);
				response.sendRedirect("profile.jsp");
				LOGGER.debug("sucessfull Loging with  " + user.getName());
			}

		} catch (Exception e) {
			LOGGER.error(e);
		}

	}

}
