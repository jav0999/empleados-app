package com.ez.sisemp.login.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/login/cerrarSesion")
public class LoginCerrarSessionServlet extends HttpServlet {

    private static final String LOGIN_JSP = "/login/login.jsp";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            session.removeAttribute("user");
            if(session!=null)
            {
                session.invalidate();
            }
            response.sendRedirect(LOGIN_JSP);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
