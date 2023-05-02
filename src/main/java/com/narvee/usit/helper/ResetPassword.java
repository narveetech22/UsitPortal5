package com.narvee.usit.helper;

import lombok.Data;

@Data
public class ResetPassword {
	private long userid;
	private String email;
	private String password;
	private String renewpassword;
	private String newpassword;
	private String resetPasswordToken;

}
