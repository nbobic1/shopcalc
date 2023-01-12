package my.shop.shopcalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivitylist extends AppCompatActivity {
private Context c=this;
public static ArrayList<Integer> broj=new ArrayList<>();
ArrayList<lista1> list=new ArrayList<>();
void saveData()
{
    SharedPreferences sp=getSharedPreferences("list1",MODE_PRIVATE);
    SharedPreferences.Editor editor= sp.edit();
    //editor.remove("list1");
    //editor.commit();
    Gson gson =new Gson();
    String json=gson.toJson(list);
    editor.putString("list1",json);
    editor.apply();
}
void loadData()
{
    SharedPreferences sp=getSharedPreferences("list1",MODE_PRIVATE);

    Gson gson =new Gson();
    String json=sp.getString("list1",null);
    Type type=new TypeToken<ArrayList<lista1>>() {}.getType();
    list=gson.fromJson(json,type);
    if(list==null)
    {
        list=new ArrayList<>();
    }
}
    LinearLayout layout;
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        list=null;
        list=new ArrayList<>();

        for(int i=0;i<layout.getChildCount();i++)
        {
            list.add(new lista1(((EditText)((LinearLayout)(layout.getChildAt(i))).getChildAt(0)).getText().toString(),broj.get(i)));
        }
        saveData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitylist);
        Button buttons=(Button)findViewById(R.id.buttons);
        Button buttonl=(Button)findViewById(R.id.buttonl);
        Button buttonk=(Button)findViewById(R.id.buttonk);
        ImageButton bt=(ImageButton)findViewById(R.id.button);
        buttonl.setEnabled(false);
        buttonk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(c, my.shop.shopcalc.MainActivitycalc.class);
                startActivity(intent);
            }
        });
        layout=(LinearLayout)findViewById(R.id.scroll);
 //_______________-----------____________________________
 loadData();
 for(int i=0;i<list.size();i++)
 {
     broj.add(list.get(i).getB());
     LinearLayout layfi=new LinearLayout(c);
     layfi.setOrientation(layfi.HORIZONTAL);
     EditText t=new EditText(c);
     t.setWidth(0);
     t.setText(list.get(i).getA());
     t.setSelectAllOnFocus(true);
     t.setImeOptions(EditorInfo.IME_ACTION_DONE);
     t.setFocusableInTouchMode(true);
     t.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                     @Override
                                     public void onFocusChange(View v, boolean hasFocus) {
                                         if (!hasFocus) {
                                                bt.setEnabled(true);
                                                t.clearFocus();
                                         }
                                         else
                                         {
                                             bt.setEnabled(false);
                                         }
                                     }
                                 });
     t.setOnEditorActionListener(new TextView.OnEditorActionListener(
     ) {
         @Override
         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

             if(actionId== EditorInfo.IME_ACTION_DONE)
             {
                 t.clearFocus();
             }
             return false;
         }
     });
     ImageButton b1=new ImageButton(c);
     ImageButton b2=new ImageButton(c);
     LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
     layfi.setLayoutParams(param2);
     LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
     LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f);
     t.setLayoutParams(param);
     t.setSingleLine(true);
     b2.setImageResource(R.drawable.ic_baseline_delete_forever_24);
     b1.setImageResource(R.drawable.ic_baseline_edit_24);
     b1.setLayoutParams(param1);
     b2.setLayoutParams(param1);
     b2.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             SharedPreferences sp=getSharedPreferences("list1",MODE_PRIVATE);
             SharedPreferences.Editor editor= sp.edit();
             editor.remove(String.valueOf(layout.indexOfChild(layfi))).commit();
             broj.remove(layout.indexOfChild(layfi));
             layout.removeView(layfi);
         }
     });
     b1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(c,MainActivitylist1.class);
             intent.putExtra("broj",broj.get(layout.indexOfChild(layfi)));
             intent.putExtra("ime",t.getText().toString());
             startActivity(intent);
         }
     });
     layfi.addView(t);
     layfi.addView(b1);
     layfi.addView(b2);
     layout.addView(layfi);
 }
 //____________________________________________________
        buttons.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                list=null;
                list=new ArrayList<>();

              for(int i=0;i<layout.getChildCount();i++)
              {
                  list.add(new lista1(((EditText)((LinearLayout)(layout.getChildAt(i))).getChildAt(0)).getText().toString(),broj.get(i)));
              }
                saveData();
                finish();
            }
        });
        bt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int k=0;
                for(int i=0;i<broj.size()+1;i++)
                {
                    k=1;
                    for(int j=0;j<broj.size();j++)
                    if(broj.get(j)==i)
                    {
                        k=0;
                    break;
                    }
                    if(k==1)
                    {
                        broj.add(i);
                        break;
                    }
                }
                LinearLayout layfi=new LinearLayout(c);
                layfi.setOrientation(layfi.HORIZONTAL);
                EditText t=new EditText(c);
                t.setHint("List name");
                t.setWidth(0);
                t.setSelectAllOnFocus(true);
                t.setImeOptions(EditorInfo.IME_ACTION_DONE);
                t.setFocusableInTouchMode(true);
                t.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            bt.setEnabled(true);
                            t.clearFocus();
                        }
                        else
                        {
                            bt.setEnabled(false);
                        }
                    }
                });
                ImageButton b2=new ImageButton(c);
                ImageButton b1=new ImageButton(c);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
                layfi.setLayoutParams(param2);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f);
                t.setLayoutParams(param);
                t.setOnEditorActionListener(new TextView.OnEditorActionListener(
                ) {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                        if(actionId== EditorInfo.IME_ACTION_DONE)
                        {
                            t.clearFocus();
                        }
                        return false;
                    }
                });
                t.setSingleLine(true);
                b1.setLayoutParams(param1);
                b2.setLayoutParams(param1);
                b2.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                b1.setImageResource(R.drawable.ic_baseline_edit_24);
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences sp=getSharedPreferences("list1",MODE_PRIVATE);
                        SharedPreferences.Editor editor= sp.edit();
                        editor.remove(String.valueOf(layout.indexOfChild(layfi))).commit();
                        broj.remove(layout.indexOfChild(layfi));
                        layout.removeView(layfi);
                    }
                });
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(c,MainActivitylist1.class);


                        intent.putExtra("broj",broj.get(layout.indexOfChild(layfi)));
                        intent.putExtra("ime",t.getText().toString());
                        startActivity(intent);
                    }
                });
                layfi.addView(t);
                layfi.addView(b1);
                layfi.addView(b2);
                layout.addView(layfi);
            }
        });
    }
}