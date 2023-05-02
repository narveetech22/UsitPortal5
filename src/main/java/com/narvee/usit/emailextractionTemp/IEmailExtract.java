package com.narvee.usit.emailextractionTemp;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.narvee.usit.entity.Email;
public interface IEmailExtract {
	
	public Optional<ExtractEmail> getmailbyid(long id);
	public List<ListExtractMailDTO> listAll();
	//public void downloadEmailAttachments(String host, String port,String userName, String password,Date fromDate,Date toDate);
	public String mailExtraction(String host, String port, String userName, String password,Date fromDate,Date toDate);
	
	//created method for fetching data from ExtractMail and EmailAttachment table
	public List<ExtractEmail> findAllMailDataWithAttachment();
}
