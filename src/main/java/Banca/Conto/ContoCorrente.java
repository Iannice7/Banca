package Banca.Conto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import Banca.Connection.DBHandler;

public class ContoCorrente extends Conto{
	public ContoCorrente() {
		this.idTipo = "CC";
		this.tasso = retrieveTasso();
	}
	public void generaInteressi(LocalDate date, Titolare t) {
		super.generaInteressi(date, t);
	}
	
    public double retrieveTasso() {
        DBHandler db = new DBHandler();
        Statement stm;
        double tasso = 0.0;
        try {
            stm = db.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("SELECT tasso FROM tipo_conto WHERE id_tipo_conto = 'CC'");
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
