package it.prova.pokeronline.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloDTO {
	private Long id;

	@NotBlank(message = "{denominazione.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String denominazione;

	@NotNull(message = "{esperienzaMinima.notnull}")
	private Integer esperienzaMinima;

	@NotNull(message = "{cifraMinima.notnull}")
	private Integer cifraMinima;

	@NotNull(message = "{dateCreated.notnull}")
	private LocalDateTime dateCreated;

	@JsonIgnoreProperties(value = { "tavolo" })
	private Set<UtenteDTO> giocatori = new HashSet<>();
	
	@JsonIgnoreProperties(value = { "tavolo" })
	@NotNull(message = "{utenteCreazione.notnull}")
	private UtenteDTO utenteCreazione;

	public TavoloDTO() {
		// TODO Auto-generated constructor stub
	}

	
	public TavoloDTO(String denominazione,Integer esperienzaMinima,Integer cifraMinima,LocalDateTime dateCreated,UtenteDTO utenteCreazione) {
		super();
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.dateCreated = dateCreated;
		this.utenteCreazione = utenteCreazione;
	}
	

	public TavoloDTO(Long id,String denominazione,Integer esperienzaMinima,Integer cifraMinima,LocalDateTime dateCreated,UtenteDTO utenteCreazione) {
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



	public Set<UtenteDTO> getGiocatori() {
		return giocatori;
	}


	public void setGiocatori(Set<UtenteDTO> giocatori) {
		this.giocatori = giocatori;
	}


	public UtenteDTO getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(UtenteDTO utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}
	
	
	public Tavolo buildTavoloModel(boolean includeIdGiocatori) {
		Tavolo result = new Tavolo(id, denominazione, esperienzaMinima, cifraMinima, dateCreated, utenteCreazione.buildUtenteModel(false));

		if(this.giocatori.size() > 1) {
			Set<Utente> set = result.getGiocatori();
			this.giocatori.forEach(utente -> set.add(utente.buildUtenteModel(false)));
		}
		return result;
	}

	// niente password...
	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel,boolean includeGiocatori) {
		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getDenominazione(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDateCreated(),UtenteDTO.buildUtenteDTOFromModel(tavoloModel.getUtenteCreazione()));

		if (tavoloModel.getGiocatori()!=null&&tavoloModel.getGiocatori().size()>0) {
			System.out.println("sono entrato nell' if demmerda");
			result.setGiocatori(UtenteDTO.createUtenteDTOSetFromModelSet(tavoloModel.getGiocatori()));
		}
		return result;
	}
	
	public static List<TavoloDTO> createTavoloDTOListFromModelList(List<Tavolo> modelListInput, boolean includeGiocatori) {
        return modelListInput.stream().map(tavoloEntity -> {
			return TavoloDTO.buildTavoloDTOFromModel(tavoloEntity, includeGiocatori);
		}).collect(Collectors.toList());
	}

	public static Set<TavoloDTO> createTavoloDTOSetFromModelSet(Set<Tavolo> modelListInput,boolean includeGiocatori) {
		return modelListInput.stream().map(tavoloEntity -> {
			return TavoloDTO.buildTavoloDTOFromModel(tavoloEntity, includeGiocatori);
		}).collect(Collectors.toSet());
	}
	
	
	
	
}
