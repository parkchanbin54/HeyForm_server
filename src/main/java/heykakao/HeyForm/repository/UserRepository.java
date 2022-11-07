package heykakao.HeyForm.repository;

import heykakao.HeyForm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.token = ?1")
    Optional<User> findByToken(String token);

    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.account = ?1")
    Optional <User> findByAccount(String account);


}
