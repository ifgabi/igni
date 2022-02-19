package gg.igni.igniserver.account.repositories;

import java.util.Optional;

import gg.igni.igniserver.account.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
	Optional<User> findById(Long id);
	Optional<User> findByEmail(String email);
	Optional<User> findByUsernameIgnoreCase(String username);
  Optional<User> findByEmailIgnoreCase(String email);
  Long deleteByUsername(String username);
  Optional<User> findByUsernameAndEmail(String username, String email);
}
