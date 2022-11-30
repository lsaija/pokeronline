
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
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.GiocatoreGiaPresenteTavoloException;

@RestController
@RequestMapping("/api/play")
public class PlayController {
	@Autowired
	private TavoloService tavoloService;

	@Autowired
	private UtenteService utenteService;

	@GetMapping("/compraCredito/{credito}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public String compraCredito(@PathVariable(value = "credito", required = true) Integer credito) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Utente utente = utenteService.findByUsername(username);

		Integer creditoAggiornato = utente.getCreditoAccumulato() + credito;

		utente.setCreditoAccumulato(creditoAggiornato);
		utenteService.aggiorna(utente);

		return "Il tuo nuovo credito e' di " + creditoAggiornato;
	}

	@GetMapping("/lastGame")
	public TavoloDTO dammiLastGame() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Utente utenteLoggato = utenteService.findByUsername(username);

		return TavoloDTO.buildTavoloDTOFromModel(tavoloService.ultimoGame(utenteLoggato.getId()),true);
	}

	@GetMapping("/abbandonaPartita")
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
				.createTavoloDTOListFromModelList(tavoloService.listEsperienzaMin(utente.getEsperienzaAccumulata()),true);

		return tavoliTrovati;
	}
	
	@GetMapping("/giocaTavolo/{id}")
	public UtenteDTO gioca(@PathVariable(value = "id", required = true) Long idTavolo) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Utente utente = utenteService.findByUsername(username);
		//Tavolo result = tavoloService.ultimoGame(utente.getId());
		
//		if(result != null) {
//			throw new GiocatoreGiaPresenteTavoloException("Giocatore presente in un altro tavolo!");
//		}
		tavoloService.entraPartita(idTavolo);
		double segnoDouble = Math.random();
		String segno = "";
		if(segnoDouble > 0.5)
			segno = "positivo";
		else
			segno = "negativo";
		
		double num = Math.random();
        int somma = (int)(num*1000+1);
		
		int totDaAggiungereOSottrarre = (int) (segnoDouble*somma);
		Integer creditoFinale = 0;
		if(segno.equals("positivo")){
			creditoFinale = utente.getCreditoAccumulato() + totDaAggiungereOSottrarre;
		}
		if(segno.equals("negativo")){
			creditoFinale = utente.getCreditoAccumulato() - totDaAggiungereOSottrarre;
		}
		
		utente.setCreditoAccumulato(creditoFinale);
		utenteService.aggiorna(utente);
		return UtenteDTO.buildUtenteDTOFromModel(tavoloService.abbandonaPartita(idTavolo));
	}

}