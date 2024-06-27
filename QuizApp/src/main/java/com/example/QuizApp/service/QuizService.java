package com.example.QuizApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.QuizApp.dao.QuestionDao;
import com.example.QuizApp.dao.QuizDao;
import com.example.QuizApp.model.Question;
import com.example.QuizApp.model.QuestionWrapper;
import com.example.QuizApp.model.Quiz;
import com.example.QuizApp.model.Response;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int noOfQuestions, String title) {

        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, noOfQuestions);
        Quiz quiz = new Quiz();

        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz successfully created", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {

        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questions = quiz.get().getQuestions();
        List<QuestionWrapper> quizQuestions= new ArrayList<>();

        for (Question q: questions){
            QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
            quizQuestions.add(questionWrapper);
        }

        return new ResponseEntity<>(quizQuestions, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(int id, List<Response> responses) {

        try {
            Optional<Quiz> quiz = quizDao.findById(id);
            List<Question> questions = quiz.get().getQuestions();
            int right = 0;

            for (Response response: responses){
                Optional<Question> question = questionDao.findById(response.getId());
                if (response.getResponse().equals(question.get().getRightAnswer())) {
					right ++;
				}
            }

            return new ResponseEntity<>(right, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(-99, HttpStatus.BAD_REQUEST);
    }
}
