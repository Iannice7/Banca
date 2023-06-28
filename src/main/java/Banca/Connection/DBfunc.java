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
				//SQL insert
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

	public void recuperaDatiUtente() {
		Scanner scanner = new Scanner(System.in);

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
					if (storedPassword.equals(inputPassword)) {
						System.out.println("Accesso consentito");
						break; // Esci dal ciclo while se l'autenticazione è riuscita
					} else {
						System.out.println("Password errata. Reinserisci username e password.");
					}
				} else {
					System.out.println("Username non trovato. Reinserisci username e password.");
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

		System.out.println("Data chiusura: |" + LocalDate.now() + " | TOTALE LORDO" + c.getTotale() + " | " + "TOTALE NETTO" + (this.totaleNetto * 0.26));
		
		try {
			//CANCELLA MOVIMENTI
			PreparedStatement deleteMove = dbHandler.getConnection().prepareStatement(query2);
			deleteMove.setInt(1, this.id_conto);
			deleteMove.executeUpdate();

			//CANCELLA CONTO
			PreparedStatement deleteConto = dbHandler.getConnection().prepareStatement(query);
			deleteConto.setInt(1, this.id_conto); // Assumendo che il metodo per ottenere l'id_conto sia getIdConto()

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
		String csvFilePath = "C:\\Workspace\\Banca\\movimenti" + t.getNome() + t.getCognome() + ".csv";

		try {

			Statement statement = dbHandler.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM movimenti WHERE id_conto ='"+this.id_conto+"'");
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
			String[] interessi = {" Interessi accumulati " + c.getTotale() , " al netto: " +  this.totaleNetto};
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
