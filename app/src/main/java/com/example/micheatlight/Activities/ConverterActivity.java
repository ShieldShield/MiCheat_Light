package com.example.micheatlight.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.micheatlight.R;
import com.example.micheatlight.Utils.Converter;
import com.example.micheatlight.Utils.NotificationSender;
import com.google.android.material.snackbar.Snackbar;

public class ConverterActivity extends AppCompatActivity {

    private static final String TAG = "ConverterActivity";

    private String[] output;
    private int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter2);

        //Buttons
        final Button convert = findViewById(R.id.buttonConvert);
        final Button buttonNext = findViewById(R.id.buttonPlus);
        final Button buttonPrevious = findViewById(R.id.buttonMinus);
        final Button buttonTestNotification = findViewById(R.id.button15);
        final Button buttonCopy = findViewById(R.id.copyButton);

        //Text
        final EditText text = findViewById(R.id.textInput);
        final TextView textView = findViewById(R.id.textViewOutput);
        final EditText textBytes = findViewById(R.id.editNummberBytes);
        final EditText textNotif = findViewById(R.id.editNumberNotifications);

        //Anderes
        //Konvertiert eingegebenen wert
        convert.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            Converter converter = new Converter(Integer.parseInt(String.valueOf(textBytes.getText())), Integer.parseInt(String.valueOf(textNotif.getText())));
                            state = 0;
                            output = converter.chunk_split(String.valueOf(text.getText()));
                            textView.setText(output[state]);
                            state = 0;
                            output = converter.chunk_split(String.valueOf(text.getText()));
                            textView.setText(output[state]);
                            setFortschritt();
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ", e);
                            sendError(view);
                        }

                    }
                });

        //Lädt nächsten Teil aus array
        buttonNext.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            if (state >= output.length-1) {
                                textView.setText(output[state]);
                                sendEnd(view);
//                                state--;
                            } else {
                                state++;
                                textView.setText(output[state]);
                                setFortschritt();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ", e);
                            sendError(view);
                        }
                    }
                });


        //Lädt vorherigen Wert aus array
        buttonPrevious.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            if (state <= 0) {
                                textView.setText(output[state]);
                                sendEnd(view);
//                                state++;
                            } else {
                                state--;
                                textView.setText(output[state]);
                                setFortschritt();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ", e);
                        }
                    }
                });

        //Sendet einmal notifications durch
        buttonTestNotification.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            int notif = Integer.parseInt(String.valueOf(textNotif.getText()));
                            Converter converter = new Converter(Integer.parseInt(String.valueOf(textBytes.getText())), notif);
                            NotificationSender notificationSender = new NotificationSender(ConverterActivity.this, converter);
                            notificationSender.sendMultipleNotifications("test", output, 0);
                            Log.i(TAG, "onClick: Notifications send");
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ", e);
                            sendError(view);
                        }
                    }
                });

        buttonCopy.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String temp=textView.getText().toString();
                        setClipboard(temp,v);
                    }
                }
        );
    }

    //Helfermethoden -------------------------------------------------------------------------------
    public void sendError(View view) {
        Snackbar.make(view, getString(R.string.snackbar_end), Snackbar.LENGTH_LONG).show();
    }

    public void sendEnd(View view) {
        Snackbar.make(view, getString(R.string.snackbar_end), Snackbar.LENGTH_LONG).show();
    }

    public void setFortschritt() {
        final TextView textView2 = findViewById(R.id.textFortschritt);
        textView2.setText(String.format("%d / %d", (state + 1), output.length));
    }

    private void setClipboard(String text, View view) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Snackbar.make(view, getString(R.string.snackbar_copied),Snackbar.LENGTH_LONG);
    }
}
