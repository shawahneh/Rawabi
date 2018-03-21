package com.techcamp.aauj.rawabi.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techcamp.aauj.rawabi.model.NewsItem;
import com.techcamp.aauj.rawabi.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class NewsFragment extends Fragment {
    private ViewPager mViewPager;
    private LinearLayout mLinearLayoutDots;

    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager=view.findViewById(R.id.viewPager);
        mLinearLayoutDots = view.findViewById(R.id.linearLayout);

        ArrayList<NewsItem> list = getContent();
        NewsSliderAdapter adapter = new NewsSliderAdapter(list);
        mViewPager.setAdapter(adapter);

        addDotsIndicator(0);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
    }
    private void addDotsIndicator(int pos){
        TextView[] mDots = new TextView[getContent().size()];

        mLinearLayoutDots.removeAllViews();

        for (int i=0;i<mDots.length;i++) {
            mDots[i] = new TextView(getContext());
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextColor(getResources().getColor(R.color.transWhite));
            mDots[i].setTextSize(35);
            mLinearLayoutDots.addView(mDots[i]);
        }
        if(mDots.length >0){
            mDots[pos].setTextColor(getResources().getColor(R.color.white));
        }
    }
    private ArrayList<NewsItem> getContent() {
        ArrayList<NewsItem> list = new ArrayList<>();
        NewsItem content = new NewsItem();
        content.setId(1);
        content.setDate(new Date());
        content.setImgUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/28950995_10156299597449581_2017080253580386197_n.jpg?oh=6b1e138f59afd9c8006863e47f1c0629&oe=5B2EFB9C");
        content.setTitle("خلال ندوة حول أوضاع القدس");
        content.setText("يهدف برنامج \"نزرع شجر أكتر، فلسطين فينا بتكبر\" الى ترسيخ مبدأ الحفاظ على البيئة الخضراء والصحية، وتوعية الأطفال بأهمية المحافظة على الطبيعة وحمايتها. وأنتم أيضاً بإمكانكم المشاركة في البرنامج وزراعة شجرة جديدة في أراضينا عن طريق الرابط التالي: \n" +
                "https://goo.gl/dfvyZr");
        NewsItem content2 = new NewsItem();
        content2.setId(1);
        content2.setDate(new Date());
        content2.setImgUrl("https://scontent.fjrs3-1.fna.fbcdn.net/v/t1.0-9/26220288_10156296112114581_111965342120004516_n.jpg?oh=3034656b52259737f05d32f9d3665374&oe=5B477E40");
        content2.setTitle("خلال ندوة حول أوضاع القدس");
        content2.setText("يهدف برنامج \"نزرع شجر أكتر، فلسطين فينا بتكبر\" الى ترسيخ مبدأ الحفاظ على البيئة الخضراء والصحية، وتوعية الأطفال بأهمية المحافظة على الطبيعة وحمايتها. وأنتم أيضاً بإمكانكم المشاركة في البرنامج وزراعة شجرة جديدة في أراضينا عن طريق الرابط التالي: \n" +
                "https://goo.gl/dfvyZr");

        list.add(content);
        list.add(content2);

        return list;
    }

        class NewsSliderAdapter extends PagerAdapter {
        List<NewsItem> contents;
        LayoutInflater layoutInflater;
        public NewsSliderAdapter(List<NewsItem> contents) {
            this.contents = contents;
        }

        @Override
        public int getCount() {
            return contents.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (CoordinatorLayout)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.slide_layout_news,container,false);

            ImageView imageView =view.findViewById(R.id.imageView);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvDate = view.findViewById(R.id.tvDate);

            Glide.with(getContext()).load(contents.get(position).getImgUrl()).into(imageView);

            tvTitle.setText(contents.get(position).getTitle());
            tvDate.setText(DateUtils.getRelativeTimeSpanString(contents.get(position).getDate().getTime()));

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }
    }

}
