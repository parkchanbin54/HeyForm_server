package heykakao.HeyForm.service;

import heykakao.HeyForm.model.Answer;
import heykakao.HeyForm.model.Choice;
import heykakao.HeyForm.model.Question;
import heykakao.HeyForm.model.Survey;
import heykakao.HeyForm.model.dto.SurveyQuestionDto;
import heykakao.HeyForm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public SurveyService(UserRepository userRepository, SurveyRepository surveyRepository, QuestionRepository questionRepository,
                      ChoiceRepository choiceRepository, AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.answerRepository = answerRepository;
    }

    public void delChoice(Long question_id, Integer choice_order) {
        Choice choice = choiceRepository.findByOrderAndQuestion_Id(choice_order, question_id).get();
        choiceRepository.deleteById(choice.getId());
    }

    public void delQuestion(Long question_id) {
        delQuetionUtil(question_id);
    }

    public void delQuestion(Long survey_id, Integer question_order) {
        Question question = questionRepository.findByOrderAndSurvey_Id(question_order, survey_id).get();
        delQuetionUtil(question.getId());
    }

    private void delQuetionUtil(Long question_id) {
        List<Choice> choices = choiceRepository.findByQuestion_Id(question_id);
        List<Long> choice_ids = choices.stream().map(Choice::getId).collect(Collectors.toList());
        choiceRepository.deleteAllById(choice_ids);

        List<Answer> answers = answerRepository.findByQuestion_Id(question_id);
        List<Long> answer_ids = answers.stream().map(Answer::getId).collect(Collectors.toList());
        answerRepository.deleteAllById(answer_ids);

        questionRepository.deleteById(question_id);
    }

    public void delSurvey(Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id).get();

        List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
        List<Long> question_ids = questions.stream().map(Question::getId).collect(Collectors.toList());

        for(Long qs_id: question_ids) {
            delQuestion(qs_id);
        }
        surveyRepository.deleteById(survey.getId());
    }

    public String getUrl(Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id).get();
        return survey.getUrl();
    }

//    public void del

    // 1. User id 값으로 설문 리스트 전부가져오기 (설문 리스트: question 이랑 choice 다 포함)
//    public List<SurveyInfo> getSurveyInfoById(Long userId) {return new SurveyInfo();}
    // 2. Json 받아서 저장
//    public boolean saveSurveyInfo(SurveyQuestionDto surveyQuestionInfo) {return true;}
//    // 3. 설문 수정
//    public boolean updateSurveyInfo(SurveyQuestionDto surveyQuestionInfo) {return true;}
//    // 4. 배포 URL
//    public String getReleaseURL(Long surveyKey){return "";}
}

/*
    찬빈 : Restful API naming 하고 Swagger? 써서 프론트에 뿌려주기, 테스트 코드
    두원 : DI 부분 다시 공부하고 Service 작성
 */
