package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.TavoloDTOForInsert;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.AncoraGiocatoriAlTavoloException;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNonCombaciaException;

@RestController
@RequestMapping("api/tavolo")
public class TavoloController {
	@Autowired
	private TavoloService tavoloService;
	@Autowired
	private UtenteService utenteService;

	@GetMapping(value = "/tavoliInfo")
	public List<TavoloDTO> getAll() {
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloService.listAllElements(true),true);
	}
	
	@GetMapping
	public List<TavoloDTO> getAllUtenteSingolo() {
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloService.listAllElementsSingoloUtente(),true);
	}

	@PostMapping
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTOForInsert tavoloInput) {
	
		if (tavoloInput.getId() != null)
			throw new IdNotNullForInsertException("Non ?? ammesso fornire un id per la creazione");
		if (tavoloInput.getUtenteCreazione() != null)
			throw new UtenteNonCombaciaException("Non ?? ammesso fornire un utente per la creazione");
		
		Tavolo tavoloInserito = tavoloService.inserisciNuovo(tavoloInput.buildTavoloInsertModel());
		return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito,true);
	}

	@GetMapping("/{id}")
	public TavoloDTO findById(@PathVariable(value = "id", required = true) long id) {
		Tavolo tavolo = tavoloService.caricaSingoloElementoEager(id);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();


		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
		//aggiungere condizione utente

		return TavoloDTO.buildTavoloDTOFromModel(tavolo,true);
	}
	
	@PutMapping("/{id}")
	public TavoloDTO update(@Valid @RequestBody TavoloDTOForInsert tavoloInput, @PathVariable(required = true) Long id) {
		Tavolo tavolo = tavoloService.caricaSingoloElemento(id);
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
		if (tavoloInput.getUtenteCreazione() != null)
			throw new UtenteNonCombaciaException("Non ?? ammesso fornire un utente per l'aggiornamento");

		if(tavolo.getGiocatori().size()>0)
			throw new AncoraGiocatoriAlTavoloException("Ci sono ancora giocatori al tavolo!");
			
		tavoloInput.setId(id);
		Tavolo tavoloAggiornato = tavoloService.aggiorna(tavoloInput.buildTavoloInsertModel());
		return TavoloDTO.buildTavoloDTOFromModel(tavoloAggiornato,true);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {
		if (tavoloService.caricaSingoloElemento(id) == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
		
		tavoloService.rimuovi(id);
	}

	
	@PostMapping("/search")
	public List<TavoloDTO> search(@RequestBody TavoloDTO example) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return TavoloDTO.createTavoloDTOListFromModelList(tavoloService.findByExample(example.buildTavoloModel(true),username),true);
	}


}
