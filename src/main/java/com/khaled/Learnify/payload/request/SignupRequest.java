package com.khaled.Learnify.payload.request;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	@Getter
	@Setter
	private String username;

	@NotBlank
	@Size(min = 3, max = 20)
	@Getter
	@Setter
	private String name;

	@NotBlank
	@Size(min = 3, max = 20)
	@Getter
	@Setter
	private String lastName;

	@NotBlank
	@Size(min = 3, max = 20)
	@Getter
	@Setter
	private String phone;

	@NotBlank
	@Size(max = 50)
	@Email
	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private Set<String> roles;

	@NotBlank
	@Size(min = 6, max = 40)
	@Getter
	@Setter
	private String password;
	@Getter
	@Setter
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date birthDate;

}