package heykakao.HeyForm.model.dto;

import heykakao.HeyForm.model.Question;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor @AllArgsConstructor @ToString
public class QuestionDto {
    private String question_type;
    private Integer question_order;

    private String question_contents;
    private List<ChoiceDto> choiceDtos;


    public QuestionDto(String question_type, Integer question_order, String question_contents) {
        this.question_type = question_type;
        this.question_order = question_order;
        this.question_contents = question_contents;
    }

    public QuestionDto(Question question) {
        this.question_type = question.getType();
        this.question_order = question.getOrder();
        this.question_contents = question.getContents();
    }

    public QuestionDto(Question question, List<ChoiceDto> choiceDtos) {
        this.question_type = question.getType();
        this.question_order = question.getOrder();
        this.question_contents = question.getContents();
        this.choiceDtos = choiceDtos;
    }

    public void setChoiceDtos(List<ChoiceDto> choiceDtos){
        this.choiceDtos = choiceDtos;
    }
}
