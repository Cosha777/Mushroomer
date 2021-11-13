package ua.cosha.mushroomer.Login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ua.cosha.mushroomer.Map.UserFragment;
import ua.cosha.mushroomer.R;
import ua.cosha.mushroomer.data.model.User;


public class LoginFragment extends Fragment {
    FirebaseAuth mAuth;

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

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(mail.getText()) && !TextUtils.isEmpty(mail.getText()))
                mAuth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String q = user.getUid();

                            Toast.makeText(getContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();

                            Fragment fragment = new UserFragment();
                            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                            ft.replace(R.id.containerMain, fragment);
                            ft.commit();
                        }else {
                            Toast.makeText(getContext(), "Вы не прошли регистрацию", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mail.getText()) && !TextUtils.isEmpty(mail.getText()))
                mAuth.signInWithEmailAndPassword(mail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String q = user.getUid();

                            Toast.makeText(getContext(), "Вы вошли успешно", Toast.LENGTH_SHORT).show();

                            Fragment fragment = new UserFragment();
                            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                            ft.replace(R.id.containerMain, fragment);
                            ft.commit();
                        }else {
                            Toast.makeText(getContext(), "Вход не выполнен", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }


}






