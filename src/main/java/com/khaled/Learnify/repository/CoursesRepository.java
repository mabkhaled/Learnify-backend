package com.khaled.Learnify.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.khaled.Learnify.models.Courses;

@RepositoryRestResource(collectionResourceRel = "courses", path = "courses")
public interface CoursesRepository extends MongoRepository<Courses, String> {

}
