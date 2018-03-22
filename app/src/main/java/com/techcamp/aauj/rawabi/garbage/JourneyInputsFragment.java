package com.techcamp.aauj.rawabi.garbage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techcamp.aauj.rawabi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JourneyInputsFragment extends Fragment {


    public JourneyInputsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journey_inputs, container, false);
    }

}
