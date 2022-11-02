package heykakao.HeyForm.repository;

import heykakao.HeyForm.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select a from Answer a where a.question.id = ?1")
    List<Answer> findByQuestion_Id(@Nullable Long id);

    @Query("select a from Answer a where a.user.token = ?1 and a.question.id = ?2")
    Optional<Answer> findByUser_TokenAndQuestion_Id(String token, Long id);

    @Query("select a from Answer a where a.user.id = ?1")
    List<Answer> findByUser_Id(Long id);







}