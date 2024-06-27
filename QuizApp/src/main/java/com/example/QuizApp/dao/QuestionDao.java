package com.example.QuizApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.QuizApp.model.Question;

@Repository
public interface QuestionDao extends JpaRepository<Question, Integer> {
    List<Question> findByCategory(String category);

    @Query(value = "SELECT * FROM Question q WHERE q.CATEGORY=:category ORDER BY Random() LIMIT :noOfQuestions",nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(String category, int noOfQuestions);
}
