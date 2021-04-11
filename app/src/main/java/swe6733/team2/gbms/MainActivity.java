/*
    SWE6733- Spring '21
    Team 2
    Sprint 2021
    Semester Project - Gaming Behavioral Matchmaking System
    *Any use of the following code is forbidden without prior consent.
*/

package swe6733.team2.gbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    /* Component Variables */
    //Sign In
    Button login;
    EditText username;
    EditText password;
    TextView new_account;
    TextView forgotPassword;

    //Sign Up
    EditText su_emailInput;
    EditText su_firstNameInput;
    EditText su_lastNameInput;
    EditText su_dobInput;
    EditText su_usernameInput;
    EditText su_passwordInput;
    Button su_createNewAccountPB;


    LinearLayout signInLayout;
    LinearLayout signUpLayout;
    LinearLayout forgotPasswordLayout;

    //Private Variables
    private static final String TAG = "GBMS: MainActivity -";

    private int loginMode;  //0 = Sign In, 1 = Sign Up, 3 = Recovery
    private FirebaseAuth firebaseAuth;  //Instance to the FirebaseAuth System

    private FirebaseUser currentUser;


    //OnCreate Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sign In Component Linking
        username = (EditText) findViewById(R.id.ET_SI_Username);
        password = (EditText) findViewById(R.id.ET_SI_Password);
        login = (Button) findViewById(R.id.PB_SI_Login);
        new_account = (TextView) findViewById(R.id.PT_SI_CreateNewAccount);
        forgotPassword = (TextView) findViewById(R.id.PT_SI_ForgotPassword);

        //Sign Up Component Linking
        su_emailInput = (EditText) findViewById(R.id.ET_SU_EmailAddressInput);
        su_firstNameInput = (EditText) findViewById(R.id.ET_SU_FirstNameInput);
        su_lastNameInput = (EditText) findViewById(R.id.ET_SU_LastNameInput);
        su_dobInput = (EditText) findViewById(R.id.ET_SU_UsernameInput);
        su_usernameInput = (EditText) findViewById(R.id.ET_SU_PasswordInput);
        su_passwordInput = (EditText) findViewById(R.id.ET_SU_PasswordInput);
        su_createNewAccountPB = (Button) findViewById(R.id.PB_SU_CreateNewAccount);

        //Layout Linking
        signInLayout = (LinearLayout) findViewById(R.id.LL_SignIn);
        signUpLayout = (LinearLayout) findViewById(R.id.LL_SignUp);
        forgotPasswordLayout = (LinearLayout) findViewById(R.id.LL_ForgotPassword);


        //Firebase Auth Instancing
        firebaseAuth = FirebaseAuth.getInstance();

    }

    //OnStart Override
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            //TODO: Auto-change to HomePage activity so user doesn't have to sign in again
        }
    }


    //Sign In Click
    public void SignInClick(View vIew) //This swaps the ViewPort from anything, to the Sign In Screen
    {
        //Ensure Everything Else is Invisible
        signUpLayout.setVisibility(View.INVISIBLE);
        forgotPasswordLayout.setVisibility(View.INVISIBLE);

        //Set SignIn Items to Visible
        signInLayout.setVisibility(View.VISIBLE);

        //Set loginMode
        loginMode = 0;
    }

    //Sign Up Click
    public void SignUpClick(View vIew) //This swaps the ViewPort from anything, to the Sign Up Screen
    {
        //Ensure Everything Else is Invisible
        signInLayout.setVisibility(View.INVISIBLE);
        forgotPasswordLayout.setVisibility(View.INVISIBLE);

        //Set SignUp Items to Visible
        signUpLayout.setVisibility(View.VISIBLE);

        //Set loginMode
        loginMode = 1;
    }

    //Forgot Password Click
    public void ForgotPasswordClick(View vIew) //This swaps the ViewPort from anything, to the Forgot Password Screen
    {
        //Ensure Everything Else is Invisible
        signInLayout.setVisibility(View.INVISIBLE);
        signUpLayout.setVisibility(View.INVISIBLE);

        //Set ForgotPassword Items to Visible
        forgotPasswordLayout.setVisibility(View.VISIBLE);

        //Set loginMode
        loginMode = 2;
    }

    //Login Initiate
    public void Login(View view)   //This actually does the Login process through Firebase Auth (depending on Login Type will auto-complete proper sign in process)
    {
        //Login Setup
        final Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        String email = username.getText().toString().toLowerCase();
        String pass = password.getText().toString().toLowerCase();

        //final String displayName;


        //check if we are in login Mode
        if (loginMode == 0) {
            //NORMAL AUTHENTICATION WITH EMAIL AND PASSWORD

            //First Check if Username or Password is Empty and Alert User
            if (Strings.isEmptyOrWhitespace(email)) {
                Toast.makeText(getApplicationContext(), "Email Cannot Be Empty", Toast.LENGTH_LONG).show();
            } else if (Strings.isEmptyOrWhitespace(pass)) {
                Toast.makeText(getApplicationContext(), "Password Cannot Be Empty", Toast.LENGTH_LONG).show();
            }
            //If User did put in a acceptable Username / Password, try Login
            else {
                try {
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Print Log
                                Log.d(TAG, "signInWithEmail:success");

                                //Store User
                                currentUser = firebaseAuth.getCurrentUser();

                                //Change Activities
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception ex) { //Catch Authentication Failed Exception

                    Toast.makeText(getApplicationContext(), "Authentication failed. " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Signup Initiate
    public void Signup (View view)
    {
        //Signup Setup
        final Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        String email = su_emailInput.getText().toString().toLowerCase();
        String pass = su_passwordInput.getText().toString().toLowerCase();
        String username = su_usernameInput.getText().toString().toLowerCase();

        //check if we are in signUp Mode
        if (loginMode == 1)
        {
            //NORMAL ACCOUNT CREATION WITH EMAIL AND PASSWORD

            //First Check if Email Address or Password are Empty
            if (Strings.isEmptyOrWhitespace(email)) {
                Toast.makeText(getApplicationContext(), "Email Cannot Be Empty", Toast.LENGTH_LONG).show();
            }
            else if (Strings.isEmptyOrWhitespace(pass)) {
                Toast.makeText(getApplicationContext(), "Password Cannot Be Empty", Toast.LENGTH_LONG).show();
            }
            //If User did put in acceptable Username / Password
            else {
                //User Firebase Auth to Create a New User
                try {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Print Log
                                Log.d(TAG, "createAccountWithEmailAddress:success");

                                //Store User
                                currentUser = firebaseAuth.getCurrentUser();

                                //Update UserName
                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "User Account Created Successfully!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                //TODO: Possibly catch Email Exists Already exception here?
                                Toast.makeText(getApplicationContext(), "User Account Failed to Create", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                catch (Exception ex) { //Catch Account Creation Failed Exception
                    Toast.makeText(getApplicationContext(), "User Account Creation failed. " + ex.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}