package heykakao.HeyForm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heykakao.HeyForm.model.dto.QuestionDto;
import heykakao.HeyForm.repository.SurveyRepository;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@ToString(exclude = "survey")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(name = "question_type")
    private String type;

    @Column(name = "question_order")
    private Integer order;

    @Column(name = "question_contents")
    private String contents;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    public Question(String type, Integer order, String contents, Survey survey) {
        this.type = type;
        this.order = order;
        this.contents = contents;
        this.survey = survey;
    }

    public void setByDto(QuestionDto questionDto) {
        this.type = questionDto.getQuestion_type();
        this.order = questionDto.getQuestion_order();
        this.contents = questionDto.getQuestion_contents();
    }

    public void setByDto(QuestionDto questionDto, Survey survey) {
        this.type = questionDto.getQuestion_type();
        this.order = questionDto.getQuestion_order();
        this.contents = questionDto.getQuestion_contents();
        this.survey = survey;
    }

//    @OneToMany(mappedBy = "question")
//    private List<Choice> choices = new ArrayList<>();
//
//    @OneToMany(mappedBy = "question")
//    private List<Answer> answers = new ArrayList<>();
}
// 1: 주관식, 2: 객관식, 3: 별점, 4: 리커트, 5: 선형