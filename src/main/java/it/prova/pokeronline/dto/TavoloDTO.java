package it.prova.pokeronline.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

public class TavoloDTO {
	private Long id;

	@NotBlank(message = "{denominazione.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String denominazione;

	@NotBlank(message = "{esperienzaMinima.notblank}")
	private Integer esperienzaMinima;

	@NotNull(message = "{cifraMinima.notnull}")
	private Integer cifraMinima;

	@NotNull(message = "{dateCreated.notnull}")
	private LocalDateTime dateCreated;

	private Long[] giocatoriIds;
	
	@JsonIgnoreProperties(value = { "tavoliCreati","tavoloGioco" })
	@NotNull(message = "{utenteCreazione.notnull}")
	private Utente utenteCreazione;

	public TavoloDTO() {
		// TODO Auto-generated constructor stub
	}

	public TavoloDTO(Long id,String denominazione, Integer esperienzaMinima,Integer cifraMinima,LocalDateTime dateCreated, Long[] giocatoriIds,Utente utenteCreazione) {
		super();
		this.id = id;
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dateCreated = dateCreated;
		this.giocatoriIds = giocatoriIds;
		this.utenteCreazione = utenteCreazione;
	}

	public TavoloDTO(String denominazione,Integer esperienzaMinima,Integer cifraMinima,LocalDateTime dateCreated,Utente utenteCreazione) {
		super();
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dateCreated = dateCreated;
		this.utenteCreazione = utenteCreazione;
	}
	

	public TavoloDTO(Long id,String denominazione,Integer esperienzaMinima,Integer cifraMinima,LocalDateTime dateCreated,Utente utenteCreazione) {
		super();
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

	public Long[] getGiocatoriIds() {
		return giocatoriIds;
	}

	public void setGiocatoriIds(Long[] giocatoriIds) {
		this.giocatoriIds = giocatoriIds;
	}

	public Utente getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(Utente utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
	
	public Tavolo buildTavoloModel(boolean includeIdGiocatori) {
		Tavolo result = new Tavolo(id, denominazione, esperienzaMinima, cifraMinima, dateCreated, utenteCreazione);
		if (includeIdGiocatori && giocatoriIds != null)
			result.setGiocatori(Arrays.asList(giocatoriIds).stream().map(id -> new Utente(id)).collect(Collectors.toSet()));
		return result;
	}

	// niente password...
	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel) {
		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getDenominazione(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDateCreated(),tavoloModel.getUtenteCreazione());
		if (!tavoloModel.getGiocatori().isEmpty())
			result.giocatoriIds = tavoloModel.getGiocatori().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}
	
	
	
	
}
