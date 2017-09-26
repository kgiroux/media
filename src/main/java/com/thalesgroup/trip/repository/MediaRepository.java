package com.thalesgroup.trip.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thalesgroup.trip.common.dto.media.TypeMedia;
import com.thalesgroup.trip.entity.MetaDataMediaEntity;

/**
 * Magic class from mongo to perform db request
 * @author scadot
 *
 */
public interface MediaRepository extends MongoRepository<MetaDataMediaEntity, String> {
	/**
	 * retrieve metadata from id
	 * @param id
	 * @return
	 */
	public MetaDataMediaEntity findById(String id);
    
	/**
	 * Retrieve metadata from type of media
	 * @param typeMedia
	 * @return
	 */
    public List<MetaDataMediaEntity> findByTypeMedia(TypeMedia typeMedia);
    
    /**
     * Retrieve metadata from type of media 
     * @param id that need to be check
     * @param name that need to be check
     * @return
     */
    public List<MetaDataMediaEntity> findByIdOrName(String id, String name);
    
}
