package heykakao.HeyForm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heykakao.HeyForm.model.dto.AnswerDto;
import heykakao.HeyForm.model.dto.ChoiceDto;
import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
@ToString(exclude = "question")
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Answer_id")
    private Long id;

    @Column(name = "question_order")
    private Integer order;

    @Column(name = "answer_contents")
    private String contents;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Answer(Integer order, String contents, Question question) {
        this.order = order;
        this.contents = contents;
        this.question = question;
    }

    public void setByDto(AnswerDto answerDto) {
        this.order = answerDto.getQuestion_order();
        this.contents = answerDto.getAnswer_contents();
    }

    public void setByDto(AnswerDto answerDto, User user, Question question) {
        this.order = answerDto.getQuestion_order();
        this.contents = answerDto.getAnswer_contents();
        this.user = user;
        this.question = question;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setQuestion(Question question) {
        this.question = question;
    }
}
