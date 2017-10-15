package com.giroux.kevin.dofustuff.media.network.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe permettant de gérer les exceptions au niveau des WebServices
 * 
 * @author thales
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3487645332685643405L;

	/**
	 * Default constructor
	 */
	public BadRequestException() {
	}

	/**
	 * Pass a message for the exception
	 * 
	 * @param message
	 */
	public BadRequestException(String message) {
		super(message);
	}
}