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
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narvee.usit.entity.Email;
import com.narvee.usit.repository.IEmailRepository;
import com.narvee.usit.repository.TechnologyRepository;
import com.narvee.usit.service.IEmailBackupService;

//commented for mail error
@Service
@Transactional
public class EmailBackupServiceimpl2 implements IEmailBackupService {

	@Autowired
	private IEmailRepository repo;

	@Value("${file.upload-dir}")
	private String saveDirectory;

	@Autowired
	private EmailService emailService;
	// @Override

	@Autowired
	private TechnologyRepository techrepo;

	public List<Email> downloadEmailAttachments(String host, String port, String userName, String password) {
		/*
		 * Properties properties = new Properties(); System.out.println(
		 * "------------------------------------------------------------"); // server
		 * setting properties.put("mail.pop3.host", host);
		 * properties.put("mail.pop3.port", port);
		 * 
		 * // SSL setting properties.setProperty("mail.pop3.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * properties.setProperty("mail.pop3.socketFactory.fallback", "false");
		 * properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
		 * 
		 * Session session = Session.getDefaultInstance(properties);
		 * 
		 */

		// imap properties
		Properties properties = new Properties();
		// String host = "imap.narveetech.com";
		// String port = "993";
		// String mailStoreType = "pop3";
		// server setting
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", port);

		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);
		List<Object[]> segregate = techrepo.gettechnologies();
		List<Email> listOfEntity = new ArrayList<>();
		Email entity = null;

		try {
			// connects to the message store
			Store store = session.getStore("imap");
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

					try {
						String result = detectingMail.substring(detectingMail.indexOf("@") + 1,
								detectingMail.indexOf("."));
						if (result.equalsIgnoreCase("gmail")) {
							entity.setCompany(detectingMail);
						} else {
							entity.setCompany(result);
						}

					} catch (StringIndexOutOfBoundsException e3) {
					}

				} else {
					entity.setFrom(frommail);
					try {
						String result = frommail.substring(frommail.indexOf("@") + 1, frommail.indexOf("."));
						if (result.contains("gmail")) {
							entity.setCompany(frommail);
						} else {
							entity.setCompany(result);
						}
					} catch (StringIndexOutOfBoundsException e4) {
					}

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
					if (finalTO != "" && finalTO != null && finalTO.length() != 0) {
						finalTO = finalTO.substring(0, finalTO.length() - 1);
					}
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
					if (finalCC != "" && finalCC != null && finalCC.length() != 0) {
						finalCC = finalCC.substring(0, finalCC.length() - 1);
					}

					entity.setCcmail(finalCC);
				} catch (NullPointerException e1) {
				}

				/************** gatting mail addresses closed *********/

				/****** getting subject of the mail ***************/
				String subject = messageRef.getSubject();
				/****** getting subject of the mail closed ***************/
				// System.out.println(subject);
				Date sentDate = messageRef.getSentDate();

