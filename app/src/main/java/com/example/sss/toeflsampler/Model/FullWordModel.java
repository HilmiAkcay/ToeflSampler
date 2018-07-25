package com.example.sss.toeflsampler.Model;

import java.io.Serializable;

/**
 * Created by Bulpet on 25.05.2017.
 */

public class FullWordModel implements Serializable {
    public int RelationId;
    public int TrId;
    public String TrWord;
    public int EngId;
    public String EngWord;
    public int Type;
    public int Hardness;
    public String Sample;

    public int getSampleId() {
        return SampleId;
    }

    public void setSampleId(int sampleId) {
        SampleId = sampleId;
    }

    public  int SampleId;

    public String getSample() {
        return Sample;
    }

    public void setSample(String sample) {
        Sample = sample;
    }

    public int getTrId() {
        return TrId;
    }

    public void setTrId(int trId) {
        TrId = trId;
    }

    public int getEngId() {
        return EngId;
    }

    public void setEngId(int engId) {
        EngId = engId;
    }

    public int getHardness() {
        return Hardness;
    }

    public void setHardness(int hardness) {
        Hardness = hardness;
    }

    public int getRelationId() {
        return RelationId;
    }

    public void setRelationId(int relationId) {
        RelationId = relationId;
    }

    public String getTrWord() {
        return TrWord;
    }

    public void setTrWord(String trWord) {
        TrWord = trWord;
    }

    public String getEngWord() {
        return EngWord;
    }

    public void setEngWord(String engWord) {
        EngWord = engWord;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
