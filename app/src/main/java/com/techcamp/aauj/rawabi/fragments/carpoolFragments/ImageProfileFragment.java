package com.techcamp.aauj.rawabi.fragments.carpoolFragments;


import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;

import static android.app.Activity.RESULT_OK;


public class ImageProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private Button btnUpload,btnSkip;
    private IListener mListener;
    private Uri mUriImage;

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
        btnUpload= view.findViewById(R.id.btnUpload);

        Glide.with(getContext()).load(R.drawable.ic_person_black_24dp)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onFragmentButtonClick(v);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    private void startUpload() {
        if(mUriImage == null){
            Toast.makeText(getContext(), "choose a image", Toast.LENGTH_SHORT).show();
            return;
        }

        WebFactory.getAuthService().setImageForUser(mUriImage, new ICallBack<String>() {
            @Override
            public void onResponse(String url) {
                Glide.with(getContext()).load(url).into(imageView);
                Toast.makeText(getContext(), "Image changed successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String err) {
                Log.d("tag","onError="+err);
                Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface IListener{
        void onFragmentButtonClick(View view);
    }
}
