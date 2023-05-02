package com.narvee.usit.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import com.narvee.usit.dto.RecruiterDTO;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Qualification;
import com.narvee.usit.entity.Technologies;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.entity.Visa;

public class ExcelUploads {

	// check that file is of excel type or not
	public static boolean checkExcelFormat(MultipartFile file) {

		String contentType = file.getContentType();
		if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		} else {
			return false;
		}
	}

	public static List<RecruiterDTO> convertExcelToListOfVendorData(InputStream is) throws IOException {
		List<RecruiterDTO> list = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		String data = "";
		XSSFSheet sheet = workbook.getSheet("data");
		Iterator<Row> iterator = sheet.iterator();
		iterator.next();
		RecruiterDTO entity = null;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			entity = new RecruiterDTO();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				int columnIndex = cell.getColumnIndex();
				switch (columnIndex) {
				case 0:
					entity.setCompany(cell.getStringCellValue());

					break;
				case 1:
					entity.setName(cell.getStringCellValue());
					break;
				case 2:
					entity.setEmail(cell.getStringCellValue());

					break;
				case 3:
					try {
						data = NumberToTextConverter.toText(cell.getNumericCellValue());
						entity.setContact_Number(data);
					} catch (IllegalStateException e) {
						entity.setContact_Number(cell.getStringCellValue());
					}
					break;
					
				case 4:
					try {
						data = NumberToTextConverter.toText(cell.getNumericCellValue());
						entity.setExt(data);
					} catch (IllegalStateException e) {
						entity.setExt(cell.getStringCellValue());
					}
					break;
				case 5:
					entity.setType(cell.getStringCellValue());
					break;
				}
				
				// end of switch
					// System.out.println("kiran ================="+entity);
			} // end of inner while
				// System.out.println("kiran =================");
			if (!list.contains(entity)) {
				list.add(entity);
			}
		} // end of outer while
//		System.out.println("===============lllll");
//		System.out.println(list);
//		System.out.println("===============lll");
		workbook.close();
		return list;
	}

	public static List<ConsultantInfo> convertExcelToListOfVendor(InputStream is) throws IOException {
		List<ConsultantInfo> list = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		XSSFSheet sheet = workbook.getSheet("data");
		String data = "";
		int rowNumber = 0;
		Integer exp = 0;
		Iterator<Row> iterator = sheet.iterator();
		iterator.next();
		Visa visa = null;
		Qualification qualification = null;
		Technologies technologies = null;
		ConsultantInfo entity = null;

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			visa = new Visa();
			qualification = new Qualification();
			technologies = new Technologies();
			entity = new ConsultantInfo();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				int columnIndex = cell.getColumnIndex();
				switch (columnIndex) {
				case 0:
					entity.setFirstname(cell.getStringCellValue());
					break;

				case 1:
					entity.setLastname(cell.getStringCellValue());
					break;

				case 2:
					data = NumberToTextConverter.toText(cell.getNumericCellValue());
					entity.setContactnumber(data);
					break;
				case 3:
					entity.setConsultantemail(cell.getStringCellValue());
					break;

				case 4:
					data = NumberToTextConverter.toText(cell.getNumericCellValue());
					// exp = String.valueOf(cell.getNumericCellValue());
					entity.setExperience(data);
					break;

				case 5:
					entity.setCurrentlocation(cell.getStringCellValue());
					break;
				case 6:
					entity.setRatetype(cell.getStringCellValue());
					break;

				case 7:
					data = NumberToTextConverter.toText(cell.getNumericCellValue());
					entity.setProjectavailabity(data);
					break;

				case 8:
					entity.setAvailabilityforinterviews(cell.getStringCellValue());
					break;

				case 9:
					try {
						qualification.setName(cell.getStringCellValue());
						entity.setQualification(qualification);
					} catch (NullPointerException e) {
					}
					break;

				case 10:
					entity.setUniversity(cell.getStringCellValue());
					break;
				case 11:
					data = NumberToTextConverter.toText(cell.getNumericCellValue());
					entity.setYop(data);
					break;
				case 12:
					try {
						visa.setVisastatus(cell.getStringCellValue());
						entity.setVisa(visa);
					} catch (NullPointerException e) {
					}
					break;
				case 13:
					try {
						technologies.setTechnologyarea(cell.getStringCellValue());
						entity.setTechnology(technologies);
					} catch (NullPointerException e) {
					}
					break;

				case 14:
					entity.setStatus(cell.getStringCellValue());
					break;

				case 15:
					entity.setRemarks(cell.getStringCellValue());
					break;
				} // end of switch
					// System.out.println("kiran ================="+entity);
			} // end of inner while
				// System.out.println("kiran =================");
			if (!list.contains(entity)) {
				list.add(entity);
			}
		} // end of outer while
		workbook.close();
		return list;
	}
}
