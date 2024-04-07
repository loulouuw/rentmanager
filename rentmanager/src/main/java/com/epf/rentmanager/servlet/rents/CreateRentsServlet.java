package com.epf.rentmanager.servlet.rents;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.ValideException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/rents/create")
public class CreateRentsServlet extends HttpServlet {
    @Autowired
    private ClientService clientService;
    @Autowired
    private VehicleService vehicleService;
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
            request.setAttribute("allClients", clientService.findAll());
            request.setAttribute("allVehicles", vehicleService.findAll());
        }
        catch (ServiceException e){
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long client_id = Long.parseLong(request.getParameter("client_id"));
            long vehicle_id = Long.parseLong(request.getParameter("vehicle_id"));
            LocalDate beginning = LocalDate.parse(request.getParameter("beginning"));
            LocalDate ending = LocalDate.parse(request.getParameter("ending"));
            Reservation reservation = new Reservation(clientService.findById(client_id), vehicleService.findById(vehicle_id), beginning, ending);
            if(reservationService.valideReserv(reservation)
            && reservationService.valideSept(reservation)){
                reservationService.create(reservation);
            }
            request.setAttribute("allReservations", reservationService.findAll());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/list.jsp").forward(request, response);
        }
        catch (ServiceException e) {
            throw new ServletException(e);
        } catch (ValideException e) {
            e.printStackTrace();
            try {
                request.setAttribute("allClients", clientService.findAll());
                request.setAttribute("allVehicles", vehicleService.findAll());
            } catch (ServiceException ex) { throw new ServletException(ex); }
            request.setAttribute("errorMessage", e.getMessage());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/modify.jsp").forward(request, response);
        }
    }
}
