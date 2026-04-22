package com.quiz.model;

import com.quiz.Player;
import com.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class QuizGame {

    private Player player1;
    private Player player2;
    private List<Question> questions;
    private int currentIndex = 0;

    public QuizGame(String name1, String name2, int level) {

        player1 = new Player(name1);
        player2 = new Player(name2);

        questions = new ArrayList<>();
        loadQuestions(level);
    }

    private void loadQuestions(int level) {

        if(level == 1) {   // EASY
            questions.add(new Question("5 + 3 = ?", 8));
            questions.add(new Question("10 - 4 = ?", 6));
            questions.add(new Question("2 * 6 = ?", 12));
            questions.add(new Question("20 / 5 = ?", 4));
        }

        else if(level == 2) {   // MEDIUM
            questions.add(new Question("5 + 5 * 4 = ?", 25));
            questions.add(new Question("12 * 3 - 6 = ?", 30));
            questions.add(new Question("50 / 5 + 7 = ?", 17));
            questions.add(new Question("15 * 2 = ?", 30));
        }

        else {   // HARD
            questions.add(new Question("25 * 4 - 10 = ?", 90));
            questions.add(new Question("36 / 3 + 15 * 2 = ?", 42));
            questions.add(new Question("125 / 5 + 20 = ?", 45));
            questions.add(new Question("(15 + 5) * 3 = ?", 60));
        }
    }

    public Question getCurrentQuestion() {
        if(currentIndex < questions.size()) {
            return questions.get(currentIndex);
        }
        return null;
    }

    public void checkAnswer(int answer1, int answer2) {

        Question q = questions.get(currentIndex);

        if(q.isCorrect(answer1)) {
            player1.incrementScore();
        }

        if(q.isCorrect(answer2)) {
            player2.incrementScore();
        }

        currentIndex++;
    }

    public boolean hasNextQuestion() {
        return currentIndex < questions.size();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}