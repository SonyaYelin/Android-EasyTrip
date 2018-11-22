package com.easytrip.easytrip.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.easytrip.easytrip.FireBase.DataBase;
import com.easytrip.easytrip.R;
import com.easytrip.easytrip.bl.User;
import com.easytrip.easytrip.fragments.SignInFragment;
import com.easytrip.easytrip.fragments.SignUpFragment;
import com.easytrip.easytrip.utils.IConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity implements IConstants {

    private User            user;
    private FirebaseAuth    auth;

    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

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
        if ( fbUser != null )
            DataBase.getInstance().getUserFromDB(fbUser.getUid(), this);
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
                            DataBase.getInstance().getUserFromDB(fbUser.getUid(), StartActivity.this);
                        }
                        else
                            Toast.makeText(getApplicationContext(), AUTH_FAILED,
                                    Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //new user
    public void signUp(final String email, final String password){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ) {
                    Toast.makeText(getApplicationContext(), REGISTRATION_SUCCEEDED, Toast.LENGTH_SHORT).show();
                    FirebaseUser fbUser = auth.getCurrentUser();
                    user = new User(email, fbUser.getUid());
                    enterApp(user);
                }
                else
                    Toast.makeText(getApplicationContext(), REGISTRATION_FAILED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void enterApp(User user){
        this.user = user;

        Intent intent = new Intent(getBaseContext(), MainPageActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }
}