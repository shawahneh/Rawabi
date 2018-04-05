package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.techcamp.aauj.rawabi.API.AuthWebApi;
import com.techcamp.aauj.rawabi.API.WebDummy;
import com.techcamp.aauj.rawabi.API.WebFactory;
import com.techcamp.aauj.rawabi.abstractAdapters.Holder;
import com.techcamp.aauj.rawabi.model.User;
import com.techcamp.aauj.rawabi.callBacks.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.controllers.SPController;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private ImageView imageView;
    private FloatingActionButton fabAddImage;
    private RecyclerView recyclerView;
    private Uri mUriImage;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);
        fabAddImage = findViewById(R.id.fabAddImage);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ProfileItem> profileItems = getItems();
        MyAdapter adapter = new MyAdapter(profileItems);
        recyclerView.setAdapter(adapter);


        User user = SPController.getLocalUser(this);
        if(user != null && user.getImageurl() != null)
            Glide.with(this).load(user.getImageurl()).apply(RequestOptions.placeholderOf(R.drawable.ic_person_black_24dp))
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);

       fabAddImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openGallery();
           }
       });

        setTitle("");
    }

    private List<ProfileItem> getItems() {
        ArrayList<ProfileItem> items = new ArrayList<>();
        User user = SPController.getLocalUser(this);

        // TODO: 4/6/2018 change these images
        items.add(new ProfileItem("Name",user.getFullname(),R.drawable.ic_person_black_24dp));
        items.add(new ProfileItem("Phone",user.getPhone(),R.drawable.ic_phone_black_24dp));
        items.add(new ProfileItem("Password","change your password",R.drawable.ic_lock_black_24dp));

        return items;
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
            Glide.with(this).load(mUriImage).apply(RequestOptions.circleCropTransform()).into(imageView);


            startUpload();
        }
    }

    private void startUpload() {
        if(mUriImage == null){
            Toast.makeText(EditProfileActivity.this, "choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);
        WebFactory.getAuthService().setImageForUser(mUriImage, new ICallBack<String>() {
            @Override
            public void onResponse(String url) {
                showProgress(false);
                Toast.makeText(EditProfileActivity.this, "Image changed successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String err) {
                showProgress(false);
                Log.d("tag","onError="+err);
                Toast.makeText(EditProfileActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showProgress(boolean b){
        progressBar.setVisibility(b?View.VISIBLE:View.GONE);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{
        List<ProfileItem> list;

        public MyAdapter(List<ProfileItem> list) {
            this.list = list;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_edit_profile_element, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            holder.render(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder{
            private TextView tvTitle,tvHint;
            private ImageView imageView;
            public MyHolder(View itemView) {
                super(itemView);
                tvHint = itemView.findViewById(R.id.tvHint);
                tvTitle= itemView.findViewById(R.id.tvTitle);
                imageView= itemView.findViewById(R.id.imageView);
            }
            void render(ProfileItem item){
                tvTitle.setText(item.getTitle());
                tvHint.setText(item.getHint());
                imageView.setImageResource(item.getImage());
            }
        }
    }

    class ProfileItem{
        private String title,hint;
        private int image;

        public ProfileItem(String title, String hint, int image) {
            this.title = title;
            this.hint = hint;
            this.image = image;
        }

        public String getTitle() {

            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }
    } /* model for edit profile recycler */
}
