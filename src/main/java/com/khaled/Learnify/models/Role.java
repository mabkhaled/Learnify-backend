package com.khaled.Learnify.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "roles") @NoArgsConstructor 
public class Role {
  @Id
  @Getter @Setter private String id;
  @Getter @Setter private ERole name;
  public Role(ERole name) {
    this.name = name;
  }

}