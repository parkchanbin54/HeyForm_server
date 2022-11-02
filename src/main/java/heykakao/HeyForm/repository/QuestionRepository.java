package heykakao.HeyForm.repository;

import heykakao.HeyForm.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q where q.survey.id = ?1")
    List<Question> findBySurvey_Id(@Nullable Long id);

    @Query("select q from Question q where q.order = ?1 and q.survey.id = ?2")
    Optional<Question> findByOrderAndSurvey_Id(@NonNull Integer order, @NonNull Long id);
}