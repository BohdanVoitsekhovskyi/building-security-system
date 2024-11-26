package com.building_security_system.util;

import com.building_security_system.models.AnswerType;
import com.building_security_system.models.Detector;

public class AnswerManager {

    public static String getAnswer(Detector detector){


        String triggeredDetectorTypeName = detector.getType().name();
        AnswerType answer = AnswerType.valueOf(triggeredDetectorTypeName);

        String[] possibleAnswers = answer.getAnswers();

        return possibleAnswers[Randomizer.getRandomNumber(possibleAnswers.length)];
    }
}
