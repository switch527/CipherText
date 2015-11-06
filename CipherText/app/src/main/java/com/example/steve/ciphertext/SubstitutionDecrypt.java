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

public class SubstitutionDecrypt extends ActionBarActivity {
    String checkString;
    Character[] check;
    int key;
    int index;
    int newIndex;
    char decrypting;
    String plainText;
    String initialFormat;
    char[] decryption;
    String cipherText;
    Button sms;
    Button copy;
    Button paste;
    ClipboardManager clippy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substitution_decrypt);



        sms = (Button) findViewById(R.id.buttonSMS);
        sms.setOnClickListener(sendSMS);

        clippy = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        check = new Character[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', '0'};

        checkString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        final TextView textViewPlainText = (TextView) findViewById(R.id.textViewPlainText);

        final EditText editTextCipherText = (EditText) findViewById(R.id.editTextCipherText);
        editTextCipherText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                editTextCipherText.removeTextChangedListener(this);

                cipherText = s.toString().toUpperCase();
                initialFormat = cipherText.replaceAll("\\s", "");
                initialFormat = initialFormat.replaceAll("[^a-zA-Z0-9\\s]", "f");
                decryption = initialFormat.toCharArray();

                for (int i = 0; i < decryption.length; i++) {
                    decrypting = decryption[i];
                    index = checkString.indexOf(decrypting);
                    newIndex = index - key;
                    if (newIndex < 0) {
                        newIndex += 36;
                    }
                    decryption[i] = check[newIndex];
                }

                plainText = new String(decryption);
                plainText = plainText.replaceAll(".{4}(?=.)", "$0 ");
                textViewPlainText.setText(plainText);

                editTextCipherText.addTextChangedListener(this);

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
                    String s = editTextCipherText.getText().toString();
                    editTextCipherText.setText(s);
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
                    editTextCipherText.setText(item.getText().toString());

                }else{

                    Toast.makeText(getApplicationContext(), "Nothing to Paste", Toast.LENGTH_SHORT).show();

                }

            }
        });

        copy = (Button) findViewById(R.id.buttonCopyPlainText);
        copy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("plain_text", plainText);
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
                    sendSMS.putExtra("sms_body", plainText);
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

        getMenuInflater().inflate(R.menu.menu_substitution_decrypt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.main:
                Intent returnMainMenu =
                        new Intent(SubstitutionDecrypt.this, CipherSelection.class);
                startActivity(returnMainMenu);
                return super.onOptionsItemSelected(item);


            case R.id.encrypt:
                Intent returnEncrypt =
                        new Intent(SubstitutionDecrypt.this, Substitution.class);
                startActivity(returnEncrypt);
                return super.onOptionsItemSelected(item);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
