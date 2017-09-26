package com.thalesgroup.trip.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thalesgroup.trip.starter.StorageProperties;

@Service("remoteUploadService")
public class RemoteUploadService {

	@Autowired
	private StorageProperties properties;
	
	
	/**
	 * Create a request for upload file in target location 
	 * @param path
	 */
	public void createRemoteSCPRequest(String path, String fileName){
		String request = "scp " + properties.getLocation() + File.separator + fileName + " " ;
	}
}
