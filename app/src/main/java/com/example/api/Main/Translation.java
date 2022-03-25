package com.example.api.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.api.Database.DataClasses.DictionaryDB;
import com.example.api.Database.DataClasses.En_Ar_Table;
import com.example.api.Database.HistoryActivity;
import com.example.api.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;


public class Translation extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener, TextToSpeech.OnInitListener {
    String Url = "";
    TextView local_Lan, target_Lan, target_text, local_text;
    String local_Lan_Str = "", target_Lan_Str = "", target_text_Str = "", local_text_Srt = "";

    String middle = "";
    String Translated;
    FloatingActionButton BookMark;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        local_Lan = findViewById(R.id.from);
        target_Lan = findViewById(R.id.to);

        target_text = findViewById(R.id.resultEditText);
        local_text = findViewById(R.id.fromEditText);

        BookMark = findViewById(R.id.Save);
        tts = new TextToSpeech(this, this);

        local_text.addTextChangedListener(new TextWatcher() {
            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
                timer = new CountDownTimer(1000, 1000) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        translate();
                         //target_text.setText(local_text.getText().toString());

                    }
                }.start();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }


    void translate() {
        if (local_text.getText().toString().isEmpty())
            return;


        if (local_Lan.getText().toString().equals("English"))
            Url = "http://api.mymemory.translated.net/get?q=" + local_text.getText().toString() + "&langpair=en|ar";
        else
            Url = "http://api.mymemory.translated.net/get?q=" + local_text.getText().toString() + "&langpair=ar|en";

        JsonObjectRequest request = new JsonObjectRequest(Url, null, this, this);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    public void change_language(View view) {
        middle = local_Lan.getText().toString();
        local_Lan.setText(target_Lan.getText().toString());
        target_Lan.setText(middle);

    }

    public void clear(View view) {
        local_text.setText("");
        target_text.setText("");
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            Translated = response.getJSONObject("responseData").getString("translatedText");
            target_text.setText(Translated);
            target_text.setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Internet Error", Toast.LENGTH_SHORT).show();
    }

    public void text_to_Speech(View view) {
        if (view.getId() == R.id.voluem1) {
            tts.speak(local_text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(target_text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);

        }


    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            Toast.makeText(this, "Feature Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    public void copy(View view) {
        ClipboardManager clipboard = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData clip = ClipData.newPlainText(target_text.getText().toString(), target_text.getText().toString());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "copied ", Toast.LENGTH_SHORT).show();
    }

    public void past(View view) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "No Data copied";

        // If it does contain data, decide if you can handle the data.
        if (!(clipboard.hasPrimaryClip())) {

        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

            // since the clipboard has data but it is not plain text

        } else {

            //since the clipboard contains plain text.
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            // Gets the clipboard as text.
            pasteData = item.getText().toString();
        }
        local_text.setText(pasteData);
    }

    public void saveToDataBase(View view) {
        local_Lan_Str = local_Lan.getText().toString().toUpperCase();
        target_Lan_Str = target_Lan.getText().toString().toUpperCase();
        target_text_Str = target_text.getText().toString();
        local_text_Srt = local_text.getText().toString();

        if (local_text_Srt.isEmpty() || target_text_Str.isEmpty()) {
            Toast.makeText(this, "Please Fill All data", Toast.LENGTH_SHORT).show();
            return;
        }

        int exists = DictionaryDB.getInstance(this).DictionaryDAO().exists(local_Lan_Str, local_text_Srt, target_Lan_Str);
        if (exists > 0) {
            Toast.makeText(this, "it's Already exists", Toast.LENGTH_SHORT).show();
            return;
        }
        En_Ar_Table table_Row = new En_Ar_Table();

        table_Row.local_Lan = local_Lan_Str;
        table_Row.local_text = local_text_Srt;

        table_Row.target_Lan = target_Lan_Str;
        table_Row.target_text = target_text_Str;

        long inserted = DictionaryDB.getInstance(this).DictionaryDAO().insert(table_Row);
        if (inserted > 0)
            Toast.makeText(this, "Done !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Error !", Toast.LENGTH_SHORT).show();

    }

    public void go_to_History(View view) {
        Intent i = new Intent(Translation.this, HistoryActivity.class);
        startActivity(i);
    }
}