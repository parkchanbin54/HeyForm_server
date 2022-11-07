package heykakao.HeyForm.controller;
import heykakao.HeyForm.model.Answer;
import heykakao.HeyForm.model.QA;
import heykakao.HeyForm.model.dto.AnswerDto;
import heykakao.HeyForm.model.dto.QADto;
import heykakao.HeyForm.model.dto.SurveyAnswerDto;
import heykakao.HeyForm.repository.AnswerRepository;
import heykakao.HeyForm.repository.QARepository;
import heykakao.HeyForm.service.DtoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@ResponseBody
public class QAController{

    @Autowired
    QARepository qaRepository;

    @Autowired
    DtoService dtoService;
    @GetMapping("/qa")
    public List<QA> getQA(){
        return qaRepository.findAll();
    }

//    @PostMapping("/qa/save/answer")
//    @ApiOperation(value = "QA 답변 등록", notes = "답변 등록")
//    public void saveQaAnswer(@RequestBody QADto qaDto){
//        dtoService.saveQaAnswer(qaDto);
//    }

    @PostMapping("/qa/save")
    @ApiOperation(value = "QA 등록", notes = "QA 등록")
    public void saveQa(@RequestBody QADto qaDto){
        dtoService.saveQa(qaDto);
    }

//    @PostMapping("/qa/delete/{qaId}")
//    @ApiOperation(value = "QA 삭제")
//    public void delQa(@PathVariable Long qaId){
//        dtoService.delQa(qaId);
//    }

}
