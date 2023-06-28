package Banca.Conto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;
import Banca.Connection.DBHandler;

public class ContoInvestimento extends Conto{
	
	public ContoInvestimento() {
		this.idTipo = "CI";
		this.tasso = tassoRandom();
		//this.tasso = retrieveTasso();
	}
	
	public void generaInteressi(LocalDate date, Titolare t) {
		super.generaInteressi(date, t);
	}
	
    public double tassoRandom() {
    	Random rand = new Random();
    	double tassoRand = rand.nextInt(200)-100;
		return tassoRand;
    }
    
    public double retrieveTasso() {
        DBHandler db = new DBHandler();
        Statement stm;
        double tasso = 0.0;
        try {
            stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("SELECT tasso FROM tipo_conto WHERE id_tipo_conto = 'CI'");
            if (rs.next()) {
                tasso = rs.getDouble("tasso");
                System.out.println("Il tasso recuperato è di: " + tasso);
            }
            rs.close();
            stm.close();
            db.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasso;
    }
    
    public double retrieveSaldo() {
        DBHandler db = new DBHandler();
        Statement stm;
        double saldo = 0.0;
        try {
            stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("SELECT saldo FROM conto WHERE id_tipo_conto = 'CC'");
            if (rs.next()) {
                saldo = rs.getDouble("saldo");
            }
            rs.close();
            stm.close();
            db.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return saldo;
    }
}
