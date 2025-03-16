package com.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class AnswerSubmitDTO {
    private List<Answer> answers;

    @Data
    public static class Answer {
        private Long questionId;
        private String optionValue;
    }
}