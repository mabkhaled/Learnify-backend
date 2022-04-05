package com.khaled.Learnify.repository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.khaled.Learnify.models.ERole;
import com.khaled.Learnify.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
