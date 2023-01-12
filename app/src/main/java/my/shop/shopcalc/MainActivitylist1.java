package my.shop.shopcalc;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivitylist1 extends AppCompatActivity {
    ArrayList<LISTA> list=new ArrayList<>();
    protected String mCurrentPhotoPath;
    private  String gh=new String();
    private Uri uri;
    private Uri uri1;
    public String txt;
    private static final String errorFileCreate = "Error file create!";
    private static final String errorConvert = "Error convert!";
    private static final int REQUEST_IMAGE1_CAPTURE = 1;
    int PERMISSION_ALL = 1;
    private Handler hand=new Handler();
    boolean flagPermissions = false;

    String[] PERMISSIONS = {
            //android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    private Context c=this;
    ImageButton button,buttonp;
    TextView res;
    ImageView firstImage;
    LinearLayout layout;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        list=null;
        list=new ArrayList<>();
        for(int i=0;i<layout.getChildCount();i++)
        {
            list.add(new LISTA(((TextView)((LinearLayout)((LinearLayout)layout.getChildAt(i)).getChildAt(0)).getChildAt(0)).getText().toString(),((TextView)((LinearLayout)((LinearLayout)layout.getChildAt(i)).getChildAt(0)).getChildAt(1)).getText().toString(),((TextView)((LinearLayout)((LinearLayout)layout.getChildAt(i)).getChildAt(1)).getChildAt(2)).getText().toString() ));
        }
        saveData();
    }
    public boolean broj(Character a)
    {
        if(a>='1'&&a<='9')
            return true;
        else return false;
    }
    public double conv(String a)
    {
        a=a.replaceAll(",",".");
        for(int i=1;i<a.length();i++){
            if(a.charAt(i)=='.')
            {
                if(!(broj(a.charAt(i-1))||broj(a.charAt(i+1))))
                {
                    a=a.substring(0,i-1)+a.substring(i+1,a.length());
                }
            }
        }
        a=a.replaceAll("[^0-9.]","");
        int k=0;
        for(int i=0;i<a.length();i++){
            if(a.charAt(i)=='.')
            {
                k++;
            }
        }
        if(a.equals("")||a==null||k>1 )
            return 0;

        else
        {
            return Math.round( Double.parseDouble(a)*100.0)/100.0;
        }

    }
    public void opa(String a)
    {

        double t=0;
        ((TextView)((LinearLayout)(((LinearLayout)layout.getChildAt(0    )).getChildAt(0))).getChildAt(0)).setText(a);
        ((TextView)((LinearLayout)(((LinearLayout)layout.getChildAt(0    )).getChildAt(0))).getChildAt(2)).setText(String.valueOf(conv(a)));
        ((TextView)((LinearLayout)(((LinearLayout)layout.getChildAt(0    )).getChildAt(0))).getChildAt(1)).setText(String.valueOf(conv(a)));
        String t1=res.getText().toString();
        res.setText("Total: "+String.valueOf(conv(a)+conv(t1)));

    }
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void checkPermissions() {
        if (!hasPermissions(c, PERMISSIONS)) {
            requestPermissions(PERMISSIONS,
                    PERMISSION_ALL);
            flagPermissions = false;
        }
        flagPermissions = true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    //slikanje*
    //novo slikanej
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    void sel()
    {
        if(uri!=null)
            c.getContentResolver().delete(uri,null,null);
        uri=null;


    }

    void saveData()
    {
        SharedPreferences sp=getSharedPreferences("lis",MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.remove(gh).commit();
        Gson gson =new Gson();
        String json=gson.toJson(list);
        System.out.println("Duzina je=2##################=="+list.size());
        editor.putString(gh,json);
        editor.commit();
        editor.apply();
    }
    void loadData()
    {
        SharedPreferences sp=getSharedPreferences("lis",MODE_PRIVATE);
        Gson gson =new Gson();
        String json=sp.getString(gh,null);
        Type type=new TypeToken<ArrayList<LISTA>>() {}.getType();
        list=gson.fromJson(json,type);
        if(list==null)
        {
            list=new ArrayList<>();
        }
        SharedPreferences.Editor editor= sp.edit();
        editor.remove(gh).commit();
        editor.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap==null)
            {
                System.out.println("dgagagaahhahahahhahahh2222222111111");
                return;
            }
//ovdje crop
            ByteArrayOutputStream byt=new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,byt);
            String path=MediaStore.Images.Media.insertImage(c.getContentResolver(),imageBitmap,"prva",null);
            uri= Uri.parse(path);
            CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).start(this);

            //kraj kropa dole to treba ubacit  nao crop a da ocr
        }
        else  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult resu1lt = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri1 = resu1lt.getUri();
                //ovjde moje
                InputImage img=null;
                try {

                    img=InputImage.fromFilePath(c,uri1);
                    TextRecognizer rec23= TextRecognition.getClient();
                    Task<Text> result =
                            rec23.process(img)
                                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                                        @Override
                                        public void onSuccess(Text visionText) {
                                            // Task completed successfully
                                            // ...
                                            // opa(visionText.getText());

                                            hand.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    opa(visionText.getText());
                                                    sel();
                                                }
                                            });
                                            System.out.println( visionText.getText());
                                        }
                                    })


                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                    sel();
                                                    System.out.println("ups ne ide!!!!!!!!!!!!");
                                                }
                                            });
                }
                catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }



                //moje
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = resu1lt.getError();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitylist1);
        TextView te=(TextView) findViewById(R.id.textView);
        ImageButton tr= (ImageButton)findViewById(R.id.imageButton2);
        gh=String.valueOf(getIntent().getExtras().getInt("broj"));
        te.setText("List: "+getIntent().getExtras().getString("ime"));
        tr.setImageResource(R.drawable.ic_baseline_save_24);
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list=null;
                list=new ArrayList<>();
                for(int i=0;i<layout.getChildCount();i++)
                {
                    list.add(new LISTA(((TextView)((LinearLayout)((LinearLayout)layout.getChildAt(i)).getChildAt(0)).getChildAt(0)).getText().toString(),((TextView)((LinearLayout)((LinearLayout)layout.getChildAt(i)).getChildAt(0)).getChildAt(1)).getText().toString(),((TextView)((LinearLayout)((LinearLayout)layout.getChildAt(i)).getChildAt(1)).getChildAt(2)).getText().toString() ));
                }
                saveData();
                finish();
            }
        });
        /*********/
        res=(TextView)findViewById(R.id.res);
        //_______________________
        button=(ImageButton)findViewById(R.id.button6);//slika
        buttonp = (ImageButton) findViewById(R.id.button);//plus
        layout=(LinearLayout)findViewById(R.id.scroll);

        ////pravim loud data d vidim sta je bio!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        list=null;
        loadData();
        Double drg=0.0;

        System.out.println("lista je duga !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!==="+list.size());
        for(int i=0;i<list.size();i++)
        {
            LinearLayout layfi=new LinearLayout(c);
            layfi.setOrientation(layfi.VERTICAL);
            LinearLayout lay1=new LinearLayout(c);
            lay1.setOrientation(lay1.HORIZONTAL);
            LinearLayout lay=new LinearLayout(c);
            lay.setOrientation(lay.HORIZONTAL);
            EditText t2=new EditText(c);
            EditText t3=new EditText(c);
            TextView t4=new TextView(c);
            EditText t1=new EditText(c);
            t1.setHint("Item");
            t3.setHint("Price");
            t4.setText("0.00");
            LinearLayout.LayoutParams par41=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.25f);
            LinearLayout.LayoutParams par2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.6f);
            LinearLayout.LayoutParams par3=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams par1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.2f);
            LinearLayout.LayoutParams par4=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.25f);
            LinearLayout.LayoutParams par=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.2f);
            t4.setLayoutParams(par);
            t4.setWidth(0);
            t3.setWidth(0);
            t1.setWidth(0);
            t3.setLayoutParams(par1);
            t1.setLayoutParams(par2);
            t3.setTextSize(20);
            t1.setTextSize(20);
            t4.setTextSize(20);
            t4.setGravity(Gravity.CENTER_VERTICAL);
            t1.setSingleLine(true);
            t3.setSingleLine(true);
            t2.setSingleLine(true);
            t1.setImeOptions(EditorInfo.IME_ACTION_DONE);
            t2.setImeOptions(EditorInfo.IME_ACTION_DONE);
            t3.setImeOptions(EditorInfo.IME_ACTION_DONE);

            ImageButton b3=new ImageButton(c);
            Button b1=new Button(c);
            b1.setText("-");
            Button b2=new Button(c);
            b2.setText("+");
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int a=Integer.parseInt(t2.getText().toString());
                    if(a==0)
                        return;
                    a--;
                    t2.setText(String.valueOf(a));
                    t4.setText(String.valueOf(a*conv(t3.getText().toString())));
                    res.setText("Total: "+String.valueOf(conv(res.getText().toString())-conv(t3.getText().toString())));

                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a=Integer.parseInt(t2.getText().toString());
                    a++;
                    t2.setText(String.valueOf(a));
                    t4.setText(String.valueOf(a*conv(t3.getText().toString())));
                    res.setText("Total: "+String.valueOf(conv(res.getText().toString())+conv(t3.getText().toString())));
                }
            });

            b3.setImageResource(R.drawable.ic_baseline_delete_forever_24);
            b3.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    double d=conv(t4.getText().toString());
                    double d1=conv(res.getText().toString());
                    res.setText("Total: "+String.valueOf(d1-d));
                    layout.removeView(layfi);

                }
            });
            t3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus)
                    {

                        double d=conv(t2.getText().toString());
                        res.setText("Total: "+String.valueOf(conv(t3.getText().toString())*d-conv(t4.getText().toString())+conv(res.getText().toString())));
                        t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                        t3.clearFocus();
                        b1.setEnabled(true);
                        b3.setEnabled(true);
                        b2.setEnabled(true);
                        button.setEnabled(true);
                        buttonp.setEnabled(true);
                    }
                    else
                    {
                        b1.setEnabled(false);
                        b3.setEnabled(false);
                        b2.setEnabled(false);
                        button.setEnabled(false);
                        buttonp.setEnabled(false);
                    }
                }
            });
            t3.setOnEditorActionListener(new TextView.OnEditorActionListener(
            ) {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(actionId== EditorInfo.IME_ACTION_DONE)
                    {
                        double d=conv(t2.getText().toString());
                        res.setText("Total: "+String.valueOf(conv(t3.getText().toString())*d-conv(t4.getText().toString())+conv(res.getText().toString())));
                        t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                        t3.clearFocus();
                    }
                    return false;
                }
            });
            t2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus)
                    {
                        double d=conv(t2.getText().toString());
                        double d1=conv(t4.getText().toString());
                        t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                        res.setText("Total: "+String.valueOf(d*conv(t3.getText().toString())+conv(res.getText().toString())-d1));
                        t2.clearFocus();
                        b1.setEnabled(true);
                        b3.setEnabled(true);
                        b2.setEnabled(true);
                        button.setEnabled(true);
                        buttonp.setEnabled(true);
                    }
                    else
                    {
                        b1.setEnabled(false);
                        b3.setEnabled(false);
                        b2.setEnabled(false);
                        button.setEnabled(false);
                        buttonp.setEnabled(false);
                    }
                }
            });
            t2.setOnEditorActionListener(new TextView.OnEditorActionListener(
            ) {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(actionId== EditorInfo.IME_ACTION_DONE)
                    {
                        double d=conv(t2.getText().toString());
                        double d1=conv(t4.getText().toString());
                        t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                        res.setText("Total: "+String.valueOf(d*conv(t3.getText().toString())+conv(res.getText().toString())-d1));
                        t2.clearFocus();
                    }
                    return false;
                }
            });
            b3.setLayoutParams(par41);
            b2.setLayoutParams(par4);
            b1.setLayoutParams(par4);
            t2.setLayoutParams(par4);
            t2.setFocusableInTouchMode(true);
            t2.setSelectAllOnFocus(true);
            t4.setSelectAllOnFocus(true);
            t1.setSelectAllOnFocus(true);
            t3.setSelectAllOnFocus(true);
            t3.setFocusableInTouchMode(true);
            t4.setFocusableInTouchMode(true);
            t2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            t2.setText("1");
