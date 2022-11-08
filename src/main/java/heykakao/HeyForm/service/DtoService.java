package heykakao.HeyForm.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import heykakao.HeyForm.model.*;
import heykakao.HeyForm.model.dto.*;
import heykakao.HeyForm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DtoService {
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final AnswerRepository answerRepository;

    private final QARepository qaRepository;
    JWTService jwtService = new JWTService();

    @Autowired
    public DtoService(UserRepository userRepository, SurveyRepository surveyRepository, QuestionRepository questionRepository,
                      ChoiceRepository choiceRepository, AnswerRepository answerRepository, QARepository qaRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.answerRepository = answerRepository;
        this.qaRepository = qaRepository;
    }

    // Save
    public Long saveSurvey(String user_token, SurveyQuestionDto surveyQuestionDto) throws Exception {

        Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
        User user = userRepository.findByAccount(user_account.toString()).get();
        SurveyDto surveyDto = surveyQuestionDto.getSurveyDto();

        Survey survey = new Survey();
        survey.setByDto(surveyDto, user);

        surveyRepository.save(survey);
        String url = makeUrl(survey.getId());
        survey.setUrl(url);
        surveyRepository.save(survey);

        AIService aiService = new AIService();
        aiService.Category_save(survey);

        List<QuestionDto> questionDtos = surveyQuestionDto.getQuestionDtos();

        for (QuestionDto questionDto : questionDtos) {
            Question question = new Question();
            question.setByDto(questionDto, survey);
            questionRepository.save(question);

            List<ChoiceDto> choiceDtos = questionDto.getChoiceDtos();

            for (ChoiceDto choiceDto : choiceDtos) {
                Choice choice = new Choice();
                choice.setByDto(choiceDto, question);
                choiceRepository.save(choice);
            }
        }

        return survey.getId();
    }

    public void saveQa(QADto qaDto){
        QA qa = new QA();
        qa.setByDto(qaDto);
        qaRepository.save(qa);
    }
    public void saveQaAnswer(QADto qaDto){
        QA qa = qaRepository.findById(qaDto.getQa_id()).get();
        qa.setByDto(qaDto);
        qaRepository.save(qa);
    }
    public void delQa(Long qa_id){
        qaRepository.deleteById(qa_id);
    }

    //error x
    private String makeUrl(Long survey_id) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(String.valueOf(survey_id).getBytes());
        timecheck(survey_id);
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }

        return hexText;
    }

