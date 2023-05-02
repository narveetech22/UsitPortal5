package com.narvee.usit.emailextractionTemp;

import java.util.Date;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SentDateTerm;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.narvee.usit.entity.EmailAttachment;
import com.narvee.usit.repository.IEmailAttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import java.text.SimpleDateFormat;

@Service
public class EmailExtractServiceImpl implements IEmailExtract {

	private static final Logger logger = LoggerFactory.getLogger(EmailExtractServiceImpl.class);

	@Autowired
	private IEmailExtractRepository repo;

	// autowired emailAttachmentRepository to store attachment names
	@Autowired
	private IEmailAttachmentRepository repository;

	@Value("${file.upload-dir}")
	private String saveDirectory;

	public static Date dateModfy(Date date, int hour, int subelements) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, subelements);
		cal.set(Calendar.SECOND, subelements);
		cal.set(Calendar.MILLISECOND, subelements);
		return cal.getTime();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String mailExtraction(String host, String port, String userName, String password, Date fromDate,
			Date toDate) {
		// Date lastInsertedDate = repo.maxDateFunction(userName);
		/*
		 * Properties properties = new Properties(); properties.put("mail.imap.host",
		 * host); properties.put("mail.imap.port", port);
		 * 
		 * properties.put("mail.imap.auth", "true");
		 * properties.put("mail.imap.starttls.enable", "true");
		 * properties.put("mail.imap.ssl.trust", "smtp.narveetech.com");
		 * 
		 * properties.setProperty("mail.imap.connectiontimeout", "5000");
		 * properties.setProperty("mail.imap.timeout", "5000");
		 * 
		 * logger.info(" mailExtraction in service Impl ");
		 * properties.setProperty("mail.imap.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory");
		 * properties.setProperty("mail.imap.socketFactory.fallback", "false");
		 * properties.setProperty("mail.imap.socketFactory.port", String.valueOf(port));
		 */
		Properties properties = new Properties();
		properties.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(properties);

		// Session session = Session.getDefaultInstance(properties);
		List<ExtractEmail> listOfEntity = new ArrayList<>();
		ExtractEmail entity = null;
		List<ExtractEmail> newemail = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		Date ToDate = dateModfy(toDate, 23, 59);
		Date FromDate = dateModfy(fromDate, 0, 0);
		logger.info("Started Try block ");
		try {
			logger.info("In Try block one");
			Store store = session.getStore("imaps");
			logger.info("In Try block two");
			// try {
			logger.info("before connection");
			// store.connect(userName, password);
			store.connect("imap.gmail.com", userName, password);
			logger.info("after connection");
			// } catch (AuthenticationFailedException e) {
			// logger.info(" Authentication error mailExtraction in service Impl ");
			// return "Authentication Error";
			// }
			logger.info("after connection try block");
			Folder folderInbox = store.getFolder("INBOX");
			logger.info("after get inbox try block");
			folderInbox.open(Folder.READ_ONLY);
			logger.info("after open inbox try block");

			Calendar cal2 = null;
			cal2 = Calendar.getInstance();
			Date minDate = new Date(cal.getTimeInMillis()); // get today date

			cal2.add(Calendar.DAY_OF_MONTH, 1); // add 1 day
			Date maxDate = new Date(cal2.getTimeInMillis()); // get tomorrow date
			// ReceivedDateTerm minDateTerm = new ReceivedDateTerm(ComparisonTerm.GE,
			// minDate);
			// ReceivedDateTerm maxDateTerm = new ReceivedDateTerm(ComparisonTerm.LE,
			// maxDate);
			// term = new AndTerm(term, minDateTerm); //concat the search terms
			// term = new AndTerm(term, maxDateTerm);
			SearchTerm search = new AndTerm(new SentDateTerm(ComparisonTerm.GE, fromDate),
					new SentDateTerm(ComparisonTerm.LE, toDate));

			Message arrayMessages[] = folderInbox.search(search);

			// Message[] arrayMessages = folderInbox.getMessages();
			for (Message messageRef : arrayMessages) {
				if (messageRef.getSentDate().after(FromDate) && messageRef.getSentDate().before(ToDate)) {
					int count = arrayMessages.length;
					entity = new ExtractEmail();
					Date receivedDate = messageRef.getReceivedDate();
					if (receivedDate.after(ToDate)) {
						logger.info("COmpleted between dates", receivedDate);
						return "Success";
					}
					if (receivedDate.after(FromDate) && receivedDate.before(ToDate)) {
						Address[] fromAdd = messageRef.getFrom();
						String frommail = fromAdd[0].toString();
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
								logger.info(" mailExtraction in service Impl StringIndexOutOfBoundsException");
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
								logger.info(" mailExtraction in service Impl StringIndexOutOfBoundsException");
							}
						}
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
						String subject = messageRef.getSubject();
						Date receivedDat = messageRef.getReceivedDate();
						Date sentDate = messageRef.getSentDate();
						entity.setReceiveddate(receivedDat);
						entity.setSentdate(sentDate);
						String contentType = messageRef.getContentType();
						String messageContent = "";
						String attachFiles = "";

						if (contentType.contains("multipart")) {
							// System.out.println(contentType+" ===Kiran "+subject);
							// removed code
							try {
								Multipart multiPart = (Multipart) messageRef.getContent();

								int numberOfParts = multiPart.getCount();
								for (int partCount = 0; partCount < numberOfParts; partCount++) {
									MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
									if (part.isMimeType("text/plain")) {
										// System.out.println("plain text " + subject);
									} else if (part.isMimeType("multipart/*")) {
										MimeMultipart mimeMultipart = (MimeMultipart) messageRef.getContent();
										messageContent = getTextFromMimeMultipart(mimeMultipart);
										entity.setBody(messageContent);
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
										part.saveFile(saveDirectory + File.separator + fileName);
										// Get the attachments and save them to the database
										List<EmailAttachment> attachments = new ArrayList<>();
										EmailAttachment email = new EmailAttachment();
										email.setFileName(fileName);
										attachments.add(email);
										for (EmailAttachment attachment : attachments) {
											attachment.setExtractEmail(entity);
											repository.save(email);
										}

									} else if (part.isMimeType("multipart/*")) {
										MimeMultipart mimeMultipart = (MimeMultipart) messageRef.getContent();
										// savemail.setData2(mimeMultipart);
										messageContent = getTextFromMimeMultipart(mimeMultipart);
										entity.setBody(messageContent);
										// System.out.println("message content after else if=====>"+messageContent);
									} else {
										// this part may be the message content
										messageContent = part.getContent().toString();
										entity.setBody(messageContent);
										// System.out.println("message content before else if====>"+messageContent);
									}
								}
								if (attachFiles.length() > 1) {
									attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
							// from here

						}
						if (contentType.contains("TEXT/HTML")) {
							// System.out.println(contentType+" ===Kiran 22 "+subject);
							try {
								messageContent = (String) messageRef.getContent();
								entity.setBody(messageContent);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						entity.setSubject(subject);
						// System.out.println("==========================");
						// System.out.println(Jsoup.parse(messageContent).wholeText());
						// System.out.println("==========================");
						// entity.setBody(messageContent);
						// entity.setBody(Jsoup.parse(messageContent).wholeText());
						// entity.setAttachment(attachFiles);
						// e.setAttachment(attachFiles);
						listOfEntity.add(entity);
						logger.info("In mail Loop");
						// logger.info(" mailExtraction in service Impl Logic in else before
						// removeDuplicate()");
						// newemail = removeDuplicates(listOfEntity);
						// logger.info(" mailExtraction in service Impl Logic in else after
						// removeDuplicate()");
						logger.info("Calling save method");
						saveEntity(listOfEntity);
						logger.info("End save method");
						// logger.info(" mailExtraction in service Impl Logic in else after save
						// complete");
					} // if for date check
				}
			}

			logger.info(" mailExtraction in service Impl ");
			folderInbox.close(false);
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for pop3.");
			ex.printStackTrace();
			return "error1";
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
			return "error2";
		}
		return "Success";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveEntity(List<ExtractEmail> email) {
		logger.info(" saveEntity in service Impl ");
		List<ExtractEmail> newemail = new ArrayList();
		for (ListIterator<ExtractEmail> it = email.listIterator(); it.hasNext();) {
			ExtractEmail value = it.next();
			List<ExtractEmail> findBySubject = repo.findBySubject(value.getSubject()); // repo.findByFrommail(value.getFrommail());
			if (findBySubject == null || findBySubject.isEmpty()) {
				newemail.add(value);
			} else {
				it.remove();
			}
		}

		// System.out.println("==========================================");
		// System.out.println(email);
		// System.out.println("==========================================");
		repo.saveAllAndFlush(newemail);
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
				result = result + "\n" + html;
				// result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

	// method implementation of findAllMailDataWithAttachment
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<ExtractEmail> findAllMailDataWithAttachment() {
		logger.info(" findAllMailDataWithAttachment ");
		/*
		 * List<Object[]> data = null;//repo.findAllEmailsWithAttachments();
		 * List<ExtractEmail> result = new ArrayList<>(); for (Object[] record : data) {
		 * ExtractEmail email = (ExtractEmail) record[0]; EmailAttachment attachment =
		 * (EmailAttachment) record[1]; email.getAttachments().add(attachment); //adds
		 * the ExtractEmail object with its attachments to the result list.
		 * result.add(email); }
		 * 
		 * return result; }
		 */
		return repo.findAll();
	}

	@Override
	public Optional<ExtractEmail> getmailbyid(long id) {
		return repo.findById(id);
	}

}
