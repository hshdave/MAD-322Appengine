package com.mad322.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import org.json.JSONArray;
import org.json.JSONObject;

@Path("/aws")
public class Awsmyqlexample {

	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	PreparedStatement preparedStatement = null;

	JSONObject mainObj = new JSONObject();
	JSONArray jsonArray = new JSONArray();
	JSONObject childObj = new JSONObject();

	@GET
	@Path("/getEmp")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmployee() {
		MysqlConnection connection = new MysqlConnection();

		con = connection.getConnection();

		try {
			stmt = con.createStatement();

			rs = stmt.executeQuery("Select * from employees");

			while (rs.next()) {
				childObj = new JSONObject();

				childObj.accumulate("Employee Number", rs.getString("employeeNumber"));
				childObj.accumulate("Last Name", rs.getString(2));
				childObj.accumulate("First Name", rs.getString("firstName"));

				jsonArray.put(childObj);
			}

			mainObj.put("Employees", jsonArray);
		} catch (SQLException e) {
			System.out.println("SQL Exception : " + e.getMessage());
		} finally {
			try {
				con.close();
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				System.out.println("Finally Block SQL Exception : " + e.getMessage());
			}
		}

		return Response.status(200).entity(mainObj.toString()).build();

	}

	@POST
	@Path("/createEmp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEmployee(Employees employees) {
		MysqlConnection connection = new MysqlConnection();

		con = connection.getConnection();

		try {

			// '' Single quotes
			// ` Grave accent

			String query = "INSERT INTO `employees`(`employeeNumber`,`lastName`,`firstName`,`extension`,`email`,`officeCode`,`reportsTo`,`jobTitle`)"
					+ "VALUES(?,?,?,?,?,?,?,?)";

			preparedStatement = con.prepareStatement(query);

			preparedStatement.setInt(1, employees.getEmployeeNumnber());
			preparedStatement.setString(2, employees.getLastName());
			preparedStatement.setString(3, employees.getFirstName());
			preparedStatement.setString(4, employees.getExtension());
			preparedStatement.setString(5, employees.getEmail());
			preparedStatement.setString(6, employees.getOfficeCode());
			preparedStatement.setInt(7, employees.getReportsTo());
			preparedStatement.setString(8, employees.getJobTitle());

			int rowCount = preparedStatement.executeUpdate();

			if (rowCount > 0) {
				System.out.println("Record inserted Successfully! : " + rowCount);

				mainObj.accumulate("Status", 201);
				mainObj.accumulate("Message", "Record Successfully added!");

			} else {
				mainObj.accumulate("Status", 500);
				mainObj.accumulate("Message", "Something went wrong!");
			}

		} catch (SQLException e) {

			mainObj.accumulate("Status", 500);
			mainObj.accumulate("Message", e.getMessage());
		} finally {
			try {
				con.close();
				preparedStatement.close();
			} catch (SQLException e) {
				System.out.println("Finally SQL Exception : " + e.getMessage());
			}
		}

		return Response.status(201).entity(mainObj.toString()).build();

	}

	@GET
	@Path("/office/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOffice(@PathParam("id") String id) {

		MysqlConnection connection = new MysqlConnection();

		con = connection.getConnection();

		try {
			stmt = con.createStatement();

			String query = "Select * from offices where officeCode=" + id;

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				mainObj.accumulate("ID", rs.getString("officeCode"));
				mainObj.accumulate("City", rs.getString("city"));
				mainObj.accumulate("phone", rs.getString("phone"));
				mainObj.accumulate("addressLine1", rs.getString("addressLine1"));
				mainObj.accumulate("addressLine2", rs.getString("addressLine2"));
				mainObj.accumulate("state", rs.getString("state"));
				mainObj.accumulate("country", rs.getString("country"));
				mainObj.accumulate("postalcode", rs.getString("postalCode"));
				mainObj.accumulate("territory", rs.getString("territory"));
			}

			if (!mainObj.isEmpty()) {
				System.out.println("Found!");
				return Response.ok().entity(mainObj.toString()).build();

			} else {
				System.out.println("Not found!");
				mainObj.accumulate("Status", 404);
				mainObj.accumulate("Message", "Content Not Found!");
				return Response.status(Response.Status.NOT_FOUND).entity(mainObj.toString()).build();
			}

		} catch (SQLException e) {
			mainObj.accumulate("Status", 404);
			mainObj.accumulate("Message", e.getMessage());

		}

		return Response.status(Response.Status.NOT_FOUND).entity(mainObj.toString()).build();

	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/updateEmp/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEmployee(@PathParam("id") int id, Employees employees) {
		MysqlConnection connection = new MysqlConnection();

		con = connection.getConnection();
		Status status = Status.OK;
		try {
			String query = "UPDATE `employees` SET `employeeNumber` = ?, `lastName` = ?, `firstName` = ?, `extension` = ?,`email` = ?,`officeCode` = ?,`reportsTo` = ?,"
					+ "`jobTitle` = ?WHERE `employeeNumber` = " + id;

			preparedStatement = con.prepareStatement(query);

			preparedStatement.setInt(1, employees.getEmployeeNumnber());
			preparedStatement.setString(2, employees.getLastName());
			preparedStatement.setString(3, employees.getFirstName());
			preparedStatement.setString(4, employees.getExtension());
			preparedStatement.setString(5, employees.getEmail());
			preparedStatement.setString(6, employees.getOfficeCode());
			preparedStatement.setInt(7, employees.getReportsTo());
			preparedStatement.setString(8, employees.getJobTitle());

			int rowCount = preparedStatement.executeUpdate();

			if (rowCount > 0) {
				status = Status.OK;
				mainObj.accumulate("Status", status);
				mainObj.accumulate("Message", "Data successfully updated!");
			} else {
				status = Status.NOT_MODIFIED;
				mainObj.accumulate("Status", status);
				mainObj.accumulate("Message", "Something Went wrong!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			status = Status.NOT_MODIFIED;
			mainObj.accumulate("Status", status);
			mainObj.accumulate("Message", "Something Went wrong!");
		}

		return Response.status(status).entity(mainObj.toString()).build();
	}

	@DELETE
	@Path("/delEmp/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEmployee(@PathParam("id") String id) {
		MysqlConnection connection = new MysqlConnection();

		con = connection.getConnection();
		Status status = Status.OK;

		try {

			String query = "DELETE FROM `employees` WHERE `employeeNumber` ="+id;
			
			stmt = con.createStatement();
			
			int rowCount = stmt.executeUpdate(query);
			
			if (rowCount > 0) {
				status = Status.OK;
				mainObj.accumulate("Status", status);
				mainObj.accumulate("Message", "Data successfully Deleted!");
			} else {
				status = Status.NOT_MODIFIED;
				mainObj.accumulate("Status", status);
				mainObj.accumulate("Message", "Something Went wrong!");
			}
			
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			status = Status.NOT_MODIFIED;
			mainObj.accumulate("Status", status);
			mainObj.accumulate("Message", "Something Went wrong!");
		}

		return Response.status(status).entity(mainObj.toString()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/demo")
	public Response getDemo()
	{
		mainObj.accumulate("Demo", "New Demo");
		
		return Response.status(200).entity(mainObj.toString()).build();
	}
}