				// ***** getting message content and attachments from mail box*****//
				String contentType = messageRef.getContentType();
				// entity.setAttachment(contentType);
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
						if (bodyPart.getContentType().toLowerCase().contains("text/plain")) {
							messageContent = messageContent + "\n" + bodyPart.getContent();
							// break; // without break same text appears twice in my tests
						} else if (bodyPart.isMimeType("text/html")) {
							String html = (String) bodyPart.getContent();
							messageContent = messageContent + "\n" + org.jsoup.Jsoup.parse(html).text();
						} else if (bodyPart.getContent() instanceof MimeMultipart) {
							messageContent = messageContent
									+ getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
						} else {
							messageContent = messageContent + bodyPart.getContent().toString();
						}
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							String fileName = part.getFileName();
							attachFiles += fileName + ", ";
							try {
								part.saveFile(saveDirectory + File.separator + fileName);
							} catch (FileNotFoundException e) {
							}

						}
					}

					if (attachFiles.length() > 1) {
						attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
					}
				} else {
					entity.setAttachment(attachFiles);
					String html = (String) messageRef.getContent();
					messageContent = org.jsoup.Jsoup.parse(html).text();
					// messageContent = messageRef.getContent().toString();// messageContent +
					// bodyPart.getContent().toString();
				}
				entity.setAttachment(attachFiles);
				entity.setSentdate(sentDate);

				for (Object[] result : segregate) {
					String tag = (String) result[1];
					if (subject != null) {
						if (subject.contains(tag)) {
							entity.setSubjectcategory(tag);
						} else {
							entity.setSubjectcategory("Others");
						}

					} else {
						entity.setSubjectcategory("Others");
					}
				}

				entity.setBody(messageContent);

				entity.setSubject(subject);
				listOfEntity.add(entity);
			}
			saveEntity(listOfEntity);
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
		return listOfEntity;
	}

	public void saveEntity(List<Email> email) {
//		System.out.println("========================");
//		System.out.println(email);
//		System.out.println("========================");
		List<Email> newemail = new ArrayList();
		for (ListIterator<Email> it = email.listIterator(); it.hasNext();) {
			Email value = it.next();
			if (value.getSubjectcategory().equals("Others")) {
				// send these records to other mails
				// emailService.mailsender(value);
				// it.remove();
			}
			List<Email> findBySubject = repo.findBySubject(value.getSubject());
			if (findBySubject == null || findBySubject.isEmpty()) {
				newemail.add(value);
			} else {
				it.remove();
			}
		}
		
		repo.saveAll(newemail);
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

	@Override
	public void deleteMessages(String host, String port, String userName, String password) {
		Properties properties = new Properties();
		// String host = "imap.narveetech.com";
		// String port = "993";
		// String mailStoreType = "pop3";
		// server setting
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", port);

		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);
		System.out.println("hello");
		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect(userName, password);

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_WRITE);

			// opens the trash folder
			Folder folderBin = store.getDefaultFolder().getFolder("INBOX.Drafts");
			folderBin.open(Folder.READ_WRITE);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();

			// Copy messages from inbox to Trash
			folderInbox.copyMessages(arrayMessages, folderBin);

			// arrayMessages = folderBin.getMessages();

			for (int i = 0; i < arrayMessages.length; i++) {
				Message message = arrayMessages[i];
				// String subject = message.getSubject();
				// if (subject.equals("RE: Regards office leave")) {
				System.out.println("===============");
				// message.setFlag(Flags.Flag.DELETED, true);
				// }
			}

			// expunges the folder to remove messages which are marked deleted
			boolean expunge = true;
			folderBin.close(expunge);
			folderInbox.close(expunge);
			// disconnect
			store.close();
		} catch (NoSuchProviderException ex) {

			System.out.println("No provider.");
			ex.printStackTrace();
		} catch (FolderNotFoundException ex) {

			System.out.println("Folder Not Found");
			ex.printStackTrace();
		} catch (MessagingException ex) {

			System.out.println("Could not connect to the message store.");
			ex.printStackTrace();
		}
		System.out.println("end up ");
	}

	@Override
	public Optional<Email> getmailbyid(long id) {
		return repo.findById(id);
	}

	@Override
	public List<Email> getAllEmails() {
		return repo.findAll(Sort.by("from").ascending()); // repo.findAll();
	}

	@Override
	public boolean deleteAllByIdInBatch(List<Long> ids) {
		repo.deletemailById(ids);
		return true;
	}
}

