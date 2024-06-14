package com.ez.sisemp.empleado.servlet;

import com.ez.sisemp.empleado.business.EmpleadoBusiness;
import com.ez.sisemp.empleado.exception.EmailAlreadyInUseException;
import com.ez.sisemp.empleado.model.Empleado;
import com.ez.sisemp.login.business.EmpleadoEditarBusiness;
import com.ez.sisemp.parametro.dao.ParametroDao;
import com.ez.sisemp.parametro.model.Departamento;
import com.ez.sisemp.shared.enums.Routes;
import com.ez.sisemp.shared.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/empleado/editar")
public class EditarEmpleadoServlet extends HttpServlet {
    private ParametroDao parametroDao;
    private EmpleadoBusiness empleadoBusiness;

    @Override
    public void init() throws ServletException {
        super.init();
        parametroDao = new ParametroDao();
        empleadoBusiness = new EmpleadoBusiness();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmpleadoEditarBusiness business = new EmpleadoEditarBusiness();

        String id = req.getParameter("id");
        try {
          //CODIGO PARA REDIRECCIONARLO A LOGIN
            HttpSession session = req.getSession();

            if(null!=session.getAttribute("user")){
                System.out.println("username is "+session.getAttribute("user").toString());

            }
            else{
                resp.sendRedirect("/login/login.jsp");
                return;
            }
            //CODIGO PARA REDIRECCIONARLO A LOGIN
            loadDepartamentos(req);
            Empleado empleado = business.editar(id);
            session.setAttribute("useredit", empleado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        req.getRequestDispatcher("/empleado/editar.jsp").forward(req, resp);
    }

    private void loadDepartamentos(HttpServletRequest request)  {
        List<Departamento> departamentos = parametroDao.obtenerDepartamentos();
        request.setAttribute("departamentos", departamentos);
    }


    private Empleado createEmpleadoFromRequestEdit(HttpServletRequest request) throws ParseException {
        String strDate = request.getParameter("fechaNacimiento");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        return new Empleado(
                Integer.parseInt(request.getParameter("id")),
                request.getParameter("codigoEmpleado"),
                request.getParameter("nombres"),
                request.getParameter("apellidoPat"),
                request.getParameter("apellidoMat"),
                Integer.parseInt(request.getParameter("idDepartamento")),
                request.getParameter("correo"),
                Double.parseDouble(request.getParameter("salario")),
                sdf.parse(strDate));
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!SessionUtils.validarSesion(request, response)) {
            return;
        }
        try {
            Empleado empleado = createEmpleadoFromRequestEdit(request);
            empleadoBusiness.editarEmpleado(empleado);
            request.setAttribute("msj", "Empleado editado correctamente");
            response.sendRedirect(Routes.EMPLEADO.getRoute());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            handleParseException(request, response, e);
        } catch (EmailAlreadyInUseException e){
            System.out.println(e.getMessage());
            handleEmailAlreadyInUseException(request, response, e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServletException(e);
        }
    }

    private void handleParseException(HttpServletRequest request, HttpServletResponse response, ParseException e) throws ServletException, IOException {
        loadDepartamentos(request);
        request.setAttribute("error", "Fecha Nacimiento no válido, el formato debe ser yyyy-MM-dd");
        request.getRequestDispatcher("/empleado/editar.jsp").forward(request, response);
    }


    private void handleEmailAlreadyInUseException(HttpServletRequest request, HttpServletResponse response, EmailAlreadyInUseException e) throws ServletException, IOException {
        loadDepartamentos(request);
        request.setAttribute("error", e.getMessage());
        request.getRequestDispatcher("/empleado/registrar.jsp").forward(request, response);
    }


}
