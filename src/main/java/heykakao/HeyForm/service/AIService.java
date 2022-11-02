package heykakao.HeyForm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import heykakao.HeyForm.model.Survey;
import heykakao.HeyForm.repository.SurveyRepository;
import okhttp3.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class AIService {
    @Autowired
    SurveyRepository surveyRepository;
    // target, categories json화
    public String Category_recommend(String target, String[] categories) throws Exception {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(categories);
        String json = "{\"titles\":\""+target+"\","+"\"categories\":"+jsonInString+"}";
        System.out.println(json);
        try{
            HttpClient client = new DefaultHttpClient();
            MyHttpGetWithEntity e = new MyHttpGetWithEntity("http://210.109.61.98:8000/api/ai/category");
            e.setEntity(new StringEntity(json,"UTF-8"));
            HttpResponse response = client.execute(e);
            if (response.getStatusLine().getStatusCode() == 200) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                result = handler.handleResponse(response);
            }

        }catch (Exception e){System.err.println(e);}

        return result;
    }

    public String Category_save(Survey survey){
        String json = "{\"title\":\""+survey.getSurveytitle()+"\","+"\"category\":\""+survey.getCategory()+"\"}";
        System.out.println("JSON: "+ json);
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://210.109.61.98:8000/api/ai/category");
            postRequest.setEntity(new StringEntity(json,"UTF-8"));
            HttpResponse response = client.execute(postRequest);
            if (response.getStatusLine().getStatusCode() == 204) {
                return "Sucess";
            }
            else{
                return "Fail";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//    public void Category_save(Survey survey) throws Exception
//    {
//        String json = "{\"title\":\""+survey.getSurveytitle()+"\","+"\"category\":"+survey.getCategory()+"}";
//        System.out.println("JSON: "+ json);
//        try{
//
//            OkHttpClient client = new OkHttpClient();
//            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//            Request.Builder builder = new Request.Builder().url("http://210.109.61.98:8000/api/ai/category")
//                    .post(requestBody);
//            Request request = builder.build();
//
//            Response response = client.newCall(request).execute();
//
//            if (response.isSuccessful()){
//                ResponseBody body = response.body();
//                if (body != null){
//                    System.out.println("Response:" + body.string());
//                }
//                else
//                    System.out.println("Error");
//            }
//            else{
//                System.out.println("fail");
//            }
//
//        }catch (Exception e){e.printStackTrace();}
//    }
}
