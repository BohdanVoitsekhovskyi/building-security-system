package com.building_security_system.models;

import lombok.Getter;

@Getter
public enum AnswerType {
    MOTION(" увімкнено запис відеокамери.",
            "система повідомила охорону.",
            "увімкнено підсвічування."),
    SMOKE(" увімкнено витяжну систему.",
            " попередження надіслано адміністрації.",
            " активовано аварійне оповіщення."),
    TEMPERATURE(" увімкнено охолоджувальну систему.",
            " повідомлення надіслано технічному персоналу."),
    FLOOD(" увімкнено аварійний насос.",
            " сповіщено технічний персонал.",
            " система перекрила подачу води.",
            " надіслано сигнал тривоги."
            );


    private final String[] answers;
    AnswerType(String... answers) {
        this.answers = answers;
    }

}
