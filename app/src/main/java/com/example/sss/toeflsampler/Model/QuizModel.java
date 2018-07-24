package com.example.sss.toeflsampler.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bulpet on 7.06.2017.
 */

public class QuizModel {
    private String Question;
    private String CorrectAnswer;
    private int CorrectIndex;

    public List<String> getWrongAnswerList() {
        return WrongAnswerList;
    }

    public void setWrongAnswerList(List<String> wrongAnswerList) {
        WrongAnswerList = wrongAnswerList;
    }

    private List<String> WrongAnswerList;

    private boolean IsAnsweredCorrect;
    private  boolean IsAnswered;

    public boolean isAnswered() {
        return IsAnswered;
    }

    public void setAnswered(boolean answered) {
        IsAnswered = answered;
    }

    private int AnswerIndex;
    private  boolean HasImage;
    private int RelationId;

    public boolean isHasImage() {
        return HasImage;
    }

    public void setHasImage(boolean hasImage) {
        HasImage = hasImage;
    }

    public int getAnswerIndex() {
        return AnswerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        AnswerIndex = answerIndex;
    }

    public QuizModel() {
       setWrongAnswerList(new ArrayList<String>());
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public int getCorrectIndex() {
        return CorrectIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        CorrectIndex = correctIndex;
    }

    public boolean isAnsweredCorrect() {
        return IsAnsweredCorrect;
    }

    public void setAnsweredCorrect(boolean answeredCorrect) {
        IsAnsweredCorrect = answeredCorrect;
    }

    public int getRelationId() {
        return RelationId;
    }

    public void setRelationId(int relationId) {
        RelationId = relationId;
    }
}
