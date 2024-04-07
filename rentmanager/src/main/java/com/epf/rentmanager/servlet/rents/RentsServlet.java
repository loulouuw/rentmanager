package com.epf.rentmanager.servlet.rents;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rents")
public class RentsServlet extends HttpServlet {

    @Autowired
    private ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            request.setAttribute("allReservations", reservationService.findAll());
        }
        catch (ServiceException e){
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(request, response);
    }
    @WebServlet("/users/details")
    public static class UserDetailsServlet extends HttpServlet {
        @Autowired
        private ClientService clientService;
        @Autowired
        private ReservationService reservationService;
        @Override
        public void init() throws ServletException {
            super.init();
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        }
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            try{
                long id = Long.parseLong(request.getParameter("id"));
                Client client = clientService.findById(id);
                request.setAttribute("client", client);
                request.setAttribute("nbReservations", reservationService.getCount(client));
                request.setAttribute("allReservations", reservationService.findResaByClientId(client));
                request.setAttribute("nbVehicles", reservationService.getActualVehiclesCount(client));
                request.setAttribute("allVehicles", reservationService.findActualVehiclesByClientId(client));
            }
            catch (ServiceException e){
                throw new ServletException();
            }
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
        }

    }
}
