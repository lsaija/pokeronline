package it.prova.pokeronline.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloDTOForInsert {
	private Long id;
	
	@NotBlank(message = "{denominazione.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String denominazione;

	@NotNull(message = "{esperienzaMinima.notnull}")
	private Integer esperienzaMinima;

	@NotNull(message = "{cifraMinima.notnull}")
	private Integer cifraMinima;
	
	private UtenteDTO utenteCreazione;
	
	public TavoloDTOForInsert(String denominazione,Integer esperienzaMinima,Integer cifraMinima) {
		super();
		this.denominazione = denominazione;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
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
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UtenteDTO getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(UtenteDTO utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public Tavolo buildTavoloInsertModel() {
		Tavolo result = new Tavolo( denominazione, esperienzaMinima, cifraMinima);
		return result;
	}

	// niente password...
	public static TavoloDTOForInsert buildTavoloDTOForInsertFromModel(Tavolo tavoloModel) {
		TavoloDTOForInsert result = new TavoloDTOForInsert(tavoloModel.getDenominazione(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima());
		return result;
	}
	
	

}
