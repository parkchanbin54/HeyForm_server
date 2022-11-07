package heykakao.HeyForm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import heykakao.HeyForm.model.dto.ChoiceDto;
import heykakao.HeyForm.model.dto.QADto;
import heykakao.HeyForm.model.dto.QuestionDto;
import heykakao.HeyForm.repository.SurveyRepository;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@ToString(exclude = "qa")
public class QA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qa_id")
    private Long id;

    @Column(name = "qa_title")
    private String qa_title;

    @Column(name = "qa_content")
    private String qa_content;

    @Column(name = "user_email")
    private String user_email;


    public QA(String qa_title, String qa_contents, String user_email) {
        this.qa_content = qa_contents;
        this.qa_title = qa_title;
        this.user_email = user_email;
    }
    public void setByDto(QADto qaDto) {
        this.qa_title = qaDto.getQa_title();
        this.qa_content = qaDto.getQa_contents();
        this.user_email = qaDto.getUser_email();
    }


}
