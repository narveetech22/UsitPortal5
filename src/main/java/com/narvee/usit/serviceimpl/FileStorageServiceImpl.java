package com.narvee.usit.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.UsitPortalApplication;
import com.narvee.usit.exception.FileStorageException;
import com.narvee.usit.exception.FileStorageProperties;
import com.narvee.usit.service.IfileStorageService;

@Service
public class FileStorageServiceImpl implements IfileStorageService {
	private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

	private final Path fileStorageLocation;
	// @Value("${file.upload.folder}")
	@Value("${file.upload-dir}")
	private String storeLocation;// = "/var/www/html/usit_mat\\";

	//constructor injection , creation of folder if not available
	@Autowired
	public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
		}
	}

	//upload single file
	public String storeFile(MultipartFile file,String mobile,String type) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = fileName.substring(fileName.indexOf(".") + 1);
		StringBuilder newfile = new StringBuilder();
		newfile.append(mobile+"-"+type+"."+extension);
		File file2 = new File(storeLocation);
		logger.info(" file location ",storeLocation);
		if (!file2.exists()) {
			file2.mkdir();
		}
		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			//System.out.println("===============================");
			//System.out.println(newfile+"== ");
			//System.out.println("===============================");
			InputStream inR = file.getInputStream();
			OutputStream osResume = new FileOutputStream(file2.getAbsolutePath() + "\\" + newfile);
			IOUtils.copy(inR, osResume);
			return newfile.toString();
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}
             //store multiple files
	public String storemultiplefiles(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		// String storeLocation = "D:/stores2";
		InputStream inR = null;
		OutputStream osResume = null;

		File file2 = new File(storeLocation);
		logger.info(" multiple file location ",storeLocation);
		if (!file2.exists()) {
			file2.mkdir();
		}

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			inR = file.getInputStream();

			osResume = new FileOutputStream(file2.getAbsolutePath() + "\\" + fileName);
			IOUtils.copy(inR, osResume);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}

		finally {
			try {
				inR.close();
				osResume.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
