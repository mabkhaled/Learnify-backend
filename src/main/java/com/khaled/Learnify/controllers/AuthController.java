package com.khaled.Learnify.controllers;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.khaled.Learnify.models.ERole;
import com.khaled.Learnify.models.Role;
import com.khaled.Learnify.models.User;
import com.khaled.Learnify.payload.request.LoginRequest;
import com.khaled.Learnify.payload.request.SignupRequest;
import com.khaled.Learnify.payload.response.MessageResponse;
import com.khaled.Learnify.payload.response.UserInfoResponse;
import com.khaled.Learnify.repository.RoleRepository;
import com.khaled.Learnify.repository.UserRepository;
import com.khaled.Learnify.security.jwt.JwtUtils;
import com.khaled.Learnify.security.services.UserDetailsImpl;
import com.khaled.Learnify.services.FilesStorageServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Khaled this is the Auth controller takes care for singup and signin
 *         requests
 *
 */

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	FilesStorageServiceImpl storageService;

	/**
	 * 
	 * @param loginRequest this is the loginRequest object that contains username
	 *                     and password
	 * @return a responceEntity object with user details
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
				new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	/**
	 * 
	 * @param signUpRequest send a singUp request object with user details
	 * @param file          profile image file
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestPart() SignupRequest signUpRequest,
			@RequestPart("profileImage") MultipartFile profileImage) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account*
		//we can change the signUpRequest object and add other fields to the user account 
		//User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
		//		encoder.encode(signUpRequest.getPassword()));
		
		User user = new User(signUpRequest.getUsername(),signUpRequest.getName(),
				signUpRequest.getLastName(),signUpRequest.getEmail(),encoder.encode(signUpRequest.getPassword()),signUpRequest.getPhone()
				,signUpRequest.getBirthDate());

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		// check for user roles in strRoles array (possible paramater are
		// ["ENSEIGNAN","ETUDIENT"])
		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
			log.info("role user is found first");
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "ENSEIGNAN":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ENSEIGNANT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					log.info("ROLE_ENSEIGNANT user is found");

					break;
				case "ETUDIENT":
					Role modRole = roleRepository.findByName(ERole.ROLE_ETUDIENT)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
					log.info("ROLE_USER user is found in default");
				}
			});
		}

		user.setRoles(roles);
		user = userRepository.save(user);

		log.info("user is registred with success");

		// after registring the user we will save the profile picture
		String imageExt = FilenameUtils.getExtension(profileImage.getOriginalFilename());
		String newImageName = user.getId()+"."+imageExt;
		String message = "";
		try {
			storageService.save(profileImage,newImageName);

			message = "Uploaded the file successfully: " + newImageName
					+ " and user is registred with success";
			log.info(message);
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + profileImage.getOriginalFilename() + "!";
			log.info(message);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}

	}

	/**
	 * 
	 * @return a userInfoResponse contains user details and informations
	 */
	@GetMapping("/getauthuser")
	public ResponseEntity<?> getAuthUser() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(
				new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));

	}

	/**
	 * 
	 * @param file a file to be uploaded
	 * @return HttpStatus OK or Exception_Failed
	 */
	@PostMapping("/profilimg")
	public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file,file.getOriginalFilename());

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
		}
	}
}
