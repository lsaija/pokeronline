package it.prova.pokeronline.repository.tavolo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>,CustomTavoloRepository{
	@Query("from Tavolo t join fetch t.giocatori where t.id = ?1")
	Tavolo findSingleTavoloEager(Long id);
	
	List<Tavolo> findByDenominazione(String denominazione);
	
	@Query("select t from Tavolo t join fetch t.giocatori")
	List<Tavolo> findAllTavoloEager();
	
	
	@Query("select t from Tavolo t left join fetch t.utenteCreazione u where u.id=?1")
	List<Tavolo> findAllTavoloByUtente(Long id);

}
