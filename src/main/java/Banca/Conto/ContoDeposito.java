package Banca.Conto;

import java.time.LocalDate;

public class ContoDeposito extends Conto{
	public ContoDeposito() {
		this.idTipo = "CD";
//		this.tasso = retrieveTasso();
		
	}  
	public void generaInteressi(LocalDate date, Titolare t) {
		super.generaInteressi(date, t);
	}
	
	 public void preleva(int n) {
		    	while(true) {
		    		if(n<1000) {
		    			super.preleva();
		    			break;
		    		}
		    		else 
		    			System.out.println("Errore, prelievo massimo di 1000$");
		    	}
		    }
}
