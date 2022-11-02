package heykakao.HeyForm.model.dto;

import heykakao.HeyForm.model.Choice;
import heykakao.HeyForm.model.Question;
import lombok.*;

@Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class ChoiceDto {
    private Integer choice_order;
    private String choice_contents;

    public ChoiceDto(Choice choice) {
        this.choice_order = choice.getOrder();
        this.choice_contents = choice.getContents();
    }
}
