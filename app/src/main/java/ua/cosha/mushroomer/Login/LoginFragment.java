package ua.cosha.mushroomer.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

import ua.cosha.mushroomer.R;


public class LoginFragment extends Fragment {
    private static final String TAG = "Tag";
    private FirebaseAuth mAuth;
    static final int RC_SIGN_IN = 234;
    static final   String IDTOKEN = "335890011285-k830csc005hh1jh7up9v4dr4ijvaoikn.apps.googleusercontent.com";
    GoogleSignInClient googleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        EditText mail = view.findViewById(R.id.mail);
        EditText password = view.findViewById(R.id.password);
        Button btnRegister = view.findViewById(R.id.btnLogin);
        Button btnSingIn = view.findViewById(R.id.btnSingIn);



        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(IDTOKEN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Toast.makeText(getContext(), "Google sign in failed", Toast.LENGTH_SHORT).show();

                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
              //  Toast.makeText(getContext(), "Google sign in failed", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           // Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            Toast.makeText(getContext(), "Вы не прошли регистрацию", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                        // ...
                    }
                });
    }

}





//        btnRegister.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//;
//
//
//
//
//
//                if(!TextUtils.isEmpty(mail.getText()) && !TextUtils.isEmpty(mail.getText()))
//                mAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            assert user != null;
//                            String q = user.getUid();
//
//                            Toast.makeText(getContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
//
//                            Fragment fragment = new UserFragment();
//                            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//                            ft.replace(R.id.containerMain, fragment);
//                            ft.commit();
//                        }else {
//                            Toast.makeText(getContext(), "Вы не прошли регистрацию", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//        });
//
//
//        btnSingIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!TextUtils.isEmpty(mail.getText()) && !TextUtils.isEmpty(mail.getText()))
//                mAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            assert user != null;
//                            String q = user.getUid();
//
//                            Toast.makeText(getContext(), "Вы вошли успешно", Toast.LENGTH_SHORT).show();
//
//                            Fragment fragment = new UserFragment();
//                            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
//                            ft.replace(R.id.containerMain, fragment);
//                            ft.commit();
//                        }else {
//                            Toast.makeText(getContext(), "Вход не выполнен", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//            }
//        });
//    }







