package org.slipchansky.lingualeo.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slipchansky.lingualeo.data.WordInfo;
import org.slipchansky.lingualeo.tools.ResourceAccessor;

public class LeoServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cookies = req.getParameter("cookies");
		String days = req.getParameter("days");
		
		int daysBefore = 30;
		if (days!=null) 
			try {
				daysBefore = Integer.parseInt(days);
			} catch (Exception e) {
				
			}
		
		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		PrintWriter out = resp.getWriter();
		
		
		List<WordInfo> dictionary = null;
		
		try {
		ResourceAccessor accessor = new ResourceAccessor (cookies);
		dictionary = accessor.getDictionary(daysBefore);
		} catch (Exception e) {
			out.println("<html><body>Please login lingualeo, go to <a href='http://lingualeo.com/ru/training'>http://lingualeo.com/ru/training</a> and try again<br>");
			out.println("<p>For start grabber, copy and paste the script into browser navigation bar:<br><pre>");
			out.println("javascript:document.location=\"http://localhost:8085/lld/go?days=5&cookies=\"+escape(document.cookie)</pre></p>");
			out.println("</body></html>");
			return;
		}
		
		
		out.println ("<html><body><form method='post' action='load'  accept-charset='utf-8'><table>");
		
		for (WordInfo w : dictionary) {
			for (String t : w.translations) {
				out.println ("<tr>");
				out.println ("<td>"+w.word+"</td>");
				out.println ("<td>"+t+"</td>");
				out.println ("<td><input type='checkbox' checked name='words' value='"+w.word+"___"+t+"___"+w.sound_url+"'></td>");
				out.println ("</tr>");
			}
		}
		
		out.println ("</table><br />");
		
		out.println ("<p>Direction<br>");
		out.println ("<input type=radio name='direction' checked value='forward'/>Word-Translation<br>");
		out.println ("<input type=radio name='direction' value='backward'/>Translation-Word</p>");

		out.println ("<p>File name<br>");
		out.println ("<input name='filename' value='dictionary.zip'/></p>");
		
		out.println ("<p><input type=submit value='download' /></p></form>");
		out.println ("</body></html>");
		
	}

}
