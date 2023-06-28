package Banca.Conto;

import java.time.LocalDate;
import java.util.Scanner;

import Banca.Connection.DBHandler;
import Banca.Connection.DBfunc;

public class GestoreATM {
	Conto sceltaUtente;
	Titolare t = new Titolare();
	DBHandler db = DBHandler.getInstance();
	DBfunc fdb = new DBfunc();
	
	public Titolare setUserUI() {
		Scanner scanner = new Scanner(System.in);
		String nome, cognome, citta, nazione,numero;
		System.out.println("Inserisci il nome: ");
		this.t.setNome(scanner.nextLine()); 
		System.out.println("Inserisci il cognome: ");
		this.t.setCognome(scanner.nextLine()); 
		System.out.println("Inserisci il citta: ");
		this.t.setCitta(scanner.nextLine()); 
		System.out.println("Inserisci il nazione: ");
		this.t.setNazione(scanner.nextLine()); 
		System.out.println("Inserisci il numero: ");
		this.t.setNumero(scanner.nextLine());
		fdb.dbUser(t);
		sceltaContoUtente();
		return t;
	}
	
	public void init() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Benvenuto, che operazione vuoi compiere: \n1 - Registrati \n2 - Login");
		int op = scanner. nextInt();
		switch (op) {
		case 1:
			register();
			break;
		case 2:
			login();
			break;
		default:
			break;
		}
	}
	
	public Conto sceltaContoUtente() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Scegli il tipo di conto che vuoi aprire\n - 1: Conto Corrente\n - 2: Conto Deposito\n - 3: Conto Investimento");
		int scelta = scanner.nextInt();
		
		switch(scelta) {
		case 1:
			 this.sceltaUtente = new ContoCorrente();
			 fdb.caricaConto(sceltaUtente, this.t);
			 display();
			 break;
		case 2:
			this.sceltaUtente = new ContoDeposito();
			fdb.caricaConto(sceltaUtente, this.t);
			display();
			break;
		case 3:
			this.sceltaUtente = new ContoInvestimento();
			fdb.caricaConto(sceltaUtente, this.t);
			display();
			break;
		}
		
		return sceltaUtente;
		
	}
	
	public void display() {
		int n = -1;
		Scanner scanner = new Scanner(System.in);
		simula();
		while(n!=6) {
		System.out.println("Scegli operazione: \n1- Preleva \n2- Versa \n3- Show Saldo\n 4- ChiudiConto\n 5- StampaEstrattoConto\n 6- Exit");
		n = scanner.nextInt();
		switch (n) {
		case 1:
			fdb.DBPreleva(sceltaUtente, this.t);
			this.sceltaUtente.preleva();
			fdb.DBAggiorna(sceltaUtente, this.t);
			break;
		case 2:
			fdb.DBVersa(sceltaUtente, this.t);
			this.sceltaUtente.versa();
			fdb.DBAggiorna(sceltaUtente, this.t);
			break;
		case 3:
			this.sceltaUtente.stampa(t);
			break;
		case 4:
			fdb.memorizzazioneInCSV(sceltaUtente, this.t);
			fdb.chiudiConto(sceltaUtente, this.t);
			break;
		case 5:
			fdb.memorizzazioneInCSV(sceltaUtente, this.t);
			break;
		case 6:
			break;

		default:
			System.out.println("Inserisci un numero valido");
			break;
		}

		}

	}
	
	
	public  void exit() {
		//System.exit(0);
	}
	
	public void register() {
		setUserUI();
	}
	
	public void login() {
		fdb.recuperaDatiUtente();
		display();
	}
	
	public void simula() {
        LocalDate date = LocalDate.of(2022, 12, 31);
	    this.sceltaUtente.generaInteressi(date,this.t);
	    this.sceltaUtente.stampa(this.t);
	    LocalDate date2 = LocalDate.of(2023, 12, 31);
	    this.sceltaUtente.generaInteressi(date2, this.t);
	    this.sceltaUtente.stampa(t);
	}

}
