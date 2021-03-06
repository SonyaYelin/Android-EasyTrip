package com.easytrip.easytrip.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.easytrip.easytrip.R;
import com.easytrip.easytrip.activities.StartActivity;
import com.easytrip.easytrip.utils.IConstants;

public class SignUpFragment extends Fragment implements IConstants {

    private EditText    emailEt;
    private EditText    passwordEt;
    private EditText    verifyPasswordEt;

    private Button      singUpBtn;
    private Button      backBtn;


    public SignUpFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_sign_up, container, false);

        setAllButtons(view);

        emailEt = (EditText) view.findViewById(R.id.et_email);
        passwordEt = (EditText) view.findViewById(R.id.et_password);
        verifyPasswordEt = (EditText) view.findViewById(R.id.et_v_password);

        return view;
    }

    private void setAllButtons(View view){
        setBackBtn(view);
        setSingUpBtn(view);
    }

    private void setBackBtn(View view){
        backBtn = (Button) view.findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity)getActivity()).setSignInFragment();
            }
        });
    }

    private void setSingUpBtn(View view){
        singUpBtn = (Button) view.findViewById(R.id.btn_signup);
        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = checkEmail();
                if ( email == null )
                    return;

                String password = checkPassword();
                if ( password == null )
                    return;

                ((StartActivity)getActivity()).signUp(email, password);
            }
        });
    }

    private String checkEmail(){
        String email = emailEt.getText().toString().trim();

        if( email.isEmpty() ){
            emailEt.setError(EMAIL_REQ);
            emailEt.requestFocus();
            return null;
        }
        if ( ! Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            emailEt.setError(VALID_EMAIL);
            emailEt.requestFocus();
            return null;
        }
        return email;
    }

    private String checkPassword(){
        String password = passwordEt.getText().toString().trim();
        String verifyPassword = verifyPasswordEt.getText().toString().trim();

        if( password.isEmpty()){
            passwordEt.setError(PASSWORD_REQ);
            passwordEt.requestFocus();
            return null;
        }
        if ( password.length() < 6 ){
            passwordEt.setError(PASSWORD_MIN);
            passwordEt.requestFocus();
            return null;
        }
        if ( verifyPassword.isEmpty() ){
            verifyPasswordEt.setError(VERIFY_REQ);
            verifyPasswordEt.requestFocus();
            return null;
        }
        if ( ! password.equals(verifyPassword) ){
            verifyPasswordEt.setError(VERIFY_FAILED);
            return null;
        }
        return password;
    }

}


