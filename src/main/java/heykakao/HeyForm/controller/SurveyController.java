package heykakao.HeyForm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heykakao.HeyForm.exception.ResourceNotFoundException;
import heykakao.HeyForm.exception.ResponseEntityHandler;
import heykakao.HeyForm.model.User;
import heykakao.HeyForm.model.dto.SurveyAnswerDto;
import heykakao.HeyForm.model.dto.SurveyQuestionDto;
import heykakao.HeyForm.repository.*;
import heykakao.HeyForm.service.AIService;
import heykakao.HeyForm.service.DtoService;
import heykakao.HeyForm.service.SurveyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
//@RequestMapping("/api/v1/")
@ResponseBody
public class SurveyController {
    @Autowired
    private final UserRepository userRepository;
    private final DtoService dtoService;
    private final SurveyService surveyService;


    @Autowired
    public SurveyController(UserRepository userRepository, DtoService dtoService, SurveyService surveyService){
        this.userRepository = userRepository;
        this.dtoService = dtoService;
        this.surveyService = surveyService;
    }


    //Surveyjson type 리턴값 : 설문 url
    //{"surveyDto":{"survey_state":0,"survey_url":"c4ca4238a0b923820dcc509a6f75849b","start_time":1670727600000,"end_time":1670817600000,"category":null,"description":null},"questionDtos":[{"question_type":1,"question_order":2,"question_contents":"qs sample2 bla bla","choiceDtos":[{"choice_order":1,"choice_contents":"ch_sample1 bla bla bla"}]}]}
    @PostMapping("/survey/{userToken}")
    @ApiOperation(value = "설문조사 생성", notes = "사용자 token을 사용해서 설문조사를 생성한다. 생성된 설문조사의 페이지 Url이 리턴된다. (설문조사 제작) 양식 : {\"surveyDto\":{\"survey_state\":0,\"start_time\":\"2022-12-11 12:00\",\"end_time\":\"2022-12-11 13:00\",\"category\":null,\"description\":null},\"questionDtos\":[{\"question_type\":1,\"question_order\":2,\"question_contents\":\"qs sample2 bla bla\",\"choiceDtos\":[{\"choice_order\":1,\"choice_contents\":\"ch_sample1 bla bla bla\"}]}]}")
    public String createSurvey(@RequestBody SurveyQuestionDto surveyDto, @PathVariable String userToken) throws Exception {
        if (!dtoService.tokencheck(userToken)){
            throw new ResourceNotFoundException(String.format("Expired token: %s",userToken));
        }
        Long survey_id = dtoService.saveSurvey(userToken,surveyDto);
        return surveyService.getUrl(survey_id);
    }

    //설문 정보를 url을 통해 전달한다.
    @GetMapping("/survey/paper/{surveyUrl}")
    @ApiOperation(value = "설문조사 페이지 생성", notes = "url을 이용해서 설문조사 정보를 불러온다. (설문조사 url)")
    public SurveyQuestionDto createPaperByURL(@PathVariable String surveyUrl){
        return dtoService.getSurveyQuestionByUrl(surveyUrl);
    }


    @PostMapping("/survey/title/recommand")
    @ApiOperation(value = "설문조사 카테고리 추천", notes = "설문조사 제목으로 카테고리 추천해준다. 설문 제목 입력후 다음 행동에 부여\n Input: 환경을 보호해야 된다고 생각하나요? , {\"환경\",\"스포츠\",\"정치\",\"학교\"} \nOutput: {\"total_score\":0.25,\"details\":[{\"reference_keyword\":\"환경\",\"matched_keyword\":\"환경\",\"score\":1.0},{\"reference_keyword\":\"스포츠\",\"matched_keyword\":null,\"score\":0.0},{\"reference_keyword\":\"정치\",\"matched_keyword\":null,\"score\":0.0},{\"reference_keyword\":\"학교\",\"matched_keyword\":null,\"score\":0.0}]}")
    public String reccomandCategory(@RequestParam String title, String[] categories) throws Exception {
        AIService aiService = new AIService();
        return aiService.Category_recommend(title, categories);
    }

