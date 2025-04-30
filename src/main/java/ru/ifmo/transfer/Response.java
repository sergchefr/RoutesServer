package ru.ifmo.transfer;

import java.io.Serializable;

public class Response implements Serializable {
    private final String answer;
    private static final long serialVersionUID = 2L;

    public Response(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}
