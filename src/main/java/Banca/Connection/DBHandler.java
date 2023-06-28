package Banca.Connection;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHandler {
	
	private static DBHandler instance = null;
	Connection conn = null;
	private String DB_NAME;
	private String DB_URL;
	private final Properties prop;
	
	public static DBHandler getInstance() {
		if (instance == null) {
			instance = new DBHandler();
		}
		return instance;
	}
    
	
	public DBHandler() {
		   prop = new Properties();
		    try {
		        FileInputStream fis = new FileInputStream("C:\\Workspace\\Banca\\resources\\config.properties");
		        prop.load(fis);
		    } catch (IOException e) {
		        System.out.println("Errore durante il caricamento del file config.properties");
		        e.printStackTrace();
		    }
		    
		    this.DB_NAME = prop.getProperty("db.name");
		    this.DB_URL = prop.getProperty("db.url") + DB_NAME;
	}
	
	public Connection getConnection() {
	    try {
            conn = DriverManager.getConnection(DB_URL,prop);
    	    //System.out.println("Connesso al database.");
	    } catch (SQLException e) {
	        // TODO Auto-generated catch block
    	    System.out.println("Impossibile connettersi al database.");
	        e.printStackTrace();
	    }

	    return conn;
	}

	public void closeConnection() {
	    if (conn != null) {
	        try {
	            conn.close();
	            //System.out.println("Connessione al database chiusa.");
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	}
	}
	
