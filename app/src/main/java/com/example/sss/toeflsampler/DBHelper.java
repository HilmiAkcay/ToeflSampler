package com.example.sss.toeflsampler;

/**
 * Created by Bulpet on 24.05.2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.example.sss.toeflsampler.Enums.WordTypeEnum;
import com.example.sss.toeflsampler.Model.FullWordModel;
import com.example.sss.toeflsampler.Model.WordModel;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Toefl.db";

    public static final String TR_TABLE_NAME = "TrWord";
    public static final String TR_COLUMN_ID = "Id";
    public static final String TR_COLUMN_NAME = "Name";
    public static final String TR_COLUMN_TYPE = "Type";

    public static final String ENG_TABLE_NAME = "EngWord";
    public static final String ENG_COLUMN_ID = "Id";
    public static final String ENG_COLUMN_NAME = "Name";
    public static final String ENG_COLUMN_TYPE = "Type";

    public static final String R_TABLE_NAME = "TrEngRelation";
    public static final String R_COLUMN_ID = "Id";
    public static final String R_COLUMN_TRID = "TrId";
    public static final String R_COLUMN_ENGID = "EngId";

    public static final String SAMPLE_TABLE_NAME = "EngSample";
    public static final String SAMPLE_COLUMN_ID = "Id";
    public static final String SAMPLE_COLUMN_ENGID = "EngId";
    public static final String SAMPLE_COLUMN_SAMPLE = "Sample";


    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        if (db.getVersion() < 2) {
            db.execSQL("create table TrWord (Id integer primary key, Name text, Type integer)");
            db.execSQL("create table EngWord (Id integer primary key, Name text, Type integer)");
            db.execSQL("create table TrEngRelation (Id integer primary key, TrId integer, EngId integer, Hardness integer)");
        }
        db.execSQL("create table EngSample(Id integer primary key, EngId integer, Sample text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS TrWord");
            db.execSQL("DROP TABLE IF EXISTS EngWord");
            db.execSQL("DROP TABLE IF EXISTS TrEngRelation");
        }

        onCreate(db);
    }

    public int SaveFullWord(FullWordModel fullWordModel) {
        long trId;
        long engId;
        long relationId;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", fullWordModel.getTrWord());
        contentValues.put("Type", fullWordModel.getType());

        if (fullWordModel.getTrId() > 0) {
            db.update(TR_TABLE_NAME, contentValues, "Id = ? ", new String[]{Integer.toString(fullWordModel.getTrId())});
        } else {
            fullWordModel.setTrId((int) db.insert(TR_TABLE_NAME, null, contentValues));
        }


        ContentValues engVal = new ContentValues();
        engVal.put("Name", fullWordModel.getEngWord());
        engVal.put("Type", fullWordModel.getType());

        if (fullWordModel.getEngId() > 0) {
            db.update(ENG_TABLE_NAME, engVal, "Id = ? ", new String[]{Integer.toString(fullWordModel.getEngId())});
        } else {
            fullWordModel.setEngId((int) db.insert(ENG_TABLE_NAME, null, engVal));
        }


        ContentValues relationVal = new ContentValues();
        relationVal.put("TrId", fullWordModel.getTrId());
        relationVal.put("EngId", fullWordModel.getEngId());
        relationVal.put("Hardness", fullWordModel.getHardness());
        if (fullWordModel.getRelationId() > 0) {
            relationId = fullWordModel.getRelationId();
            db.update(R_TABLE_NAME, relationVal, "Id = ? ", new String[]{Integer.toString(fullWordModel.getRelationId())});
        } else {
            relationId = db.insert(R_TABLE_NAME, null, relationVal);
        }

        if (fullWordModel.getSample().isEmpty() == false) {
            ContentValues sampleValues = new ContentValues();
            sampleValues.put(SAMPLE_COLUMN_ENGID, fullWordModel.getEngId());
            sampleValues.put(SAMPLE_COLUMN_SAMPLE, fullWordModel.getSample());

            if (fullWordModel.getSampleId() > 0) {
                db.update(SAMPLE_TABLE_NAME, sampleValues, "Id = ? ", new String[]{Integer.toString(fullWordModel.getSampleId())});
            } else {
                fullWordModel.setSampleId((int) db.insert(SAMPLE_TABLE_NAME, null, sampleValues));
            }
        }

        return ((int) relationId);
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from EngWord where id=" + id + "", null);
        return res;
    }

    /*public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }*/


    public Integer deleteFullWord(FullWordModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(R_TABLE_NAME, "Id = ? ", new String[]{Integer.toString(model.getRelationId())});
        db.delete(TR_TABLE_NAME, "Id = ? ", new String[]{Integer.toString(model.getTrId())});
        db.delete(ENG_TABLE_NAME, "Id = ? ", new String[]{Integer.toString(model.getEngId())});
        db.delete(SAMPLE_TABLE_NAME, "Id = ?", new String[]{Integer.toString(model.getSampleId())});
        return 1;
    }

    public List<FullWordModel> getAllCotacts() {
        List<FullWordModel> array_list = new ArrayList<FullWordModel>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select t.Id as TrId, e.Id as EngId, r.Id as RelationId, t.Name as TrWord, e.Name as EngWord , t.Type, r.Hardness, s.Sample, s.Id as SampleId " +
                "from TrWord t " +
                "join TrEngRelation r on t.Id = r.TrId " +
                "join EngWord e on r.EngId = e.Id " +
                "left join EngSample s on e.Id = s.EngId", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            FullWordModel model = new FullWordModel();
            model.setTrWord(res.getString(res.getColumnIndex("TrWord")));
            model.setEngWord(res.getString(res.getColumnIndex("EngWord")));
            model.setType(res.getInt(res.getColumnIndex("Type")));
            model.setHardness(res.getInt(res.getColumnIndex("Hardness")));
            model.setRelationId(res.getInt(res.getColumnIndex("RelationId")));
            model.setTrId(res.getInt(res.getColumnIndex("TrId")));
            model.setEngId(res.getInt(res.getColumnIndex("EngId")));
            model.setSampleId(res.getInt(res.getColumnIndex("SampleId")));
            model.setSample(res.getString(res.getColumnIndex("Sample")));
            array_list.add(model);
            res.moveToNext();
        }
        return array_list;
    }
}
