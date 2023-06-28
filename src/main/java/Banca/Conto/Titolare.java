package Banca.Conto;
import java.sql.Date;
import java.time.LocalDate;

public class Titolare {
	protected int id;
	private String username;
	private String password;
	public String nome;
	public String cognome;
	public String citta;
	public Date dataApertura; //DA FAR DIVENTARE DATA ISCRIZIONE
	public String nazione;
	public String numero;
	

	
	
	public Titolare(int id, String nome, String cognome, String citta, String nazione, String numero) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.citta = citta;
		//this.dataApertura = dataApertura2;
		this.nazione = nazione;
		this.numero = numero;
	}
	
	public Titolare() {}


	public String getNazione() {return nazione;}
	public void setNazione(String nazione) {this.nazione = nazione;}
	public String getNumero() {return numero;}
	public void setNumero(String numero) {this.numero = numero;}
	public String getNome() {return nome;}
	public String getCitta() {return citta;}
	public void setCitta(String citta) {this.citta = citta;}
	public Date getDataApertura() {return dataApertura;}
	public void setDataApertura(Date dataApertura) {this.dataApertura = dataApertura;}
	public void setNome(String nome) {this.nome = nome;}
	public String getCognome() {return cognome;}
	public void setCognome(String cognome) {this.cognome = cognome;}
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}

	@Override
	public String toString() {
		return "Titolare [nome=" + nome + ", cognome=" + cognome + ", citta=" + citta + ", dataApertura=" + dataApertura
				+ ", nazione=" + nazione + ", numero=" + numero + "]";
	}

}
