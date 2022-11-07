package heykakao.HeyForm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class SurveyAnswerDto {
    private Long survey_id;
    private String user_token;
    private String user_gender;
    private String user_age;
    private Long user_id;
    private List<AnswerDto> answerDtos;

}
