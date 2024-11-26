package com.building_security_system.util;

public class Randomizer {
    public static int getRandomNumber(int upperLimit ){
        return (int)(Math.random() * ( upperLimit+ 1));
    }
}
