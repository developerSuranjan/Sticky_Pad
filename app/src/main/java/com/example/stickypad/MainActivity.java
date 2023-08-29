package com.example.stickypad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText noteEdt;
    private Button increaseSizebtn,decreaseSizebtn,boldBtn,underlineBtn,italicBtn;
    private TextView sizeTV;

    float currentSize;
    private boolean isUnderlined = false;
    boolean isBold = false;

    StickyNote note=new StickyNote();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteEdt=findViewById(R.id.idNoteEdt);
        increaseSizebtn=findViewById(R.id.idBtnIncreaseSize);
        decreaseSizebtn=findViewById(R.id.idReduceTextSize);
        boldBtn=findViewById(R.id.idBtnBold);
        underlineBtn=findViewById(R.id.idBtnUnderline);
        italicBtn=findViewById(R.id.idBtnItalic);
        sizeTV=findViewById(R.id.idTVSize);
        currentSize=noteEdt.getTextSize();
        sizeTV.setText(""+currentSize);
        increaseSizebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeTV.setText(""+currentSize);
                currentSize++;
                noteEdt.setTextSize(currentSize);

            }
        });

        decreaseSizebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeTV.setText(""+currentSize);
                currentSize--;
                noteEdt.setTextSize(currentSize);
            }
        });



        boldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                italicBtn.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                italicBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.bold_color));

                if (isBold) {
                    noteEdt.setTypeface(Typeface.DEFAULT);
                    boldBtn.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    boldBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.bold_color));
                } else {
                    noteEdt.setTypeface(null, Typeface.BOLD);
                    boldBtn.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.bold_color));
                    boldBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                }

                isBold = !isBold; // Toggle the flag
            }
        });

        underlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start = noteEdt.getSelectionStart();
                int end = noteEdt.getSelectionEnd();
                if (isUnderlined) {
                    // Remove underline
                    noteEdt.setPaintFlags(noteEdt.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                    underlineBtn.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                    underlineBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.underline_color));
                } else {
                    // Apply underline
                    String udata = noteEdt.getText().toString();
                    SpannableString content = new SpannableString(udata);
                    content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
                    noteEdt.setText(content);
                    underlineBtn.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.underline_color));
                    underlineBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white));
                }
                isUnderlined = !isUnderlined; // Toggle the flag
                noteEdt.setSelection(start,end);
            }
        });


        italicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boldBtn.setTextColor(getResources().getColor(R.color.white));
                boldBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.bold_color));
                if(noteEdt.getTypeface().isItalic()){
                    noteEdt.setTypeface(Typeface.DEFAULT);
                    italicBtn.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.white));
                    italicBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.bold_color));
                }else{
                    italicBtn.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.bold_color));
                    italicBtn.setBackgroundColor(ContextCompat.getColor(MainActivity.this,R.color.white));
                    noteEdt.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));

                }
            }
        });

    }

    public void saveButton(View view) {
        note.setStick(noteEdt.getText().toString(),this);
        updateWidget();
        Toast.makeText(this, "Sticky Note is updated...", Toast.LENGTH_SHORT).show();
    }

    private void updateWidget(){
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(this);
        RemoteViews remoteViews=new RemoteViews(this.getPackageName(),R.layout.widget_layout);
        ComponentName thisWidget = new ComponentName(this,AppWidget.class);
        remoteViews.setTextViewText(R.id.idTVWidget,noteEdt.getText().toString());
        appWidgetManager.updateAppWidget(thisWidget,remoteViews);
    }
}