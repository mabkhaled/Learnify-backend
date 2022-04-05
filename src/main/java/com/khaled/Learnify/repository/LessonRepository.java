package com.khaled.Learnify.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.khaled.Learnify.models.Lesson;

@RepositoryRestResource(collectionResourceRel = "lessons", path = "lessons")
public interface LessonRepository extends MongoRepository<Lesson, String> {

}
