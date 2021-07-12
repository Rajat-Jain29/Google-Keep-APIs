package com.example.API.control;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.*;
@RestController
public class control {
	
	Connection connection;
	Statement statement;
	String query,connectionDB;
	ResultSet resultSet;
	FileHandler filehandler;
	Logger logger = Logger.getLogger("MyLog");
	final String string=""+java.time.LocalDate.now()+".txt";
	public control() throws SecurityException, IOException {
        super();
        filehandler = new FileHandler(string);  
        logger.addHandler(filehandler);
        SimpleFormatter formatter = new SimpleFormatter();  
        filehandler.setFormatter(formatter);
        logger.setLevel(Level.ALL);
        logger.addHandler(filehandler);
        try{
        	Class.forName("com.mysql.jdbc.Driver");
        	connectionDB = "jdbc:mysql://localhost:3306/note";
        	connection = DriverManager.getConnection(connectionDB, "root", "");
    		statement=connection.createStatement();
    		logger.info("Connected to Database");
        }
        catch(Exception e){
	    	   logger.info("Error message : "+e.getMessage());
	       }
    }
	
	@GetMapping("/")
	public ArrayList<HashMap<String, String>>show(HttpServletRequest request, HttpServletResponse res) throws IOException, SQLException {
		ArrayList<HashMap<String, String>> notesList=new ArrayList<>();
		try {
			query="select * from notes where isdeleted = '"+false+"' ";
			resultSet=statement.executeQuery(query);
			while(resultSet.next()) {	
				HashMap<String, String> map = new HashMap<>();
				map.put("noteId",""+resultSet.getInt("noteId") );
				map.put("title",""+resultSet.getString("title") );
				map.put("note",""+resultSet.getString("note") );	
				notesList.add(map);		
			}
		} catch (Exception e) {
			logger.info("Error message : "+e.getMessage());
		}
		return notesList;
	}
	
	@DeleteMapping("/{id}")
	public void deleteNote(@PathVariable("id") int id , HttpServletRequest request, HttpServletResponse res) throws Exception {
		query="update notes set isdeleted = '"+true+"'  where noteId = "+id+"";
		statement.executeUpdate(query);
		res.getWriter().print("Deleted SuccessFully");
	}
	
	@PostMapping("/create")
	public void postNote(HttpServletRequest request, HttpServletResponse res) throws Exception {
		String title=request.getParameter("title");
		String note=request.getParameter("note");
		query="insert into notes (title,note,isdeleted,ispinned) values ('"+title+"','"+note+"', '"+false+"' , '"+false+"')";
		statement.executeUpdate(query);
		res.getWriter().print("Inserted Succesfully");
	}
	
	@PutMapping("/edit/{id}")
	public void editNote(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse res) throws Exception {
		String title=request.getParameter("title");
		query="update notes set title = '"+title+"' where noteId = "+id+"  ";
		statement.executeUpdate(query);
		res.getWriter().print("Updated Succesfully");
	}
	
	@PutMapping("/restore/{id}")
	public void restore(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse res) throws Exception {
		query="update notes set isdeleted = '"+false+"' where noteId = "+id+"  ";
		statement.executeUpdate(query);
		res.getWriter().print("Restored Succesfully");
	}
	
	@PutMapping("/pinned/{id}")
	public void pinned(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse res) throws Exception {
		query="update notes set ispinned = '"+true+"' where noteId = "+id+" ";
		statement.executeUpdate(query);
		res.getWriter().print("Pinned Succesfully");
	}
	
	@PutMapping("/removepinned/{id}")
	public void removepinned(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse res) throws Exception {
		query="update notes set ispinned = '"+false+"' where noteId = "+id+" ";
		statement.executeUpdate(query);
		res.getWriter().print("Unpinned Succesfully");
	}
	
	@GetMapping("/showpinned")
	public ArrayList<HashMap<String, String>  >  showpinned(HttpServletRequest request, HttpServletResponse res) throws IOException, SQLException {
		ArrayList<HashMap<String, String>> a=new ArrayList<>();
		try {
			query="select * from notes where isdeleted = '"+false+"' and ispinned='"+true+"' ";
			resultSet=statement.executeQuery(query);
			while(resultSet.next()) {	
				HashMap<String, String> map = new HashMap<>();
				map.put("noteId",""+resultSet.getInt("noteId") );
				map.put("title",""+resultSet.getString("title") );
				map.put("note",""+resultSet.getString("note") );	
				a.add(map);		
			}
		} catch (Exception e) {
			logger.info("Error message : "+e.getMessage());
		}
		return a;
	}
	
}