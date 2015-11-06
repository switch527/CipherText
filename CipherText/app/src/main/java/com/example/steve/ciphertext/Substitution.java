package com.example.steve.ciphertext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Substitution extends ActionBarActivity {
    String checkString;
    Character[] check;
    int key;
    int index;
    int newIndex;
    char encrypting;
    String plainText;
    String initialFormat;
    char[] encryption;
    String cipherText;
    Button sms;
    Button copy;
    Button paste;
    ClipboardManager clippy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitution);

        sms = (Button) findViewById(R.id.buttonSMS);
        sms.setOnClickListener(sendSMS);

        clippy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        check = new Character[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', '0'};

        checkString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        final TextView textViewCipherText = (TextView) findViewById(R.id.textViewCipherText);

        final EditText editTextPlainText = (EditText) findViewById(R.id.editTextPlainText);
        editTextPlainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                editTextPlainText.removeTextChangedListener(this);

                plainText = s.toString().toUpperCase();
                initialFormat = plainText.replaceAll("\\s", "");
                initialFormat = initialFormat.replaceAll("[^a-zA-Z0-9\\s]", "f");
                encryption = initialFormat.toCharArray();

                for (int i = 0; i < encryption.length; i++) {
                    encrypting = encryption[i];
                    index = checkString.indexOf(encrypting);
                    newIndex = index + key;
                    if (newIndex > 35) {
                        newIndex -= 36;
                    }
                    encryption[i] = check[newIndex];
                }

                cipherText = new String(encryption);
                cipherText = cipherText.replaceAll(".{4}(?=.)", "$0 ");
                textViewCipherText.setText(cipherText);

                editTextPlainText.addTextChangedListener(this);

            }
        });

        final TextView textViewShift = (TextView) findViewById(R.id.textViewShift);

        final SeekBar seekBarKey = (SeekBar) findViewById(R.id.seekBarKey);
        seekBarKey.setMax(35);

        seekBarKey.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    key = progress;
                    textViewShift.setText(String.valueOf(key));
                    String s = editTextPlainText.getText().toString();
                    editTextPlainText.setText(s);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


            }
        });

        paste = (Button) findViewById(R.id.buttonPaste);
        paste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clippy.hasPrimaryClip()== true){
                    ClipData.Item item = clippy.getPrimaryClip().getItemAt(0);
                    editTextPlainText.setText(item.getText().toString());

                }else{

                    Toast.makeText(getApplicationContext(), "Nothing to Paste", Toast.LENGTH_SHORT).show();

                }

            }
        });

        copy = (Button) findViewById(R.id.buttonCopyCipherText);
        copy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("cipher_text", cipherText);
                clippy.setPrimaryClip(clip);

            }
        });

    }

    OnClickListener sendSMS = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cipherText != null) {
                try {

                    Intent sendSMS = new Intent(Intent.ACTION_VIEW);
                    sendSMS.putExtra("sms_body", cipherText);
                    sendSMS.setType("vnd.android-dir/mms-sms");
                    startActivity(sendSMS);

                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(),
                            "SMS failed!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }

            } else {


            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_substitution, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.main:
                Intent returnMainMenu =
                        new Intent(Substitution.this, CipherSelection.class);
                startActivity(returnMainMenu);
                return super.onOptionsItemSelected(item);


            case R.id.decrypt:
                Intent returnDecrypt =
                        new Intent(Substitution.this, SubstitutionDecrypt.class);
                startActivity(returnDecrypt);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}