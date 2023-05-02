package com.narvee.usit.emailextractionTemp;
/*
import java.util.Date;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import java.text.SimpleDateFormat;


@Service
@Transactional
public class EmailExtractServiceImplwithList implements IEmailExtract {

	private static final Logger logger = LoggerFactory.getLogger(EmailExtractServiceImplwithList.class);

	@Autowired
	private IEmailExtractRepository repo;
	private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");

	public String mailExtraction(String host, String port, String userName, String password) {
		// imap properties
		Date dd = repo.maxDateFunction();
		System.out.println("============" + dd);
		Properties properties = new Properties();
		properties.put("mail.imap.host", host);
		properties.put("mail.imap.port", port);
		logger.info(" mailExtraction in service Impl ");
		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));

		Session session = Session.getDefaultInstance(properties);
		List<Object[]> segregate = null;// techrepo.gettechnologies();
		List<ExtractEmail> listOfEntity = new ArrayList<>();
		ExtractEmail entity = null;

		try {
			// connects to the message store
			Store store = session.getStore("imap");
			try {
				store.connect(userName, password);
			} catch (AuthenticationFailedException e) {
				return "Authentication Error";
			}

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] arrayMessages = folderInbox.getMessages();
			for (int i = 0; i < arrayMessages.length; i++) {
				entity = new ExtractEmail();
				Message messageRef = arrayMessages[i];

				// System.out.println(" maeesage " + i);
				// gatting mail addresses closed //
				// Getting Emailed_From
				Address[] fromAdd = messageRef.getFrom();
				String frommail = fromAdd[0].toString();
				// taking from addres from inbox mail logic to get only mail addres
				// logic to get only from address
				//
				// if frommail contain < symabol removing them and get only mail address
				if (frommail.contains("<")) {
					String detectingMail = frommail.substring(frommail.indexOf("<") + 1,
							frommail.indexOf(">", frommail.indexOf(">")));
					entity.setFrommail(detectingMail);

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
					entity.setFrommail(frommail);
					try {
						String result = frommail.substring(frommail.indexOf("@") + 1, frommail.indexOf("."));
						if (result.contains("gmail")) {
							entity.setCompany(frommail);
						} else {
							entity.setCompany(result);
						}
					} catch (StringIndexOutOfBoundsException e4) {
						// return "error";
					}
				}
				// logic to get only from address
				//

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
					// return "error123";
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
					// return "error22222";
				}

				// gatting mail addresses closed //

				// getting subject of the mail //
				String subject = messageRef.getSubject();
				// getting subject of the mail closed //
				// System.out.println(subject);
				Date recievedDate = messageRef.getReceivedDate();
				Date sentDate = messageRef.getSentDate();
				DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
				String yy = formatter2.format(sentDate);
				System.out.println(yy + " kiran " + sentDate);
				// messageRef.getReceivedDate();
				entity.setRecieveddate(recievedDate);
				entity.setSentdate(sentDate);
				// getting message content and attachments from mail box//
				String contentType = messageRef.getContentType();
				// entity.setAttachment(contentType);
				String messageContent = "";
				// store attachment file name, separated by comma
				entity.setSubject(subject);
				// System.out.println(sentDate);
				if (i % 5 == 0) {
					listOfEntity.clear();
					continue;
				} else {
					listOfEntity.add(entity);
					if (listOfEntity.size() == 4) {
						List<ExtractEmail> newemail = removeDuplicates(listOfEntity);
						saveEntity(newemail);
						// return "Success";
					}
				}

			}
			// saveEntity(listOfEntity);
			logger.info(" mailExtraction in service Impl ");

			logger.info(" mailExtraction in service Impl ");
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
			return "error";
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
			return "error";
		}
		return "Success";
	}

	public void saveEntity(List<ExtractEmail> email) {
		// System.out.println("ooooooooooooooooo");
		logger.info(" saveEntity in service Impl ");
		List<ExtractEmail> newemail = new ArrayList();
		for (ListIterator<ExtractEmail> it = email.listIterator(); it.hasNext();) {
			ExtractEmail value = it.next();
			// System.out.println(" 000000000 " + value);
			List<ExtractEmail> findBySubject = repo.findByFrommail(value.getFrommail());
			if (findBySubject == null || findBySubject.isEmpty()) {
				newemail.add(value);
			} else {
				it.remove();
			}
		}
		repo.saveAll(email);
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
		return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static <ExtractEmail> List<ExtractEmail> removeDuplicates(List<ExtractEmail> list) {
		List<ExtractEmail> distinctElements = list.stream()
				.filter(distinctByKey(cust -> ((com.narvee.usit.emailextractionTemp.ExtractEmail) cust).getFrommail()))
				.collect(Collectors.toList());
		// return the new list
		return distinctElements;
	}

	public List<ListExtractMailDTO> listAll() {
		return repo.listAll();
	}

}

*/
