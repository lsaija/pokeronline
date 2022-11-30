
package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

@RestController
@RequestMapping("/api/play")
public class PlayController {
	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	@PostMapping("compraCredito/{credito}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String compraCredito(@PathVariable(value = "credito", required = true) Integer credito) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Utente utente = utenteService.findByUsername(username);

		Integer creditoAggiornatp = utente.getCreditoAccumulato() + credito;

		utente.setCreditoAccumulato(creditoAggiornatp);
		utenteService.aggiorna(utente);

		return "Il tuo nuovo credito e' di " + creditoAggiornatp;
	}

	@GetMapping("/lastGame")
	public TavoloDTO dammiLastGame() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Utente utenteLoggato = utenteService.findByUsername(username);

		return TavoloDTO.buildTavoloDTOFromModel(tavoloService.ultimoGame(utenteLoggato.getId()));
	}

	@PostMapping("abbandonaPartita")
	public UtenteDTO abbandonaPartita() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Utente utente = utenteService.findByUsername(username);

		Long idTavolo = tavoloService.ultimoGame(utente.getId()).getId();

		return UtenteDTO.buildUtenteDTOFromModel(tavoloService.abbandonaPartita(idTavolo));
	}

	@GetMapping
	public List<TavoloDTO> ricercaTavoli() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Utente utente = utenteService.findByUsername(username);

		List<TavoloDTO> tavoliTrovati = TavoloDTO
				.createTavoloDTOListFromModelList(tavoloService.listEsperienzaMin(utente.getEsperienzaAccumulata()));

		return tavoliTrovati;
	}

}