//                t1.setBackground(c.getDrawable(R.drawable.izgled2));
            //  t3.setBackground(c.getDrawable(R.drawable.izgled2));
            //
            //    t4.setBackground(c.getDrawable(R.drawable.izgled2));
            // layfi.setBackground(c.getDrawable(R.drawable.izgled));
            t1.setText(list.get(i).getA());
            t2.setText(list.get(i).getC());
            t3.setText(list.get(i).getB());
            t4.setText(String.valueOf(conv(t2.getText().toString())*conv(t3.getText().toString())));
            lay.addView(t1);
            lay.addView(t3);
            lay.addView(t4);
            lay1.addView(b3);
            lay1.addView(b1);
            lay1.addView(t2);
            lay1.addView(b2);
            layfi.addView(lay);
            layfi.addView(lay1);
            layout.addView(layfi,0);
            drg+=conv(t4.getText().toString());
        }
        res.setText("Total: "+String.valueOf(drg));



        //!!!!!!!!!!!!!!!!!!!kra loutda
        // layout.setBackground(c.getDrawable(R.drawable.izgled2));
        buttonp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layfi=new LinearLayout(c);
                layfi.setOrientation(layfi.VERTICAL);
                LinearLayout lay1=new LinearLayout(c);
                lay1.setOrientation(lay1.HORIZONTAL);
                LinearLayout lay=new LinearLayout(c);
                lay.setOrientation(lay.HORIZONTAL);
                EditText t2=new EditText(c);
                EditText t3=new EditText(c);
                TextView t4=new TextView(c);
                EditText t1=new EditText(c);
                t1.setHint("Item");
                t3.setHint("Price");
                t4.setText("0.00");
                LinearLayout.LayoutParams par41=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.25f);
                LinearLayout.LayoutParams par2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.6f);
                LinearLayout.LayoutParams par3=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams par1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.2f);
                LinearLayout.LayoutParams par4=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.25f);
                LinearLayout.LayoutParams par=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.2f);
                t4.setLayoutParams(par);
                t4.setWidth(0);
                t3.setWidth(0);
                t1.setWidth(0);
                t3.setLayoutParams(par1);
                t1.setLayoutParams(par2);
                t3.setTextSize(20);
                t1.setTextSize(20);
                t4.setTextSize(20);
                t4.setGravity(Gravity.CENTER_VERTICAL);
                t1.setSingleLine(true);
                t3.setSingleLine(true);
                t2.setSingleLine(true);
                t1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                t2.setImeOptions(EditorInfo.IME_ACTION_DONE);
                t3.setImeOptions(EditorInfo.IME_ACTION_DONE);

                ImageButton b3=new ImageButton(c);
                Button b1=new Button(c);
                b1.setText("-");
                Button b2=new Button(c);
                b2.setText("+");
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int a=Integer.parseInt(t2.getText().toString());
                        if(a==0)
                            return;
                        a--;
                        t2.setText(String.valueOf(a));
                        t4.setText(String.valueOf(a*conv(t3.getText().toString())));
                        res.setText("Total: "+String.valueOf(conv(res.getText().toString())-conv(t3.getText().toString())));

                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int a=Integer.parseInt(t2.getText().toString());
                        a++;
                        t2.setText(String.valueOf(a));
                        t4.setText(String.valueOf(a*conv(t3.getText().toString())));
                        res.setText("Total: "+String.valueOf(conv(res.getText().toString())+conv(t3.getText().toString())));
                    }
                });

                b3.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                b3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        double d=conv(t4.getText().toString());
                        double d1=conv(res.getText().toString());
                        res.setText("Total: "+String.valueOf(d1-d));
                        layout.removeView(layfi);

                    }
                });
                t3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus)
                        {

                            double d=conv(t2.getText().toString());
                            res.setText("Total: "+String.valueOf(conv(t3.getText().toString())*d-conv(t4.getText().toString())+conv(res.getText().toString())));
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            t3.clearFocus();
                            b1.setEnabled(true);
                            b3.setEnabled(true);
                            b2.setEnabled(true);
                            button.setEnabled(true);
                            buttonp.setEnabled(true);
                        }
                        else
                        {
                            b1.setEnabled(false);
                            b3.setEnabled(false);
                            b2.setEnabled(false);
                            button.setEnabled(false);
                            buttonp.setEnabled(false);
                        }
                    }
                });
                t3.setOnEditorActionListener(new TextView.OnEditorActionListener(
                ) {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId== EditorInfo.IME_ACTION_DONE)
                        {
                            double d=conv(t2.getText().toString());
                            res.setText("Total: "+String.valueOf(conv(t3.getText().toString())*d-conv(t4.getText().toString())+conv(res.getText().toString())));
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            t3.clearFocus();
                        }
                        return false;
                    }
                });
                t2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus)
                        {
                            double d=conv(t2.getText().toString());
                            double d1=conv(t4.getText().toString());
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            res.setText("Total: "+String.valueOf(d*conv(t3.getText().toString())+conv(res.getText().toString())-d1));
                            t2.clearFocus();
                            b1.setEnabled(true);
                            b3.setEnabled(true);
                            b2.setEnabled(true);
                            button.setEnabled(true);
                            buttonp.setEnabled(true);
                        }
                        else
                        {
                            b1.setEnabled(false);
                            b3.setEnabled(false);
                            b2.setEnabled(false);
                            button.setEnabled(false);
                            buttonp.setEnabled(false);
                        }
                    }
                });
                t2.setOnEditorActionListener(new TextView.OnEditorActionListener(
                ) {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId== EditorInfo.IME_ACTION_DONE)
                        {
                            double d=conv(t2.getText().toString());
                            double d1=conv(t4.getText().toString());
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            res.setText("Total: "+String.valueOf(d*conv(t3.getText().toString())+conv(res.getText().toString())-d1));
                            t2.clearFocus();
                        }
                        return false;
                    }
                });
                b3.setLayoutParams(par41);
                b2.setLayoutParams(par4);
                b1.setLayoutParams(par4);
                t2.setLayoutParams(par4);
                t2.setFocusableInTouchMode(true);
                t2.setSelectAllOnFocus(true);
                t4.setSelectAllOnFocus(true);
                t1.setSelectAllOnFocus(true);
                t3.setSelectAllOnFocus(true);
                t3.setFocusableInTouchMode(true);
                t4.setFocusableInTouchMode(true);
                t2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                t2.setText("1");
