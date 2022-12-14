package it.prova.pokeronline.service;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

public interface TavoloService {
	List<Tavolo> listAllElements(boolean eager);
	
	List<Tavolo> listAllElementsSingoloUtente();

	Tavolo caricaSingoloElemento(Long id);

	Tavolo caricaSingoloElementoEager(Long id);

	Tavolo aggiorna(Tavolo tavoloInstance);

	Tavolo inserisciNuovo(Tavolo tavoloInstance);

	void rimuovi(Long idToRemove);

	List<Tavolo> findByDenominazione(String denominazione);
	
	List<Tavolo> findByExample(Tavolo Example,String username);
	
	Tavolo ultimoGame(Long id);
	
	Utente abbandonaPartita(Long idTavolo);
	
	List<Tavolo> listEsperienzaMin(Integer min);
	
	void entraPartita(Long idTavolo);
	

}
