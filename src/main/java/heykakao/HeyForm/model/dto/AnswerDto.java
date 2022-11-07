package heykakao.HeyForm.model.dto;

import heykakao.HeyForm.model.Answer;
import heykakao.HeyForm.model.Choice;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AnswerDto {
    private Integer question_order;
    private String answer_contents;

    private String answer_time;

    public AnswerDto(Answer answer) {
        this.question_order = answer.getOrder();
        this.answer_contents = answer.getContents();
        this.answer_time = answer.getTime();
    }
}
