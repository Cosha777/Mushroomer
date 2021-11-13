package ua.cosha.mushroomer.Map;

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
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ua.cosha.mushroomer.R;
import ua.cosha.mushroomer.data.model.User;

public class UserFragment extends Fragment {
    Button toMap;
    EditText editName;
    User user;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editName = view.findViewById(R.id.editName);
        toMap = view.findViewById(R.id.btnToMap);



        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editName.getText();
                if(!TextUtils.isEmpty(editName.getText())){
                    Fragment fragment = new MapsFragment();
                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    ft.replace(R.id.containerMain, fragment);
                    ft.commit();
                }else {
                    Toast.makeText(getContext(), "Введите имя", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
