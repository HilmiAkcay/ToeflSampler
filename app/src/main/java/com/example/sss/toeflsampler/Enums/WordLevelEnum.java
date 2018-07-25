package com.example.sss.toeflsampler.Enums;

/**
 * Created by Bulpet on 31.05.2017.
 */

public enum WordLevelEnum {
    Easy,
    Medium,
    Hard,
    Common;


    public static Integer StringToInt(String strVal)
    {
        if(strVal == WordLevelEnum.Easy.name())
            return  0;
        else if(strVal == WordLevelEnum.Medium.name())
            return  1;
        else if(strVal == WordLevelEnum.Hard.name())
            return  2;
        else if(strVal == WordLevelEnum.Common.name())
            return  3;
        else return  -1;

    }

    public static String IntToString(int enumVal) {
        String val = "";
        switch (enumVal) {
            case 0: {
                val = Easy.name();
                break;
            }
            case 1: {
                val = Medium.name();
                break;
            }
            case 2:{
                val = Hard.name();
                break;
            }
            case 3:{
                val = Common.name();
                break;
            }
            default:
                break;
        }

        return val;
    }
}