    //{"user_token":"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJoZXlmb3JtIiwiZW1haWwiOiJ0ZXN0MSIsImlhdCI6MTY2NjY1OTI2NiwiZXhwIjoxNjY2NjYyODY2fQ.o4kat2Tj6NGWPnurfEsaP5PrPT6V8hqw5ZEye0mUjIA","survey_id":1,"answerDtos":[{"question_order":1,"answer_contents":"testansewr1"},{"question_order":2,"answer_contents":"test ansewr1"}]}
    @PostMapping("/survey/paper/result")
    @ApiOperation(value = "설문조사 응답 저장", notes = "Url을 통해 설문조사 완료 한 응답들을 저장한다. (설문조사 Url 제출 버튼) 양식 : {\"user_token\":\"Token\",\"survey_id\": 1,\"answerDtos\":[{\"question_order\":1,\"answer_contents\":\"testansewr1\"}]}")
    public void postSurveyResult(@RequestBody SurveyAnswerDto surveyAnswerDto) {
        Long survey_id = surveyAnswerDto.getSurvey_id();
        dtoService.saveAnswer(survey_id, surveyAnswerDto);
    }

    @DeleteMapping("/survey/{surveyId}")
    @ApiOperation(value = "설문조사 제거", notes = "설문조사 id를 이용해서 설문조사를 제거한다. (마이페이지 / 설문조사 제거)")
    public void deleteSurvey(@RequestParam Long surveyId, @RequestParam String userToken){
        if (!dtoService.tokencheck(userToken)){
            throw new ResourceNotFoundException(String.format("Expired token: %s",userToken));
        }
        surveyService.delSurvey(surveyId);
    }

    //surveyId를 통해 업데이트..
    @PostMapping("/survey/update")
    @ApiOperation(value = "설문조사 업데이트", notes = "설문조사를 업데이트한다. (마이페이지 / 설문조사 업데이트)")
    public String updateSurvey(@RequestBody SurveyQuestionDto surveyQuestionDto, @RequestParam String userToken) throws JsonProcessingException, NoSuchAlgorithmException {
        if (!dtoService.tokencheck(userToken)){
            throw new ResourceNotFoundException(String.format("Expired token: %s",userToken));
        }
        return dtoService.updateSurvey(surveyQuestionDto);
    }

    //surveyId를 통해 설문지 정보 불러오기
    @GetMapping("/survey/list/{surveyId}")
    @ApiOperation(value = "설문조사 정보 조회" , notes = "설문조사 id를 이용해서 설문조사 정보를 불러온다. (마이페이지 / 설문조사 정보보기)")
    public SurveyQuestionDto getSurveyInfoBySurveyId(@PathVariable Long surveyId, @RequestParam String userToken){
        if (!dtoService.tokencheck(userToken)){
            throw new ResourceNotFoundException(String.format("Expired token: %s",userToken));
        }
        SurveyQuestionDto surveyQuestionDto = dtoService.getSurveyQuestionBySurveyId(surveyId);
        return surveyQuestionDto;
    }

    // userId를 통해 해당 유저의  survey, question, answer 정보 모두 불러오기
    @GetMapping("/survey/total/{userToken}")
    @ApiOperation(value = "사용자 정보 조회", notes = "사용자 token을 사용해서 사용자가 생성한 설문조사 리스트 가져오기. (마이페이지)")
    public List<SurveyQuestionDto> getInfoByUserAccount(@PathVariable String userToken){
        if (!dtoService.tokencheck(userToken)){
            throw new ResourceNotFoundException(String.format("Expired token: %s",userToken));
        }
        return dtoService.getSurveysByUserToken(userToken);
    }

    @GetMapping("/survey/check/{surveyUrl}")
    @ApiOperation(value = "설문조사 url 조회", notes = "url이 존재하는지 확인")
    public boolean checkUrl(@PathVariable String surveyUrl, @RequestParam String userToken){
        if (!dtoService.tokencheck(userToken)){
            throw new ResourceNotFoundException(String.format("Expired token: %s",userToken));
        }
        return dtoService.Urlcheck(surveyUrl);
    }

    // 테스트용
    @GetMapping("/survey")
    @ApiOperation(value = "Test 용")
    public List<List<SurveyQuestionDto>> getAllSurvey(){
        List<User> users = userRepository.findAll();
        List<List<SurveyQuestionDto>> allInfo = new ArrayList<>();
        for (User user : users) {
            List<SurveyQuestionDto> surveyQuestionDtos =  dtoService.getSurveysByUserToken(user.getAccount());
            allInfo.add(surveyQuestionDtos);
        }

        return allInfo;
    }
}