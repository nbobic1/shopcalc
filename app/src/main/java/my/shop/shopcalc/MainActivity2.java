package my.shop.shopcalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button b=(Button)findViewById(R.id.button2);
        Button b1=(Button)findViewById(R.id.button3);
        Button b2=(Button)findViewById(R.id.button4);

        ImageView i=(ImageView)findViewById(R.id.imageView);
        i.setImageResource(R.drawable.prva);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.prva);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.druga);

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}