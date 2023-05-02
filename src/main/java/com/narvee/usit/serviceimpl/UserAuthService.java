package com.narvee.usit.serviceimpl;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.entity.Users;

@Service
public class UserAuthService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUsersRepository repo;

	@Autowired
	private EmailService emailService;
	public static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users users = repo.findByEmail(username);
		Roles role = new Roles();
		String pasw = "";
		List<Roles> rls = new ArrayList<>();
		try {
			role.setRolename(users.getRole().getRolename());
			rls.add(role);
			pasw = users.getPassword();
		} catch (NullPointerException e) {
		}

		List<GrantedAuthority> grantedAuthorities = rls.stream().map(r -> {
			return new SimpleGrantedAuthority(r.getRolename());
		}).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(username, pasw, grantedAuthorities);
	}

	

}
