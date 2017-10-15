package com.giroux.kevin.dofustuff.media.persistence.repository;

import com.giroux.kevin.dofustuff.commons.media.TypeMedia;
import com.giroux.kevin.dofustuff.media.persistence.entity.MetaDataMediaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Magic class from mongo to perform db request
 * @author scadot
 *
 */
@Repository
public interface MediaRepository extends CrudRepository<MetaDataMediaEntity, String> {
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
