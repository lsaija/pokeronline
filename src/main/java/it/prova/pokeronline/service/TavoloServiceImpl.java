package it.prova.pokeronline.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.repository.utente.UtenteRepository;
import it.prova.pokeronline.web.api.exception.AncoraGiocatoriAlTavoloException;
import it.prova.pokeronline.web.api.exception.NonInGiocoException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNonCombaciaException;
import it.prova.pokeronline.web.api.exception.UtenteNotFoundException;

@Service
@Transactional
public class TavoloServiceImpl implements TavoloService {
	@Autowired
	private TavoloRepository repository;
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private UtenteService utenteService;

	public List<Tavolo> listAllElements(boolean eager) {
		List<Tavolo> result;
		if (eager) {
			result = (List<Tavolo>) repository.findAllTavoloEager();
			System.out.println(result.get(0).getGiocatori().isEmpty());
			return result;
		}

		return (List<Tavolo>) repository.findAll();
	}

	public List<Tavolo> listAllElementsSingoloUtente() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UtenteDTO utenteItem = UtenteDTO.buildUtenteDTOFromModel(utenteService.findByUsername(username));
		return repository.findAllTavoloByUtente(utenteItem.getId());
	}

	public Tavolo caricaSingoloElemento(Long id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (utenteService.findByUsername(username).isAdmin() || utenteService.findByUsername(username)
				.getId() == repository.findById(id).get().getUtenteCreazione().getId()) {
			return repository.findById(id).orElse(null);
		} else {
			throw new UtenteNonCombaciaException("Utente non ammesso!");
		}
	}

	public Tavolo caricaSingoloElementoEager(Long id) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		if (utenteService.findByUsername(username).isAdmin() || utenteService.findByUsername(username)
				.getId() == repository.findById(id).get().getUtenteCreazione().getId()) {
			return repository.findSingleTavoloEager(id);
		} else {
			throw new UtenteNonCombaciaException("Utente non ammesso!");
		}
	}

	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		if (!tavoloInstance.getGiocatori().isEmpty())
			throw new AncoraGiocatoriAlTavoloException("Ci sono ancora giocatori al tavolo!");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UtenteDTO utenteItem = UtenteDTO.buildUtenteDTOFromModel(utenteService.findByUsername(username));
		tavoloInstance.setUtenteCreazione(utenteService.findByUsername(username));
		if (tavoloInstance.getUtenteCreazione().getId() != utenteItem.getId()
				|| !utenteService.findByUsername(username).isAdmin())
			throw new UtenteNonCombaciaException("Utente non ammesso!");
		return repository.save(tavoloInstance);
	}

	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		tavoloInstance.setUtenteCreazione(utenteService.findByUsername(username));
		if (!tavoloInstance.getUtenteCreazione().equals(utenteService.findByUsername(username)))
			throw new UtenteNonCombaciaException("Utente non ammesso!");
		tavoloInstance.setDateCreated(LocalDateTime.now());

		return repository.save(tavoloInstance);
	}

	@Transactional
	public void rimuovi(Long idToRemove) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		repository.findById(idToRemove)
				.orElseThrow(() -> new TavoloNotFoundException("Tavolo not found con id: " + idToRemove));
		if (!repository.findSingleTavoloEager(idToRemove).getGiocatori().isEmpty())
			throw new AncoraGiocatoriAlTavoloException("Ci sono ancora giocatori al tavolo!");

		if (repository.findSingleTavoloEager(idToRemove).getUtenteCreazione().getId() != utenteService
				.findByUsername(username).getId() || !utenteService.findByUsername(username).isAdmin())
			throw new UtenteNonCombaciaException("Utente non combacia!");
		repository.deleteById(idToRemove);
	}

	public List<Tavolo> findByDenominazione(String denominazione) {
		return repository.findByDenominazione(denominazione);
	}

	@Override
	public List<Tavolo> findByExample(Tavolo example, String username) {
		Utente utenteInSessione = utenteRepository.findByUsername(username).orElse(null);
		if (utenteInSessione == null)
			throw new UtenteNotFoundException("Utente non trovato.");
		
		if(utenteInSessione.isAdmin())
			return repository.findByExample(example);
		
		if(utenteInSessione.isSpecial())
			example.setUtenteCreazione(utenteInSessione);
			return repository.findByExampleEager(example);
		
	}
	
	@Override
	@Transactional
	public Tavolo ultimoGame(Long id) {
		Tavolo result = repository.findByGiocatoriId(id);

		if (result == null)
			throw new NonInGiocoException("Non si è in nessun tavolo");

		return result;
	}
	
	@Transactional
	public Utente abbandonaPartita(Long idTavolo) {
		Tavolo tavoloInstance = repository.findById(idTavolo).orElse(null);
		if (tavoloInstance == null)
			throw new TavoloNotFoundException("Tavolo con id: " + idTavolo + " not Found");

		Utente utente = utenteRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		
		tavoloInstance.getGiocatori().remove(utente);

		utente.setEsperienzaAccumulata(utente.getEsperienzaAccumulata() + 1);
		repository.save(tavoloInstance);
		utenteRepository.save(utente);

		return utente;
	}
	
	@Transactional
	public List<Tavolo> listEsperienzaMin(Integer min) {
		return (List<Tavolo>) repository.findAllByEsperienzaMin(min);
	}
	
	@Transactional
	public void entraPartita(Long idTavolo) {
		Tavolo tavolo = repository.findById(idTavolo).orElse(null);
		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo con id: " + idTavolo + " not Found");
		Utente utente = utenteRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		tavolo.getGiocatori().add(utente);
		repository.save(tavolo);
	}

}
