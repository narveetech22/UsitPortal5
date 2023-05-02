package com.narvee.usit.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;

import com.narvee.usit.entity.H1BApplicants;

public interface IH1BApplicantsService {
	 public H1BApplicants saveH1BApplicants(H1BApplicants h1bApplicants);
		public H1BApplicants getH1BApplicantsById(Long id);
		public List<H1BApplicants> getAllH1BApplicants();
		public void deleteH1BApplicantsById(Long id);
		//passportdoc everifydoc i9doc I797doc i94doc ssndoc
		public int update(String passportdoc, String everifydoc, String i9doc,String I797doc, String i94doc, String ssndoc, Long id);
		public Resource downloadfile(long id, String doctype) throws FileNotFoundException;
		public int removeFile(long id, String type);
		
		public void checkFilesUploaded() throws IOException;
		
}
