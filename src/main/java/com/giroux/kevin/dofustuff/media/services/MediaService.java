package com.giroux.kevin.dofustuff.media.services;

import com.giroux.kevin.dofustuff.commons.media.ErrorMedia;
import com.giroux.kevin.dofustuff.commons.media.Media;
import com.giroux.kevin.dofustuff.commons.media.TypeMedia;
import com.giroux.kevin.dofustuff.media.network.exception.NotFoundException;
import com.giroux.kevin.dofustuff.media.persistence.IMediaMetaDataPersistence;
import com.giroux.kevin.dofustuff.media.persistence.entity.MetaDataMediaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Ensemble de services permettant de gérer les services
 *
 * @author thales
 *
 */
@Service("mediaService")
public class MediaService {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MediaService.class);

	/**
	 * Service de stockage des médias
	 */
	@Autowired
	@Qualifier("metaDataMediaPersistence")
	private IMediaMetaDataPersistence mediaPersistence;

	/**
	 * Service permettant de récupérer un média
	 *
	 * @param id
	 * @return
	 */
	public File retrieveMedia(String id) {

		Media media = mediaPersistence.retrieveMetaDataMedia(id);
		if (media == null) {
			LOG.error("Le fichier media pour la référence {} n'est plus disponible", id);
			return null;
		} else {
			File fileToReturn = new File(media.getPath());
			if (fileToReturn.exists()) {
				return fileToReturn;
			} else {
				LOG.error("Le fichier n'existe plus sur le référenciel");
				return null;
			}
		}
	}

	/**
	 * Creation of a media
	 *
	 * @param media
	 */
	public void createMedia(Media media) {
		mediaPersistence.createMetaDataMedia(media);
	}

	/**
	 * Delete a media
	 *
	 * @param id
	 */
	public void deleteMedia(String id) {
		Media media = mediaPersistence.retrieveMetaDataMedia(id);
		if (media == null) {
			LOG.error("Le fichier media pour la référence {} n'est plus disponible", id);
			throw new NotFoundException(ErrorMedia.ERR_MEDIA_05.toString());
		} else {
			File fileToDelete = new File(media.getPath() + media.getFileName());
			if (fileToDelete.exists()) {
				// Delete file on the media server before delete from database
				if(fileToDelete.delete()){
					// Delete from Database
					mediaPersistence.deleteMetaDataMedia(id);
				}
			} else {
				LOG.error("Le fichier n'existe plus sur le référenciel");
				throw new NotFoundException(ErrorMedia.ERR_MEDIA_05.toString());
			}
		}
	}

	/**
	 * Service providing all metadata from type
	 *
	 * @param typeMedia
	 *            type of media
	 * @return
	 */
	public List<Media> retrieveMetaDataMediaFromType(final TypeMedia typeMedia) {
		List<Media> listToReturn = mediaPersistence.retrieveMetaDataFromType(typeMedia);
		if(!CollectionUtils.isEmpty(listToReturn)){
			Collections.sort(listToReturn);
			return listToReturn;
		}
		
		return new ArrayList<>();
	}




	/**
	 * Add a file to the media server
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void createFile(MultipartFile file, Media media) {
		// Check if the media not already exist
		List<Media> mediaEntity = mediaPersistence.retrieveMetaDataFromIdOrName(media.getId(), media.getName());
		if (!CollectionUtils.isEmpty(mediaEntity)) {
			LOG.error("Media already existing on the database");
			throw new IllegalArgumentException(ErrorMedia.ERR_MEDIA_03.toString());
		}
		// Build the media path
		
		MetaDataMediaEntity metadata = mediaPersistence.createMetaDataMedia(media);
		if (metadata != null) {
			try {
				if (file != null) {
					this.store(file, media);
				} else {
					LOG.error("File null");
					throw new IllegalArgumentException(ErrorMedia.ERR_MEDIA_02.toString());
				}
			} catch (IOException ex) {
				// revert the metadata creation
				mediaPersistence.deleteMetaDataMedia(metadata.getId());
				LOG.error(ex.getMessage());
				throw new IllegalArgumentException(ErrorMedia.ERR_MEDIA_01.toString());
			}

		}

	}

	public void store(MultipartFile file,Media media) throws IOException {
		try {
			Path path = Paths.get(media.getPath());
			Files.createDirectories(path);
			LOG.debug("Try to create file into {}", path.toUri());
			if (file.isEmpty()) {
				throw new IOException("Failed to store empty file " + file.getOriginalFilename());
			}
			Files.copy(file.getInputStream(), path.resolve(media.getFileName()));
		} catch (IOException e) {
			throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

}
