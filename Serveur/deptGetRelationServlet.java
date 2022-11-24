package servlet;

import java.io.*;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;

import inc.Fonction;
import objets.*;

public class deptGetRelationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("text/plain");

        PrintWriter out = res.getWriter();

        String requete = req.getParameter("requete");

        Fonction f = new Fonction();
        
        try {
            Table relation = f.traiterRequete(requete);
            req.setAttribute("relation", relation);
            req.setAttribute("requete", requete);
            RequestDispatcher dispat = req.getRequestDispatcher("/index.jsp");
            dispat.forward(req, res); // Ne rien faire sur req et res
        } catch (Exception e) {
            out.println(e.getLocalizedMessage());
        }

    }
}