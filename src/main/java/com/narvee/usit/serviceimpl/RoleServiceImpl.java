package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.narvee.usit.helper.GetRoles;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.entity.Users;
import com.narvee.usit.repository.IRolesRepository;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.service.IRoleService;

@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

	public static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

	@Autowired
	private IRolesRepository iRoleRepo;

	@Autowired
	private IUsersRepository userRepo;

	public boolean saveRole(Roles role) {
		logger.info("RoleServiceImpl.saveRole()");
		List<String> finaAllRolByRolName = iRoleRepo.findRolByRolName(role.getRolename().toLowerCase());
		if ((finaAllRolByRolName == null || finaAllRolByRolName.isEmpty())) {
			logger.info("Role saved after checking duplicate records available or not");
			Roles saveRole = iRoleRepo.save(role);
			return true;
		} else {
			return false;
		}
	}

	public boolean updateRole(Roles role) {
		logger.info("RoleServiceImpl.saveRole()");
		List<String> finaAllRolByRolName = iRoleRepo.findRolByRolName(role.getRolename().toLowerCase());
		Optional<Roles> roles = iRoleRepo.findByRolenameAndRoleidNot(role.getRolename(), role.getRoleid());
		if ((roles == null || roles.isEmpty())) {
			logger.info("Role saved after checking duplicate records available or not");
			Roles saveRole = iRoleRepo.save(role);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Roles> getAllRoles() {
		logger.info("RoleServiceImpl.saveRole()");
		return iRoleRepo.findAll();
	}

	@Override
	public Roles getRole(Long id) {
		logger.info("RoleServiceImpl.getRole by id()=> " + id);
		return iRoleRepo.findById(id).get();
	}

	@Override
	public boolean deleteRole(Long id) {
		logger.info("RoleServiceImpl.deleteRole() by id => " + id);
		Users user = userRepo.findByRoleRoleid(id);
		if (user == null) {
			iRoleRepo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int changeStatus(String status, Long id, String rem) {
		logger.info("RoleServiceImpl.changeStatus()status " + status + " id  =>" + id + " remarks =>" + rem);
		return iRoleRepo.toggleStatus(status, id, rem);
	}

	@Override
	public List<GetRoles> getRoles() {
		logger.info("RoleServiceImpl.getRoles()");
		return iRoleRepo.getAllRoles();
	}

	@Override
	public List<Long> finabyrolenumber(long roleno) {
		return iRoleRepo.findByRoleno(roleno);
	}

}
