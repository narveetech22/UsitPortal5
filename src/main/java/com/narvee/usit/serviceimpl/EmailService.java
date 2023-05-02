package com.narvee.usit.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.MailcapCommandMap;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.H1bApplicantDTO;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.H1BApplicants;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.entity.Submissions;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.IRecInterviewHelper;
import com.narvee.usit.repository.IConsultantRepository;
import com.narvee.usit.repository.IQualificationRepository;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.repository.IVendorRepository;
import com.narvee.usit.repository.IVisaRepository;
import com.narvee.usit.repository.InterviewRepository;
import javax.mail.util.ByteArrayDataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class EmailService {
	// commented for mail error
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private IUsersRepository userRepo;

	@Autowired
	private IConsultantRepository consultantrepo;

	@Autowired
	private InterviewRepository interviewRepo;
	
	@Autowired
	private  IVendorRepository ivendorRepo;

	@Value("${requirement-ccmail}")
	private String[] requirementccmails;

	// @Value("${emailusername}")
	private String mailusername = "saikiran@narveetech.com";

	// @Value("${emailadmin}")
	// private String adminmail = "saikiran@narveetech.com";;

	public void resetlinkmail(String username, String recipientEmail, String link)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(mailusername, "Password Reset Link");
		message.addRecipient(RecipientType.CC, new InternetAddress(mailusername));
		helper.setTo(recipientEmail);
		String subject = "Here's the link to reset your password";
		String content = "<p>Hi " + username + ",</p>  <p> Your request for change password</p>"
				+ "<p>Please click on below link to reset your password "
				+ "<br><a href='http://69.216.19.140/usitats/#/change-password'>Click here to Reset</a> ";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	// sending regarding registration success message
	public void employeeRegistarionMail(String email, String username)
			throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: employeeRegistarionMail");
		// kiran commented
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message); //
//		 message.addRecipient(RecipientType.CC, new InternetAddress(mailusername));
//		 helper.setFrom(mailusername,"Registration successful with Narvee USIT Protal"); 
		message.addRecipient(RecipientType.BCC, new InternetAddress("kiranjava010@gmail.com"));
		//helper.setFrom("s.narvee@gmail.com", "Registration successful with Narvee USIT Protal");
		helper.setTo(email);
		String subject = "Registartion Successful with Narvee USIT Portal";
		String content = "<p>Dear " + username + ",</p>  <p> Your registration successful with Narvee USIT Portal</p>"
				+ "<p>Please login with UserName : " + email + ", Password : Narvee123$.</p>"
				+ "<br><a href='http://69.216.19.140/usitats'>Click here to login</a> ";
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}

	@Autowired
	private IVisaRepository visarepo;

	@Autowired
	private IQualificationRepository qualirepo;
	
	// sending mail for consutant entry
	public void consultantEntry(ConsultantInfo consultant) throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: employeeRegistarionMail");
		String name = consultant.getConsultantname();
		String number = consultant.getContactnumber();
		String email = consultant.getConsultantemail();
		String currentLocation = consultant.getCurrentlocation();
		String linkedin = consultant.getLinkedin();
		long visaid = consultant.getVisa().getVid();
		String visa = visarepo.findById(visaid).get().getVisastatus();
		long qid = consultant.getQualification().getId();
		String education = qualirepo.findById(qid).get().getName();
		String technology = consultant.getPosition();
		String university = consultant.getUniversity();
		String yop = consultant.getYop();
		String status = consultant.getStatus();
		StringBuilder stringBuilder = new StringBuilder();
		Users addebyInfo = userRepo.findById(1L).get();
		String frommail = addebyInfo.getEmail();
		String username = addebyInfo.getPseudoname();
		String rolename = addebyInfo.getRole().getRolename();
		//String usernumber = addebyInfo.getPersonalcontactnumber().getInternationalNumber();
		String designation = addebyInfo.getDesignation();//addebyInfo.getRole().getRolename();
		String subject = visa + " candidate has been added in Narvee USIT Portal ("+status+")";
		String heading = "I have been added "+visa+" consultant in  Narvee USIT Portal";
		if(consultant.getUpdatedby()== null) {
			 addebyInfo = userRepo.findById(consultant.getAddedby().getUserid()).get();
			 frommail = addebyInfo.getEmail();
			 username = addebyInfo.getPseudoname();
			 rolename = addebyInfo.getRole().getRolename();
			 //usernumber = addebyInfo.getPersonalcontactnumber().getInternationalNumber();
			 designation = addebyInfo.getDesignation();//addebyInfo.getRole().getRolename();
			 subject = visa + " candidate has been added in Narvee USIT Portal ("+status+")";
			 heading = "I have been added "+visa+" consultant in  Narvee USIT Portal";
		}
		else {
			addebyInfo = userRepo.findById(consultant.getUpdatedby().getUserid()).get();
			 frommail = addebyInfo.getEmail();
			 username = addebyInfo.getPseudoname();
			 rolename = addebyInfo.getRole().getRolename();
			 //usernumber = addebyInfo.getPersonalcontactnumber().getInternationalNumber();
			 designation =  addebyInfo.getDesignation(); //   addebyInfo.getRole().getRolename();
			 subject = visa + " candidate has been modified by "+username;
			 heading = "I have modified the consultant please have a look";
		}
		if(linkedin!=null){
			linkedin = "https://www.linkedin.com/in/"+linkedin;
		}
		stringBuilder.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
		        .append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>")
				.append(heading).append("<table class='styled-table' width='100%'>")
				.append("<thead>").append("<tr>").append("<th colspan='2'>Consultant Details</th>").append("</tr>")
				.append("</thead>").append("<tbody>").append("<tr >")
				.append("<td width='30%'><b>Legal name: <br>(First Middle Last name)</b> </td>")
				.append("<td>" + name + "</td>").append("</tr>").append("<tr><td><b>Contact Number :</b> </td>")
				.append("<td>" + number + "</td>").append("</tr>").append("<tr><td><b>Email :</b> </td>")
				.append("<td>" + email + "</td>").append("</tr>")
				.append("<tr><td><b>Current location (City and State) :</b> </td>")
				.append("<td>" + currentLocation + "</td>").append("</tr>").append("<tr><td><b>LinkedIn ID:</td>")
				.append("<td>"+linkedin+ "</td></tr>").append("<tr><td><b>Work Authorization</td>")
				.append("<td>" + visa + "</td></tr>").append("<tr><td><b>Education Details /University / Year:</td>")
				.append("<td>" + education + "/" + university + "/" + yop + "</td></tr>")
				.append("<tr><td><b>Technology :</td>").append("<td>" + technology + "</td></tr>")
				.append("<tr><td><b>Status :</td>").append("<td>" + status + "</td></tr>")
				.append("</tbody>")
				.append("</table>").append("</body>").append("</html>")
		        .append("<br>")
		        .append("<h3>Yours faithfully,</h3>")
		        .append("<strong>"+username+",</strong><br>")
		        .append("<strong>"+designation+",</strong><br>")
		        .append("<strong>Narvee Technologies,</strong><br>")
		        .append("<strong style='color:maroon'>\r\n"
		        		+ "		404, 4th Floor, Aditya Trade Center, Ameerpet,\r\n"
		        		+ "		<br>\r\n"
		        		+ "		Hyderabad, Telangana 500038, India\r\n"
		        		+ "		</strong>\r\n"
		        		+ "		<br>")
		        .append("Email : <strong>"+frommail+"</strong><br>");
		        //.append("Phone : <strong>"+usernumber+"</strong>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message); //
		String[] allusers = {};
		String shortMessage = "Profile added in Pre-sales";
		
		if (consultant.getConsultantflg().equalsIgnoreCase("presales") || consultant.getConsultantflg().equalsIgnoreCase("sales")) {
			if(rolename.equalsIgnoreCase("Employee")){
				long  tlid = addebyInfo.getTeamlead();
				long mangerid = addebyInfo.getManager();
				allusers = userRepo.executivemail(tlid,mangerid);
			}
			else if (rolename.equalsIgnoreCase("Team Lead")) {
				long mangerid =addebyInfo.getManager();
				allusers = userRepo.teamleadmail(mangerid);
			}
			else if (designation.equalsIgnoreCase("Manager")) {
				long mangerid =addebyInfo.getUserid();
				allusers = userRepo.teamleadmail(mangerid);
			}
			else  {
				allusers = new String[] {"kiranjava010@gmail.com"}; //userRepo.findsalesrecruiterMail();
			}
		} 
		if (allusers.length != 0) {
			helper.setBcc(allusers);
		}
		helper.setFrom(frommail, shortMessage);
		helper.setSubject(subject);
		helper.setText(stringBuilder.toString(), true);
		mailSender.send(message);
		logger.info("!!! inside class: EmailService, !! method: submission mailing send and update");
	}

	final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");	
	public void pendingConsultantInfo(List<ConsultantDTO> entity,LocalDateTime Currentdate) {
		String formattedString = Currentdate.format(CUSTOM_FORMATTER);
		
		StringBuilder heading = new StringBuilder();
		heading.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>")
				.append("<p>These are the below consultants newly added into portal please review and approve them</p>")
				.append("<table class='styled-table' width='100%'>")
				.append("<thead>")
				.append("<tr>")
				.append("<th>Created Date</th><th>(First Middle Last name)</th><th>Contact Number</th><th>Email</th><th>Current location (City and State) </th><th>LinkedIn ID</th>"
						+ "<th>Work Authorization</th><th>Education Details /University / Year</th><th>Technology</th><th>Status</th></tr>")
				.append("</thead>");
		entity.forEach(consultant->{
			String name = consultant.getConsultantname();
			String number = consultant.getContactnumber();
			String email = consultant.getConsultantemail();
			String currentLocation = consultant.getCurrentlocation();
			String linkedin = consultant.getLinkedin();
			String visa = consultant.getVisa_status();
			String education = consultant.getQualification();
			String technology = consultant.getTechnologyarea();
			String university = consultant.getUniversity();
			String yop = consultant.getYop();
			LocalDateTime createddate = consultant.getCreateddate();
			String Staringcreateddate = createddate.format(CUSTOM_FORMATTER);
			String status = consultant.getStatus();
			heading.append("<tbody><tr><td style='width:13%'>"+Staringcreateddate+"</td><td>"+name+"</td>"
					+ "<td>"+number+"</td>"
					+ "<td>"+email+"</td>"
					+ "<td>"+currentLocation+"</td>"
					+ "<td>"+linkedin+"</td>"
					+ "<td>"+visa+"</td>"
					+ "<td>"+education+"/"+university+"/"+yop+"</td>"
					+ "<td>"+technology+"</td>"
					+ "<td>"+status+"</td></tr></tbody>");
		});
		heading.append("</table>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("abhidatasol@gmail.com", "Pending consultant list");
			helper.setTo("kiranjava010@gmail.com");
			//helper.setCc(new String[] {"hr@narveetech.com"});
			String subject = "Here's the pending consultant details "+formattedString;
			helper.setSubject(subject);
			helper.setText(heading.toString(), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
	public boolean resetpasswordmail(String email, String password, String fullname)
			throws MessagingException, UnsupportedEncodingException {
		/*
		 * kiran commented MimeMessage message = mailSender.createMimeMessage();
		 * MimeMessageHelper helper = new MimeMessageHelper(message);
		 * message.addRecipient(RecipientType.BCC, new InternetAddress(mailusername));
		 * helper.setFrom(mailusername, "Password updated for Narvee USIT Protal");
		 * helper.setTo(mailusername); String subject = fullname +
		 * " has  updated his  Narvee USIT Portal password"; String content =
		 * "<p>Dear Admin ,</p>  <p> " + fullname + " updated his password</p>" +
		 * "<p>Here is the new Password  : " + password + "</p>";
		 * helper.setSubject(subject); helper.setText(content, true);
		 * mailSender.send(message); return true;
		 */
		return true;
	}

	// requirements entry and update email
	public void sendrequirementMail(String[] to, String raisebyEmail, String subject, String body) {
		/*
		 * kiran commented MimeMessage mimeMessage = mailSender.createMimeMessage();
		 * MimeMessageHelper helper = new MimeMessageHelper(mimeMessage); try {
		 * helper.setFrom(raisebyEmail, "Here is the new Requirement");
		 * helper.setBcc(to); // helper.setCc("saikiran@narveetech.com "); //
		 * helper.setCc(new String[] { "saikiran@narveetech.com ", //
		 * "kiranjava010@gmail.com " }); //helper.setCc(requirementccmails);
		 * helper.setSubject(subject); StringBuilder stringBuilder = new
		 * StringBuilder();
		 * stringBuilder.append("<html>").append("<body>").append("Dear Users, <br>").
		 * append("<p>").append(body)
		 * .append("</p>").append("</body>").append("<html>");
		 * helper.setText(stringBuilder.toString(), true); } catch
		 * (UnsupportedEncodingException | MessagingException e) { e.printStackTrace();
		 * } mailSender.send(mimeMessage);
		 */
		logger.info("!!! inside class: EmailService, !! method: requirements mailing send and update");
	}

	// submisiion entry mail update
	public void sendsubmissionEmail(Submissions submission) throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: submission mailing send and update");
		/* kiran commented */
		// System.out.println(submission);
		String from = "abhidatasol@gmail.com";
		// Users submittedBy = new Users();
		String username="Admin";
		String designation="Admin";
		String subject = "Here's  the new submission status";
		long userid = 0;
		if (submission.getSubmissionid() == null) {
			Users submittedBy = userRepo.findById(submission.getUser().getUserid()).get();
			from = submittedBy.getEmail();
			username = submittedBy.getPseudoname();
			designation = submittedBy.getDesignation();
			userid = submittedBy.getUserid();
			subject = "Here's the submission status";
		} else {
			Users submittedBy = userRepo.findById(submission.getUpdatedby()).get();
			from = submittedBy.getEmail();
			username = submittedBy.getPseudoname();
			designation = submittedBy.getDesignation();
			subject = "Here's the submission follow up status";
		}
		// String from = submittedBy.getEmail();
		String[] allusers = {};

		if (submission.getFlg().equalsIgnoreCase("sales")) {
			//allusers = userRepo.findsalesrecruiterMail();
			// userRepo.findsalesrecruiterMail();

		} else {
			allusers = userRepo.findrecruiterMail();
		}
		ConsultantInfo consultant = consultantrepo.findById(submission.getConsultant().getConsultantid()).get();
		String name = consultant.getConsultantname();
		String number = consultant.getContactnumber();
		String email = consultant.getConsultantemail();
		String currentLocation = consultant.getCurrentlocation();
		String linkedin = consultant.getLinkedin();
		String visa = consultant.getVisa().getVisastatus();
		String ppnumber = consultant.getPassportnumber();
		String education = consultant.getQualification().getName()+"/"+consultant.getUniversity() + "  (" + consultant.getYop() + ")";
		String projectjoining = consultant.getProjectavailabity();
		String techskill = consultant.getTechnology().getTechnologyarea();
		String client = submission.getEndclient();
		String rate = submission.getSubmissionrate();
		String location = submission.getProjectlocation();
		
		VendorDetails vms = ivendorRepo.findById(submission.getVendor().getVmsid()).get();
		String company = vms.getCompany();
		String cmptype = vms.getCompanytype();
		String vendortype = vms.getVendortype();
		// submission.getRequirement().getLocation();
		String ratetype = submission.getRatetype();
		// String relocation = consultant.
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Dear Team,<br>").append("I have been submitted ")
				.append(name + "(" + techskill + ")").append(" for " + client).append(" at " + location)
				.append(", " + rate + "$/per Hr").append(" On " + ratetype).append("<br>")
				.append("<table class='styled-table' width='100%'>").append("<thead>").append("<tr>")
				.append("<th colspan='2'>Submission Details</th>").append("</tr>").append("</thead>").append("<tbody>")
				.append("<tr >").append("<td width='30%'><b>Legal name: <br>(First Middle Last name)</b> </td>")
				.append("<td>" + name + "</td>").append("</tr>").append("<tr><td><b>Contact Number :</b> </td>")
				.append("<td>" + number + "</td>").append("</tr>").append("<tr><td><b>Email :</b> </td>")
				.append("<td>" + email + "</td>").append("</tr>")
				.append("<tr><td><b>Current location (City and State) :</b> </td>")
				.append("<td>" + currentLocation + "</td>").append("</tr>").append("<tr><td><b>LinkedIn ID:</td>")
				.append("<td>" + linkedin + "</td></tr>").append("<tr><td><b>Work Authorization with Validity:</td>")
				.append("<td>" + visa + "</td></tr>").append("<tr><td><b>Education Details /University / Year:</td>")
				.append("<td>" + education + "</td></tr>").append("<tr><td><b>Availability to join the project:</td>")
				.append("<td>" + projectjoining + "</td></tr>")
				.append("<tr><td><b>Client :</td>")
				.append("<td>" + client + "</td></tr>")
				.append("<tr><td><b>Project Location :</td>")
				.append("<td>" + location + "</td></tr>")
				.append("<tr><td><b>Submission Rate ($ Per Hour):</td>")
				.append("<td>" + rate + "</td></tr>")
				.append("<tr><td><b>Contract Type:</td>")
				.append("<td>" + ratetype + "</td></tr>")
				//.append("<tr><td colspan='2'><b>Vendor Details</td></tr>")
				.append("<tr><td ><b>Vendor</td>")
				.append("<td>" + company + "</td></tr>")
				//.append("<tr><td><b>Vendor Type:</td>")
				//.append("<td>" + vendortype + "</td></tr>")
				.append("</tbody>").append("</table>").append("</body>")
				.append("</html>")
		        .append("<br>")
		        .append("<h3>Yours faithfully,</h3>")
		        .append("<strong>"+username+",</strong><br>")
		        .append("<strong>"+designation+",</strong><br>")
		        .append("<strong>Narvee Technologies,</strong><br>")
		        .append("<strong style='color:maroon'>\r\n"
	        		+ "		404, 4th Floor, Aditya Trade Center, Ameerpet,\r\n"
	        		+ "		<br>\r\n"
	        		+ "		Hyderabad, Telangana 500038, India\r\n"
	        		+ "		</strong>\r\n"
	        		+ "		<br>")
	        .append("Email : <strong>"+from+"</strong><br>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			if (allusers.length != 0) {
				//helper.setBcc(allusers);
				// helper.setCc(allusers);
			}
			// helper.setCc();
			helper.setCc(new
			String[]{"kiranjava010@gmail.com","abhidatasol@gmail.com"});
			helper.setFrom(from, "Submission followUp mail");
			helper.setSubject(subject);
			helper.setText(stringBuilder.toString(), true);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
		logger.info("!!! inside class: EmailService, !! method: submission mailing send and update");
	}

	// define somewhere the icalendar date format bsaikiran612@gmail.com
	private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");

	// interview entry mail
	public void interviewFollowupmail(Interview interviews) throws MessagingException, UnsupportedEncodingException {
		logger.info("!!! inside class: EmailService, !! method: interviewFollowupmail mailing send and update");
		/*
		 * kiran commented MimeMessage mimeMessage = mailSender.createMimeMessage();
		 * MimeMessageHelper helper = new MimeMessageHelper(mimeMessage); String from =
		 * "abhidatasol@gmail.com"; String subject = "Here's the submission status";
		 * String topic = ""; IRecInterviewHelper ent =
		 * interviewRepo.getinfo(interviews.getIntrid()); LocalDateTime intrdate =
		 * ent.getInterview_date(); Calendar calendar = Calendar.getInstance();
		 * calendar.clear(); calendar.set(intrdate.getYear(), intrdate.getMonthValue() -
		 * 1, intrdate.getDayOfMonth(), intrdate.getHour(), intrdate.getMinute(),
		 * intrdate.getSecond()); Date intdate = calendar.getTime();
		 * 
		 * if (interviews.getUpdatedby() == 0) { Users user =
		 * userRepo.findById(interviews.getUsers().getUserid()).get(); from =
		 * user.getEmail(); subject = "Invitation : " + ent.getJobtitle() + " - " +
		 * ent.getName() + " - " + ent.getRound() + " - " + intdate; topic =
		 * ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() +
		 * " round "; } else { Users updatedby =
		 * userRepo.findById(interviews.getUpdatedby()).get(); from =
		 * updatedby.getEmail(); subject = "Invitation : " + ent.getJobtitle() + " - " +
		 * ent.getName() + " - " + ent.getRound() + " - " + intdate; topic =
		 * ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() +
		 * " round "; ; } // String[] allusers = userRepo.findrecruiterMail(); // String
		 * consultantmail = ent.getEmail(); // allusers[0] = consultantmail;
		 * 
		 * // String from = submittedBy.getEmail(); String[] allusers = {};
		 * 
		 * if (interviews.getFlg().equalsIgnoreCase("sales")) { allusers =
		 * userRepo.findsalesrecruiterMail();
		 * 
		 * } else { allusers = userRepo.findrecruiterMail(); }
		 * 
		 * String name = ent.getName(); boolean flg = false;
		 * 
		 * StringBuilder stringBuilder = new StringBuilder(); if
		 * (interviews.getInterviewstatus().equals("Schedule")) { if
		 * (interviews.getRound().equals("First")) { flg = true; subject =
		 * "Invitation : for First round of interview - " + ent.getJobtitle() + " - " +
		 * ent.getName() + " - " + ent.getRound() + " - " + intdate;
		 * 
		 * topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() +
		 * " round "; ; // stringBuilder.append("<p>Dear " + name + ",<br>")
		 * stringBuilder.append("<p>Dear Team ,<br>") .append(" Interview has been " +
		 * ent.getInterview_status() + " for " + ent.getRound() + " round  at " +
		 * intdate + " " + ent.getTimezone()) .append(" for " + ent.getJobtitle() +
		 * " position ,") .append(" Mode of interview " + ent.getMode() + "<br>")
		 * .append("Requirement Number :" + ent.getReqnumber()).append("<br>")
		 * .append("Vendor / Implementation Partner :" +
		 * ent.getVendor()).append("<br>"); } else if
		 * (interviews.getRound().equals("Second")) { flg = true; subject =
		 * "Invitation : for Second round of interview - " + ent.getJobtitle() + " - " +
		 * ent.getName() + " - " + ent.getRound() + " - " + intdate; topic =
		 * ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() +
		 * " round "; ; stringBuilder.append("<p>Dear " + name + ",<br>") .append(name +
		 * " Profile has been selected for " + ent.getRound() +
		 * " round at and interview Schedule at " + intdate + " " + ent.getTimezone())
		 * .append(" for " + ent.getJobtitle() + " position ,")
		 * .append(" Mode of interview " + ent.getMode() + "<br>")
		 * .append("Requirement Number :" + ent.getReqnumber()).append("<br>")
		 * .append("Vendor / Implementation Partner :" +
		 * ent.getVendor()).append("<br>"); } else if
		 * (interviews.getRound().equals("Third")) { flg = true; topic =
		 * ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() +
		 * " round "; ; subject = "Invitation : for third round of interview " +
		 * ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() + " - " +
		 * intdate; stringBuilder.append("<p>Dear " + name + ",<br>") .append(name +
		 * " Profile has been selected for " + ent.getRound() +
		 * " round and interview Schedule at " + intdate + " " + ent.getTimezone())
		 * .append(" for " + ent.getJobtitle() + " position ,")
		 * .append(" Mode of interview " + ent.getMode() + "<br>")
		 * .append("Requirement Number :" + ent.getReqnumber()).append("<br>")
		 * .append("Vendor / Implementation Partner :" +
		 * ent.getVendor()).append("<br>"); }
		 * 
		 * } else { flg = false; subject = "interview follow up email : " +
		 * ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getReqnumber();
		 * 
		 * topic = ent.getJobtitle() + " - " + ent.getName() + " - " + ent.getRound() +
		 * " round "; ; stringBuilder.append("<p>Dear Team ,<br>").append(name +
		 * " profile has been " + ent.getInterview_status()) .append(" for " +
		 * ent.getJobtitle() + " position for ") .append("Requirement Number :" +
		 * ent.getReqnumber()).append(",") .append("Vendor / Implementation Partner :" +
		 * ent.getVendor()).append("<br>");
		 * 
		 * }
		 * 
		 * // register the handling of text/calendar mime type MailcapCommandMap mailcap
		 * = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap(); mailcap.
		 * addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain"
		 * ); Multipart multipart = new MimeMultipart("alternative");
		 * 
		 * // part 1, html text // BodyPart messageBodyPart; MimeBodyPart
		 * descriptionPart = new MimeBodyPart(); String content =
		 * stringBuilder.toString(); descriptionPart.setContent(content,
		 * "text/html; charset=utf-8"); BodyPart messageBodyPart = descriptionPart;
		 * multipart.addBodyPart(messageBodyPart);
		 * 
		 * // Add part two, the calendar // BodyPart calendarPart; if (flg) { try {
		 * BodyPart calendarPart = buildCalendarPart(ent.getInterview_date(), topic,
		 * content); multipart.addBodyPart(calendarPart); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * try { if (allusers.length != 0) { helper.setBcc(allusers); } //
		 * consultantmail // helper.setTo(consultantmail); //
		 * helper.setCc(requirementccmails); helper.setFrom(from, "Interview " +
		 * ent.getInterview_status() + " mail"); helper.setSubject(subject); //
		 * helper.setText(stringBuilder.toString(), true);
		 * mimeMessage.setContent(multipart); } catch (UnsupportedEncodingException |
		 * MessagingException e) { e.printStackTrace(); } mailSender.send(mimeMessage);
		 */
		logger.info(
				"!!! inside class: EmailService, !! method: interviewFollowupmail mailing send and update after sent");
	}

	private BodyPart buildCalendarPart(LocalDateTime dt, String subject, String body) throws Exception {
		BodyPart calendarPart = new MimeBodyPart();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(dt.getYear(), dt.getMonthValue() - 1, dt.getDayOfMonth(), dt.getHour(), dt.getMinute(),
				dt.getSecond());
		Date dd = calendar.getTime();

		// check the icalendar spec in order to build a more complicated meeting request
		String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n" + "PRODID: BCP - Meeting\n" + "VERSION:2.0\n"
				+ "BEGIN:VEVENT\n" + "DTSTAMP:" + iCalendarDateFormat.format(dd) + "\n" + "DTSTART:"
				+ iCalendarDateFormat.format(dd) + "\n" + "DTEND:" + iCalendarDateFormat.format(dd) + "\n" + "SUMMARY:"
				+ subject + "\n" + "UID:324\n"
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:bsaikiran612@gmail.com\n" +
				// "ORGANIZER:MAILTO:bsaikiran612@gmail.com\n" +
				// "LOCATION:on the net\n" +
				"DESCRIPTION:" + body + "\n" + "SEQUENCE:0\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "STATUS:CONFIRMED\n"
				+ "TRANSP:OPAQUE\n" + "BEGIN:VALARM\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:REMINDER\n"
				+ "TRIGGER;RELATED=START:-PT00H15M00S\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR";

		calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		return calendarPart;
	}

	
	public void unlockUserMail(Users user)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(mailusername, "User unlocked Successfully");
		message.addRecipient(RecipientType.CC, new InternetAddress(mailusername));
		helper.setTo("kiranjava010@gmail.com");
		String subject = "User unlocked Successfully";
		String content = "<p>Dear Sir ,</p><p>"+user.getFullname()+" account has unlocked</p>"
				+ "<p>Last login time "+user.getLastLogin()+"</p><p> Remarks for unlock "+user.getRemarks();
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
	
	//Pending H1bApplicants list for file uploading's
	final static DateTimeFormatter CUSTOM_FORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");	
	public void pendingH1BApplicantsUploadInfo(List<H1bApplicantDTO> entity,LocalDateTime currentdate) {
		String formattedString = currentdate.format(CUSTOM_FORMATTER1);
		
		StringBuilder heading = new StringBuilder();
		heading.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>")
				.append("<p>These are the list of H1BApplicants who are not yet uploaded their Files even after 3 day's of data submmission</p>")
				.append("<table class='styled-table' width='100%'>")
				.append("<thead>")
				.append("<tr>")
				.append("<th>Applicant Id</th><th>Employee name</th><th>Contact number</th><th>Email</th><th>Receipt number</th><th>Petitioner</th>"
						+ "<th>created date</th></tr>")
				.append("</thead>");
		    entity.forEach(h1bapplicant->{
		    Long applicantId = h1bapplicant.getApplicantid();
			String name = h1bapplicant.getEmployeename();
			String number = h1bapplicant.getContactnumber();
			String email = h1bapplicant.getEmail();
			String receiptNumber = h1bapplicant.getReceiptnumber();
			String petitioner = h1bapplicant.getPetitioner();
			String noticeType = h1bapplicant.getNoticetype();
			LocalDateTime createdDate= h1bapplicant.getUpdateddate();
			String formattedcreateddate = createdDate.format(CUSTOM_FORMATTER1);
			LocalDateTime updatedDate = h1bapplicant.getUpdateddate();
//			String h1bUpdateddate = updatedDate.format(CUSTOM_FORMATTER1);
			
			heading.append("<tbody>"
					+ "<tr>"
					+ "<td>"+applicantId+"</td>"
					+ "<td>"+name+"</td>"
					+ "<td>"+number+"</td>"
					+ "<td>"+email+"</td>"
					+ "<td>"+receiptNumber+"</td>"
					+ "<td>"+petitioner+"</td>"
					+ "<td>"+formattedcreateddate+"</td>"
					+ "</tr>"
					+ "</tbody>"
					);
		});
		    
		heading.append("</table>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("abhidatasol@gmail.com", "Pending h1bapplicants files upload list");
//			helper.setTo("kiranjava010@gmail.com");
			helper.setTo("lakshmanchalla22@gmail.com");
//			helper.setTo("hr@narvee.com");
//			helper.setCc(new String[] {"hr@narveetech.com"});
			String subject = "Here's the pending H1bApplicant's details "+formattedString;
			helper.setSubject(subject);
			helper.setText(heading.toString(), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
	public void dailyConsultantInfo(List<ConsultantDTO> entity,LocalDateTime Currentdate) { 
		String formattedString = Currentdate.format(CUSTOM_FORMATTER);
		
		StringBuilder heading = new StringBuilder();
		heading.append("<html>").append("<head>").append("<style>").append(
				".styled-table { border-collapse: collapse; font-size: 0.9em; font-family: sans-serif; border: 1px solid #dddddd;}")
				.append(".styled-table thead tr { background-color: #009879; color: #ffffff; }")
				.append(".styled-table th, .styled-table td { padding: 12px 15px; }")
				.append(".styled-table tbody tr td { border: 1px solid black;}")
				.append(".styled-table tbody tr { border-bottom: 1px solid #dddddd; }")
				.append(".styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; }")
				.append(".styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; }")
				.append(".styled-table tbody tr.active-row { font-weight: bold; color: #009879;}").append("</style>")
				.append("</head>").append("<body>").append("<p>Hi Team,<br>")
				.append("<p>These are the below consultants newly added into portal please review and approve them</p>")
				.append("<table class='styled-table' width='100%'>")
				.append("<thead>")
				.append("<tr>")
				.append("<th>Created Date</th><th>(First Middle Last name)</th><th>Contact Number</th><th>Email</th><th>Current location (City and State) </th><th>LinkedIn ID</th>"
						+ "<th>Work Authorization</th><th>Education Details /University / Year</th><th>Technology</th><th>Status</th></tr>")
				.append("</thead>");
		entity.forEach(consultant->{
			String name = consultant.getConsultantname();
			String number = consultant.getContactnumber();
			String email = consultant.getConsultantemail();
			String currentLocation = consultant.getCurrentlocation();
			String linkedin = consultant.getLinkedin();
			String visa = consultant.getVisa_status();
			String education = consultant.getQualification();
			String technology = consultant.getTechnologyarea();
			String university = consultant.getUniversity();
			String yop = consultant.getYop();
			LocalDateTime createddate = consultant.getCreateddate();
			String Staringcreateddate = createddate.format(CUSTOM_FORMATTER);
			String status = consultant.getStatus();
			heading.append("<tbody><tr><td style='width:13%'>"+Staringcreateddate+"</td><td>"+name+"</td>"
					+ "<td>"+number+"</td>"
					+ "<td>"+email+"</td>"
					+ "<td>"+currentLocation+"</td>"
					+ "<td>"+linkedin+"</td>"
					+ "<td>"+visa+"</td>"
					+ "<td>"+education+"/"+university+"/"+yop+"</td>"
					+ "<td>"+technology+"</td>"
					+ "<td>"+status+"</td></tr></tbody>");
		});
		heading.append("</table>");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setFrom("abhidatasol@gmail.com", "Pending consultant list");
			helper.setTo("kiranjava010@gmail.com");
			//helper.setCc(new String[] {"hr@narveetech.com"});
			String subject = "Here's the pending consultant details "+formattedString;
			helper.setSubject(subject);
			helper.setText(heading.toString(), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailSender.send(message);
	}
	
}
