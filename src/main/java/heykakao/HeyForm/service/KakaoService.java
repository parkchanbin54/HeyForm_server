package heykakao.HeyForm.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import heykakao.HeyForm.model.User;
import heykakao.HeyForm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class KakaoService {

    @Autowired
    UserRepository userRepository;


    public void getInfoByKakaoToken(String token){
        JWTService jwtService = new JWTService();

        String myTocken = "Bearer " + token;

        //헤더 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", myTocken);

        //요청 url
        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl("https://kapi.kakao.com/v2/user/me");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        //요청
        try {

            response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);


        } catch (HttpStatusCodeException e) {

            System.out.println("error :" + e);

        }

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(response.getBody());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement kakao_account = jsonObject.get("kakao_account");

        jsonElement = jsonParser.parse(String.valueOf(kakao_account));
        jsonObject = jsonElement.getAsJsonObject();

        JsonElement nameElement = jsonParser.parse(String.valueOf(jsonObject.get("profile")));
        JsonObject nameObject = nameElement.getAsJsonObject();


        String name = "",email = "",age = "",gender = "";

        name = nameObject.get("nickname").getAsString();


        if (jsonObject.get("is_email_valid").getAsString() == "true"){
            email = jsonObject.get("email").getAsString();

        }
        if (jsonObject.get("has_age_range").getAsString() == "true"){
            String age_range = jsonObject.get("age_range").getAsString();
            if (age_range.equals("0~9") || age_range.equals("10~19")) {
                age = "10대 이하";
            }
            else if (age_range.equals("20~29")) {
                age = "20대";
            }
            else if (age_range.equals("30~39")) {
                age = "30대";
            }
            else if (age_range.equals("40~49")) {
                age = "40대";
            }
            else if (age_range.equals("50~59")) {
                age = "50대";
            }
            else {
                age = "60대 이상";
            }

        }
        if (jsonObject.get("has_gender").getAsString() == "true"){
            gender = jsonObject.get("gender").getAsString();
        }
        User user;
        if (userRepository.findByEmail(email).isPresent()){
            user = userRepository.findByEmail(email).get();
            user.setAccount(token);

//            if (!jwtService.validateToken(userRepository.findByEmail(email).get().getToken())){
//                String jwtToken = jwtService.createToken(JWTService.SECRET_KEY, token);
//                user.setToken(jwtToken);
//                userRepository.save(user);
//                return false;
//            }
            String jwtToken = jwtService.createToken(JWTService.SECRET_KEY, token);
            user.setToken(jwtToken);
            userRepository.save(user);

        }

        else{

            user = new User(token, name,email, age, gender);
            String jwtToken = jwtService.createToken(JWTService.SECRET_KEY, token);
            user.setToken(jwtToken);


            userRepository.save(user);
        }


    }


}
