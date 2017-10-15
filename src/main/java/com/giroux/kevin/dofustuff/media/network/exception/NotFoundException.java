package com.giroux.kevin.dofustuff.media.network.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe permettant de gérer les exceptions au niveau des WebServices
 * 
 * @author thales
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String string) {
		super(string);
	}

	/**
     * 
     */
    private static final long serialVersionUID = -3487645332685643405L;

}
