package com.narvee.usit.serviceimpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.dto.H1bApplicantDTO;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.H1BApplicants;
import com.narvee.usit.repository.IH1BApplicantsRepository;
import com.narvee.usit.service.IH1BApplicantsService;

@Transactional
@Service
public class H1BApplicantsServiceImpl implements IH1BApplicantsService {

	private static final Logger logger = LoggerFactory.getLogger(H1BApplicantsServiceImpl.class);
	@Value("${file.upload-dir}")
	private String filesPath;

	@Autowired
	private EmailService emailService;

	
	@Autowired
	private IH1BApplicantsRepository h1bApplicantsRepo;

	@Override
	public H1BApplicants saveH1BApplicants(H1BApplicants h1bApplicants) {
		logger.info("!!! inside class: CityServiceImpl, !! method: saveCity");
		return h1bApplicantsRepo.save(h1bApplicants);
	}

	@Override
	public H1BApplicants getH1BApplicantsById(Long id) {
		Optional<H1BApplicants> optional = h1bApplicantsRepo.findById(id);
		//H1BApplicants h1bApplicants = optional.get();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<H1BApplicants> getAllH1BApplicants() {
		// List<H1BApplicants> h1bApplicantsList= h1bApplicantsRepo.findAll();
		return h1bApplicantsRepo.findAll(Sort.by(Sort.Direction.DESC, "applicantid"));
		// return h1bApplicantsList;
	}

	@Override
	public void deleteH1BApplicantsById(Long id) {
		h1bApplicantsRepo.deleteById(id);
	}

	@Override
	public Resource downloadfile(long id, String doctype) throws FileNotFoundException {
		H1BApplicants entity = h1bApplicantsRepo.findById(id)
				.orElseThrow(() -> new FileNotFoundException("File does not exist" + id));

		String filename = null;
		if (doctype.equalsIgnoreCase("passportdoc"))
			filename = entity.getPassportdoc();
		else if (doctype.equalsIgnoreCase("everifydoc"))
			filename = entity.getEverifydoc();
		else if (doctype.equalsIgnoreCase("i9doc"))
			filename = entity.getI9doc();
		else if (doctype.equalsIgnoreCase("I797doc"))
			filename = entity.getI797doc();
		else if (doctype.equalsIgnoreCase("i94doc"))
			filename = entity.getI94doc();
		else
			filename = entity.getSsndoc();

		try {
			Path file = Paths.get(filesPath).resolve(filename);
			logger.info(" filesPath ", filesPath);
			logger.info(" Path ", file);
			Resource resource = new UrlResource(file.toUri());
			logger.info(" resource location ", resource);
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public int removeFile(long id, String doctype) {
		H1BApplicants entity = h1bApplicantsRepo.findById(id).get();
		
		if (doctype.equalsIgnoreCase("passportdoc"))
			entity.setPassportdoc(null);
		else if (doctype.equalsIgnoreCase("everifydoc"))
			entity.setEverifydoc(null);
		else if (doctype.equalsIgnoreCase("i9doc"))
			 entity.setI9doc(null);
		else if (doctype.equalsIgnoreCase("I797doc"))
			entity.setI797doc(null);
		else if (doctype.equalsIgnoreCase("i94doc"))
			 entity.setI94doc(null);
		else
			 entity.setSsndoc(null);
		h1bApplicantsRepo.save(entity);
		
		return 1;
	}

	@Override
	public int update(String passportdoc, String everifydoc, String i9doc, String I797doc, String i94doc, String ssndoc,
			Long id) {
		Integer upd = h1bApplicantsRepo.update(passportdoc, id);
		if (upd == null) {
			return 0;
		} else {
			return 1;
		}
	}
	
	@Scheduled(cron = "0 */1 * * * *")                 // runs every minute 
//	@Scheduled(cron = "0 0 0 */2 * ?")                 //runs for every 2 day's
    public void checkFilesUploaded() throws IOException {
		System.out.println("----------------------------------------");
        List<H1bApplicantDTO> fileUploads = h1bApplicantsRepo.findByCreateddateAndEverifydocandI9docIsNull();
        ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		System.out.println("current localdatatime:"+now);
        emailService.pendingH1BApplicantsUploadInfo(fileUploads,now);                           //sending mails if files were not uploaded even after 3 day's
            }
    }
