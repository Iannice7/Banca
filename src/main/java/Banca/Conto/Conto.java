package Banca.Conto;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public abstract class Conto {
	    //private static final double SALDO_INIZIALE = 1000;
	    private int idConto;
	    private int idCorrentista;
	    protected String idTipo;
		protected Titolare titolare;
		private String importo;
		protected LocalDate dataUltimoMovimento; //dataUltimoMovimento
	    //protected LocalDate dataSucc; //dataLocale
	    protected double saldo;
	    protected double tasso;
	    private double totale;
		double ultimoInteresse;
    	boolean flag = true;

	    public Conto() {}
	    

	    public double aggiornaSaldo() {
	        this.saldo += this.totale;
	        this.saldo = Math.round(this.saldo * 100.0) / 100.0; 
	        this.totale = 0;
	        System.out.println("Saldo aggiornato con il totale accumulato: " + Math.round(this.saldo * 100.0) / 100.0);
	        return this.saldo;
	    }	   	   
	    
	    public void preleva() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Quanto vuoi prelevare? ");
	        int n = scanner.nextInt();
	        if (n > saldo) {
	            System.out.println("Impossibile prelevare, saldo non disponibile.");
	            }
	            else {
	            	char asciiMinus = (char) 45; // codice ASCII per "+"
	    	        this.importo = Character.toString(asciiMinus) + " " + n;
	                this.saldo -= n;
	                this.saldo = Math.round(this.saldo * 100.0) / 100.0;
	            }
            this.dataUltimoMovimento = getDataUltimoMovimento();
	        }
	    

	    public void versa() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Quanto vuoi versare? ");
	        int n = scanner.nextInt(); 
	        char asciiPlus = (char) 43; // codice ASCII per "+"
	        this.importo = Character.toString(asciiPlus) + " " + n;
	        this.saldo += n;
	        this.saldo = Math.round(this.saldo * 100.0) / 100.0;
	        
	        this.dataUltimoMovimento = getDataUltimoMovimento();
	    }
	    
	    public void generaInteressi(LocalDate date, Titolare t, Conto c) {
	    	System.out.println(date);
	    	if(flag) {
		    	this.dataUltimoMovimento = date;
		    	this.flag = false;
	    	}
	    	else {
	    	double interesseGiornaliero;
	    	interesseGiornaliero = ((getTasso() * getSaldo()) / 100)/365;
	    	long n = ChronoUnit.DAYS.between(getDataUltimoMovimento(), date);
	    	System.out.println("Dal tuo ultimo accesso sono passati: " + n + " giorni");
	    	ultimoInteresse = (Math.abs(interesseGiornaliero) * n) * Math.signum(interesseGiornaliero);
	    	ultimoInteresse = Math.round(ultimoInteresse * 100.0) / 100.0;
	    	System.out.println("Ultimo interesse: " + ultimoInteresse);
			totale += ultimoInteresse;
			totale = Math.round(totale * 100.0) / 100.0;
			setDataUltimoMovimento(date);
			System.out.println(getDataUltimoMovimento());
			n = 0;
	    	}
	    	//SQL INSERIRE ULTIMO INTERESSE NELLA STAMPA ESTRATT CONTO DI ISTANZA  	 
	    }
	    
	    public long calcolaDiffAnni(LocalDate dataPrecedente, LocalDate dataSuccessiva) {
	        return ChronoUnit.YEARS.between(dataPrecedente, dataSuccessiva);
	    }
	    	    

	    public Titolare getTitolare() {return titolare;}
		public void setTitolare(Titolare titolare) {this.titolare = titolare;}
		public double getSaldo() {return saldo;}
		public void setSaldo(double saldo) {this.saldo = saldo;}
		public double getTasso() {return tasso;}
		public void setTasso(double tasso) {this.tasso = tasso;}		
		public String getIdTipo() {return idTipo;}
		public void setIdTipo(String idTipo) {this.idTipo = idTipo;}
		public double getTotale() {return totale;}
		public void setTotale(double totale) {this.totale = totale;}	
		public String getImporto() {return importo;}
		public void setImporto(String importo) {this.importo = importo;}				
		public int getIdConto() {return idConto;}
		public void setIdConto(int idConto) {this.idConto = idConto;}		
		public int getIdCorrentista() {return idCorrentista;}
		public void setIdCorrentista(int idCorrentista) {this.idCorrentista = idCorrentista;}						
		public LocalDate getDataUltimoMovimento() {return dataUltimoMovimento;}
		public void setDataUltimoMovimento(LocalDate dataUltimoMovimento) {this.dataUltimoMovimento = dataUltimoMovimento;}
		public double getUltimoInteresse() {return ultimoInteresse;}
		public void setUltimoInteresse(double ultimoInteresse) {this.ultimoInteresse = ultimoInteresse;}


		public void stampa(Titolare titolare) {   //TOGLIERE LO STAMPA E METTERLO COME METODO ABSTARCT
	    	System.out.println(titolare.getNome() + " " + titolare.getCognome() +  " " + getSaldo());
	    }
    
	}
