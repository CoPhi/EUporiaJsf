package it.cnr.ilc.ga.utils;

import it.cnr.ilc.ga.action.management.ManagerBean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ReloadPericopes
 */
public class ReloadPericopes extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReloadPericopes() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String[] splittedModPericopes = null;
		String modPericopes = request.getParameter("modPericopes");

		if (null != modPericopes) {
			splittedModPericopes  = modPericopes.split("|");
		}
		// trattamento della lista delle pericopi modificate in modPericopes

		try {
			ManagerBean.getInstance().loadPericopes(splittedModPericopes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.sendRedirect("/GA_Wapp/controlPanelViewArabic.xhtml");

	}

}
