package heykakao.HeyForm.controller;

import heykakao.HeyForm.model.Question;
import heykakao.HeyForm.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/question")
    public List<Question> getAllQuestion(){
        return questionRepository.findAll();
    }
}