//                t1.setBackground(c.getDrawable(R.drawable.izgled2));
                //  t3.setBackground(c.getDrawable(R.drawable.izgled2));
                //
                //    t4.setBackground(c.getDrawable(R.drawable.izgled2));
                // layfi.setBackground(c.getDrawable(R.drawable.izgled));
                lay.addView(t1);
                lay.addView(t3);
                lay.addView(t4);
                lay1.addView(b3);
                lay1.addView(b1);
                lay1.addView(t2);
                lay1.addView(b2);
                layfi.addView(lay);
                layfi.addView(lay1);
                layout.addView(layfi,0);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //prvo slikanje da rijesim
                System.out.println("ima li perisije="+hasPermissions(c,PERMISSIONS));
                if (!hasPermissions(c,PERMISSIONS)) {
                    checkPermissions();
                    System.out.print("falg=");
                    return;
                }
                dispatchTakePictureIntent();

                //gadsadgdgadggagagag
                LinearLayout layfi=new LinearLayout(c);
                layfi.setOrientation(layfi.VERTICAL);
                LinearLayout lay1=new LinearLayout(c);
                lay1.setOrientation(lay1.HORIZONTAL);
                LinearLayout lay=new LinearLayout(c);
                lay.setOrientation(lay.HORIZONTAL);

                EditText t2=new EditText(c);
                EditText t3=new EditText(c);
                TextView t4=new TextView(c);
                EditText t1=new EditText(c);
                t1.setHint("Item");
                t3.setHint("Price");
                t4.setText("0.00");
                LinearLayout.LayoutParams par2=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.6f);
                LinearLayout.LayoutParams par3=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams par1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.2f);
                LinearLayout.LayoutParams par4=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,0.25f);
                LinearLayout.LayoutParams par=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.2f);
                LinearLayout.LayoutParams par41=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT,0.25f);

                t4.setLayoutParams(par);
                t4.setWidth(0);
                t3.setWidth(0);
                t1.setWidth(0);
                t3.setLayoutParams(par1);
                t1.setLayoutParams(par2);
                t3.setTextSize(20);
                t1.setTextSize(20);
                t4.setTextSize(20);
                t4.setGravity(Gravity.CENTER_VERTICAL);
                t1.setSingleLine(true);
                t3.setSingleLine(true);
                t2.setSingleLine(true);
                t1.setImeOptions(EditorInfo.IME_ACTION_DONE);
                t2.setImeOptions(EditorInfo.IME_ACTION_DONE);
                t3.setImeOptions(EditorInfo.IME_ACTION_DONE);
                ImageButton b3=new ImageButton(c);
                Button b1=new Button(c);
                b1.setText("-");
                Button b2=new Button(c);
                b2.setText("+");
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int a=Integer.parseInt(t2.getText().toString());
                        if(a==0)
                            return;
                        a--;
                        t2.setText(String.valueOf(a));
                        t4.setText(String.valueOf(a*conv(t3.getText().toString())));
                        res.setText("Total: "+String.valueOf(conv(res.getText().toString())-conv(t3.getText().toString())));

                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int a=Integer.parseInt(t2.getText().toString());
                        a++;
                        t2.setText(String.valueOf(a));
                        t4.setText(String.valueOf(a*conv(t3.getText().toString())));
                        res.setText("Tatal: "+String.valueOf(conv(res.getText().toString())+conv(t3.getText().toString())));

                    }
                });

                b3.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                b3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        double d=conv(t4.getText().toString());
                        double d1=conv(res.getText().toString());
                        res.setText("Total: "+String.valueOf(d1-d));
                        layout.removeView(layfi);
                    }
                });
                t3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus)
                        {
                            double d=conv(t2.getText().toString());
                            res.setText("Total: "+String.valueOf(conv(t3.getText().toString())*d-conv(t4.getText().toString())+conv(res.getText().toString())));
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            t3.clearFocus();
                            b1.setEnabled(true);
                            b3.setEnabled(true);
                            b2.setEnabled(true);
                            button.setEnabled(true);
                            buttonp.setEnabled(true);
                        }
                        else
                        {
                            b1.setEnabled(false);
                            b3.setEnabled(false);
                            b2.setEnabled(false);
                            button.setEnabled(false);
                            buttonp.setEnabled(false);
                        }
                    }
                });
                t3.setOnEditorActionListener(new TextView.OnEditorActionListener(
                ) {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId== EditorInfo.IME_ACTION_DONE)
                        {
                            double d=conv(t2.getText().toString());
                            res.setText("Total: "+String.valueOf(conv(t3.getText().toString())*d-conv(t4.getText().toString())+conv(res.getText().toString())));
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            t3.clearFocus();
                        }
                        return false;
                    }
                });
                t2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus)
                        {
                            double d=conv(t2.getText().toString());
                            double d1=conv(t4.getText().toString());
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            res.setText("Total: "+String.valueOf(d*conv(t3.getText().toString())+conv(res.getText().toString())-d1));
                            t2.clearFocus();
                            b1.setEnabled(true);
                            b3.setEnabled(true);
                            b2.setEnabled(true);
                            button.setEnabled(true);
                            buttonp.setEnabled(true);
                        }
                        else
                        {
                            b1.setEnabled(false);
                            b3.setEnabled(false);
                            b2.setEnabled(false);
                            button.setEnabled(false);
                            buttonp.setEnabled(false);
                        }
                    }
                });
                t2.setOnEditorActionListener(new TextView.OnEditorActionListener(
                ) {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId== EditorInfo.IME_ACTION_DONE)
                        {
                            double d=conv(t2.getText().toString());
                            double d1=conv(t4.getText().toString());
                            t4.setText(String.valueOf(d*conv(t3.getText().toString())));
                            res.setText("Total: "+String.valueOf(d*conv(t3.getText().toString())+conv(res.getText().toString())-d1));
                            t2.clearFocus();
                        }
                        return false;
                    }
                });


                b3.setLayoutParams(par41);
                b2.setLayoutParams(par4);
                b1.setLayoutParams(par4);
                t2.setLayoutParams(par4);
                t2.setSelectAllOnFocus(true);
                t4.setSelectAllOnFocus(true);
                t1.setSelectAllOnFocus(true);
                t3.setSelectAllOnFocus(true);
                t2.setText("1");
                t2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //t2.setBackground(c.getDrawable(R.drawable.izgled2));
                //t1.setBackground(c.getDrawable(R.drawable.izgled2));
                //t3.setBackground(c.getDrawable(R.drawable.izgled2));
                //t4.setBackground(c.getDrawable(R.drawable.izgled2));
                // layfi.setBackground(c.getDrawable(R.drawable.izgled));
                lay.addView(t1);
                lay.addView(t3);
                lay.addView(t4);
                lay1.addView(b3);
                lay1.addView(b1);
                lay1.addView(t2);
                lay1.addView(b2);
                layfi.addView(lay);
                layfi.addView(lay1);
                layout.addView(layfi,0);
            }
        });
    }

    /********/
}
