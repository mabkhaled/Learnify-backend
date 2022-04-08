package com.khaled.Learnify.payload.response;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class UserInfoResponse {
  private String id;
  private String username;
  private String email;
  private List<String> roles;
  @Getter
  @Setter
  private String token;

  public UserInfoResponse(String id, String username, String email, List<String> roles,String token) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.token=token;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }
}
