package com.example.sss.toeflsampler.Enums;

/**
 * Created by Bulpet on 24.05.2017.
 */

public enum WordTypeEnum {
    Noun,
    Verb,
    Adjective,
    Adverb,
    Preposition,
    Conjunction;

    public static Integer StringToInt(String strVal)
    {
        if(strVal == WordTypeEnum.Noun.name())
            return  0;
        else if(strVal == WordTypeEnum.Verb.name())
            return  1;
        else if(strVal == WordTypeEnum.Adjective.name())
            return  2;
        else if(strVal == WordTypeEnum.Adverb.name())
            return  3;
        else if(strVal == WordTypeEnum.Preposition.name())
            return  4;
        else if(strVal == WordTypeEnum.Conjunction.name())
            return  5;
        else return  -1;

    }

    public static String IntToString(int enumVal) {
        String val = "";
        switch (enumVal) {
            case 0: {
                val = Noun.name();
                break;
            }
            case 1: {
                val = Verb.name();
                break;
            }
            case 2:{
                val = Adjective.name();
                break;
            }
            case 3:{
                val = Adverb.name();
                break;
            }
            case 4:
            {
                val = Preposition.name();
                break;
            }
            case 5:
            {
                val = Conjunction.name();
                break;
            }
            default:
                break;
        }

        return val;
    }
}