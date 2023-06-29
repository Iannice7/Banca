package Banca.Connection;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;
import com.opencsv.CSVWriter;
import Banca.Conto.Conto;
import Banca.Conto.ContoCorrente;
import Banca.Conto.ContoDeposito;
import Banca.Conto.ContoInvestimento;
import Banca.Conto.Titolare;

public class DBfunc {

	DBHandler dbHandler = DBHandler.getInstance();
	Scanner scanner = new Scanner(System.in);
	int id_conto;
	int id_sessione;
	double totaleNetto= 0;

	public void dbUser(Titolare t) {
		dbHandler.getConnection();
		boolean isUsernameValid = false;
		while (!isUsernameValid) {
			System.out.println("Inserisci username: ");
			t.setUsername(scanner.nextLine());
			if (t.getUsername().trim().isEmpty()) {
				System.out.println("Hai inserito una stringa vuota. Per favore, prova di nuovo.");
			} else {
				// Esegui la query per verificare se l'username esiste già nel database
				try {
					PreparedStatement checkUsernameStmt = dbHandler.getConnection().prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
					checkUsernameStmt.setString(1, t.getUsername());
					ResultSet resultSet = checkUsernameStmt.executeQuery();
					resultSet.next();
					int count = resultSet.getInt(1);
					if (count > 0) {
						System.out.println("L'username è già presente. Per favore, inserisci un altro username.");
					} else {
						System.out.println("Hai inserito la stringa: '" + t.getUsername());
						isUsernameValid = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		while(true) {
			System.out.println("Inserisci password: ");
			t.setPassword(scanner.nextLine());
			if (t.getPassword().trim().isEmpty()) {
				System.out.println("Hai inserito una stringa vuota. Per favore, prova di nuovo.");
			} else if(t.getPassword().length()>8){
				System.out.println("La password deve essere di 8 caratteri");
			}
			else{
				System.out.println("Hai inserito la stringa: '" + t.getPassword());
				break;
			}
		}

		try {
			PreparedStatement insertCorrentistaStmt = dbHandler.getConnection().prepareStatement("INSERT INTO correntista (nome, cognome, citta,nazione,telefono) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			insertCorrentistaStmt.setString(1, t.getNome());
			insertCorrentistaStmt.setString(2, t.getCognome());
			insertCorrentistaStmt.setString(3, t.getCitta());
			insertCorrentistaStmt.setString(4, t.getNazione());
			insertCorrentistaStmt.setString(5, t.getNumero());
			insertCorrentistaStmt.executeUpdate();
			ResultSet generatedKeys = insertCorrentistaStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				int idCorrentista = generatedKeys.getInt(1);
				t.setId(idCorrentista);
			}
			PreparedStatement st = dbHandler.getConnection().prepareStatement("INSERT INTO users(username,pass,id_correntista) VALUES (?, ?, ?)");
			st.setString(1, t.getUsername());
			st.setString(2, t.getPassword());
			st.setInt(3, t.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbHandler.closeConnection();
	}
	
    public Titolare retrieveTitolare(Conto c) {
    	Titolare t = new Titolare() ;
		DBHandler db = new DBHandler();
		PreparedStatement titolare;
		try {
			titolare = db.getConnection().prepareStatement("select correntista.id_correntista,correntista.nome,correntista.cognome,correntista.citta,correntista.citta,correntista.nazione,correntista.telefono from correntista join conto on correntista.id_correntista = conto.id_correntista where conto.id_conto = ?");
			titolare.setInt(1, c.getIdConto());
			ResultSet rs = titolare.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id_correntista");
				String nome = rs.getString("nome");
				String cognome = rs.getString("cognome");
				String citta = rs.getString("citta");
				String nazione =rs.getString("nazione");
				String numero =rs.getString("telefono");
				t = new Titolare(id,nome,cognome , citta ,  nazione , numero);	
			}
			return t;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
    }


	public Conto recuperaDatiUtente() {
		Scanner scanner = new Scanner(System.in);
		int id_correntista = 0;
		int scelta = 0;
		int contoid = 0;
		String tipoconto = null;
		double saldo = 0;
		int corr = 0;
		double tasso = 0;

		while (true) {
			System.out.print("Inserisci l'username: ");
			String inputUsername = scanner.nextLine();

			System.out.print("Inserisci la password: ");
			String inputPassword = scanner.nextLine();

			String query = "SELECT * FROM users WHERE username = ?";
			try {
				PreparedStatement statement = dbHandler.getConnection().prepareStatement(query);
				statement.setString(1, inputUsername);

				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
					String storedPassword = resultSet.getString("pass");
					id_correntista = resultSet.getInt("id_correntista");
					if (storedPassword.equals(inputPassword)) {
						System.out.println("Accesso consentito");


						PreparedStatement conto  = dbHandler.getConnection().prepareStatement("select conto.id_conto,conto.id_correntista,conto.id_tipo,conto.saldo from conto join correntista on conto.id_correntista = correntista.id_correntista join users on users.id_correntista = conto.id_correntista where conto.id_correntista = ?");
						conto.setInt(1, id_correntista);
						ResultSet conti = conto.executeQuery();
						while(conti.next()) {
						    contoid = conti.getInt("id_conto");
						    corr = conti.getInt("id_correntista");
						    tipoconto = conti.getString("id_tipo");
						    saldo = conti.getInt("saldo");

						    System.out.println(contoid + " " + corr + " " + saldo + " " + tipoconto);

						    if (contoid == scelta) { // se l'ID del conto corrisponde alla scelta dell'utente
						        System.out.println("HAI SELEZIONATO: "+contoid + " " + corr + " " + saldo);
						        break; // interrompi il ciclo
						    }
						}

						System.out.println("Scegli il conto al quale vuoi accedere. \nInserisci il numero di conto correlato:");

						scelta = scanner.nextInt();                      
						PreparedStatement scegliConto = dbHandler.getConnection().prepareStatement("select conto.id_conto,conto.id_tipo from conto join correntista on conto.id_correntista = correntista.id_correntista where conto.id_conto = ? and conto.id_correntista = ?");
						scegliConto.setInt(1, scelta);
						scegliConto.setInt(2, corr);
						ResultSet rsscelta = scegliConto.executeQuery();
						if(rsscelta.next()) {
							contoid = rsscelta.getInt("id_conto");
							tipoconto = rsscelta.getString("id_tipo");							
						}

						Conto conto2;
						switch (tipoconto) {
						case "CC":
							conto2 = new ContoCorrente();
							break;
						case "CD":
							conto2 = new ContoDeposito();
							break;
						case "CI":
							conto2 = new ContoInvestimento();
							break;
						default:
							throw new RuntimeException("Tipo conto sconosciuto: " + tipoconto);
						}
						
			            PreparedStatement stmt = dbHandler.getConnection().prepareStatement("SELECT saldo FROM conto WHERE id_tipo = ? and id_conto = ?");			            
			            stmt.setString(1, tipoconto);
			            stmt.setInt(2, contoid);
			            ResultSet rs = stmt.executeQuery();
			            if (rs.next()) {
			                saldo = rs.getDouble("saldo");
			            }
			            
			            PreparedStatement stm = dbHandler.getConnection().prepareStatement("SELECT tasso FROM tipo_conto join conto on tipo_conto.id_tipo_conto = conto.id_tipo WHERE conto.id_tipo = ? and id_conto = ?");
			            stm.setString(1, tipoconto);
			            stm.setInt(2, contoid);
			            ResultSet rs1 = stm.executeQuery();
			            if (rs1.next()) {
			                tasso = rs1.getDouble("tasso");
			                System.out.println("Tasso recuperato : " + tasso);
			            }
			            
			            conto2.setDataUltimoMovimento(LocalDate.now());
						conto2.setIdConto(contoid);
						conto2.setTitolare(retrieveTitolare(conto2));
						conto2.setSaldo(saldo);
						conto2.setTasso(tasso);
						return conto2;
					}
				} else {
					System.out.println("Password errata. Reinserisci username e password.");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}




public int DBrecuperaIDConto (Conto c, Titolare t) {
	PreparedStatement recuperoID;
	try {
		recuperoID = dbHandler.getConnection().prepareStatement("SELECT id_conto from conto where id_correntista = ?");
		recuperoID.setInt(1, t.getId());
		ResultSet rs = recuperoID.executeQuery();
		rs.next();
		id_conto = rs.getInt(1);
		System.out.println("L'ID del conto è: " + id_conto);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return id_conto;
}


public double DBPreleva (Conto c, Titolare t) {
	double saldo = 0;
	try {
		id_conto = DBrecuperaIDConto(c, t);

		PreparedStatement setSaldo = dbHandler.getConnection().prepareStatement("INSERT into movimenti (saldo_precedente,id_tipo_movimento,id_conto) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
		setSaldo.setDouble(1, c.getSaldo()); //ALLA PRIMA ITERATA DARA' 1000
		setSaldo.setString(2, "PRE");
		setSaldo.setInt(3, this.id_conto);
		setSaldo.executeUpdate();

		ResultSet generatedKeys = setSaldo.getGeneratedKeys();
		if (generatedKeys.next()) {
			int idSessione = generatedKeys.getInt(1);
			this.id_sessione = idSessione;
		}


	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	dbHandler.closeConnection();
	return saldo;
}

public double DBVersa (Conto c, Titolare t) {
	double saldo = 0;
	try {
		id_conto = DBrecuperaIDConto(c, t);

		PreparedStatement setSaldo = dbHandler.getConnection().prepareStatement("INSERT into movimenti (saldo_precedente,id_tipo_movimento,id_conto) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
		setSaldo.setDouble(1, c.getSaldo());
		setSaldo.setString(2, "VER");
		setSaldo.setInt(3, this.id_conto);
		setSaldo.executeUpdate();

		ResultSet generatedKeys = setSaldo.getGeneratedKeys();
		if (generatedKeys.next()) {
			int idSessione = generatedKeys.getInt(1);
			this.id_sessione = idSessione;
		}


	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	dbHandler.closeConnection();
	return saldo;
}

public void DBAggiorna (Conto c, Titolare t) {
	LocalDate date = LocalDate.now();
	int id_conto = 0;

	try {
		id_conto = DBrecuperaIDConto(c, t);

		PreparedStatement setDataMove = dbHandler.getConnection().prepareStatement("UPDATE movimenti set data_del_movimento = ? where id_sessione = ?");
		setDataMove.setDate(1, java.sql.Date.valueOf(date));
		setDataMove.setInt(2, this.id_sessione);
		setDataMove.executeUpdate();

		PreparedStatement setSaldoSuccessiva = dbHandler.getConnection().prepareStatement("UPDATE movimenti set saldo_successivo =? where id_sessione = ?");
		setSaldoSuccessiva.setDouble(1, c.getSaldo());
		setSaldoSuccessiva.setInt(2, this.id_sessione);
		setSaldoSuccessiva.executeUpdate();

		PreparedStatement aggiornaSaldo = dbHandler.getConnection().prepareStatement("UPDATE conto set saldo = ? where id_conto = ?");
		aggiornaSaldo.setDouble(1, c.getSaldo());
		aggiornaSaldo.setInt(2, id_conto);
		aggiornaSaldo.executeUpdate();	

		PreparedStatement aggiornaImporto = dbHandler.getConnection().prepareStatement("Update movimenti set importo = ? where id_sessione = ?");
		aggiornaImporto.setString(1, c.getImporto());
		aggiornaImporto.setInt(2, this.id_sessione);
		aggiornaImporto.executeUpdate();

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}



public void caricaConto (Conto c, Titolare t) {
	LocalDate date = LocalDate.now();
	try {
		PreparedStatement insertCorrentistaStmt = dbHandler.getConnection().prepareStatement("INSERT INTO conto (id_correntista,id_tipo,data_apertura,saldo) VALUES (?,?,?,?)");
		insertCorrentistaStmt.setInt(1, t.getId());
		insertCorrentistaStmt.setString(2, c.getIdTipo());
		insertCorrentistaStmt.setDate(3, java.sql.Date.valueOf(date));
		insertCorrentistaStmt.setDouble(4, c.getSaldo());
		insertCorrentistaStmt.executeUpdate();
	}catch(Exception e) {
		e.printStackTrace();
	}

	dbHandler.closeConnection();
}

public void chiudiConto(Conto c, Titolare t) {
	String query = "DELETE FROM conto WHERE id_conto = ?";
	String query2 = "DELETE FROM movimenti WHERE id_conto = ?";

	System.out.println("Data chiusura: |" + LocalDate.now() + " | TOTALE LORDO " + c.getTotale() + " | " + "TOTALE NETTO " + (this.totaleNetto * 0.26));

	try {
		//CANCELLA MOVIMENTI
		PreparedStatement deleteMove = dbHandler.getConnection().prepareStatement(query2);
		deleteMove.setInt(1, c.getIdConto());
		deleteMove.executeUpdate();

		//CANCELLA CONTO
		PreparedStatement deleteConto = dbHandler.getConnection().prepareStatement(query);
		deleteConto.setInt(1, c.getIdConto()); // Assumendo che il metodo per ottenere l'id_conto sia getIdConto()

		int righeCancellate = deleteConto.executeUpdate();
		if (righeCancellate > 0) {
			System.out.println("Conto cancellato con successo.");
		} else {
			System.out.println("Nessuna riga cancellata.");
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}

public void memorizzazioneInCSV(Conto c, Titolare t) {
	String csvFilePath = ".\\movimenti" + t.getNome() + t.getCognome() + ".csv";

	try {

		Statement statement = dbHandler.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM movimenti WHERE id_conto ='"+ this.id_conto+"'");
		CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath)); 
		// Recupera i metadati delle colonne
		int numColumns = resultSet.getMetaData().getColumnCount();
		String[] header = new String[numColumns];
		for (int i = 0; i < numColumns; i++) {
			header[i] = resultSet.getMetaData().getColumnLabel(i + 1);
		}
		csvWriter.writeNext(header);

		// Scrivi i dati delle righe nel file CSV
		while (resultSet.next()) {
			String[] row = new String[numColumns];
			for (int i = 0; i < numColumns; i++) {
				row[i] = resultSet.getString(i + 1);
			}
			csvWriter.writeNext(row);
		}
		String[] interessi = {"Interessi accumulati " + c.getTotale() , " al netto: " +  this.totaleNetto};
		csvWriter.writeNext(interessi);
		System.out.println("Dati della tabella inseriti nel file CSV con successo.");
		csvWriter.close();

	} catch (IOException e) {
		e.printStackTrace();
	} catch (Exception e) {
		e.printStackTrace();
	}
}


}
