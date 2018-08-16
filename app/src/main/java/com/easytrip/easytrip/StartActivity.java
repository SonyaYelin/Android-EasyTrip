package com.easytrip.easytrip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.easytrip.easytrip.FireBase.DataBase;
import com.easytrip.easytrip.bl.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity implements  IConstants{

    private FirebaseAuth    auth;

    private SignInFragment  signInFragment;
    private SignUpFragment  signUpFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();
        signInFragment = new SignInFragment();
        signUpFragment = new SignUpFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        //check if user is signed in
        FirebaseUser fbUser = auth.getCurrentUser();
        if ( fbUser != null ){
            enterApp(fbUser.getUid());
        }
        else
            setSignInFragment();
    }

    private void setFragment(Fragment newFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, newFragment);
        ft.commit();
    }

    public void setSignInFragment(){
        setFragment( signInFragment );
    }

    public void setSignUpFragment(){
        setFragment( signUpFragment );
    }

    //existing user
    public void signIn(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if ( task.isSuccessful() ) {
                            FirebaseUser fbUser = auth.getCurrentUser();
                            enterApp(fbUser.getUid());
                        }
                        else
                            Toast.makeText(getApplicationContext(), AUTH_FAILED,
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //new user
    public void signUp(final String email, final String password, final String name, final int age){ auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ) {
                    Toast.makeText(getApplicationContext(), REGISTRATION_SUCCEEDED, Toast.LENGTH_SHORT).show();
                    FirebaseUser fbUser = auth.getCurrentUser();
                    User user = new User(name, age, email);
                    String userID = fbUser.getUid();

                    DataBase.getInstance().saveUserToDB(user, userID);
                    enterApp(userID);
                }
                else
                    Toast.makeText(getApplicationContext(), REGISTRATION_FAILED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enterApp(String userID){
        Intent intent = new Intent(getBaseContext(), MainPageActivity.class);
        intent.putExtra(USER, userID);
        startActivity(intent);
        finish();
    }
}