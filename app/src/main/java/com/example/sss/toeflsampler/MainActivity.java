package com.example.sss.toeflsampler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;

import com.example.sss.toeflsampler.Enums.WordTypeEnum;
import com.example.sss.toeflsampler.Model.FullWordModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.id;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    List<FullWordModel> kisiler=new ArrayList<FullWordModel>();
    EditText editsearch;
    WordAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Loadlist();
        ListView list =(ListView)findViewById(R.id.listId);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final ListView listView = (ListView)findViewById(R.id.listId);
                final FullWordModel model = (FullWordModel)listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
                intent.putExtra("fullModel",model);
                MainActivity.this.startActivity(intent);

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {

                final ListView listView = (ListView)findViewById(R.id.listId);
                final FullWordModel model = (FullWordModel)listView.getItemAtPosition(pos);
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.delete_word_message)
                        .setTitle(R.string.delete_title);

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        DBHelper helper = new DBHelper(MainActivity.this);
                        helper.deleteFullWord(model);
                        adapter = (WordAdapter)listView.getAdapter();
                        adapter.Remove(pos);
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

// 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();

                dialog.show();

                return true;
            }
        });


        editsearch = (EditText) findViewById(R.id.inputSearch);

        // Capture Text in EditText
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });



    }




    @Override
    protected void onRestart() {

        Loadlist();
        super.onRestart();
    }

    private void  Loadlist()
    {
        ListView list =(ListView)findViewById(R.id.listId);

        DBHelper helper = new DBHelper(this);
        kisiler = helper.getAllCotacts();
        adapter = new WordAdapter(this,kisiler);
        list.setAdapter(adapter);
    }


    public  void onAddNew_Click(View view)
    {
        Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
        MainActivity.this.startActivity(intent);

        Toast.makeText(MainActivity.this, "This is my Toast message!",
                Toast.LENGTH_LONG).show();
    }
}
