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

    private Long user_id;

    private String gender;
    private String age;
    public AnswerDto(Answer answer) {
        this.question_order = answer.getOrder();
        this.answer_contents = answer.getContents();
        this.user_id = answer.getUser().getId();
        this.age = answer.getUser().getAge();
        this.gender = answer.getUser().getGender();
    }
}
