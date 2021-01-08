package com.example.smartly;

class TFQuestion {
    String question;
    boolean ans;

    public TFQuestion(String question, boolean ans) {
        this.question = question;
        this.ans = ans;
    }
    public TFQuestion() {

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isAns() {
        return ans;
    }

    public void setAns(boolean ans) {
        this.ans = ans;
    }
}
