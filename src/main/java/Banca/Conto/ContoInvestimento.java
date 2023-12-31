package Banca.Conto;
import java.time.LocalDate;
import java.util.Random;

public class ContoInvestimento extends Conto{
	
	public ContoInvestimento() {
		this.idTipo = "CI";
		this.tasso = tassoRandom();
	}
	
	public void generaInteressi(LocalDate date, Titolare t, ContoInvestimento c) {
		super.generaInteressi(date, t,c);
	}
	
    public double tassoRandom() {
    	Random rand = new Random();
    	double tassoRand = rand.nextInt(200)-100;
		return tassoRand;
    }
    
}
