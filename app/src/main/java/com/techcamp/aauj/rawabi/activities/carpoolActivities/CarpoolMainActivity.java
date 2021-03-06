package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.abstractActivities.BasicActivity;
import com.techcamp.aauj.rawabi.activities.abstractActivities.EmptyActivity;
import com.techcamp.aauj.rawabi.fragments.carpoolFragments.CarpoolMainFragment;
import com.techcamp.aauj.rawabi.sliderLib.SliderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALa on 2/9/2018.
 */
// First Activity for carpool
public class CarpoolMainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private LinearLayout mLinearLayoutDots;
    private Button btnRider,btnDriver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool);
        mViewPager=findViewById(R.id.viewPager);
        mLinearLayoutDots = findViewById(R.id.linearLayout);
        btnDriver = findViewById(R.id.btnDriver);
        btnRider = findViewById(R.id.btnRider);

        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarpoolMainActivity.this,MapDriverActivity.class));
            }
        });
        btnRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarpoolMainActivity.this,MapRiderActivity.class));
            }
        });

        ArrayList<SlideContent> list = getContent();
        CarpoolSliderAdapter adapter = new CarpoolSliderAdapter(this,list);
        mViewPager.setAdapter(adapter);

        addDotsIndicator(0);

        mViewPager.addOnPageChangeListener(listener);

        setTitle(getString(R.string.carpool));


    }
    private void addDotsIndicator(int pos){
        TextView[] mDots = new TextView[getContent().size()];
        
        mLinearLayoutDots.removeAllViews();

        for (int i=0;i<mDots.length;i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextColor(getResources().getColor(R.color.transWhite));
            mDots[i].setTextSize(35);
            mLinearLayoutDots.addView(mDots[i]);
        }
        if(mDots.length >0){
            mDots[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private ArrayList<SlideContent> getContent() {
        ArrayList<SlideContent> list = new ArrayList<>();
        SlideContent content = new SlideContent();
        content.setTitle(getString(R.string.carpool));
        content.setDescription("This will help you find a driver near you, and take you to or from Rawabi");
        content.setImage(R.drawable.carpool_h1);

        SlideContent content1 = new SlideContent();
        content1.setTitle(getString(R.string.carpool));
        content1.setDescription("You Set your location and Destination");
        content1.setImage(R.drawable.carpool_h2);

        SlideContent content2 = new SlideContent();
        content2.setTitle(getString(R.string.carpool));
        content2.setDescription("Find the driver nearby, and call him to setup a meeting location.");
        content2.setImage(R.drawable.carpool_h3);

        list.add(content);
        list.add(content1);
        list.add(content2);

        return list;
    }

//    class CarpoolSliderAdapter extends PagerAdapter{
//        List<SlideContent> contents;
//        LayoutInflater layoutInflater;
//        public CarpoolSliderAdapter(List<SlideContent> contents) {
//            this.contents = contents;
//        }
//
//        @Override
//        public int getCount() {
//            return contents.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == (LinearLayout)object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//            View view = layoutInflater.inflate(R.layout.slide_layout_carpool,container,false);
//
//            ImageView imageView =view.findViewById(R.id.imageView);
//            TextView tvTitle = view.findViewById(R.id.tvTitle);
//            TextView tvDescription = view.findViewById(R.id.tvDescription);
//
//            imageView.setImageResource(contents.get(position).getImage());
//            tvTitle.setText(contents.get(position).getTitle());
//            tvDescription.setText(contents.get(position).getDescription());
//
//            container.addView(view);
//            return view;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((LinearLayout)object);
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.carpool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_profile) {
            startActivity(new Intent(this,ProfileActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class CarpoolSliderAdapter extends SliderAdapter<SlideContent>{

    public CarpoolSliderAdapter(Context mContext, ArrayList<SlideContent> items) {
        super(mContext, items);
    }

    @Override
    public void bind(View view,SlideContent item, int pos) {

            ImageView imageView =view.findViewById(R.id.imageView);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvDescription = view.findViewById(R.id.tvDescription);

            imageView.setImageResource(item.getImage());
            tvTitle.setText(item.getTitle());
            tvDescription.setText(item.getDescription());
    }

    @Override
    public int getLayout() {
        return R.layout.slide_layout_carpool;
    }
    }

    class SlideContent{
        private String title,description;
        private int image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }
    }
}
