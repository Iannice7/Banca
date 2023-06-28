package Banca.Conto;

import java.time.LocalDate;

public class ContoCorrente extends Conto{
	public ContoCorrente() {
		this.idTipo = "CC";

	}
	public void generaInteressi(LocalDate date, Titolare t) {
		super.generaInteressi(date, t);
	}   
}
