package com.mad322.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/calculate")
public class Calculationsapi {

	
        //Pull Request Demo	
	
		@Path("/ftoc/{f}")
		@GET
		@Produces("application/json")
		public Response convertFtoC(@PathParam("f") double f)
		{
			JSONObject jsonObj = new JSONObject();
			
			double fahrenheit = f;
			double celsius = 0.0;
			
		
			try
			{
				celsius = (fahrenheit-32) * 5 / 9 ;
				
				jsonObj.accumulate("Fahrenheit", fahrenheit);
				jsonObj.accumulate("Celsius", celsius);
				
				
			}catch(Exception e)
			{
				jsonObj.accumulate("Error", "Something went wrong!");
				jsonObj.accumulate("Message", e.getMessage());
			}
			
			return Response.status(200).entity(jsonObj.toString()).build();
			
			
		}
		
		
		@Path("/ctof/{c}")
		@GET
		@Produces("applcation/json")
		public Response convertCtoF(@PathParam("c") double c)
		{
			
			JSONObject jsonObj = new JSONObject();
			double celsius = c;
			double fahrenheit = 0.0;
			
			celsius = ((celsius * 9)/5)+32;
			

			try
			{
				celsius = (fahrenheit-32) * 5 / 9 ;
				
				jsonObj.accumulate("Fahrenheit", fahrenheit);
				jsonObj.accumulate("Celsius", celsius);
				
				
			}catch(Exception e)
			{
				jsonObj.accumulate("Error", "Something went wrong!");
				jsonObj.accumulate("Message", e.getMessage());
			}
			
			return Response.status(200).entity(jsonObj.toString()).build();
		}
		
		
		@Path("/jsondata")
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public Response jsonData()
		{
			JSONObject mainObj = new JSONObject();
			JSONArray studentArray = new JSONArray();
			JSONObject childObj = new JSONObject();
			
			//First Child Object
			childObj.accumulate("id", 101);
			childObj.accumulate("FirstName", "Harsh");
			childObj.accumulate("LastName", "Dave");
			childObj.accumulate("Location", "Montreal");
			
			studentArray.put(childObj);
			
			//Second Child Object
			childObj = new JSONObject();
			
			childObj.accumulate("id",102);
			childObj.accumulate("FirstName", "Test");
			childObj.accumulate("LastName", "Student");
			childObj.accumulate("Location", "Toronto");
			
			studentArray.put(childObj);
			
			mainObj.put("Students", studentArray);
	
			
		
			return Response.status(200).entity(mainObj.toString()).build();	
			
		}

}
