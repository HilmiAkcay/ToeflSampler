package com.example.sss.toeflsampler.Model;

import com.example.sss.toeflsampler.Enums.LanguageEnum;
import com.example.sss.toeflsampler.Enums.WordTypeEnum;

/**
 * Created by Bulpet on 24.05.2017.
 */

public class WordModel {

    private int Id;
    private String Name;
    private LanguageEnum LanguageType;
    private WordTypeEnum WordType;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public LanguageEnum getLanguageType() {
        return LanguageType;
    }

    public void setLanguageType(LanguageEnum languageType) {
        LanguageType = languageType;
    }

    public WordTypeEnum getWordType() {
        return WordType;
    }

    public void setWordType(WordTypeEnum wordType) {
        WordType = wordType;
    }
}
