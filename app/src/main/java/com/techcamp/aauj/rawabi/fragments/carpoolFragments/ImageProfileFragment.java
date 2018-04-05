package com.techcamp.aauj.rawabi.fragments.carpoolFragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.fragments.abstractFragments.ListFragment;

import static android.app.Activity.RESULT_OK;


public class ImageProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private ImageView imageView,imageAdd;
    private Button btnContinue,btnSkip;
    private IListener mListener;
    private Uri mUriImage;
    private ProgressBar progressBar;


    public ImageProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.imageView);
        btnSkip = view.findViewById(R.id.btnSkip);
        btnContinue= view.findViewById(R.id.btnContinue);
        imageAdd= view.findViewById(R.id.imageAdd);
        progressBar= view.findViewById(R.id.progressBar);

        imageAdd.setVisibility(View.VISIBLE);

        Glide.with(getContext()).load(R.color.gray)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onFragmentImageProfileSkipClick();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onFragmentImageProfileSkipClick();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCommand();
            }
        });
    }

    private void uploadCommand() {
        openGallery();
    }
    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        startActivityForResult(getIntent, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            mUriImage = data.getData();
            Glide.with(this).load(mUriImage).into(imageView);


            startUpload();
            imageAdd.setVisibility(View.GONE);
        }
    }

    private void startUpload() {
        if(mUriImage == null){
            Toast.makeText(getContext(), "choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        WebFactory.getAuthService().setImageForUser(mUriImage, new ICallBack<String>() {
            @Override
            public void onResponse(String url) {
                showProgress(false);
                Glide.with(getContext()).load(url).into(imageView);
                Toast.makeText(getContext(), "Image changed successfully", Toast.LENGTH_SHORT).show();
                btnContinue.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.GONE);
            }

            @Override
            public void onError(String err) {
                showProgress(false);
                Log.d("tag","onError="+err);
                Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgress(boolean b){
        progressBar.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ImageProfileFragment.IListener){
            mListener = (ImageProfileFragment.IListener) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement IListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface IListener{
        void onFragmentImageProfileSkipClick();
    }
}
