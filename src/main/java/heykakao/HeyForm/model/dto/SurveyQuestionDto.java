package heykakao.HeyForm.model.dto;

import lombok.*;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class SurveyQuestionDto {
    private SurveyDto surveyDto;
    private List<QuestionDto> questionDtos;
}