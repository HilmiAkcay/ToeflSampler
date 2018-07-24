package com.example.sss.toeflsampler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sss.toeflsampler.Enums.WordLevelEnum;
import com.example.sss.toeflsampler.Enums.WordTypeEnum;
import com.example.sss.toeflsampler.Model.FullWordModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AddWordActivity extends AppCompatActivity {

    private final String FolderName = "Toefl_Wise";
    private FullWordModel fullWordModel;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView imageView = null;
    private Uri uriImagePath; // file url to store image/video
    private String uniqueFile;
    private String fullPath;
    String imageFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        imageView = (ImageView) findViewById(R.id.imgWord);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            FullWordModel model = (FullWordModel) bundle.get("fullModel");
            fullWordModel = model;
            LoadFullModel(model);
        }

        Spinner mySpinner = (Spinner) findViewById(R.id.SpinWordType);

        mySpinner.setAdapter(new ArrayAdapter<WordTypeEnum>(this, R.layout.spinner_item, WordTypeEnum.values()));

        Spinner wordLevelSpiiner = (Spinner) findViewById(R.id.SpinHarness);

        wordLevelSpiiner.setAdapter(new ArrayAdapter<WordLevelEnum>(this, R.layout.spinner_item, WordLevelEnum.values()));
    }

    private void LoadFullModel(final FullWordModel model) {
        EditText txtTr = (EditText) findViewById(R.id.TxtTrWord);
        EditText txtEng = (EditText) findViewById(R.id.TxtEngWord);
        EditText txtSample = (EditText) findViewById(R.id.TxtEngSample);
        final Spinner spinType = (Spinner) findViewById(R.id.SpinWordType);
        final Spinner spinHardness = (Spinner) findViewById(R.id.SpinHarness);

        txtTr.setText(model.getTrWord());
        txtEng.setText(model.getEngWord());
        txtSample.setText(model.getSample());
        spinType.post(new Runnable() {
            @Override
            public void run() {
                spinType.setSelection(model.getType());
            }
        });
        spinHardness.post(new Runnable() {
            @Override
            public void run() {
                spinHardness.setSelection(model.getHardness());
            }
        });

        if (model.getRelationId() > 0) {
            ImageView img = (ImageView) findViewById(R.id.imgWord);
            File path = Environment.getExternalStoragePublicDirectory("ToeflWise");
            File fn = new File(path, String.valueOf(model.getRelationId()) + ".jpg");
            if (fn.exists()) {
                Uri uri = Uri.fromFile(fn);
                img.setImageURI(uri);
            }

        }

    }


    public void addWordClick(View view) {

        DBHelper helper = new DBHelper(this);

        TextView trView = (TextView) findViewById(R.id.TxtTrWord);
        TextView engView = (TextView) findViewById(R.id.TxtEngWord);
        TextView sample = (TextView) findViewById(R.id.TxtEngSample);
        Spinner spin = (Spinner) findViewById(R.id.SpinWordType);
        Spinner spinHardness = (Spinner) findViewById(R.id.SpinHarness);


        String trWord = trView.getText().toString();
        String engWord = engView.getText().toString();

        Object wordTypeObj = spin.getSelectedItem();
        int wordType = WordTypeEnum.StringToInt(wordTypeObj.toString());

        Object hardness = spinHardness.getSelectedItem();

        int rate = WordLevelEnum.StringToInt(hardness.toString());

        if (fullWordModel == null)
            fullWordModel = new FullWordModel();

        fullWordModel.setTrWord(trWord);
        fullWordModel.setEngWord(engWord);
        fullWordModel.setType(wordType);
        fullWordModel.setHardness(rate);

        if(sample.getText() != null && sample.getText().toString().isEmpty() ==false)
        {
            fullWordModel.setSample(sample.getText().toString());
        }

        int relationId = helper.SaveFullWord(fullWordModel);

        if(uniqueFile!=null) {
            File path = Environment.getExternalStoragePublicDirectory("ToeflWise");
            File f = new File(path, uniqueFile);
            File fn = new File(path, String.valueOf(relationId) + ".jpg");
            f.renameTo(fn);
        }
        uniqueFile = null;

        finish();
    }


    public void btnAttach_Click(View view) {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);*/


        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        File path = Environment.getExternalStoragePublicDirectory("ToeflWise");
        uniqueFile = UUID.randomUUID().toString() + ".jpg";
        File f = new File(path, uniqueFile);
        uriImagePath = Uri.fromFile(f);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        photoPickerIntent.putExtra("return-data", true);
        startActivityForResult(photoPickerIntent, SELECT_PICTURE);


    }

    public void btnCamera_Click(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {
                // Uri selectedImageURI = data.getData();
                //imageView.setImageURI(selectedImageURI);

                File folder = new File(imageFolder);
                File f = new File(imageFolder + "img" + ".jpeg");
                if (!folder.exists()) {
                    try {
                        folder.mkdir();
                        if (f.exists() == false)
                            f.createNewFile();

                        copyFile(new File(getRealPathFromURI(data.getData())), f);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


            }

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageURI = data.getData();
                imageView.setImageURI(selectedImageURI);

                File path = Environment.getExternalStoragePublicDirectory("ToeflWise");
                File f = new File(path, uniqueFile);

                if (path.exists() == false)
                    path.mkdirs();

                try {
                    if (f.exists() == false)
                        f.createNewFile();

                    copyFile(new File(getRealPathFromURI(data.getData())), f);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }


    }

    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