//    public User saveUser(User user) {
//        String email = user.getEmail();
//        this.userRepository.findByEmail(email)
//                .ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");
//                });
//        String token = jwtService.createToken(JWTService.SECRET_KEY, email);
//        user.setToken(token);
//        return userRepository.save(user);
//    }


    //error x
    public void saveAnswer(Long survey_id, SurveyAnswerDto surveyAnswerDto) {
        try {
            surveyRepository.findById(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
        String user_token = surveyAnswerDto.getUser_token();
        Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
        User user = userRepository.findByAccount(user_account.toString()).get();
        List<AnswerDto> answerDtos = surveyAnswerDto.getAnswerDtos();

        for (AnswerDto answerDto : answerDtos) {
            Integer question_order = answerDto.getQuestion_order();
            Question question = questionRepository.findByOrderAndSurvey_Id(question_order, survey_id).get();
            Answer answer = new Answer();
            answer.setByDto(answerDto, user, question);
            answerRepository.save(answer);
        }
    }

    // Update
    //error x
    public void updateSurveyInfo(SurveyDto surveyDto) {

        try {
            Survey survey = surveyRepository.findById(surveyDto.getSurvey_id()).get();
            survey.setByDto(surveyDto);
            surveyRepository.save(survey);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다");
        }

    }

    public void updateQuestion(Long survey_id, QuestionDto questionDto) {
        try {
            surveyRepository.findById(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
        try {
            Question question = questionRepository.findByOrderAndSurvey_Id(questionDto.getQuestion_order(), survey_id).get();
            question.setByDto(questionDto);
            questionRepository.save(question);
        } catch (Exception e) {
            throw new IllegalStateException("해당 질문이 존재하지 않습니다.");
        }
    }

    public void updateChoice(Long question_id, ChoiceDto choiceDto) {
        try {
            Choice choice = choiceRepository.findByOrderAndQuestion_Id(choiceDto.getChoice_order(), question_id).get();
            choice.setByDto(choiceDto);
            choiceRepository.save(choice);
        } catch (Exception e) {
            throw new IllegalStateException("해당 choice가 존재하지 않습니다.");
        }
    }

    //error x
    public void updateAllChoices(Long question_id, List<ChoiceDto> choiceDtos) {
        try {
            questionRepository.findById(question_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 질문이 존재하지 않습니다.");
        }
        try {
            List<Choice> choices = choiceRepository.findByQuestion_Id(question_id);
            for (Choice choice : choices) {
                ChoiceDto choiceDto = choiceDtos.stream().filter(ch_dto -> ch_dto.getChoice_order().equals(choice.getOrder())).collect(Collectors.toList()).get(0);
                choice.setByDto(choiceDto);
                choiceRepository.save(choice);
            }
        } catch (Exception e) {
            throw new IllegalStateException("해당 choice가 존재하지 않습니다.");
        }
    }

    //error x
    public void updateAllQuestions(Long survey_id, List<QuestionDto> questionDtos) {
        try {
            surveyRepository.findById(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
        try {
            List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
            for (Question question : questions) {
                QuestionDto questionDto = questionDtos.stream().filter(qs_dto -> qs_dto.getQuestion_order().equals(question.getOrder())).collect(Collectors.toList()).get(0);
                question.setByDto(questionDto);
                questionRepository.save(question);
                updateAllChoices(question.getId(), questionDto.getChoiceDtos());
            }
        } catch (Exception e) {
            throw new IllegalStateException("해당 질문이 존재하지 않습니다,");
        }
    }

    //error x
    public String updateSurvey(SurveyQuestionDto surveyQuestionDto) throws NoSuchAlgorithmException {

        SurveyDto surveyDto = surveyQuestionDto.getSurveyDto();
        List<QuestionDto> questionDtos = surveyQuestionDto.getQuestionDtos();
        updateSurveyInfo(surveyDto);
        updateAllQuestions(surveyDto.getSurvey_id(), questionDtos);
        Survey survey = surveyRepository.getReferenceById(surveyDto.getSurvey_id());
        survey.setUrl(makeUrl(surveyDto.getSurvey_id()));
        surveyRepository.save(survey);
        return survey.getUrl();
    }

    //error x
    public void updateAnswer(Long survey_id, SurveyAnswerDto surveyAnswerDto) {
        try {
            surveyRepository.findById(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
        try {
            String user_token = surveyAnswerDto.getUser_token();
            Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
            User user = userRepository.findByAccount(user_account.toString()).get();
            List<AnswerDto> answerDtos = surveyAnswerDto.getAnswerDtos();

            for (AnswerDto answerDto : answerDtos) {
                Integer question_order = answerDto.getQuestion_order();
                Question question = questionRepository.findByOrderAndSurvey_Id(question_order, survey_id).get();
                Answer answer = answerRepository.findByUser_AccountAndQuestion_Id(user_account.toString(), question.getId()).get();
                answer.setByDto(answerDto);
                answerRepository.save(answer);
            }
        } catch (Exception e) {
            throw new IllegalStateException("해당 답변이 없습니다.");
        }
    }

    // Get
    public List<SurveyQuestionDto> getSurveysByUserToken(String user_token) {
        try {
            Object user_account = jwtService.getClaims(jwtService.getClaims(user_token,JWTService.SECRET_KEY),"email");
            Long user_id = userRepository.findByAccount(String.valueOf(user_account)).get().getId();

            return getSurveyQuestionDtos(user_id);
        } catch (Exception e) {
            throw new IllegalStateException("일치 정보가 없습니다.");
        }
    }


    public SurveyQuestionDto getSurveyQuestionBySurveyId(Long survey_id) {

        try {
            return getSurveyQuestionDto(survey_id);
        } catch (Exception e) {
            throw new RuntimeException("없는 설문조사입니다.");
        }
    }

    public SurveyQuestionDto getSurveyQuestionByUrl(String survey_url) {
        try {
            Survey survey = surveyRepository.findByUrl(survey_url).get();
            return survey2surveyQuestionDto(survey);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
    }

    public List<AnswerDto> getAnswersBySurveyId(Long survey_id) {
        try {
            return getSurveyAnswerDto(survey_id);
        } catch (Exception e) {
            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
        }
    }

//    public List<AnswerDto> getAnswersByUserId(Long user_id) {
//        try {
//            return getSurveyAnswerDtoByUserId(user_id);
//        } catch (Exception e) {
//            throw new IllegalStateException("해당 설문이 존재하지 않습니다.");
//        }
//    }

//    public List<SurveyAnswerDto> getSurveyAnswerBySurveyId(Long user_id) {
//        return getSurveyAnswerDtos2(user_id);
//    }

    public String getTokenByEmail(String user_email) {
        Optional<User> user = userRepository.findByEmail(user_email);
        String jwtToken = jwtService.createToken(JWTService.SECRET_KEY,user.get().getAccount());
        return jwtToken;
    }

    private SurveyQuestionDto survey2surveyQuestionDto(Survey survey) {
        try {
            SurveyDto surveyDto = new SurveyDto(survey);
            List<QuestionDto> questionDtos = new ArrayList<>();
            List<Question> questions = questionRepository.findBySurvey_Id(survey.getId());

            for (Question question : questions) {
                questionDtos.add(question2questionDto(question));
            }

            return new SurveyQuestionDto(surveyDto, questionDtos);
        } catch (Exception e) {
            throw new IllegalStateException("오류");
        }
    }

    private QuestionDto question2questionDto(Question question) {
        try {
            List<Choice> choices = choiceRepository.findByQuestion_Id(question.getId());
            List<ChoiceDto> choiceDtos = new ArrayList<>();
            for (Choice choice : choices) {
                ChoiceDto choiceDto = new ChoiceDto(choice);
                choiceDtos.add(choiceDto);
            }
            return new QuestionDto(question, choiceDtos);
        } catch (Exception e) {
            throw new IllegalStateException("오류");
        }
    }

    private List<ChoiceDto> getChoiceDtos(Long question_id) {
        List<ChoiceDto> choiceDtos = new ArrayList<>();
        List<Choice> choices = choiceRepository.findByQuestion_Id(question_id);
        for (Choice choice : choices) {
            ChoiceDto choiceDto = new ChoiceDto(choice);
            choiceDtos.add(choiceDto);
        }
        return choiceDtos;
    }

    private List<QuestionDto> getQuestionDtos(Long survey_id) {
        List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question : questions) {
            List<ChoiceDto> choiceDtos = getChoiceDtos(question.getId());

            QuestionDto questionDto = new QuestionDto(question, choiceDtos);
            questionDtos.add(questionDto);
        }
        return questionDtos;
    }

    //    private SurveyAnswerDto getSurveyAnswerDto(Long survey_id, String user_token){
//        Survey survey = surveyRepository.findById(survey_id).get();
//        SurveyDto surveyDto = new SurveyDto(survey);
//
//        List<AnswerDto> answerDtos = getAnswerDtos
//    }
//    private SurveyAnswerDto getSurveyAnswerDto(Long survey_id, String user_token){
//        Survey survey = surveyRepository.findById(survey_id).get();
//        SurveyDto surveyDto = new SurveyDto(survey);
//
//        List<AnswerDto> answerDtos = getSurveyAnswerDtos(survey_id,user_token);
//        return new SurveyAnswerDto(surveyDto,answerDtos);
//    }

    //여기
    private SurveyQuestionDto getSurveyQuestionDto(Long survey_id) {
        Survey survey = surveyRepository.findById(survey_id).get();
        SurveyDto surveyDto = new SurveyDto(survey);
        timecheck(surveyDto.getSurvey_id());
        List<QuestionDto> questionDtos = getQuestionDtos(survey_id);

        return new SurveyQuestionDto(surveyDto, questionDtos);
    }

    //여기
    private List<SurveyQuestionDto> getSurveyQuestionDtos(Long user_id) {
        List<SurveyQuestionDto> surveyQuestionDtos = new ArrayList<>();
        List<Long> survey_ids = surveyRepository.findByUser_Id(user_id).stream().map(Survey::getId).collect(Collectors.toList());
        for (Long survey_id : survey_ids) {
            timecheck(survey_id);
            surveyQuestionDtos.add(getSurveyQuestionDto(survey_id));
        }
        return surveyQuestionDtos;
    }
//
//    private List<SurveyAnswerDto> getSurveyAnswerDtos(Long user_id) {
//        List<SurveyAnswerDto> surveyAnswerDtos = new ArrayList<>();
//        List<Long> survey_ids = surveyRepository.findByUser_Id(user_id).stream().map(Survey::getId).collect(Collectors.toList());
//        for (Long survey_id : survey_ids) {
//
//        }
//    }
//
//        private List<SurveyAnswerDto> getSurveyAnswerDtos(Long survey_Id,String user_token){
//        List<SurveyAnswerDto> surveyAnswerDtos = new ArrayList<>();
//        List<Long> survey_ids = surveyRepository.findById(survey_Id).stream().map(Survey::getId).collect(Collectors.toList());
//        for(Long survey_id : survey_ids){
//            surveyAnswerDtos.add(getSurveyAnswerDto(survey_id,user_token));
//        }
//    }
    public Long getIdByEmail(String user_email) {
        Optional<User> user = userRepository.findByEmail(user_email);
        return user.get().getId();
    }

    private List<AnswerDto> getSurveyAnswerDto(Long survey_id) {
        List<AnswerDto> answerDtos = new ArrayList<>();

        List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
        for (Question question : questions) {

            List<Answer> answers = answerRepository.findByQuestion_Id(question.getId());
            for (Answer answer : answers) {
                AnswerDto answerDto = new AnswerDto(answer);
                answerDtos.add(answerDto);
            }
            }
        return answerDtos;
    }

//    private List<AnswerDto> getSurveyAnswerDtoByUserId(Long user_id) {
//        List<AnswerDto> answerDtos = new ArrayList<>();
//        List<Answer> answers = answerRepository.findByUser_Id(user_id);
//
//        for (Answer answer : answers) {
//            AnswerDto answerDto = new AnswerDto(answer);
//            answerDtos.add(answerDto);
//        }
//        return answerDtos;
//    }

    public List<SurveyAnswerDto> getSurveyAnswerDtoByUserId(Long user_id) {

        List<Long> answer_ids = answerRepository.findByUser_Id(user_id).stream().map(Answer::getId).collect(Collectors.toList());
        List<Long> survey_ids = new ArrayList<>();
        for (Long answer_id : answer_ids){
            List <Long> questions_ids = questionRepository.findById(answerRepository.getReferenceById(answer_id).getQuestion().getId()).stream().map(Question::getId).collect(Collectors.toList());;
            for (Long question_id : questions_ids){
                Long survey_id = questionRepository.findById(question_id).get().getSurvey().getId();
                if (survey_ids.contains(survey_id) == false){
                    survey_ids.add(survey_id);
                }
            }
        }


        String user_account = userRepository.findById(user_id).get().getAccount();
        String user_token = jwtService.createToken(JWTService.SECRET_KEY,user_account);
        List<SurveyAnswerDto> surveyAnswerDtos = new ArrayList<>();
        User user = userRepository.getReferenceById(user_id);
        for (Long survey_id : survey_ids) {
            List<AnswerDto> answerDtos = new ArrayList<>();

            List<Question> questions = questionRepository.findBySurvey_Id(survey_id);
            for (Question question : questions) {

                    List<Answer> answers = answerRepository.findByQuestion_Id(question.getId());
                    for (Answer answer : answers) {
                        if (answer.getUser().getId() == user_id) {

                            AnswerDto answerDto = new AnswerDto(answer);
                            answerDtos.add(answerDto);
                        }
                    }
                }


            SurveyAnswerDto surveyAnswerDto = new SurveyAnswerDto(questions.get(0).getSurvey().getId(),user_token,user.getGender(),user.getAge(),user.getId(),answerDtos);
            surveyAnswerDtos.add(surveyAnswerDto);
        }

        return surveyAnswerDtos;
    }
    private void timecheck(Long survey_id){
        Survey survey = surveyRepository.getReferenceById(survey_id);
        Timestamp start_time = Timestamp.valueOf(survey.getStarttime());
        Timestamp end_time = Timestamp.valueOf(survey.getEndtime());
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(end_time.before(now)){
            survey.setState(2);
        }
        else if (start_time.after(now)){
            survey.setState(0);
        }
    }
    public boolean Urlcheck(String url){
        if (surveyRepository.findByUrl(url).isPresent()){return true;}
        else{return false;}
    }


    public String statistic(Long survey_id){
        List<Long> question_ids = questionRepository.findBySurvey_Id(survey_id).stream().map(Question::getId).collect(Collectors.toList());
        JSONObject result = new JSONObject();
        Integer sequence = 0;
        for(Long question_id : question_ids){
            JSONArray titleJson = new JSONArray();
            JSONArray genderJson = new JSONArray();
            JSONArray ageJson = new JSONArray();


            LinkedHashMap<String,Integer> titles = new LinkedHashMap<>();
            LinkedHashMap<String,Integer> genders = new LinkedHashMap<>();
            LinkedHashMap<String,Integer> ages = new LinkedHashMap<>();

            Question question = questionRepository.getReferenceById(question_id);
            if (question.getType() == "주관식"){
                return "주관식";
            }

            else {
                List<Answer> answers = answerRepository.findByQuestion_Id(question.getId()).stream().collect(Collectors.toList());
                for (Answer answer : answers) {

                    String content = answer.getContents();
                    if (!titles.containsKey(content)) {
                        titles.put(content, 1);
                    } else {
                        titles.replace(content, titles.get(content) + 1);
                    }


                    String gender = answer.getUser().getGender();
                    if (!genders.containsKey(gender)){
                        genders.put(gender,1);
                    }
                    else{
                        genders.replace(gender, genders.get(gender) + 1);
                    }

                    String age = answer.getUser().getAge();
                    if(!ages.containsKey(age)){
                        ages.put(age,1);
                    }
                    else{
                        ages.replace(age,ages.get(age)+1);
                    }
                }

                for (String title : titles.keySet()) {
                    JSONObject jsonObject_title = new JSONObject();
                    if (question.getType().equals("단답식"))
                    {
                        jsonObject_title.put("value",title);
                        jsonObject_title.put("count",titles.get(title));
                    }
                    else if(question.getType().equals("객관식")){


                        jsonObject_title.put("choice",title);
                        jsonObject_title.put("응답수",titles.get(title));

                    }
                    else if(question.getType().equals("리커트")){
                        jsonObject_title.put("likert",title);
                        jsonObject_title.put("응답수",titles.get(title));
                    }
                    else if(question.getType().equals("별점")){
                        jsonObject_title.put("star",title);
                        jsonObject_title.put("응답수",titles.get(title));
                    }
                    else if(question.getType().equals("감정바")){
                        jsonObject_title.put("id",title);
                        jsonObject_title.put("value",titles.get(title));
                    }
                    titleJson.add(jsonObject_title);
                }

                for (String gender : genders.keySet()){
                    JSONObject jsonObject_gender = new JSONObject();
                    jsonObject_gender.put("gender",gender);
                    jsonObject_gender.put("응답자수",genders.get(gender));
                    genderJson.add(jsonObject_gender);
                }

                for (String age : ages.keySet()){
                    JSONObject jsonObject_age = new JSONObject();
                    jsonObject_age.put("id",age);
                    jsonObject_age.put("value",ages.get(age));
                    ageJson.add(jsonObject_age);
                }
            }

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(titleJson);
            jsonArray.add(genderJson);
            jsonArray.add(ageJson);
            sequence = sequence + 1;
            result.put(sequence,jsonArray);
        }
        Gson gson = new Gson();

        return gson.toJson(result);
    }

    public boolean tokencheck(String token){

        if (!jwtService.validateToken(token)){
            return false;
        }
        else{
            return true;
        }
    }

    public User findUserByAccount(String Account){
        return userRepository.findByAccount(Account).get();
    }

    public User deleteJWTToken(String kakaoToken){
        User user = userRepository.findByAccount(kakaoToken).get();
        String jwtToken = user.getToken();
        user.setToken("");
        userRepository.save(user);
        user.setToken(jwtToken);
        return user;
    }
}
