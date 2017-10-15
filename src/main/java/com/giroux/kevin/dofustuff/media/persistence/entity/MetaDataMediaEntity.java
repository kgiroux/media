package com.giroux.kevin.dofustuff.media.persistence.entity;

import com.giroux.kevin.dofustuff.commons.media.TypeMedia;

import javax.persistence.*;


/**
 * Classe représentant les meta-data persisté en BD
 * 
 * @author scadot
 *
 */
@Entity
@Table(name="media", schema="sch_dofustuff")
public class MetaDataMediaEntity {
	/**
	 * Identifiant fonctionnel
	 */
	@Id
	@Column
	private String id;

	/**
	 * Chemin complet du fichier
	 */
	@Column
	private String path;

	/**
	 * Nom fonctionnel du média
	 */
	@Column
	private String name;

	/**
	 * Nom du fichier
	 */
	@Column
	private String fileName;

	/**
	 * Type du média
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private TypeMedia typeMedia;

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

	public TypeMedia getTypeMedia() {
		return typeMedia;
	}
	
	public void setTypeMedia(TypeMedia typeMedia) {
		this.typeMedia = typeMedia;
	}
}
