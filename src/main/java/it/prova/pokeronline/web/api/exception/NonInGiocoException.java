package it.prova.pokeronline.web.api.exception;

public class NonInGiocoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NonInGiocoException(String message) {
		super(message);
	}
	

}
