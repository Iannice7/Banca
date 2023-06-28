
package Banca.Banca;
import java.time.LocalDate;
import java.util.Random;

import Banca.Conto.Conto;
import Banca.Conto.ContoCorrente;
import Banca.Conto.GestoreATM;
import Banca.Conto.Titolare;

public class App 
{
    public static void main( String[] args )
    {
        GestoreATM display = new GestoreATM();
        display.init();
        while(true) {
        	if(display.logout()==false)
        		display = new GestoreATM();
        		display.init();
        	break;
        }
        
    }
    
    public static LocalDate dataRandom(Titolare t) {
    	LocalDate data = null;
        Random random = new Random();
        int rangeGiorni = 31 - 1 + 1;
        int rangeMesi = 12 - 1 + 1;
        int rangeAnni = 2015 - t.getDataApertura().getYear() + 1;        
        int annoCasuale = t.getDataApertura().getYear() + random.nextInt(rangeAnni);
        int meseCasuale= 1 + random.nextInt(rangeMesi);
        int giornoCasuale = getGiorniDelMese(annoCasuale, meseCasuale);
        data = LocalDate.of(annoCasuale, meseCasuale, giornoCasuale);
        System.out.println("Generata data: " +  data);
		return data;

    }
    
    public static void controllaData( LocalDate date, Conto conto) {
    	if (date.getMonthValue()==12 && date.getDayOfMonth() == date.lengthOfMonth()) {
    		conto.aggiornaSaldo();
    		
    		//STAMPARE SU PDF TUTTI GLI ESTRATTI CONTI CICLATI DA APERTURA FINO A QUELLA CAUSALE
    }
    }
    
    private static int getGiorniDelMese(int anno, int mese) {
        LocalDate data = LocalDate.of(anno, mese, 1);
        return data.lengthOfMonth();
    }
}


