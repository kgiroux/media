package com.giroux.kevin.dofustuff.media.persistence.factory;

import com.giroux.kevin.dofustuff.commons.media.Media;
import com.giroux.kevin.dofustuff.commons.utils.Factory;
import com.giroux.kevin.dofustuff.media.persistence.entity.MetaDataMediaEntity;
import org.springframework.stereotype.Component;

@Component
public class MetaDataMediaFactory implements Factory<MetaDataMediaEntity,Media>{
	
    /**
     * Créer une entité à partir d'un dto
     * 
     * @param media
     *           Le DTO du media à convertir en entité
     * @return L'entité convertie
     */
    public MetaDataMediaEntity dtoToEntity(Media media) {
    	MetaDataMediaEntity metaDataEntity = new MetaDataMediaEntity();

    	metaDataEntity.setId(media.getId());
    	metaDataEntity.setPath(media.getPath());
    	metaDataEntity.setName(media.getName());
    	metaDataEntity.setFileName(media.getFileName());
    	metaDataEntity.setTypeMedia(media.getTypeMedia());

        return metaDataEntity;
    }
    
    /**
     * Créer un DTO à partir d'une entité
     * 
     * @param metaDataEntity
     *            L'entité du media à convertir en DTO
     * @return Le DTO converti
     */
    public Media entityToDto(MetaDataMediaEntity metaDataEntity) {
    	Media media = new Media();
        
    	media.setId(metaDataEntity.getId());
    	media.setPath(metaDataEntity.getPath());
    	media.setName(metaDataEntity.getName());
    	media.setFileName(metaDataEntity.getFileName());
        media.setTypeMedia(metaDataEntity.getTypeMedia());
        
        return media;
    }
}
