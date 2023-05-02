package com.narvee.usit.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.GetVendor;
import com.narvee.usit.helper.ListVendor;

public interface IVendorService {
	public VendorDetails save(VendorDetails vms);

	//public Vms save(Vms vms);

	public List<VendorDetails> getall();

	public boolean deleteById(long id);

	public VendorDetails getbyId(long id);

	public List<GetVendor> getvendordetails(String flg);

	public int approve(String stat, Long id);
	//public int approve(String stat, Long id, Long role);

	public int changeStatus(String status, Long id, String remarks);
	
	public int rejectVendor(Long id, String remarks);

	public Page<VendorDetails> findPaginated(int pageNo, int pageSize);

	public void saveexcel(MultipartFile file);

	public List<ListVendor> getvendor(String role, long userid);

	public List<VendorDetails> findByCompanyAndHeadquerter(String company, String location);
	
	//checking duplicate records
	public  List<VendorDetails> checkDuplicateVendor(String vendorname);
	

}
