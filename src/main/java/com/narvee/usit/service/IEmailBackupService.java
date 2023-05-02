package com.narvee.usit.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

import com.narvee.usit.entity.Email;
import com.narvee.usit.entity.Technologies;

public interface IEmailBackupService {

	/// public void saveBackup(Email email);
	public Optional<Email> getmailbyid(long id);

	public List<Email> getAllEmails();

	public boolean deleteAllByIdInBatch(List<Long> ids);

	public List<Email> downloadEmailAttachments(String host, String port, String userName, String password);

	public void deleteMessages(String host, String port, String userName, String password);
}
