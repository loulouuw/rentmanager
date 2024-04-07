package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository

public class VehicleDao {
	
	private static VehicleDao instance = null;
	
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructor, modele, seats) VALUES(?, ?, ?);";
	private static final String MODIFY_VEHICLE_QUERY = "UPDATE Vehicle SET constructor=?, modele=?, seats=?, reserved=? WHERE id=?;";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructor, modele, seats, reserved FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructor, modele, seats, reserved FROM Vehicle;";
	private static final String COUNT_VEHICLES_QUERY = "SELECT COUNT(*) AS total FROM Vehicle;";

	public void delete(long id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(DELETE_VEHICLE_QUERY);
			statement.setLong(1, id);
			statement.execute();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public void create(Vehicle vehicle) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(CREATE_VEHICLE_QUERY);
			statement.setString(1, vehicle.getConstructor());
			statement.setString(2, vehicle.getModele());
			statement.setInt(3, vehicle.getSeats());
			statement.execute();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public Vehicle findById(long id) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(FIND_VEHICLE_QUERY);
			statement.setLong(1, id);
			ResultSet rs = statement.executeQuery();
			rs.next();
			String constructor = rs.getString("constructor");
			String modele = rs.getString("modele");
			int seats = rs.getInt("seats");
			boolean reserved = rs.getBoolean("reserved");
			return new Vehicle(id, constructor, modele, seats, reserved);
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public List<Vehicle> findAll() throws DaoException {
		List<Vehicle> vehicules = new ArrayList<Vehicle>();
		try(Connection connection = ConnectionManager.getConnection();){
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(FIND_VEHICLES_QUERY);
			while(rs.next()){
				long id = rs.getInt("id");
				String constructor = rs.getString("constructor");
				String modele = rs.getString("modele");
				int seats = rs.getInt("seats");
				boolean reserved = rs.getBoolean("reserved");
				Vehicle vehicule = new Vehicle(id, constructor, modele, seats, reserved);
				vehicules.add(vehicule);
			}
			return vehicules;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public int getCount() throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(COUNT_VEHICLES_QUERY);
			rs.next();
			int count = rs.getInt("total");
			return count;
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}

	public void modify(Vehicle vehicle) throws DaoException {
		try(Connection connection = ConnectionManager.getConnection();){
			PreparedStatement statement = connection.prepareStatement(MODIFY_VEHICLE_QUERY);
			statement.setString(1, vehicle.getConstructor());
			statement.setString(2, vehicle.getModele());
			statement.setInt(3, vehicle.getSeats());
			statement.setBoolean(4, vehicle.isReserved());
			statement.setLong(5, vehicle.getId());
			statement.execute();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw new DaoException(e);
		}
	}
}
