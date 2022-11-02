package heykakao.HeyForm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heykakao.HeyForm.model.dto.ChoiceDto;
import heykakao.HeyForm.model.dto.QuestionDto;
import lombok.*;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor
@ToString(exclude = "question")
public class Choice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long id;

    @Column(name = "choice_order")
    private Integer order;

    @Column(name = "choice_contents")
    private String contents;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public Choice(Integer order, String contents, Question question) {
        this.order = order;
        this.contents = contents;
        this.question = question;
    }

    public void setByDto(ChoiceDto choiceDto) {
        this.order = choiceDto.getChoice_order();
        this.contents = choiceDto.getChoice_contents();
    }
    public void setByDto(ChoiceDto choiceDto, Question question) {
        this.order = choiceDto.getChoice_order();
        this.contents = choiceDto.getChoice_contents();
        this.question = question;
    }

}
// 0: 질문 1...: 보기 숫자