package com.epf.rentmanager.servlet.cars;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.ValideException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cars/create")
public class CreateCarsServlet extends HttpServlet {
    @Autowired
    private VehicleService vehicleService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/views/cars/create.jsp").forward(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Vehicle vehicle = null;
        try {
            String constructor = request.getParameter("constructor");
            String modele = request.getParameter("modele");
            int seats = Integer.parseInt(request.getParameter("seats"));
            vehicle = new Vehicle(constructor, modele, seats);
            if (vehicleService.valideSeats(vehicle)) {
                vehicleService.create(vehicle);
            }
            request.setAttribute("allVehicles", vehicleService.findAll());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/cars/list.jsp").forward(request, response);
        } catch (ServiceException e) {
            throw new ServletException(e);
        } catch (ValideException e) {
            e.printStackTrace();
            request.setAttribute("vehicle", vehicle);
            request.setAttribute("errorMessage", e.getMessage());
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/cars/modify.jsp").forward(request, response);
        }
    }
}
