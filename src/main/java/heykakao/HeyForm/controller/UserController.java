package heykakao.HeyForm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heykakao.HeyForm.model.Survey;
import heykakao.HeyForm.model.User;
import heykakao.HeyForm.model.dto.SurveyQuestionDto;
import heykakao.HeyForm.model.dto.UserDto;
import heykakao.HeyForm.repository.UserRepository;
import heykakao.HeyForm.service.DtoService;
import heykakao.HeyForm.service.KakaoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DtoService dtoService;

    UserDto userDto;

    @Autowired
    KakaoService kakaoService;
    @GetMapping("/user")
    public List<User> getAllUser(){
        return userRepository.findAll();
    }

//    @PostMapping("/user/register")
//    @ApiOperation(value = "사용자 등록" , notes = "사용자 처음 회원가입 할 때 정보를 저장한다. (회원가입)")
//    public User registerUser(@Validated @RequestBody User usr){
//        return dtoService.saveUser(usr);
//    }


    @GetMapping("/user/token/{userEmail}")
    @ApiOperation(value = "사용자 토큰 조회", notes = "사용자의 토큰을 반환한다.")
    public String getTokenByEmail(@PathVariable String userEmail){
        return dtoService.getTokenByEmail(userEmail);
    }

    @GetMapping("/user/id/{userEmail}")
    @ApiOperation(value = "사용자 id 조회", notes = "사용자의 이메일을 반환한다.")
    public Long getIdByEmail(@PathVariable String userEmail){
        return dtoService.getIdByEmail(userEmail);
    }

    @PostMapping("/user/token/request")
    @ApiOperation(value = "유저 토큰 요청", notes = "유저 토큰 요청")
    public User getToken(@RequestParam String Kakaotoken){
        kakaoService.getInfoByKakaoToken(Kakaotoken);
        return dtoService.deleteJWTToken(Kakaotoken);
    }



}

