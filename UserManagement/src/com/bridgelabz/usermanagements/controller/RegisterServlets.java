package com.bridgelabz.usermanagements.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import com.bridgelabz.usermanagements.model.User;
import com.bridgelabz.usermanagements.service.Service;
import com.bridgelabz.usermanagements.service.ServiceImplemation;

/**
 * @author vikash kumar
 * @date: 04/04/2020
 * @version: 1.1 Servlet implementation class RegisterServlets this is register
 *           servlets
 * 
 */
@MultipartConfig
public class RegisterServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = Logger.getLogger(RegisterServlets.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.debug("This is Register Servlets");
		String check = request.getParameter("check");
		try {
			PrintWriter out = response.getWriter();
			if (check == null) {
				out.print("please check the terms and condition");
				LOGGER.debug("Box is not checked ");
			} else {
				String name = request.getParameter("user_name");
				String email = request.getParameter("user_email");
				String password = request.getParameter("user_password");
				String gender = request.getParameter("gender");
				String about = request.getParameter("about");

				User newUser = new User(name, email, password, gender, about);
				Service service = new ServiceImplemation();

				if (service.userRegistred(newUser)) {
					LOGGER.info("User Registered with " + newUser.getEmail());
					out.print("done");
					LOGGER.debug("Done User save");
				} else {
					out.print("Something error");
					LOGGER.error("Something wents wrong");
				}

			}

		} catch (Exception e) {
			LOGGER.error(e);

		}
	}

}
