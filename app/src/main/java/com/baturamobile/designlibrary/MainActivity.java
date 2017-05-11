package com.baturamobile.designlibrary;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baturamobile.design.BaturaButton;
import com.baturamobile.design.BaturaEditText;

public class MainActivity extends AppCompatActivity {

    BaturaEditText baturaEditText;

    TextInputLayout editTextInputLayout;

    TextInputLayout editTextEmailLayout;

    BaturaEditText editTextEmail;

    BaturaEditText notEditableEditText;

    BaturaButton verifyEmailButton;

    BaturaButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (BaturaButton) findViewById(R.id.pil_show_error);
        editTextInputLayout = (TextInputLayout)findViewById(R.id.password_input_layout);

        editTextEmailLayout = (TextInputLayout)findViewById(R.id.email_input_layout);
        editTextEmail = (BaturaEditText)findViewById(R.id.email_input_edittext);

        notEditableEditText = (BaturaEditText)findViewById(R.id.disbled_edittext);


        editTextEmailLayout.setEnabled(true);
        editTextInputLayout.setEnabled(true);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextInputLayout.getError() == null) {
                    editTextInputLayout.setError(getString(R.string.show_error));
                }else{
                    editTextInputLayout.setError(null);
                }
            }
        });

        verifyEmailButton = (BaturaButton)findViewById(R.id.email_verify_button);

        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.isValid()){
                    editTextEmailLayout.setError(getString(R.string.email_invalid));
                }else{
                    editTextEmailLayout.setError(null);
                }
            }
        });

        editTextEmail.setEditable(true);
        notEditableEditText.setEditable(false);
    }
}
