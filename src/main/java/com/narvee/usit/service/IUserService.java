package com.narvee.usit.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.usit.dto.UserInfoDTO;
import com.narvee.usit.entity.Users;
import com.narvee.usit.helper.GetRecruiter;

public interface IUserService {

	public boolean saveUser(Users users);

	public boolean updateUser(Users users);

	public Users findUserByEmail(String email);

	public Users findUserByEmailandUid(String email, Long id);

	public List<Users> getAllUsers();

	public Users finduserById(Long id);
	
	public List<UserInfoDTO> finduserInfoById(Long id);

	public boolean deleteUsers(Long id);

	public int changeStatus(String status, Long id, String remarks);
	
	public int unlockUser(Users users);

	public List<Users> filterEmployee(String keyword);

	public List<String> filterEmployee2(String keyword);

	Page<List<Users>> findPaginated(int pageNo, int pageSize);

	public List<Object[]> getUser();

	public List<GetRecruiter> getRecruiter();

	public List<Object[]> managerDropDown();

	public List<Object[]> TLDropDown(long id);

}
