/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridgelabz.usermanagements.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bridgelabz.usermanagements.model.Message;

/**
 * @author vikash kumar
 * @date: 04/04/2020
 * @version: 1.1 This is Logout servlets
 * 
 */
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = -4640444441703845486L;
	static final Logger LOGGER = Logger.getLogger(LogoutServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		try (PrintWriter out = response.getWriter()) {
			HttpSession s = request.getSession();

			// removing the current user from the session
			s.removeAttribute("currentUser");

			Message m = new Message("Logout Successfully", "success", "alert-success");
			LOGGER.debug("SucessFully Logout");

			s.setAttribute("msg", m);

			response.sendRedirect("login_page.jsp");

			out.println("</body>");
			out.println("</html>");
		} catch (Exception e) {
			LOGGER.error(e);
		}
	}

}
