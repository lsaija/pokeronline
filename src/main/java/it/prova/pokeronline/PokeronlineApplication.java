package it.prova.pokeronline;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.tavolo.TavoloRepository;
import it.prova.pokeronline.service.RuoloService;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

@SpringBootApplication
public class PokeronlineApplication implements CommandLineRunner {

	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private TavoloRepository tavoloRepository;
	@Autowired
	private TavoloService tavoloServiceInstance;
	public static void main(String[] args) {
		SpringApplication.run(PokeronlineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", Ruolo.ROLE_ADMIN));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Classic Player", Ruolo.ROLE_CLASSIC_PLAYER) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Classic Player", Ruolo.ROLE_CLASSIC_PLAYER));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Special Player", Ruolo.ROLE_SPECIAL_PLAYER) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Special Player", Ruolo.ROLE_SPECIAL_PLAYER));
		}
		LocalDateTime oraInizio = LocalDateTime.now();
		LocalDateTime oraFine = LocalDateTime.of(2025, Month.JUNE, 29, 19, 30, 40);
		// a differenza degli altri progetti cerco solo per username perche' se vado
		// anche per password ogni volta ne inserisce uno nuovo, inoltre l'encode della
		// password non lo
		// faccio qui perche gia lo fa il service di utente, durante inserisciNuovo
		if (utenteServiceInstance.findByUsername("admin") == null) {
			Utente admin = new Utente("admin", "admin", "Mario", "Rossi", new Date());
			admin.setEmail("a.admin@prova.it");
			admin.setCreditoAccumulato(0);
			admin.setEsperienzaAccumulata(0);
			admin.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN));
			utenteServiceInstance.inserisciNuovo(admin);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(admin.getId());
		}

		if (utenteServiceInstance.findByUsername("user") == null) {
			Utente classicUser = new Utente("user", "user", "Antonio", "Verdi", new Date());
			classicUser.setEmail("u.user@prova.it");
			classicUser.setCreditoAccumulato(0);
			classicUser.setEsperienzaAccumulata(6);
			classicUser.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic Player", Ruolo.ROLE_CLASSIC_PLAYER));
			utenteServiceInstance.inserisciNuovo(classicUser);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser.getId());

		}

		if (utenteServiceInstance.findByUsername("user1") == null) {
			Utente classicUser1 = new Utente("user1", "user1", "Antonioo", "Verdii", new Date());
			classicUser1.setEmail("u.user1@prova.it");
			classicUser1.setCreditoAccumulato(0);
			classicUser1.setEsperienzaAccumulata(0);
			classicUser1.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Classic Player", Ruolo.ROLE_CLASSIC_PLAYER));
			utenteServiceInstance.inserisciNuovo(classicUser1);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser1.getId());
		}

		if (utenteServiceInstance.findByUsername("special") == null) {
			Utente classicUser2 = new Utente("special", "user2", "Antoniooo", "Verdiii", new Date());
			classicUser2.setEmail("u.user2@prova.it");
			classicUser2.setCreditoAccumulato(0);
			classicUser2.setEsperienzaAccumulata(0);
			classicUser2.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("Special player", Ruolo.ROLE_SPECIAL_PLAYER));
			utenteServiceInstance.inserisciNuovo(classicUser2);
			// l'inserimento avviene come created ma io voglio attivarlo
			utenteServiceInstance.changeUserAbilitation(classicUser2.getId());
		}
		
		
		// Mettere dati tavolo
				Tavolo tavolo1 = new Tavolo("tavoloFigo",10, 500, oraInizio);
				tavolo1.setUtenteCreazione(utenteServiceInstance.findByUsername("admin"));
				//Set<Utente> giocatori = Set.of(utenteServiceInstance.findByUsername("admin"), utenteServiceInstance.findByUsername("user"), utenteServiceInstance.findByUsername("special"));
				tavolo1.setGiocatori(utenteServiceInstance.listAllUtenti().stream().collect(Collectors.toSet()));
				System.out.println(tavolo1.getGiocatori().isEmpty());
				if (tavoloServiceInstance.findByDenominazione(tavolo1.getDenominazione()).isEmpty())
					tavoloRepository.save(tavolo1);
				
				Tavolo tavolo2 = new Tavolo("tavoloBrutto",5, 50, oraInizio);
				tavolo2.setUtenteCreazione(utenteServiceInstance.findByUsername("special"));
				
				if (tavoloServiceInstance.findByDenominazione(tavolo2.getDenominazione()).isEmpty())
					tavoloRepository.save(tavolo2);
	}
}
