package com.narvee.usit.service;

import org.springframework.web.multipart.MultipartFile;

public interface IfileStorageService {

	public String storeFile(MultipartFile file, String mobile, String type);

	public String storemultiplefiles(MultipartFile file);

}
