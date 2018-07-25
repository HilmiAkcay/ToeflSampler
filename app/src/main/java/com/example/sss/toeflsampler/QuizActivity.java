package com.example.sss.toeflsampler;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sss.toeflsampler.Model.FullWordModel;
import com.example.sss.toeflsampler.Model.QuizModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity {

    private List<FullWordModel> fullWordModelList = null;
    private List<FullWordModel> askedWordModelList = null;
    private List<QuizModel> askedQuestionList = null;
    private QuizModel currentQuestionModel;
    Integer questionIndex = 0;
    TextView txtCorrect;
    TextView txtWrong;
    TextView txtQuestion;
    TextView txtTimer;
    TextView txtQuestionCount;
    ImageButton imgBtnNext;
    ImageButton imgBtnPrew;
    RadioGroup radioGroup;
    int correctCount;
    int wrongCount;
    int questionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        DBHelper helper = new DBHelper(QuizActivity.this);
        fullWordModelList = helper.getAllCotacts();
        askedWordModelList = new ArrayList<FullWordModel>(fullWordModelList);
        askedQuestionList = new ArrayList<QuizModel>();
        txtCorrect = (TextView) findViewById(R.id.txtCorrect);
        txtWrong = (TextView) findViewById(R.id.txtWrong);
        txtQuestion = (TextView) findViewById(R.id.txtQuizQuestion);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtQuestionCount = (TextView) findViewById(R.id.txtQuestionCount);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        imgBtnNext = (ImageButton) findViewById(R.id.btnNext);
        imgBtnPrew = (ImageButton) findViewById(R.id.btnPrew);
        questionCount = 20;
        txtQuestionCount.setText(String.valueOf(questionIndex) + "/" + String.valueOf(questionCount));
        GenerateFullQuestion();

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = radioGroup.findViewById(checkedId);
                int index = radioGroup.indexOfChild(radioButton);

                ImageView img = (ImageView) findViewById(R.id.imgCorrect);
                if (index != -1)
                    setIsAnswerCorrect(index, img);
            }
        });
        int totalTime = questionCount * 5 * 1000;
        CountDownTimer timer = new CountDownTimer(totalTime, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                txtTimer.setText(hms);
            }

            public void onFinish() {
                txtTimer.setText("done!");
                imgBtnNext.setEnabled(false);
                imgBtnPrew.setEnabled(false);
            }
        };
    }

    public void btnPrew_Click(View view) {
        questionIndex--;
        GenerateFullQuestion();
        txtQuestionCount.setText(String.valueOf(questionIndex) + "/" + String.valueOf(questionCount));
    }

    public void btnNext_Click(View view) {
        questionIndex++;
        GenerateFullQuestion();
        txtQuestionCount.setText(String.valueOf(questionIndex) + "/" + String.valueOf(questionCount));
    }

    private void clearScreen() {
        RadioButton r1 = (RadioButton) findViewById(R.id.radio_1);
        RadioButton r2 = (RadioButton) findViewById(R.id.radio_2);
        RadioButton r3 = (RadioButton) findViewById(R.id.radio_3);
        RadioButton r4 = (RadioButton) findViewById(R.id.radio_4);
        r1.setText("");
        r2.setText("");
        r3.setText("");
        r4.setText("");
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
    }

    private void GenerateFullQuestion() {
        clearScreen();
        QuizModel model = GenerateQuestion();
        if (model == null) return;
        setQuestionUI(model);
    }

    private QuizModel GenerateQuestion() {

        boolean isNew = false;
        int index = -1;

        if (askedQuestionList != null && askedQuestionList.size() > 0) {

            try {
                currentQuestionModel = askedQuestionList.get(questionIndex);
                index = questionIndex;
            } catch (Exception ex) {
                currentQuestionModel = null;
            }
        }

        if (askedWordModelList.size() == 0) return null;

        if (currentQuestionModel == null) {

            index = generateRandom(askedWordModelList.size());
            currentQuestionModel = new QuizModel();

            FullWordModel model = askedWordModelList.get(index);
            currentQuestionModel.setRelationId(model.getRelationId());
            currentQuestionModel.setQuestion(model.getEngWord());
            currentQuestionModel.setCorrectAnswer(model.getTrWord());
            int correctIndex = generateRandom(3);
            currentQuestionModel.setCorrectIndex(correctIndex);
            GenerateInCorrectAnswer(model.getTrWord());
            askedWordModelList.remove(index);
            askedQuestionList.add(currentQuestionModel);
        }

        return currentQuestionModel;
    }

    private void GenerateInCorrectAnswer(String answer) {

        boolean isGenerated = false;
        List<String> wrongList = currentQuestionModel.getWrongAnswerList();

        while (isGenerated == false) {
            int random = generateRandom(fullWordModelList.size());

            FullWordModel model = fullWordModelList.get(random);

            if (wrongList.contains(model.getTrWord()) == false && model.getTrWord() != answer)
                wrongList.add(model.getTrWord());

            isGenerated = wrongList.size() > 2;
        }
    }


    private int generateRandom(int max) {
        Random r = new Random();
        return r.nextInt(max);
    }

    private void setQuestionUI(QuizModel model) {

        TextView txtQuestion = (TextView) findViewById(R.id.txtQuizQuestion);
        ImageView img = (ImageView) findViewById(R.id.quiz_image);
        RadioButton r1 = (RadioButton) findViewById(R.id.radio_1);
        RadioButton r2 = (RadioButton) findViewById(R.id.radio_2);
        RadioButton r3 = (RadioButton) findViewById(R.id.radio_3);
        RadioButton r4 = (RadioButton) findViewById(R.id.radio_4);
        ImageView imgCorrect = (ImageView) findViewById(R.id.imgCorrect);
        imgCorrect.setVisibility(View.INVISIBLE);


        File path = Environment.getExternalStoragePublicDirectory("ToeflWise");
        File fn = new File(path, String.valueOf(model.getRelationId()) + ".jpg");

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (fn.exists()) {
            Uri uri = Uri.fromFile(fn);
            img.setImageURI(uri);
            txtQuestion.setVisibility(View.INVISIBLE);
            img.setVisibility(View.VISIBLE);
            p.addRule(RelativeLayout.BELOW, R.id.quiz_image);

        } else {
            txtQuestion.setText(model.getQuestion());
            img.setVisibility(View.GONE);
            txtQuestion.setVisibility(View.VISIBLE);
            p.addRule(RelativeLayout.BELOW, R.id.txtQuizQuestion);
        }

        radioGroup.setLayoutParams(p);

        int aIndex = model.getCorrectIndex();

        if (aIndex == 0)
            r1.setText(model.getCorrectAnswer());
        else if (aIndex == 1)
            r2.setText(model.getCorrectAnswer());
        else if (aIndex == 2)
            r3.setText(model.getCorrectAnswer());
        else if (aIndex == 3)
            r4.setText(model.getCorrectAnswer());



        for (String wrong : model.getWrongAnswerList()) {
            if (r1.getText() == "")
                r1.setText(wrong);
            else if (r2.getText() == "")
                r2.setText(wrong);
            else if (r3.getText() == "")
                r3.setText(wrong);
            else if (r4.getText() == "")
                r4.setText(wrong);
        }

        if (model.isAnswered()) {
            if (model.getAnswerIndex() == 0)
                r1.setChecked(true);
            else if (model.getAnswerIndex() == 1)
                r2.setChecked(true);
            else if (model.getAnswerIndex() == 2)
                r3.setChecked(true);
            else if (model.getAnswerIndex() == 3)
                r4.setChecked(true);
        }
    }

    private boolean setIsAnswerCorrect(int index, ImageView img) {
        img.setVisibility(View.VISIBLE);
        int correctIndex = currentQuestionModel.getCorrectIndex();
        boolean isCorrect = correctIndex == index;
        if (isCorrect) {
            img.setColorFilter(ContextCompat.getColor(QuizActivity.this, R.color.colorGreen));
            img.setImageResource(R.drawable.tick);
        } else {
            img.setColorFilter(ContextCompat.getColor(QuizActivity.this, R.color.colorRed));
            img.setImageResource(R.drawable.incorrect);
        }
        if (currentQuestionModel.isAnswered() == false) {
            currentQuestionModel.setAnsweredCorrect(isCorrect);
            if (isCorrect)
                correctCount++;
            else
                wrongCount++;

            txtCorrect.setText(String.valueOf(correctCount));
            txtWrong.setText(String.valueOf(wrongCount));
        }
        currentQuestionModel.setAnswered(true);
        currentQuestionModel.setAnswerIndex(index);
        return isCorrect;
    }


}