///////////////////////////////
/*
 * 
 * public void deleteMessages2(String host, String port, String userName, String
 * password, String subjectToDelete) { Properties properties = new Properties();
 * // server setting properties.put("mail.pop3.host", host);
 * properties.put("mail.pop3.port", port); // subjectToDelete = "arr"; // SSL
 * setting System.out.println("====================");
 * properties.setProperty("mail.pop3.socketFactory.class",
 * "javax.net.ssl.SSLSocketFactory");
 * properties.setProperty("mail.pop3.socketFactory.fallback", "false");
 * properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
 * Session session = Session.getDefaultInstance(properties); try { // connects
 * to the message store Store store = session.getStore("pop3");
 * store.connect(userName, password); // opens the inbox folder Folder
 * folderInbox = store.getFolder("INBOX"); folderInbox.open(Folder.READ_WRITE);
 * // opens the trash folder // Folder folderBin =
 * store.getDefaultFolder().getFolder("INBOX.Drafts"); //
 * folderBin.open(Folder.READ_WRITE);
 * 
 * // fetches new messages from server Message[] arrayMessages =
 * folderInbox.getMessages(); // folderInbox.copyMessages(arrayMessages,
 * folderBin); for (int i = 0; i < arrayMessages.length; i++) { Message message
 * = arrayMessages[i]; String subject = message.getSubject(); if
 * (subject.equals("RE: Regards office leave")) {
 * System.out.println("==============="); message.setFlag(Flags.Flag.DELETED,
 * true); } } // expunges the folder to remove messages which are marked deleted
 * boolean expunge = true; // folderBin.close(expunge);
 * folderInbox.close(expunge); // disconnect store.close(); } catch
 * (NoSuchProviderException ex) { System.out.println("No provider.");
 * ex.printStackTrace(); } catch (MessagingException ex) {
 * System.out.println("Could not connect to the message store.");
 * ex.printStackTrace(); } System.out.println("===================="); }
 * 
 * 
 * 
 * 
 * @Override public void saveBackup(String host, String port, String userName,
 * String password) { Properties properties = new Properties(); // server
 * setting properties.put("mail.pop3.host", host);
 * properties.put("mail.pop3.port", port);
 * 
 * // SSL setting properties.setProperty("mail.pop3.socketFactory.class",
 * "javax.net.ssl.SSLSocketFactory");
 * properties.setProperty("mail.pop3.socketFactory.fallback", "false");
 * properties.setProperty("mail.pop3.socketFactory.port", String.valueOf(port));
 * Session session = Session.getDefaultInstance(properties); List<Email>
 * listOfEntity = new ArrayList<>(); Email entity = null; try { // connects to
 * the message store Store store = session.getStore("pop3");
 * store.connect(userName, password);
 * 
 * // opens the INBOX folder Folder folderInbox = store.getFolder("INBOX");
 * folderInbox.open(Folder.READ_ONLY);// READ_ONLY READ_WRITE
 * 
 * // fetches new messages from server Message[] arrayMessages =
 * folderInbox.getMessages();
 * 
 * for (int i = 0; i < arrayMessages.length; i++) { entity = new Email();
 * Message message = arrayMessages[i];
 * 
 * // Getting Emailed_From Address[] fromAdd = message.getFrom(); String from =
 * fromAdd[0].toString(); if (from.contains("<")) { String value =
 * from.substring(from.indexOf("<") + 1, from.indexOf(">", from.indexOf(">")));
 * entity.setFrom(value); try { String result =
 * value.substring(value.indexOf("@") + 1, value.indexOf(".")); if
 * (result.equalsIgnoreCase("gmail")) { // e.setCompany(value);
 * entity.setCompany(value); //
 * System.out.println("=========================="); } else { //
 * e.setCompany(result); entity.setCompany(value); }
 * 
 * } catch (StringIndexOutOfBoundsException e3) { // e.setCompany(value); //
 * savemail.setCompany(value); //
 * System.out.println("String index out of bounds. String length: " ); } } else
 * { // e.setFrom(from); entity.setFrom(from); try { String result =
 * from.substring(from.indexOf("@") + 1, from.indexOf(".")); //
 * System.out.println(result+" 888 " + from); // e.setCompany(result); if
 * (result.contains("gmail")) { // e.setCompany(from); entity.setCompany(from);
 * } else { entity.setCompany(result); } // e.setCompany(result);
 * 
 * } catch (StringIndexOutOfBoundsException e4) { // e.setCompany(from); //
 * System.out.println("String index out of bounds. String length: " ); }
 * 
 * } // Getting Emailed_To Address[] to =
 * message.getRecipients(Message.RecipientType.TO); try { String to1 =
 * to[0].toString(); if (to1.contains("<")) { String value =
 * to1.substring(to1.indexOf("<") + 1, to1.indexOf(">", to1.indexOf(">"))); //
 * System.out.println("To=======>==== " + value); // e.setTo(value);
 * entity.setTomail(value); } else { // System.out.println("To=======> " + to1);
 * // e.setTo(to1); entity.setTomail(to1); } } catch (NullPointerException e) {
 * // TODO: handle exception }
 * 
 * // Getting Emailed_CC Address[] cc =
 * message.getRecipients(Message.RecipientType.CC); String finalCC = ""; try {
 * for (int j = 0; j < cc.length; j++) { String fromCC = cc[j].toString(); if
 * (fromCC.contains("<")) { String value = fromCC.substring(fromCC.indexOf("<")
 * + 1, fromCC.indexOf(">", fromCC.indexOf(">"))); value = value.concat(", ");
 * finalCC = finalCC.concat(value); } else { fromCC = fromCC.concat(",");
 * finalCC = finalCC.concat(fromCC); } } finalCC = finalCC.substring(0,
 * finalCC.length() - 1); entity.setCcmail(finalCC); } catch
 * (NullPointerException e1) { } String subject = message.getSubject();
 * 
 * if (subject != null) { if (subject.contains("Java")) { //
 * e.setSubjectcategory("Java Developer");
 * entity.setSubjectcategory("Java Developer"); } else if
 * (subject.contains("Password")) { // e.setSubjectcategory("Password");
 * entity.setSubjectcategory("Password"); } else if (subject.contains("Hello"))
 * { // e.setSubjectcategory("rHello"); entity.setSubjectcategory("Hello"); }
 * else { // e.setSubjectcategory("Others123");
 * entity.setSubjectcategory("Others123"); } } else { //
 * e.setSubjectcategory("Others=="); entity.setSubjectcategory("Others=="); }
 * 
 * // String sentDate = message.getSentDate().toString(); String contentType =
 * message.getContentType(); String messageContent = ""; // store attachment
 * file name, separated by comma String attachFiles = ""; if
 * (contentType.contains("multipart")) { // content may contain attachments
 * Multipart multiPart = (Multipart) message.getContent(); int numberOfParts =
 * multiPart.getCount(); for (int partCount = 0; partCount < numberOfParts;
 * partCount++) { MimeBodyPart part = (MimeBodyPart)
 * multiPart.getBodyPart(partCount);
 * 
 * if (part.isMimeType("text/plain")) { // System.out.println("plain text"); }
 * else if (part.isMimeType("multipart/*")) { MimeMultipart mimeMultipart =
 * (MimeMultipart) message.getContent(); messageContent =
 * getTextFromMimeMultipart(mimeMultipart); //
 * System.out.println("message content after else if=====>"+messageContent); }
 * else if (part.isMimeType("message/rfc822")) { //
 * System.out.println("message/rfc822"); } else { //
 * System.out.println("text/html content"); }
 * 
 * if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) { // this part
 * is attachment String fileName = part.getFileName(); attachFiles += fileName +
 * ", "; // part.saveFile(saveDirectory + File.separator + fileName); } else if
 * (part.isMimeType("multipart/*")) { MimeMultipart mimeMultipart =
 * (MimeMultipart) message.getContent(); // savemail.setData2(mimeMultipart);
 * messageContent = getTextFromMimeMultipart(mimeMultipart); //
 * System.out.println("message content after else if=====>"+messageContent); }
 * else { // this part may be the message content messageContent =
 * part.getContent().toString(); //
 * System.out.println("message content before else if====>"+messageContent); } }
 * if (attachFiles.length() > 1) { attachFiles = attachFiles.substring(0,
 * attachFiles.length() - 2); } } System.out.println(subject + " == " +
 * messageContent);
 * 
 * // e.setSubject(subject); entity.setSubject(subject);
 * entity.setCcmail(finalCC); // e.setCc(finalCC); // e.setBcc(ebcc); //
 * e.setBody2(messageContent); // e.setAttachment(attachFiles);
 * entity.setAttachment(attachFiles);
 * entity.setBody(Jsoup.parse(messageContent).wholeText());
 * 
 * // e.setBody(messageContent); // repo.save(e); listOfEntity.add(entity); //
 * message.setFlag(Flags.Flag.DELETED, true); //
 * arrayMessages[i].setFlag(Flags.Flag.DELETED, true); //
 * message.setFlag(Flags.Flag.DELETED, true); } saveEntity(listOfEntity); //
 * saveMail(listsaveem); // disconnect boolean expunge = true; //
 * folderInbox.close(expunge); store.close(); } catch (NoSuchProviderException
 * ex) { // System.out.println("No provider for pop3."); ex.printStackTrace(); }
 * catch (MessagingException ex) { //
 * System.out.println("Could not connect to the message store");
 * ex.printStackTrace(); } catch (IOException ex) { ex.printStackTrace(); }
 * 
 * }
 * 
 * @Override public void deleteMessages2(String host, String port, String
 * userName, String password, String subjectToDelete) { // TODO Auto-generated
 * method stub
 * 
 * }
 */

//////////////////////////////
