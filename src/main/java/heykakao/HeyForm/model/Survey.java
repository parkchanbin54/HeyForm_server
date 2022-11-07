package heykakao.HeyForm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heykakao.HeyForm.model.dto.SurveyDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@ToString(exclude = "user")
public class Survey {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long id;

    @Column(name = "survey_state")
    private Integer state; //0: during, 1: complete(before release) 2: terminate(after release)

    @Column(name = "survey_title")
    private String surveytitle;

    @Column(name = "survey_url")
    private String url;

    @Column(name = "start_time")
    private String starttime;
//    private Timestamp starttime;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "end_time")
    private String endtime;
//    private Timestamp endtime;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    public Survey(Integer state, String url, User user, String starttime, String endtime, String category, String description, String surveytitle) {
        this.state = state;
        this.url = url;
        this.user = user;
        this.starttime = starttime;
        this.endtime = endtime;
        this.category = category;
        this.description = description;
        this.surveytitle = surveytitle;
    }

    public void setByDto(SurveyDto surveyDto) {
        this.id = surveyDto.getSurvey_id();
        this.state = surveyDto.getSurvey_state();
        this.url = surveyDto.getSurvey_url();
        this.starttime = surveyDto.getStart_time();
        this.endtime = surveyDto.getEnd_time();
        this.category = surveyDto.getCategory();
        this.description = surveyDto.getDescription();
        this.surveytitle = surveyDto.getSurvey_title();
    }

    public void setByDto(SurveyDto surveyDto, User user) {
        this.id = surveyDto.getSurvey_id();
        this.state = surveyDto.getSurvey_state();
        this.url = surveyDto.getSurvey_url();
        this.starttime = surveyDto.getStart_time();
        this.endtime = surveyDto.getEnd_time();
        this.category = surveyDto.getCategory();
        this.description = surveyDto.getDescription();
        this.surveytitle = surveyDto.getSurvey_title();
        this.user = user;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setState(Integer state){this.state = state;}
}

//질문 : {주관식, something}, {객관식, {1,2,3,4,5} }
//답변 : {주관식, something}, {객관식, "1,2" }