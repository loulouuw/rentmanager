package com.epf.rentmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.exception.ValideException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.stereotype.Service;

@Service

public class VehicleService {
	private VehicleDao vehicleDao;
	private ReservationDao reservationDao;
	private VehicleService(VehicleDao vehicleDao, ReservationDao reservationDao){
		this.vehicleDao = vehicleDao;
		this.reservationDao = reservationDao;
	}
	public void create(Vehicle vehicle) throws ServiceException {
		try{
			vehicleDao.create(vehicle);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public void delete(long id) throws ServiceException {
		try{
			for (Reservation reservation:reservationDao.findResaByVehicleId(vehicleDao.findById(id))) {
				reservationDao.delete(reservation);
			}
			vehicleDao.delete(id);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public Vehicle findById(long id) throws ServiceException {
		try{
			return vehicleDao.findById(id);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public List<Vehicle> findAll() throws ServiceException {
		try{
			return vehicleDao.findAll();
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public int getCount() throws ServiceException {
		try{
			return vehicleDao.getCount();
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public void modify(Vehicle vehicle) throws ServiceException {
		try{
			vehicleDao.modify(vehicle);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}
	}
	public boolean valideSeats(Vehicle vehicle) throws ValideException {
		if(vehicle.getSeats() < 2 || vehicle.getSeats() > 9){
			throw new ValideException("Une voiture possède 2 à 9 places.");
		}
		return true;
	}
}