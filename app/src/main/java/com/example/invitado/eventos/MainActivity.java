package com.example.invitado.eventos;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ImageView ivFalafel;
    ImageView ivBulgogi;
    ImageView ivChilaquiles;
    ImageView ivTopokki;
    ImageView ivKebap;
    ImageView ivKimbap;
    TextView falafel;
    TextView bulgogi;
    TextView topokki;
    TextView chilaquiles;
    TextView kebap;
    TextView kimbap;
    RatingBar ratingBar;
    Button botonEnviar;

    int posicion,posicion2, nFalafel, nBulgogi, nTopokki, nChilaquiles, nKebap, nKimbap, comida;
    float numStars;
    String orden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ivBulgogi=(ImageView) findViewById(R.id.ivBulgogi);
        ivFalafel=(ImageView) findViewById(R.id.ivFalafel);
        ivChilaquiles=(ImageView) findViewById(R.id.ivChilaquiles);
        ivTopokki=(ImageView) findViewById(R.id.ivTopokki);
        ivKebap=(ImageView) findViewById(R.id.ivKebap);
        ivKimbap=(ImageView) findViewById(R.id.ivKimbap);
        falafel=(TextView)findViewById(R.id.falafel);
        bulgogi=(TextView)findViewById(R.id.bulgogi);
        topokki=(TextView)findViewById(R.id.topokki);
        chilaquiles=(TextView)findViewById(R.id.chilaquiles);
        kebap=(TextView)findViewById(R.id.kebap);
        kimbap=(TextView)findViewById(R.id.kimbap);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        botonEnviar=(Button)findViewById(R.id.botonEnviar);

       ivFalafel.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {

               nFalafel = nFalafel - 1;
               if (nFalafel<=0){
                   nFalafel=0;
               }
               falafel.setTextColor(Color.RED);
               falafel.setText("Falafel: "+nFalafel);
               return true;
               }
               });



       ivBulgogi.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               nBulgogi=nBulgogi-1;
               if (nBulgogi<=0){
                   nBulgogi=0;
               }
               bulgogi.setTextColor(Color.RED);
               bulgogi.setText("Bulgogi: "+nBulgogi);

               return true;
           }
       });

       ivTopokki.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               nTopokki=nTopokki-1;
               if (nTopokki<=0){
                   nTopokki=0;
               }
               topokki.setTextColor(Color.RED);
               topokki.setText("Topokki: "+nTopokki);
               return true;
           }
       });
       ivChilaquiles.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               nChilaquiles=nChilaquiles-1;
               if (nChilaquiles<=0){
                   nChilaquiles=0;
               }
               chilaquiles.setTextColor(Color.RED);
               chilaquiles.setText("Chilaquiles: "+nChilaquiles);
               return true;
           }
       });
       ivKebap.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               nKebap=nKebap-1;
               if (nKebap<=0){
                   nKebap=0;
               }
               kebap.setTextColor(Color.RED);
               kebap.setText("Kebap: "+nKebap);
               return true;
           }
       });
       ivKimbap.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               nKimbap=nKimbap-1;
               if (nKimbap<=0){
                   nKimbap=0;
               }
               kimbap.setTextColor(Color.RED);
               kimbap.setText("Kimbap: "+nKimbap);
               return true;
           }
       });

        ivFalafel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nFalafel = nFalafel + 1;
                falafel.setTextColor(Color.GREEN);
                falafel.setText("Falafel: "+nFalafel);
            }
        });
        ivBulgogi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nBulgogi=nBulgogi+1;
                bulgogi.setTextColor(Color.GREEN);
                bulgogi.setText("Bulgogi: "+nBulgogi);
            }
        });
        ivTopokki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nTopokki=nTopokki+1;
                topokki.setTextColor(Color.GREEN);
                topokki.setText("Topokki: "+nTopokki);
            }
        });
        ivChilaquiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chilaquiles.setTextColor(Color.GREEN);
                nChilaquiles=nChilaquiles+1;
                chilaquiles.setText("Chilaquiles: "+nChilaquiles);
            }
        });
        ivKebap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nKebap=nKebap+1;
                kebap.setTextColor(Color.GREEN);
                kebap.setText("Kebap: "+nKebap);
            }
        });
        ivKimbap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nKimbap=nKimbap+1;
                kimbap.setTextColor(Color.GREEN);
                kimbap.setText("Kimbap: "+nKimbap);

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        numStars=ratingBar.getRating();

                        }
                        });



        botonEnviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage("+525513894675", null,
                        "La orden es: \n Falafel " + nFalafel + "\n Bulgogi " + nBulgogi +
                                "\n Topokki " + nTopokki + "\n Chilaquiles " + nChilaquiles +
                                "\n Kebap " + nKebap + "\n Kimbap " + nKimbap +
                                "\n La calificacion asignada por el cliente es: " + numStars+"\n Abi Fernandez",
                        null, null);

                }
        });


    }
}
