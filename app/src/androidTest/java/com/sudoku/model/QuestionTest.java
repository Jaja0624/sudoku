package com.sudoku.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionTest {

    @Test
    public void getQuestion() {
        String question = "ni hao";
        String ans = "hello";
        Question q = new Question(question, ans);
        assertEquals(question, q.getQuestion());
        assertNotEquals("hello", q.getQuestion());

        q = new Question("", ans);
        assertEquals("", q.getQuestion());
        assertNotEquals(" ", q.getQuestion());
    }

    @Test
    public void getAnswer() {
        String question = "ni hao";
        String ans = "hello";
        Question q = new Question(question, ans);
        assertEquals(ans, q.getAnswer());
        assertNotEquals("hello", q.getAnswer());

        q = new Question("", "");
        assertEquals("", q.getAnswer());
        assertNotEquals(" ", q.getAnswer());

    }
}