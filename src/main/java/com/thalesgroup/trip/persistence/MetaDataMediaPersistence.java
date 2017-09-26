package com.thalesgroup.trip.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.thalesgroup.trip.common.dto.media.Media;
import com.thalesgroup.trip.common.dto.media.TypeMedia;
import com.thalesgroup.trip.entity.MetaDataMediaEntity;
import com.thalesgroup.trip.factory.MetaDataMediaFactory;
import com.thalesgroup.trip.repository.MediaRepository;

/**
 * Implementation de la persistence des métadata des médias
 * @author scadot
 *
 */
@Repository("metaDataMediaPersistence")
public class MetaDataMediaPersistence implements IMediaMetaDataPersistence {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MetaDataMediaPersistence.class);

	/**
	 * Interface de communication avec Mongo
	 */
	@Autowired
	private MediaRepository mediaRepository;

	/**
	 * Factory d'image
	 */
	@Autowired
	private MetaDataMediaFactory factory;

	@Override
	public Media retrieveMetaDataMedia(String id) {
		MetaDataMediaEntity metaDataEntity = mediaRepository.findById(id);

		// Si l'entité a été trouvée, on renvoie le DTO
		if (metaDataEntity != null) {
			return factory.entityToDto(metaDataEntity);
		}

		// Null si aucune entité correspondante n'a été trouvé
		return null;
	}

	@Override
	public MetaDataMediaEntity createMetaDataMedia(Media media) {
		MetaDataMediaEntity metadata = null;
		if (media != null) {
			metadata = mediaRepository.save(factory.dtoToEntity(media));
		} else {
			LOG.error("Impossible de créer la ressource en base de donnée");
		}
		return metadata;
	}

	@Override
	public void deleteMetaDataMedia(String id) {
		mediaRepository.delete(id);
	}

	@Override
	public List<Media> retrieveMetaDataFromType(TypeMedia type) {
		List<MetaDataMediaEntity> metadataEntities = mediaRepository.findByTypeMedia(type);
		if(CollectionUtils.isEmpty(metadataEntities)){
			return null;
		}
		List<Media> media = new ArrayList<>();
		for(MetaDataMediaEntity entity: metadataEntities){
			media.add(factory.entityToDto(entity));
		}
		return media;
	}

	@Override
	public List<Media> retrieveMetaDataFromIdOrName(String id, String name) {
		List<MetaDataMediaEntity> metadataEntities = mediaRepository.findByIdOrName(id, name);
		if(CollectionUtils.isEmpty(metadataEntities)){
			return null;
		}
		List<Media> media = new ArrayList<>();
		for(MetaDataMediaEntity entity: metadataEntities){
			media.add(factory.entityToDto(entity));
		}
		
		return media;   
	}

}
