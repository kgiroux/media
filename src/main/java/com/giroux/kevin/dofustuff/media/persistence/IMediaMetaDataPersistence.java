package com.giroux.kevin.dofustuff.media.persistence;

import com.giroux.kevin.dofustuff.commons.media.Media;
import com.giroux.kevin.dofustuff.commons.media.TypeMedia;
import com.giroux.kevin.dofustuff.media.persistence.entity.MetaDataMediaEntity;

import java.util.List;

/**
 * Interface de gestion de la persistence des métadata
 * 
 * @author kgiroux
 *
 */
public interface IMediaMetaDataPersistence {

	/**
	 * Méthode permettant de récupérer un media
	 * 
	 * @param id
	 *            Référence du media à récupérer
	 * @return Le media trouvé, null si aucun media ne correspond à la référence
	 */
	Media retrieveMetaDataMedia(String id);

	/**
	 * Methode qui permet l'ajout d'un Media;
	 * 
	 * @param media
	 */
	MetaDataMediaEntity createMetaDataMedia(Media media);

	/**
	 * Méthode qui permet la suppresion d'un Media;
	 * 
	 * @param id
	 */
	void deleteMetaDataMedia(String id);
	
	/**
	 * Méthode permettant de récupérer l'ensemble des métadatas à partir d'un type
	 * @param type type du média (video, son, image...)
	 * @return Liste des métadatas correspondant au type
	 */
	List<Media> retrieveMetaDataFromType(final TypeMedia type);
	
	/**
	 * Retrieve the list of of media with a given id or name
	 * @param id the given id
	 * @param name the given name
	 * @return
	 */
	List<Media> retrieveMetaDataFromIdOrName(final String id, final String name);
	
}
