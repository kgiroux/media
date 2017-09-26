package com.thalesgroup.trip.services;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.thalesgroup.trip.common.dto.error.ErrorMedia;
import com.thalesgroup.trip.common.dto.media.Media;
import com.thalesgroup.trip.common.dto.media.TypeMedia;
import com.thalesgroup.trip.entity.MetaDataMediaEntity;
import com.thalesgroup.trip.persistence.IMediaMetaDataPersistence;
import com.thalesgroup.trip.starter.StorageProperties;
import com.thalesgroup.trip.webservices.NotFoundException;

/**
 * Ensemble de services permettant de gérer les services
 *
 * @author thales
 *
 */
@Service("mediaService")
public class MediaService {

	@Autowired
	private StorageProperties properties;
	/**
	 * Root location
	 */
	private Path rootLocation;

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MediaService.class);

	/**
	 * Chargement des informations liées à la génération du qr code
	 */
	@Value("${qrcode.path}")
	private String QRCODE_PATH;
	/**
	 * QRCODE format
	 */
	@Value("${qrcode.imageFormat}")
	private String QRCODE_IMG_FORMAT;
	/**
	 * QRCODE SIZE
	 */
	@Value("${qrcode.size}")
	private int QRCODE_SIZE;

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
			File fileToReturn = new File(media.getPath() + media.getFileName());
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
	public void deleteMedia(String id) throws NotFoundException{
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
		
		return null;
	}

	/**
	 * Create a QR Code media
	 *
	 * @param data
	 */
	public void createQrCode(String data) {
		try {
			
			generateQRCode(data);
			
			// Creation of the media
			Media mediaQRCode = new Media();
			mediaQRCode.setId(data);
			mediaQRCode.setPath(QRCODE_PATH);
			mediaQRCode.setName("qrcode");
			mediaQRCode.setFileName("qrcode" + data + "." + QRCODE_IMG_FORMAT);
			mediaQRCode.setTypeMedia(TypeMedia.QRCODE);

			// Saving the media in the db
			mediaPersistence.createMetaDataMedia(mediaQRCode);
		} catch (IOException | WriterException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Create QRCode with an id
	 * 
	 * @param id
	 * @param data
	 */
	public void createQrCodeWithId(String id, String data) {
		try {
			generateQRCode(data);
			
			// Creation of the media
			Media mediaQRCode = new Media();
			mediaQRCode.setId(id);
			mediaQRCode.setPath(QRCODE_PATH);
			mediaQRCode.setName("qrcode");
			mediaQRCode.setFileName("qrcode" + id + "." + QRCODE_IMG_FORMAT);
			mediaQRCode.setTypeMedia(TypeMedia.QRCODE);

			// Saving the media in the db
			mediaPersistence.createMetaDataMedia(mediaQRCode);
		} catch (IOException | WriterException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	private void generateQRCode(String data) throws WriterException, IOException {
		String outputFileName = QRCODE_PATH + "qrcode" + data + "." + QRCODE_IMG_FORMAT;

		// Encoding the QR Code
		QRCode qr = new QRCode();
		Encoder.encode(data, ErrorCorrectionLevel.L, qr);
		ByteMatrix matrix = qr.getMatrix();

		// Java 2D Traitement de Area
		// Futurs modules
		Area a = new Area();
		Area module = new Area(new Rectangle.Float(0, 0, 1, 1));

		// Deplacement du module
		AffineTransform at = new AffineTransform();
		int width = matrix.getWidth();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (matrix.get(j, i) == 1) {
					// Ajout du module
					a.add(module);
				}
				// Decalage a droite
				at.setToTranslation(1, 0);
				module.transform(at);
			}

			// Ligne suivante
			at.setToTranslation(-width, 1);
			module.transform(at);
		}

		// Agrandissement de l'Area pour le remplissage de l'image
		double ratio = QRCODE_SIZE / (double) width;

		// Quietzone : 4 modules de bordures autour du QR Code (zone vide
		// pour bien identifier le code dans la page imprimee)
		double adjustment = width / (double) (width + 8);
		ratio = ratio * adjustment;
		at.setToTranslation(4, 4);
		a.transform(at);

		// On agrandit le tour a la taille souhaitee.
		at.setToScale(ratio, ratio);
		a.transform(at);

		// Java 2D Traitement l'image
		BufferedImage im = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) im.getGraphics();

		// Modules verts
		Color bleu = new Color(0, 0, 153);
		g.setPaint(bleu);

		g.setBackground(new Color(0xFFFFFF));

		// Fond blanc
		g.clearRect(0, 0, QRCODE_SIZE, QRCODE_SIZE);

		// Remplissage des modules
		g.fill(a);

		// Ecriture sur le disque
		File f = new File(outputFileName);
		f.setWritable(true);
		ImageIO.write(im, QRCODE_IMG_FORMAT, f);
		f.createNewFile();
	}

	/**
	 * Add a file to the media server
	 * 
	 * @param file
	 * @throws IOException
	 */
	public void createFile(MultipartFile file, Media media) throws IllegalArgumentException {
		// Check if the media not already exist
		List<Media> mediaEntity = mediaPersistence.retrieveMetaDataFromIdOrName(media.getId(), media.getName());
		if (!CollectionUtils.isEmpty(mediaEntity)) {
			LOG.error("Media already existing on the database");
			throw new IllegalArgumentException(ErrorMedia.ERR_MEDIA_03.toString());
		}
		// Build the media path
		String pathMedia = retrievePathByType(media);
		if(!pathMedia.equals("")){
			media.setPath(pathMedia);
		}else{
			LOG.error("Invalid type of media");
			throw new IllegalArgumentException(ErrorMedia.ERR_MEDIA_04.toString());
		}
		
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

	private String retrievePathByType(Media media) {
		switch (media.getTypeMedia()) {
		case VIDEO:
			return properties.getLocationVideos();
		case LOGO_LINE:
		case LOGO_COMPANY:
		case PICTURE:
			return properties.getLocationImages();
		case SOUND:
			return properties.getLocationSounds();
		default:
			break;
		}
		return "";
	}

	public void store(MultipartFile file,Media media) throws IOException {
		try {
			Path path = Paths.get(media.getPath());
			Files.createDirectories(path);
			LOG.debug("Try to create file into {}", path.toUri().toString());
			if (file.isEmpty()) {
				throw new IOException("Failed to store empty file " + file.getOriginalFilename());
			}
			Files.copy(file.getInputStream(), path.resolve(media.getFileName()));
		} catch (IOException e) {
			throw new IOException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

	@PostConstruct
	public void init() throws IOException {
		this.rootLocation = Paths.get(properties.getLocation());
		try {
			if (!Files.exists(rootLocation, LinkOption.NOFOLLOW_LINKS)) {
				Files.createDirectory(rootLocation);
			}
		} catch (IOException ex) {
			LOG.error("Could not initialize storage", ex);
			throw new IOException("Could not initialize storage");
		}
	}
}
