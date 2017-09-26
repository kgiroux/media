package com.thalesgroup.trip.starter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	/**
     * Folder location for storing files
     */
	@Value("${media.upload.dir}")
    private String location;

	/**
     * Folder location for storing files images
     */
	@Value("${media.upload.images}")
    private String locationImages;
	
	/**
     * Folder location for storing files images
     */
	@Value("${media.upload.sounds}")
    private String locationSounds;
	
	/**
     * Folder location for storing files images
     */
	@Value("${media.upload.videos}")
    private String locationVideos;
	
	/**
	 * User for the SCP
	 */
	@Value("${media.upload.user}")
	private String user;
	
	/**
	 * Target for the SCP
	 */
	@Value("${media.upload.target}")
	private String target;
	
	
	public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	/**
	 * @return the locationImages
	 */
	public String getLocationImages() {
		return locationImages;
	}

	/**
	 * @param locationImages the locationImages to set
	 */
	public void setLocationImages(String locationImages) {
		this.locationImages = locationImages;
	}

	/**
	 * @return the locationSounds
	 */
	public String getLocationSounds() {
		return locationSounds;
	}

	/**
	 * @param locationSounds the locationSounds to set
	 */
	public void setLocationSounds(String locationSounds) {
		this.locationSounds = locationSounds;
	}

	/**
	 * @return the locationVideos
	 */
	public String getLocationVideos() {
		return locationVideos;
	}

	/**
	 * @param locationVideos the locationVideos to set
	 */
	public void setLocationVideos(String locationVideos) {
		this.locationVideos = locationVideos;
	}

    
}
