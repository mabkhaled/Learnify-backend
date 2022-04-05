package com.khaled.Learnify.models;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Document(collection = "users") @NoArgsConstructor
public class User {
  @Getter @Setter @Id private String id;
  @NotBlank
  @Size(max = 20)
  @Getter @Setter private String username;
  @Getter @Setter private String name;
  @Getter @Setter private String lastName;
  @NotBlank
  @Size(max = 50)
  @Email
  @Getter @Setter private String email;
  @NotBlank
  @Size(max = 120)
  @Getter @Setter private String password;
  
  @Getter @Setter private String phone;
  @DBRef
  @Getter @Setter private Set<Role> roles = new HashSet<>();
  public User(String username, String email, String password) {
	    this.username = username;
	    this.email = email;
	    this.password = password;
	  }
  
  
}
