package com.epf.rentmanager.servlet.users;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.ValideException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/users/create")
public class CreateUserServlet extends HttpServlet {

    @Autowired
    private ClientService clientService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Client client = null;
        try {
            LocalDate bdate = LocalDate.parse(request.getParameter("bdate"));
            String prenom = request.getParameter("firstname");
            String nom = request.getParameter("lastname");
            String email = request.getParameter("email");

            client = new Client(prenom, nom, email, bdate);

            if (clientService.valideAge(client) &&
                    clientService.valideName(prenom) &&
                    clientService.valideName(nom) &&
                    clientService.valideEmail(client)) {
                clientService.create(client);
            }

            request.setAttribute("allClients", clientService.findAll());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/list.jsp").forward(request, response);
        } catch (ServiceException e) {
            throw new ServletException(e);
        } catch (ValideException e) {
            e.printStackTrace();
            request.setAttribute("client", client);
            request.setAttribute("errorMessage", e.getMessage());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/modify.jsp").forward(request, response);
        }
    }

}
