package com.giroux.kevin.dofustuff.media.network.webservices;

import com.giroux.kevin.dofustuff.commons.media.ErrorMedia;
import com.giroux.kevin.dofustuff.commons.media.Media;
import com.giroux.kevin.dofustuff.commons.media.TypeMedia;
import com.giroux.kevin.dofustuff.media.network.exception.NotFoundException;
import com.giroux.kevin.dofustuff.media.services.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

/**
 * Exposition des web services pour le traitement des medias
 *
 * @author scadot
 *
 */
@RestController
@RequestMapping("/medias")
@CrossOrigin(origins = "*")
public class MediaWS {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MediaService.class);
	
	/**
	 * Service de gestion des médias
	 */
	@Autowired
	@Qualifier("mediaService")
	private MediaService mediaService;

	/**
	 * Web service permettant de récuperer un média à partir de sa réference
	 *
	 * @param id
	 *            Référence du média
	 * @return le média recherché
	 */
	@RequestMapping(name ="Read one Media", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_OCTET_STREAM_VALUE }, value = "/{id}")
	public FileSystemResource getMedia(@PathVariable("id") String id) {
		File fileFormService = mediaService.retrieveMedia(id);
		if (fileFormService != null) {
			return new FileSystemResource(fileFormService);
		} else {
			throw new NotFoundException("The media was not found");
		}
	}

	/**
	 * Create a media
	 *
	 * @param media
	 *            media to create
	 */
	@RequestMapping(name ="Create/Update Media", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE }, value="/metadata")
	public void createMedia(@RequestBody Media media) {
		mediaService.createMedia(media);
	}

	/**
	 * Delete a media
	 *
	 * @param id
	 *            id of the media to delete
	 */
	@RequestMapping(name ="Delete Media", method = RequestMethod.DELETE, consumes = { MediaType.APPLICATION_JSON_VALUE }, value="/metadata/{id}")
	public void deleteMedia(@PathVariable("id") String id) {
		try {
			mediaService.deleteMedia(id);
		} catch (NotFoundException e) {
			LOG.error("Cannot delete file",e);
			throw new NotFoundException(e.getMessage());
		}
		
	}

	/**
	 * Web service permettant de récupérer les métadatas en fonction d'un type
	 * (video, son, image...)
	 *
	 * @param type
	 * @return La liste des métadatas des médias du type spécifié
	 */
	@RequestMapping(name ="Read Medias by type", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE }, value = "/metadata/type/{type}")
	public List<Media> getMetaDataFromType(@PathVariable("type") TypeMedia type) {
		
		List<Media> retrieveList = mediaService.retrieveMetaDataMediaFromType(type);
		if(CollectionUtils.isEmpty(retrieveList)){
			throw new NotFoundException(ErrorMedia.ERR_MEDIA_05.toString());
		}
		return retrieveList;
	}

}
