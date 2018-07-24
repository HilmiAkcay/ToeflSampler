package com.example.sss.toeflsampler;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.sss.toeflsampler.Enums.WordLevelEnum;
import com.example.sss.toeflsampler.Enums.WordTypeEnum;
import com.example.sss.toeflsampler.Model.FullWordModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bulpet on 24.05.2017.
 */

public class WordAdapter  extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<FullWordModel>     mKisiListesi;
    private List<FullWordModel>     mKisiListesiTemp;

    public WordAdapter(Activity activity, List<FullWordModel> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mKisiListesi = kisiler;
        mKisiListesiTemp = new ArrayList<FullWordModel>();
        mKisiListesiTemp.addAll(kisiler);
    }

    @Override
    public int getCount() {
        return mKisiListesi.size();
    }

    @Override
    public FullWordModel getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mKisiListesi.get(position);
    }

    public  void Remove(int position)
    {
        mKisiListesi.remove(position);
        mKisiListesiTemp.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.list_design, null);
        TextView textEng =
                (TextView) satirView.findViewById(R.id.engWord);
        TextView textTr =
                (TextView) satirView.findViewById(R.id.trWord);

        TextView textType =
                (TextView) satirView.findViewById(R.id.type);

        TextView textHardness =
                (TextView) satirView.findViewById(R.id.txtViewWordHardness);

        TextView textSample =
                (TextView) satirView.findViewById(R.id.engSample);

        FullWordModel kisi = mKisiListesi.get(position);

        textEng.setText(kisi.getEngWord());

        textTr.setText(kisi.getTrWord());

        if(kisi.getSample()!= null)
        {
            textSample.setText(kisi.getSample());
            textSample.setVisibility(View.VISIBLE);
        }


        String wordType = WordTypeEnum.IntToString(kisi.getType());
        textType.setText(wordType);

        String hardness = WordLevelEnum.IntToString(kisi.getHardness());
        textHardness.setText(hardness);

        return satirView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mKisiListesi.clear();
        if (charText.length() == 0) {
            mKisiListesi.addAll(mKisiListesiTemp);
        } else {
            for (FullWordModel wm : mKisiListesiTemp) {
                if (wm.getEngWord().toLowerCase(Locale.getDefault()).contains(charText)
                        || wm.getTrWord().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mKisiListesi.add(wm);
                }
            }
        }
        notifyDataSetChanged();
    }
}
