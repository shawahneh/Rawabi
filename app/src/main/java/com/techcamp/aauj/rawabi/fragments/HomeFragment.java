package com.techcamp.aauj.rawabi.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.LoginRegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button btnOpenCarpool;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        btnOpenCarpool = view.findViewById(R.id.btnOpenCarpool);

        btnOpenCarpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), LoginRegisterActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

}
