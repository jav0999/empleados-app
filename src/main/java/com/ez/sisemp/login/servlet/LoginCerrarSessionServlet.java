package com.ez.sisemp.login.servlet;

import com.ez.sisemp.login.business.UsuarioBusiness;
import com.ez.sisemp.login.enumeration.Roles;
import com.ez.sisemp.login.exception.UserOrPassIncorrectException;
import com.ez.sisemp.login.model.Usuario;
import com.ez.sisemp.shared.enums.Routes;

import javax.servlet.ServletException;
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
            response.sendRedirect(LOGIN_JSP);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
