package com.khaled.Learnify.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "lessons")
public class Lesson {
	@Id
	@Getter @Setter  private String id;
	@Getter @Setter private String title;
	@Getter @Setter private String support;
	@DBRef
	@Getter @Setter private Courses course;
	@Getter @Setter private String meetCode;
	
}
