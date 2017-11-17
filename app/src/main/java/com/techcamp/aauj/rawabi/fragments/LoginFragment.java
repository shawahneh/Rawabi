package com.techcamp.aauj.rawabi.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.techcamp.aauj.rawabi.API.AuthWebApi;
import com.techcamp.aauj.rawabi.API.WebApi;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.ITriger;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.CarpoolActivity;
import com.techcamp.aauj.rawabi.activities.UserTypeActivity;


public class LoginFragment extends Fragment {

    private EditText mEditTextEmail,mEditTextPassword;
    private AuthWebApi mAuthWebApi = WebApi.getInstance(getContext());
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        View btn = view.findViewById(R.id.btnLogin);
        mEditTextEmail = view.findViewById(R.id.txtEmail);
        mEditTextPassword = view.findViewById(R.id.txtPassword);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getContext(), UserTypeActivity.class);
//                getActivity().finish();
//                startActivity(i);
                mAuthWebApi.login(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString(),
                        new ITriger<User>() {
                            @Override
                            public void onTriger(User value) {
                                if (value!=null)
                                {
                                    Toast.makeText(getContext(), "Logged In", Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(getContext(), "Not Logged In", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
