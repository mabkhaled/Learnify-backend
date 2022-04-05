package com.khaled.Learnify.models;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Document(collection="courses")
public class Courses {
	@Getter @Setter @Id private String id;
	@Getter @Setter private String title;
	@Getter @Setter private String Desc;
	@Getter @Setter private String imgPath;
	@Getter @Setter private int nbrSeance;
	@Getter @Setter private String tag;
	@Getter @Setter private float price;
	@Getter @Setter private User auteur;
	@DBRef
	@Getter @Setter private Set<Lesson> lessons;

}
