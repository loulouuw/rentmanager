package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.stereotype.Repository;

@Repository

public class ReservationDao {
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, beginning, ending) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String MODIFY_RESERVATION_QUERY = "UPDATE Reservation SET client_id=?, vehicle_id=?, beginning=?, ending=? WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, beginning, ending FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, beginning, ending FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATION_QUERY = "SELECT vehicle_id, client_id, beginning, ending FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, beginning, ending FROM Reservation;";
	private static final String COUNT_RESERVATIONS_QUERY = "SELECT COUNT(*) AS total FROM Reservation;";
	private static final String COUNT_RESERVATIONS_BY_CLIENT_QUERY = "SELECT COUNT(*) AS total FROM Reservation WHERE client_id=?;";
	private static final String COUNT_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT COUNT(*) AS total FROM Reservation WHERE vehicle_id=?;";
	ClientDao clientDao;
	VehicleDao vehicleDao;

	private ReservationDao(ClientDao clientDao, VehicleDao vehicleDao){
		this.clientDao = clientDao;
		this.vehicleDao = vehicleDao;
	}

	public void delete(Reservation reservation) throws DaoException {

		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(DELETE_RESERVATION_QUERY);
			statement.setLong(1, reservation.getId());

			statement.execute();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public void create(Reservation reservation) throws DaoException {

		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(CREATE_RESERVATION_QUERY);
			statement.setLong(1, reservation.getClient().getId());
			statement.setLong(2, reservation.getVehicle().getId());
			statement.setDate(3, Date.valueOf(reservation.getBeginning()));
			statement.setDate(4, Date.valueOf(reservation.getEnding()));
			statement.execute();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public void modify(Reservation reservation) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(MODIFY_RESERVATION_QUERY);
			statement.setLong(1, reservation.getClient().getId());
			statement.setLong(2, reservation.getVehicle().getId());
			statement.setDate(3, Date.valueOf(reservation.getBeginning()));
			statement.setDate(4, Date.valueOf(reservation.getEnding()));
			statement.setLong(5, reservation.getId());
			statement.execute();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public Reservation findById(long id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATION_QUERY);
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			long vehicleId = rs.getLong("vehicle_id");
			long clientId = rs.getLong("client_id");
			LocalDate beginning = rs.getDate("beginning").toLocalDate();
			LocalDate ending = rs.getDate("ending").toLocalDate();
			rs.close();
			return new Reservation(id, clientDao.findById(clientId), vehicleDao.findById(vehicleId), beginning, ending);
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public List<Reservation> findResaByClientId(Client client) throws DaoException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);
			statement.setLong(1, client.getId());
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				long id = rs.getLong("id");
				long vehicleId = rs.getLong("vehicle_id");
				LocalDate beginning = rs.getDate("beginning").toLocalDate();
				LocalDate ending = rs.getDate("ending").toLocalDate();
				Reservation reservation = new Reservation(id, clientDao.findById(client.getId()), vehicleDao.findById(vehicleId), beginning, ending);
				reservations.add(reservation);
			}
			return reservations;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	public List<Reservation> findResaByVehicleId(Vehicle vehicle) throws DaoException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);
			statement.setLong(1, vehicle.getId());
			ResultSet rs = statement.executeQuery();
			while(rs.next()){
				long id = rs.getLong("id");
				long clientId = rs.getLong("client_id");
				LocalDate beginning = rs.getDate("beginning").toLocalDate();
				LocalDate ending = rs.getDate("ending").toLocalDate();
				Reservation reservation = new Reservation(id, clientDao.findById(clientId), vehicleDao.findById(vehicle.getId()), beginning, ending);
				reservations.add(reservation);
			}
			return reservations;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
	public List<Reservation> findAll() throws DaoException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		try(Connection connection = ConnectionManager.getConnection();){
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(FIND_RESERVATIONS_QUERY);
			while(rs.next()){
				long id = rs.getLong("id");
				long clientId = rs.getLong("client_id");
				long vehicleId = rs.getLong("vehicle_id");
				LocalDate beginning = rs.getDate("beginning").toLocalDate();
				LocalDate ending = rs.getDate("ending").toLocalDate();
				Reservation reservation = new Reservation(id, clientDao.findById(clientId), vehicleDao.findById(vehicleId), beginning, ending);
				reservations.add(reservation);
			}
			return reservations;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public int getCount() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(COUNT_RESERVATIONS_QUERY);
			rs.next();
			int count = rs.getInt("total");
			return count;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public int getCount(Client client) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(COUNT_RESERVATIONS_BY_CLIENT_QUERY);
			statement.setLong(1, client.getId());
			ResultSet rs = statement.executeQuery();
			rs.next();
			int count = rs.getInt("total");
			return count;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public int getCount(Vehicle vehicle) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(COUNT_RESERVATIONS_BY_VEHICLE_QUERY);
			statement.setLong(1, vehicle.getId());
			ResultSet rs = statement.executeQuery();
			rs.next();
			int count = rs.getInt("total");
			return count;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
}
