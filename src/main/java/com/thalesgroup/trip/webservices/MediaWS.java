package com.thalesgroup.trip.webservices;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thalesgroup.trip.common.dto.error.ErrorMedia;
import com.thalesgroup.trip.common.dto.media.Media;
import com.thalesgroup.trip.common.dto.media.TypeMedia;
import com.thalesgroup.trip.common.util.TripUtils;
import com.thalesgroup.trip.services.MediaService;

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
	 * Create a file
	 * @param file
	 * @throws IOException 
	 */
	@RequestMapping(name ="Create File", method = RequestMethod.POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public void createFile(@RequestParam("fileData") MultipartFile file, @RequestParam("metadata") String mediaStr ) {
		
		try {
			Media media = TripUtils.deserializerJSON(mediaStr, Media.class);
			mediaService.createFile(file,media);
		}catch(IOException ex){ 
			LOG.error("Invalid data",ex);
			throw new BadRequestException(ErrorMedia.ERR_MEDIA_04.toString());
		}catch (IllegalArgumentException e) {
			LOG.error("Cannot create file",e);
			throw new BadRequestException(e.getMessage());
		}
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
