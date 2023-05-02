package com.narvee.usit.serviceimpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.sun.mail.util.BASE64DecoderStream;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.narvee.usit.entity.Email;
import com.narvee.usit.repository.IEmailRepository;
import com.narvee.usit.service.IEmailBackupService;

//commented for mail error
//@Service
public class EmailBackupServiceimpl {
//implements IEmailBackupService 
	@Autowired
	private IEmailRepository repo;

//	@Value("${file.upload-dir}")
//	private String saveDirectory;
//
//	@Autowired
//	private EmailService emailService;

	//@Override
	public void saveBackup(Email email) {

	}

	//@Override
	public void saveBackup(String host, String port, String userName, String password) {
		Properties properties = new Properties();
		// server setting
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);

		// SSL setting
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
		Session session = Session.getDefaultInstance(properties);
		List<Email> listsaveem = new ArrayList<>();
		Email savemail = null;
		try {
			// connects to the message store
			Store store = session.getStore("pop3");
			store.connect(userName, password);

			// opens the INBOX folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);// READ_ONLY READ_WRITE

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				savemail = new Email();
				Message message = arrayMessages[i];

				// Getting Emailed_From
				Address[] fromAdd = message.getFrom();
				String from = fromAdd[0].toString();
				if (from.contains("<")) {
					String value = from.substring(from.indexOf("<") + 1, from.indexOf(">", from.indexOf(">")));
					savemail.setFrom(value);
					try {
						String result = value.substring(value.indexOf("@") + 1, value.indexOf("."));
						if (result.equalsIgnoreCase("gmail")) {
							// e.setCompany(value);
							savemail.setCompany(value);
							// System.out.println("==========================");
						} else {
							// e.setCompany(result);
							savemail.setCompany(value);
						}

					} catch (StringIndexOutOfBoundsException e3) {
						// e.setCompany(value);
						// savemail.setCompany(value);
						// System.out.println("String index out of bounds. String length: " );
					}
				} else {
					// e.setFrom(from);
					savemail.setFrom(from);
					try {
						String result = from.substring(from.indexOf("@") + 1, from.indexOf("."));
						// System.out.println(result+" 888 " + from);
						// e.setCompany(result);
						if (result.contains("gmail")) {
							// e.setCompany(from);
							savemail.setCompany(from);
						} else {
							savemail.setCompany(result);
						}
						// e.setCompany(result);

					} catch (StringIndexOutOfBoundsException e4) {
						// e.setCompany(from);
						// System.out.println("String index out of bounds. String length: " );
					}

				}
				// Getting Emailed_To
				Address[] to = message.getRecipients(Message.RecipientType.TO);
				String to1 = to[0].toString();
				if (to1.contains("<")) {
					String value = to1.substring(to1.indexOf("<") + 1, to1.indexOf(">", to1.indexOf(">")));
					// System.out.println("To=======>==== " + value);
					// e.setTo(value);
					savemail.setTomail(value);
				} else {
					// System.out.println("To=======> " + to1);
					// e.setTo(to1);
					savemail.setTomail(to1);
				}
				// Getting Emailed_CC
				Address[] cc = message.getRecipients(Message.RecipientType.CC);
				String finalCC = "";
				try {
					for (int j = 0; j < cc.length; j++) {
						String fromCC = cc[j].toString();
						if (fromCC.contains("<")) {
							String value = fromCC.substring(fromCC.indexOf("<") + 1,
									fromCC.indexOf(">", fromCC.indexOf(">")));
							value = value.concat(", ");
							finalCC = finalCC.concat(value);
						} else {
							fromCC = fromCC.concat(",");
							finalCC = finalCC.concat(fromCC);
						}
					}
					finalCC = finalCC.substring(0, finalCC.length() - 1);
				} catch (NullPointerException e1) {
				}
				String subject = message.getSubject();

				if (subject != null) {
					if (subject.contains("Java")) {
						// e.setSubjectcategory("Java Developer");
						savemail.setSubjectcategory("Java Developer");
					} else if (subject.contains("Password")) {
						// e.setSubjectcategory("Password");
						savemail.setSubjectcategory("Password");
					} else if (subject.contains("Hello")) {
						// e.setSubjectcategory("rHello");
						savemail.setSubjectcategory("Hello");
					} else {
						// e.setSubjectcategory("Others123");
						savemail.setSubjectcategory("Others123");
					}
				} else {
					// e.setSubjectcategory("Others==");
					savemail.setSubjectcategory("Others==");
				}

			//	String sentDate = message.getSentDate().toString();
				String contentType = message.getContentType();
				String messageContent = "";
				// store attachment file name, separated by comma
				String attachFiles = "";
				if (contentType.contains("multipart")) {
					// content may contain attachments
					Multipart multiPart = (Multipart) message.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);

						if (part.isMimeType("text/plain")) {
							// System.out.println("plain text");
						} else if (part.isMimeType("multipart/*")) {
							MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
							messageContent = getTextFromMimeMultipart(mimeMultipart);
							// System.out.println("message content after else if=====>"+messageContent);
						} else if (part.isMimeType("message/rfc822")) {
							// System.out.println("message/rfc822");
						} else {
							// System.out.println("text/html content");
						}

						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
						// part.saveFile(saveDirectory + File.separator + fileName);
						} else if (part.isMimeType("multipart/*")) {
							MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
							// savemail.setData2(mimeMultipart);
							messageContent = getTextFromMimeMultipart(mimeMultipart);
							// System.out.println("message content after else if=====>"+messageContent);
						} else {
							// this part may be the message content
							messageContent = part.getContent().toString();
							// System.out.println("message content before else if====>"+messageContent);
						}
					}
					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				}
				System.out.println(subject+" == "+messageContent);

				// e.setSubject(subject);
				savemail.setSubject(subject);
				savemail.setCcmail(finalCC);
				// e.setCc(finalCC);
				// e.setBcc(ebcc);
				// e.setBody2(messageContent);
				// e.setAttachment(attachFiles);
				savemail.setAttachment(attachFiles);
				savemail.setBody(Jsoup.parse(messageContent).wholeText());

				// e.setBody(messageContent);
				// repo.save(e);
				listsaveem.add(savemail);
				message.setFlag(Flags.Flag.DELETED, true);

				// arrayMessages[i].setFlag(Flags.Flag.DELETED, true);
				// message.setFlag(Flags.Flag.DELETED, true);
			}

			// saveMail(listsaveem);
			// disconnect
			boolean expunge = true;
			// folderInbox.close(expunge);
			store.close();
		} catch (NoSuchProviderException ex) {
			// System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			// System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void saveMail(List<Email> email) {
		System.out.println("kiran");
		// System.out.println(email);
		email.forEach(g -> {
			System.out.println(g.getSubject());
		});
		List<Email> newemail = new ArrayList();
		// check already exists or not

		for (ListIterator<Email> it = email.listIterator(); it.hasNext();) {
			Email value = it.next();
			// System.out.println(value.getSubjectcategory()+"
			// ======INNNNNNNNNN================");
			if (value.getSubjectcategory().equals("Others123")) {
				// send these records to other mails

				// emailService.mailsender(value);

				// it.remove();
			}
			List<Email> findBySubject = repo.findBySubject(value.getSubject());
			if (findBySubject == null || findBySubject.isEmpty()) {
			} else {
				it.remove();
			}
//		    if (value.equals("4")) {
//		        it.remove();
//		       // it.add("6");
//		    }
//
//		    System.out.println("List Value: " + value);
		}

//		email.forEach(e->{
//			 List<Email> findBySubject = repo.findBySubject(e.getSubject());
//			 if(findBySubject == null || findBySubject.isEmpty()) {
//				newemail.addAll(email);
//				// newemail.add((Email) findBySubject);
//				 
//			 }
//			 else {
//				 findBySubject.forEach(em->{
//					 email.removeAll(email);
//					 email.forEach(g1->{
//							System.out.println(g1.getSubject()+" -----------------------------");
//						});
//				 });
//				// email.removeAll(findBySubject);
//				 System.out.println("0000000000000000000000000000000000");
//			 }
//		});

		// System.out.println("=====================================================================================");
		email.forEach(g1 -> {
			// System.out.println(g1.getSubjectcategory()+" ====================");
			repo.save(g1);
		});
		// System.out.println(email);
		// repo.saveAll(email);
	}

	//@Override
	public List<Email> getAllEmails() {
		// repo.deleteAll();
		return repo.findAll();
	}

	//@Override
	public Email getEmail(long id) {

		return repo.findById(id).get();
	}

	//@Override
	public List<Email> findEmailByFilter(String keyword) {
		if (keyword != null) {
			return repo.getAllEmailBasedOnFilter(keyword);
		}
		return repo.findAll();
	}

	public void getAllmails(String host, String port, String userName, String password)
			throws MessagingException, IOException {
		System.out.println("Mail Start Here");
		Properties props = new Properties();
		props.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.pop3.socketFactory.fallback", "false");
		props.put("mail.pop3.socketFactory.port", "995");
		props.put("mail.pop3.port", "995");
		props.put("mail.pop3.host", "smtp.narveetech.com");
		props.put("mail.pop3.user", userName);
		props.put("mail.store.protocol", "pop3");
		props.put("mail.pop3.ssl.protocols", "TLSv1.2");
		Session session = Session.getDefaultInstance(props);

		// 4. Get the POP3 store provider and connect to the store.
		Store store = session.getStore("pop3");
		store.connect("smtp.narveetech.com", userName, password);

		// 5. Get folder and open the INBOX folder in the store.
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);

		// 6. Retrieve the messages from the folder.
		Message[] allmsgdata = inbox.getMessages();
		for (int i = 0; i < allmsgdata.length; i++) {
			Message messageRef = allmsgdata[i];

			/************** gatting mail addresses closed *********/
			// Getting Emailed_From
			Address[] fromAdd = messageRef.getFrom();
			String frommail = fromAdd[0].toString();
			List<String> fromMaill = new ArrayList();
			// taking from addres from inbox mail logic to get only mail addres
			/*******************
			 * logic to get only from address
			 *****************************/
			// if frommail contain < symabol removing them and get only mail address
			if (frommail.contains("<")) {
				String detectingMail = frommail.substring(frommail.indexOf("<") + 1,
						frommail.indexOf(">", frommail.indexOf(">")));
				fromMaill.add(detectingMail);
			} else {
				fromMaill.add(frommail);
			}
			/*******************
			 * logic to get only from address
			 *****************************/

			// Getting Emailed_To
			Address[] tomailAdd = messageRef.getRecipients(Message.RecipientType.TO);
			String finalTO = "";
			try {
				for (int j = 0; j < tomailAdd.length; j++) {
					String Tomail = tomailAdd[j].toString();
					if (Tomail.contains("<")) {
						String value = Tomail.substring(Tomail.indexOf("<") + 1,
								Tomail.indexOf(">", Tomail.indexOf(">")));
						value = value.concat(", ");
						finalTO = finalTO.concat(value);
					} else {
						Tomail = Tomail.concat(",");
						finalTO = finalTO.concat(Tomail);
					}
				}
				finalTO = finalTO.substring(0, finalTO.length() - 1);
			} catch (NullPointerException e1) {
			}
			// Getting Emailed_CC
			Address[] ccmail = messageRef.getRecipients(Message.RecipientType.CC);
			String finalCC = "";
			try {
				for (int j = 0; j < ccmail.length; j++) {
					String fromCC = ccmail[j].toString();
					if (fromCC.contains("<")) {
						String value = fromCC.substring(fromCC.indexOf("<") + 1,
								fromCC.indexOf(">", fromCC.indexOf(">")));
						value = value.concat(", ");
						finalCC = finalCC.concat(value);
					} else {
						fromCC = fromCC.concat(",");
						finalCC = finalCC.concat(fromCC);
					}
				}
				finalCC = finalCC.substring(0, finalCC.length() - 1);
			} catch (NullPointerException e1) {
			}

			/************** gatting mail addresses closed *********/

			/****** getting subject of the mail ***************/
			String subject = messageRef.getSubject();
			/****** getting subject of the mail closed ***************/

			/****** getting body of the mail ***************/

			// looking for content of the body
			String contentType = messageRef.getContentType();
			String messageContent = "";
			String attachFiles = "";

			if (contentType.contains("multipart")) {
				// content may contain attachments
				Multipart multiPart = (Multipart) messageRef.getContent();

				int numberOfParts = multiPart.getCount();
				for (int partCount = 0; partCount < numberOfParts; partCount++) {
					MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
					BodyPart bodyPart = multiPart.getBodyPart(partCount);
					if (bodyPart.isMimeType("text/plain")) {
						messageContent = messageContent + "\n" + bodyPart.getContent();
						// System.out.println("=======");
						break; // without break same text appears twice in my tests
					} else if (bodyPart.isMimeType("text/html")) {
						String html = (String) bodyPart.getContent();
						messageContent = messageContent + "\n" + org.jsoup.Jsoup.parse(html).text();
						// System.out.println("-------------");
					} else if (bodyPart.getContent() instanceof MimeMultipart) {
						// System.out.println("000000000");
						messageContent = messageContent
								+ getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
					} else {
						messageContent = bodyPart.getContent().toString();
					}

					if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
						// this part is attachment
						String fileName = part.getFileName();
						attachFiles += fileName + ", ";
						//part.saveFile(saveDirectory + File.separator + fileName);
					} else {
						// this part may be the message content
						// messageContent = part.getContent().toString();

						// BodyPart bodyPart1 = multiPart.getBodyPart(partCount);

					}

				}
				if (attachFiles.length() > 1) {
					attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
				}
			}

			System.out.println(subject + "===" + attachFiles);

			// System.out.println(finalTO);
			// System.out.println("Email Number " + i);
			// System.out.println("From: " + messageRef.getFrom()[0]);
			// System.out.println(finalCC);
		}

		// 7. Close folder and close store.
		inbox.close(false);
		store.close();

	}

	public void downloadEmailAttachments(String host, String port, String userName, String password) {
		Properties properties = new Properties();

		// server setting
		properties.put("mail.pop3.host", host);
		properties.put("mail.pop3.port", port);

		// SSL setting
		properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);

		List<Email> listOfEntity = new ArrayList<>();
		Email entity = null;

		try {
			// connects to the message store
			Store store = session.getStore("pop3");
			store.connect(userName, password);

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				entity = new Email();
				Message messageRef = arrayMessages[i];

				/************** gatting mail addresses closed *********/
				// Getting Emailed_From
				Address[] fromAdd = messageRef.getFrom();
				String frommail = fromAdd[0].toString();
				// taking from addres from inbox mail logic to get only mail addres
				/*******************
				 * logic to get only from address
				 *****************************/
				// if frommail contain < symabol removing them and get only mail address
				if (frommail.contains("<")) {
					String detectingMail = frommail.substring(frommail.indexOf("<") + 1,
							frommail.indexOf(">", frommail.indexOf(">")));
					entity.setFrom(detectingMail);
				} else {
					entity.setFrom(frommail);
				}
				/*******************
				 * logic to get only from address
				 *****************************/

				// Getting Emailed_To
				Address[] tomailAdd = messageRef.getRecipients(Message.RecipientType.TO);
				String finalTO = "";
				try {
					for (int j = 0; j < tomailAdd.length; j++) {
						String Tomail = tomailAdd[j].toString();
						if (Tomail.contains("<")) {
							String value = Tomail.substring(Tomail.indexOf("<") + 1,
									Tomail.indexOf(">", Tomail.indexOf(">")));
							value = value.concat(", ");
							finalTO = finalTO.concat(value);
						} else {
							Tomail = Tomail.concat(",");
							finalTO = finalTO.concat(Tomail);
						}
					}
					finalTO = finalTO.substring(0, finalTO.length() - 1);
					entity.setTomail(finalTO);
				} catch (NullPointerException e1) {
				}

				// Getting Emailed_CC
				Address[] ccmail = messageRef.getRecipients(Message.RecipientType.CC);
				String finalCC = "";
				try {
					for (int j = 0; j < ccmail.length; j++) {
						String fromCC = ccmail[j].toString();
						if (fromCC.contains("<")) {
							String value = fromCC.substring(fromCC.indexOf("<") + 1,
									fromCC.indexOf(">", fromCC.indexOf(">")));
							value = value.concat(", ");
							finalCC = finalCC.concat(value);
						} else {
							fromCC = fromCC.concat(",");
							finalCC = finalCC.concat(fromCC);
						}
					}
					finalCC = finalCC.substring(0, finalCC.length() - 1);
					entity.setCcmail(finalCC);
				} catch (NullPointerException e1) {
				}

				/************** gatting mail addresses closed *********/

				/****** getting subject of the mail ***************/
				String subject = messageRef.getSubject();
				/****** getting subject of the mail closed ***************/

				//String sentDate = messageRef.getSentDate().toString();

				// ***** getting message content and attachments from mail box*****//
				String contentType = messageRef.getContentType();
				
				String messageContent = "";
				// store attachment file name, separated by comma
				String attachFiles = "";
				if (contentType.contains("multipart")) {
					// content may contain attachments
					Multipart multiPart = (Multipart) messageRef.getContent();
					int numberOfParts = multiPart.getCount();
					for (int partCount = 0; partCount < numberOfParts; partCount++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
						BodyPart bodyPart = multiPart.getBodyPart(partCount);
						// this part may be the message content
						if (bodyPart.getContentType().toLowerCase().contains("text/plain")) {
							messageContent = messageContent + "\n" + bodyPart.getContent();
							//System.out.println(contentType+" ===  "+subject+" ----- "+messageContent);
							// break; // without break same text appears twice in my tests
						} else if (bodyPart.isMimeType("text/html")) {
							String html = (String) bodyPart.getContent();
							messageContent = messageContent + "\n" + org.jsoup.Jsoup.parse(html).text();
						} else if (bodyPart.getContent() instanceof MimeMultipart) {
							messageContent = messageContent
									+ getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
						} 
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							// this part is attachment
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
//							try {
//							//	part.saveFile(saveDirectory + File.separator + fileName);
//							} catch (FileNotFoundException e) {
//							}

						}
					}

					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				}
				
				//System.out.println(contentType+" ===  "+subject);

//				List<String> subCat = List.of("Java ", "Test ", "Sunday ", "Work ", "Narvee ");
//
//				for (ListIterator<String> it = subCat.listIterator(); it.hasNext();) {
//					String value = it.next();
//					if (subject.contains(value)) {
//						entity.setSubjectcategory(value);
//					} else {
//						entity.setSubjectcategory("Others");
//					}
//
//				}
				if (subject.contains("Test ")) {
					entity.setSubjectcategory("Test ");
				} else if (subject.contains("Sunday ")) {
					entity.setSubjectcategory("Sunday ");
				}

				else if (subject.contains("Work ")) {
					entity.setSubjectcategory("Work ");
				}

				else if (subject.contains("Narvee ")) {
					entity.setSubjectcategory("Narvee ");
				}

				else {
					entity.setSubjectcategory("Others");
				}
				entity.setBody(messageContent);
				entity.setAttachment(attachFiles);
				entity.setSubject(subject);
				listOfEntity.add(entity);
			}
			saveEntity(listOfEntity);
			// disconnect
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void saveEntity(List<Email> email) {
		System.out.println("========================");
		System.out.println(email);
		repo.saveAll(email);
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "\n" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

}
