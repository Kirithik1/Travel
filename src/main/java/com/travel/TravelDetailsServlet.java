package com.travel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class TravelDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/travel";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "7966";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

      protected void doPost(HttpServletRequest request, HttpServletResponse response)
              throws IOException {
    	// Response Header
          response.setContentType("application/json");

          // Allow requests from any origin
          response.setHeader("Access-Control-Allow-Origin", "*");

          // Allow specific HTTP methods
          response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

          // Allow specific headers
          response.setHeader("Access-Control-Allow-Headers", "Content-Type");

          // Allow credentials (if needed)
          response.setHeader("Access-Control-Allow-Credentials", "true");
          
      try {
          String destinationid = request.getParameter("destinationid");
          String destination = request.getParameter("destination");
          String date = request.getParameter("date");
          String eat = request.getParameter("Eat");
          String leisure = request.getParameter("Leisure");
          String transportation = request.getParameter("Transportation");

          try {
              Class.forName("com.mysql.cj.jdbc.Driver");
              Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
              String insertQuery = "INSERT INTO itinerary (destinationid, destination, date, Eat, Leisure, Transportation) VALUES (?, ?, ?, ?, ?, ?)";
              try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                  preparedStatement.setString(1, destinationid);
                  preparedStatement.setString(2, destination);
                  preparedStatement.setString(3, date);
                  preparedStatement.setString(4, eat);
                  preparedStatement.setString(5, leisure);
                  preparedStatement.setString(6, transportation);
                  preparedStatement.executeUpdate();
              } catch (Exception e) {
                  
        
              }

          } catch (Exception e) {
              
              
          }

          response.setStatus(HttpServletResponse.SC_CREATED);
      } catch (Exception e) {
          e.printStackTrace();
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding destination");
      }
  }

      protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	  
          JSONObject result = new JSONObject();

          // Response Header
          response.setContentType("application/json");

          // Allow requests from any origin
          response.setHeader("Access-Control-Allow-Origin", "*");

          // Allow specific HTTP methods
          response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

          // Allow specific headers
          response.setHeader("Access-Control-Allow-Headers", "Content-Type");

          // Allow credentials (if needed)
          response.setHeader("Access-Control-Allow-Credentials", "true");

          try {
              String destinationID = null;
              String pathInfo = request.getPathInfo();
              if (pathInfo != null && pathInfo.length() > 1) {
                  destinationID = pathInfo.substring(1);
              }

              String destination = request.getParameter("destination");
              String date = request.getParameter("date");
              String eat = request.getParameter("Eat");
              String leisure = request.getParameter("Leisure");
              String transportation = request.getParameter("Transportation");

              try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                  String updateQuery = "UPDATE itinerary SET destination = ?, date = ?, eat = ?, leisure = ?, transportation = ? WHERE destinationID = ?";
                  try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                      preparedStatement.setString(1, destination);
                      preparedStatement.setString(2, date);
                      preparedStatement.setString(3, eat);
                      preparedStatement.setString(4, leisure);
                      preparedStatement.setString(5, transportation);

                      if (destinationID != null) {
                          preparedStatement.setString(6, destinationID);

                          int rowsAffected = preparedStatement.executeUpdate();
                          if (rowsAffected > 0) {
                              result.put("status", "success");
                              result.put("message", "Itinerary details updated successfully.");
                              response.setStatus(HttpServletResponse.SC_OK);
                          } else {
                              result.put("status", "error");
                              result.put("message", "Destination ID not found.");
                              response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                          }
                      } else {
                          result.put("status", "error");
                          result.put("message", "Destination ID is missing in the request path.");
                          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                      }
                  }
              }
          } catch (SQLException e) {
              e.printStackTrace();
              result.put("status", "error");
              result.put("message", "Error updating destination details");
              response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          } catch (IllegalArgumentException e) {
              result.put("status", "error");
              result.put("message", e.getMessage());
              response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
          }

          response.getWriter().println(result.toString());
      }
  }