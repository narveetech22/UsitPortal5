package com.narvee.usit.service;

import java.util.Optional;

import com.narvee.usit.entity.Users;

public interface ILoginService {

	// acount signin
	public Users findByEmail(String email);

	public Users getByuserCredentials(String email, String password);

	public Users findbyuserid(long userid);

	public Users findBytoken(String token);

	public void updatePassword(Users user, String newPsw);

	public void updateResetPasswordToken(String token, String email);
}
