package com.bacchelli.totaljobs.controllers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bacchelli.totaljobs.models.Answer;
import com.bacchelli.totaljobs.models.Answers;

@RestController
@RequestMapping("/")
public class HelloController {
	
	@Value("classpath:data/questions.json")
	Resource questionsFile;
	
	
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@GetMapping(path = "/api/questions", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> apiQuestions() {
        
        try (FileReader reader = new FileReader(questionsFile.getFile()))
        {
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            
            return new ResponseEntity<Object>(obj.toString(), HttpStatus.OK);
            
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			e.printStackTrace();
		}

        return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path = "/api/answers", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> answer(@RequestBody Answers answers) {
		
		String result = "";
		
		if(answers.getAnswers() != null) {
			result = "Answers received #" + answers.getAnswers().size();	
		}else {
			return new ResponseEntity<Object>("No request body received", HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

}