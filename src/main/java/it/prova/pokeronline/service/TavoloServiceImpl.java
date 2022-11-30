package it.prova.pokeronline.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.web.api.exception.AncoraGiocatoriAlTavoloException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteNonCombaciaException;

@Service
@Transactional(readOnly = true)
public class TavoloServiceImpl implements TavoloService {
	@Autowired
	private TavoloRepository repository;
	@Autowired
	private UtenteService utenteService;

	public List<Tavolo> listAllElements(boolean eager) {
		if (eager)
			return (List<Tavolo>) repository.findAllTavoloEager();

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

	// Implementare!!!!!!!!!!
	@Override
	public List<Tavolo> findByExample(Tavolo Example) {
		return repository.findByExample(Example);
	}

}
