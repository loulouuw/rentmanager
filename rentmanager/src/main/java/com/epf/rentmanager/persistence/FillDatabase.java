package com.epf.rentmanager.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.DeleteDbFiles;

public class FillDatabase {
    public static void main(String[] args) throws Exception {
        try {
            DeleteDbFiles.execute("~", "RentManagerDatabase", true);
            insertWithPreparedStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	private static void insertWithPreparedStatement() throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        PreparedStatement createPreparedStatement = null;

        List<String> createTablesQueries = new ArrayList<>();
        createTablesQueries.add("CREATE TABLE IF NOT EXISTS Client(id INT primary key auto_increment, client_id INT, lastname VARCHAR(100), firstname VARCHAR(100), email VARCHAR(100), bdate DATETIME)");
        createTablesQueries.add("CREATE TABLE IF NOT EXISTS Vehicle(id INT primary key auto_increment, constructor VARCHAR(100), modele VARCHAR(100), seats TINYINT(255), reserved BOOLEAN)");
        createTablesQueries.add("CREATE TABLE IF NOT EXISTS Reservation(id INT primary key auto_increment, client_id INT, foreign key(client_id) REFERENCES Client(id), vehicle_id INT, foreign key(vehicle_id) REFERENCES Vehicle(id), beginning DATETIME, ending DATETIME)");

        try {
            connection.setAutoCommit(false);
            for (String createQuery : createTablesQueries) {
            	createPreparedStatement = connection.prepareStatement(createQuery);
	            createPreparedStatement.executeUpdate();
	            createPreparedStatement.close();
            }
            Statement stmt = connection.createStatement();
            stmt.execute("INSERT INTO Vehicle(constructor, modele, seats, reserved) VALUES('Renault', 'Clio', 4, FALSE)");
            stmt.execute("INSERT INTO Vehicle(constructor, modele, seats, reserved) VALUES('Peugeot', '3008', 4, FALSE)");
            stmt.execute("INSERT INTO Vehicle(constructor, modele, seats, reserved) VALUES('Seat', 'Ibiza', 4, FALSE)");
            stmt.execute("INSERT INTO Vehicle(constructor, modele, seats, reserved) VALUES('Nissan', 'Qashqai', 4, TRUE)");
            
            stmt.execute("INSERT INTO Client(lastname, firstname, email, bdate) VALUES('Dupont', 'Jean', 'jean.dupont@email.com', '1988-01-22')");
            stmt.execute("INSERT INTO Client(lastname, firstname, email, bdate) VALUES('Morin', 'Sabrina', 'sabrina.morin@email.com', '1988-01-22')");
            stmt.execute("INSERT INTO Client(lastname, firstname, email, bdate) VALUES('Afleck', 'Steeve', 'steeve.afleck@email.com', '1988-01-22')");
            stmt.execute("INSERT INTO Client(lastname, firstname, email, bdate) VALUES('Rousseau', 'Jacques', 'jacques.rousseau@email.com', '1988-01-22')");

            stmt.execute("INSERT INTO Reservation(client_id, vehicle_id, beginning, ending) VALUES(1, 1, '2020-04-10', '2020-04-15')");
            stmt.execute("INSERT INTO Reservation(client_id, vehicle_id, beginning, ending) VALUES(1, 1, '2022-07-11', '2022-07-15')");
            stmt.execute("INSERT INTO Reservation(client_id, vehicle_id, beginning, ending) VALUES(2, 2, '2024-06-28', '2024-08-04')");

            connection.commit();
            System.out.println("Success!");
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
