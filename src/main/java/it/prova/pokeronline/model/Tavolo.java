package it.prova.pokeronline.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name = "tavolo")
public class Tavolo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "denominazione")
	private String denominazione;
	@Column(name="esperienzaMinima")
	private Integer esperienzaMinima;
	@Column(name="cifraMinima")
	private Integer cifraMinima;
	@Column(name = "dateCreated")
	private LocalDateTime dateCreated;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tavoloGioco")
	private Set<Utente> giocatori = new HashSet<Utente>(0);
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_id", nullable = false)
	private Utente utenteCreazione;
	
	
	public Tavolo() {
		// TODO Auto-generated constructor stub
	}

	public Tavolo(Long id, String denominazione, Integer esperienzaMinima, Integer cifraMinima,
			LocalDateTime dateCreated, Set<Utente> giocatori, Utente utenteCreazione) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dateCreated = dateCreated;
		this.giocatori = giocatori;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(String denominazione, Integer esperienzaMinima, Integer cifraMinima, LocalDateTime dateCreated,
			Utente utenteCreazione) {
		super();
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dateCreated = dateCreated;
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo(String denominazione, Utente utenteCreazione) {
		super();
		this.denominazione = denominazione;
		this.utenteCreazione = utenteCreazione;
	}
	

	public Tavolo(Long id) {
		super();
		this.id = id;
	}

	public Tavolo(Long id, String denominazione, Integer esperienzaMinima, Integer cifraMinima,
			LocalDateTime dateCreated, Utente utenteCreazione) {
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dateCreated = dateCreated;
		this.utenteCreazione = utenteCreazione;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public Integer getEsperienzaMinima() {
		return esperienzaMinima;
	}

	public void setEsperienzaMinima(Integer esperienzaMinima) {
		this.esperienzaMinima = esperienzaMinima;
	}

	public Integer getCifraMinima() {
		return cifraMinima;
	}

	public void setCifraMinima(Integer cifraMinima) {
		this.cifraMinima = cifraMinima;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Set<Utente> getGiocatori() {
		return giocatori;
	}

	public void setGiocatori(Set<Utente> giocatori) {
		this.giocatori = giocatori;
	}

	public Utente getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(Utente utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
	
	
	
}
