package com.epf.rentmanager.servlet.cars;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
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

@WebServlet("/cars/details")
public class CarsDetailsServlet extends HttpServlet {
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
            long id = Long.parseLong(request.getParameter("id"));
            Vehicle vehicle = vehicleService.findById(id);
            String isReserved = "Non";
            if(vehicle.isReserved()){
                isReserved = "Oui";
            }
            request.setAttribute("vehicle", vehicle);
            request.setAttribute("isReserved", isReserved);
            request.setAttribute("nbReservations", reservationService.getCount(vehicle));
            request.setAttribute("allReservations", reservationService.findResaByVehicleId(vehicle));
        }
        catch (ServiceException e){
            throw new ServletException();
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/cars/details.jsp").forward(request, response);
    }
}
