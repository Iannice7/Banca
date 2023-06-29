package Banca.Conto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
			//			display();
			break;
		case 2:
			this.sceltaUtente = new ContoDeposito();
			fdb.caricaConto(sceltaUtente, this.t);
			//			display();
			break;
		case 3:
			this.sceltaUtente = new ContoInvestimento();
			fdb.caricaConto(sceltaUtente, this.t);
			//			display();
			break;
		}

		return sceltaUtente;

	}

	public void display() {
		int n = -1;
		Scanner scanner = new Scanner(System.in);
		while(n!=6) {
			System.out.println("Scegli operazione: \n1- Preleva \n2- Versa \n3- Show Saldo\n4- Estratto Conto\n5- Chiudi Conto(Logout)\n6- Exit \n7- Crea Nuovo");
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
				this.sceltaUtente.stampa(this.t);
				break;
			case 4:
				fdb.memorizzazioneInCSV(sceltaUtente, this.t);
				break;
			case 5:	
				fdb.memorizzazioneInCSV(sceltaUtente, this.t);
				fdb.chiudiConto(this.sceltaUtente, this.t);
				new GestoreATM().init();
				break;
			case 7: 
				sceltaUtente = sceltaContoUtente();
				break;

			case 6:
				sceltaUtente.setDataUltimoMovimento(LocalDate.now());
				sceltaUtente.stampa(this.t);
				sceltaUtente.generaInteressi(sceltaUtente.getDataUltimoMovimento(), this.t, this.sceltaUtente);
				LocalDate date2 = LocalDate.of(2023, 12, 31);
				this.sceltaUtente.generaInteressi(date2, this.t,this.sceltaUtente);
				this.sceltaUtente.stampa(t);
				logout();
				break;

			default:
				System.out.println("Inserisci un numero valido");
				break;
			}

		}

	}


	public  void exit() {
		System.exit(0);
	}

	public void register() {
		setUserUI();
	}

	public void login() {
		this.sceltaUtente = (fdb.recuperaDatiUtente());
		if (this.sceltaUtente != null) {
			this.t = fdb.retrieveTitolare(sceltaUtente);
			if (this.t != null) {
				//this.sceltaUtente.retrieveSaldo();
				System.out.println(t.toString());
				display();
			} else {
				System.out.println("No Titolare found with the provided ID.");
			}
		} else {
			System.out.println("No user data found.");
		}
	}

	public void simula() {
		this.sceltaUtente.generaInteressi(sceltaUtente.getDataUltimoMovimento(),this.t,this.sceltaUtente);
		this.sceltaUtente.stampa(this.t);
		LocalDate date2 = LocalDate.of(2023, 12, 31);
		this.sceltaUtente.generaInteressi(date2, this.t,this.sceltaUtente);
		this.sceltaUtente.stampa(t);
	}

	public boolean logout() {
		return false;
	}

	public long convertiData(Conto sceltaUtente, Titolare t) {		
		String dateString = "2029-11-30";
		LocalDate parsedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate currentDate = LocalDate.now();
		long daysDifference = ChronoUnit.DAYS.between(parsedDate, currentDate);
		System.out.println("Difference in days: " + daysDifference);
		return daysDifference;
	}


}
