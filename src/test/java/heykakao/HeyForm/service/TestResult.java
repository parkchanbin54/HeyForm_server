//package heykakao.HeyForm.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import heykakao.HeyForm.JasyptConfigDES;
//import heykakao.HeyForm.model.*;
//import heykakao.HeyForm.model.dto.SurveyAnswerDto;
//import heykakao.HeyForm.model.dto.SurveyQuestionDto;
//import heykakao.HeyForm.repository.*;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.transaction.Transactional;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Date;
//import java.util.Optional;
//
//@SpringBootTest
//class TestResult{
////    @Autowired
////    UserRepository userRepository;
////    @Autowired
////    SurveyRepository surveyRepository;
////    @Autowired
////    QuestionRepository questionRepository;
////    @Autowired
////    ChoiceRepository choiceRepository;
////    @Autowired
////    AnswerRepository answerRepository;
////
////    @Autowired
////    DtoService dtoService;
////
////    @Autowired
////    SurveyService surveyService;
////
////    @Autowired
////    KakaoService kakaoService;
//
//
//    @Test
//    public void jasypt_encrypt_decrypt_test() {
//        String plainText = "root";
//
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        encryptor.setProvider(new BouncyCastleProvider());
//        encryptor.setPoolSize(2);
//        encryptor.setPassword("password");
//        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC");
//
//        String encryptedText = encryptor.encrypt(plainText);
//        String decryptedText = encryptor.decrypt(encryptedText);
//
//        System.out.println(encryptedText);
//        System.out.println(decryptedText);
//    }
//
//
////    @Test
////    public void test() throws Exception {
////        JWTService jwtService = new JWTService();
////        String user_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJoZXlmb3JtIiwiZW1haWwiOiJhYm9ZS1RyRXdnNG1VdWhpUG5GcmotOTYtLTVmUXNrTmo2TnVzNzI0Q2lsdnVBQUFBWVJQWC1aRCIsImlhdCI6MTY2Nzc4MTcwNSwiZXhwIjoxNjY3NzgyMDY1fQ.bJd09k77lvHdPcfAkzxJNW-gXTGFRPAWWh2-3UKef8U";
////        Object user_account = jwtService.getClaims(user_token,JWTService.SECRET_KEY).getBody().get("email");
////        String test = "test";
//
////
////        System.out.println(String.valueOf(user_account));
////        Long user_id = userRepository.findByAccount(String.valueOf(user_account)).get().getId();
////
////    }
////    @Test
////    public void timetest(){
////        String tmp = "2022-11-02 22:13:00";
////        Timestamp now = new Timestamp(System.currentTimeMillis());
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////        Timestamp tx = Timestamp.valueOf(tmp);
////        if (tx.before(now)){
////            System.out.println("passed");
////        }
////        else{
////            System.out.println("not yet");
////        }
////        System.out.println(sdf.format(now));
////        System.out.println(tx);
////    }
////    @DisplayName("AIService 테스트")
////    @Test
////    public void AIService() throws Exception{
////        ObjectMapper mapper = new ObjectMapper();
////        AIService aiService = new AIService();
////        String[] tmp ={"환경","스포츠","정치","학교"};
////        System.out.println("RESULT : "+aiService.Category_recommend("환경을 보호해야 된다고 생각하나요?", tmp));
////
////        Survey survey = new Survey(1,"test",userRepository.getReferenceById(1L),"test","ss","환경","환경", "환경을 지켜요");
////        System.out.println(aiService.Category_save(survey));
////    }
////
////    @DisplayName("DtoService saveUser() 테스트")
////    @Test
////    @Transactional
////    public void DtoService_saveUser() throws Exception{
////        User user = new User("Test1","Test2","M","20대","Test");
////        dtoService.saveUser(user);
////    }
////
////    @Test
////    public void dd(){
////        System.out.println(dtoService.getAnswersByUserId(1L));
////    }
////
////    @DisplayName("DtoService saveSurvey() 테스트")
////    @Test
////    @Transactional
////    public void DtoService_saveSurvey() throws Exception {
////        User user = userRepository.getReferenceById(1L);
////        ObjectMapper objectMapper = new ObjectMapper();
////
////        String testJson = "{\"surveyDto\":{\"survey_state\":0,\"start_time\":\"2022-12-11 12:00\",\"end_time\":\"2022-12-11 13:00\",\"category\":null,\"description\":null},\"questionDtos\":[{\"question_type\":1,\"question_order\":2,\"question_contents\":\"qs sample2 bla bla\",\"choiceDtos\":[{\"choice_order\":1,\"choice_contents\":\"ch_sample1 bla bla bla\"}]}]}";
////        SurveyQuestionDto surveyQuestionDto = objectMapper.readValue(testJson, SurveyQuestionDto.class);
////        dtoService.saveSurvey(user.getToken(),surveyQuestionDto);
////    }
////
////    @DisplayName("DtoService saveAnswer() 테스트")
////    @Test
////    @Transactional
////    public void DtoService_saveAnswer() throws Exception{
////        User user = userRepository.getReferenceById(1L);
////        String testJson = "{\"user_token\":\""+user.getToken()+"\",\"survey_id\": 1,\"answerDtos\":[{\"question_order\":2,\"answer_contents\":\"testansewr1\"}]}";
////        System.out.println(testJson);
////        ObjectMapper objectMapper = new ObjectMapper();
////        SurveyAnswerDto surveyAnswerDto = objectMapper.readValue(testJson, SurveyAnswerDto.class);
////        Long survey_id = surveyAnswerDto.getSurvey_id();
////        dtoService.saveAnswer(survey_id, surveyAnswerDto);
////    }
////
////    @DisplayName("DtoService getSurveysByUserToken() 테스트")
////    @Test
////    public void DtoService_getSurveyByUserToken(){
////        User user = userRepository.getReferenceById(1L);
////        dtoService.getSurveysByUserToken(user.getToken());
////    }
////
////    @DisplayName("JWTService 테스트")
////    @Test
////    @Transactional
////    public void JWTService(){
////        JWTService jwtService = new JWTService();
////        String test_email = "Test@Test.com";
////        var token = jwtService.createToken(jwtService.SECRET_KEY,test_email);
////        System.out.println(token);
////        System.out.println(jwtService.getClaims(token, jwtService.SECRET_KEY));
////    }
////
////    @DisplayName("DtoService deleteSurvey() 테스트")
////    @Test
////    @Transactional
////    public void DtoService_deleteSurvey() throws Exception{
////        Long test_id = 1L;
////        surveyService.delSurvey(test_id);
////    }
////
////    @DisplayName("DtoService updateSurvey() 테스트")
////    @Test
////    @Transactional
////    public void DtoService_updateSurvey() throws  Exception{
////
////    }
//
//}