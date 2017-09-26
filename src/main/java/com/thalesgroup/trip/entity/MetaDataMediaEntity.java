package com.thalesgroup.trip.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.thalesgroup.trip.common.dto.media.TypeMedia;

/**
 * Classe représentant les meta-data persisté en BD
 * 
 * @author scadot
 *
 */
@Document(collection = "metadatamedia")
public class MetaDataMediaEntity {
	/**
	 * Identifiant fonctionnel
	 */
	@Id
	private String id;

	/**
	 * Chemin complet du fichier
	 */
	private String path;

	/**
	 * Nom fonctionnel du média
	 */
	private String name;

	/**
	 * Nom du fichier
	 */
	private String fileName;

	/**
	 * Type du média
	 */
	private TypeMedia typeMedia;

	/**
	 * Catégorie du média
	 */
	private List<String> categories;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	public TypeMedia getTypeMedia() {
		return typeMedia;
	}
	
	public void setTypeMedia(TypeMedia typeMedia) {
		this.typeMedia = typeMedia;
	}
}
