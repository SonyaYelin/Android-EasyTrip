package com.easytrip.easytrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SignInFragment extends Fragment implements IConstants{

    private EditText    emailEt;
    private EditText    passwordEt;

    private Button      signInBtn;
    private Button      signUpBtn;

    public SignInFragment(){

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
        View view  = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailEt = (EditText) view.findViewById(R.id.et_email);
        passwordEt = (EditText) view.findViewById(R.id.et_password);
        setAllButtons(view);

        return view;
    }

    private void setAllButtons(View view){
        setSignInBtn(view);
        setSignUpBtn(view);
    }

    private void setSignInBtn(View view){
        signInBtn = (Button) view.findViewById(R.id.btn_signin);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                if( email.isEmpty()){
                    emailEt.setError(EMAIL_REQ);
                    return;
                }
                if( password.isEmpty()){
                    passwordEt.setError(PASSWORD_REQ);
                    return;
                }
                ((StartActivity)getActivity()).signIn(email, password);
            }
        });
    }

    private void setSignUpBtn(View view){
        signUpBtn = (Button) view.findViewById(R.id.btn_signup);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity)getActivity()).setSignUpFragment();
            }
        });
    }
}

