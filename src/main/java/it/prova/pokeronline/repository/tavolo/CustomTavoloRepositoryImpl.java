package it.prova.pokeronline.repository.tavolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.prova.pokeronline.model.Tavolo;


public class CustomTavoloRepositoryImpl implements CustomTavoloRepository{
	@PersistenceContext
	private EntityManager entityManager;
	

	
	//Implementare!!!!!!!!!!!!!!!!!!
	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		return null;
		/*Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select a from Agenda a where a.id = a.id ");

		if (StringUtils.isNotEmpty(example.getDescrizione())) {
			whereClauses.add(" a.descrizione  like :descrizione ");
			paramaterMap.put("descrizione", "%" + example.getDescrizione() + "%");
		}
		
		if (example.getDataOraInizio() != null) {
			whereClauses.add(" a.dataOraInizio >= :dataOraInizio ");
			paramaterMap.put("dataOraInizio", "%" + example.getDataOraInizio() + "%");
		}
		if (example.getDataOraFine() !=null) {
			whereClauses.add(" a.dataOraFine >=:dataOraFine ");
			paramaterMap.put("dataOraFine", "%" + example.getDataOraFine()+ "%");
		}
	
		if (example.getUtente() != null) {
			whereClauses.add("a.utente = :utente ");
			paramaterMap.put("utente", example.getUtente());
		}
		
		queryBuilder.append(!whereClauses.isEmpty()?" and ":"");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Agenda> typedQuery = entityManager.createQuery(queryBuilder.toString(), Agenda.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();*/
	}

}
