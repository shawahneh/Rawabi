package com.techcamp.aauj.rawabi.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.techcamp.aauj.rawabi.API.BasicApi;
import com.techcamp.aauj.rawabi.API.WebService;
import com.techcamp.aauj.rawabi.Beans.Event;
import com.techcamp.aauj.rawabi.ICallBack;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.activities.ItemDetailsActivities.EventDetailsActivity;

import java.util.ArrayList;
import java.util.Date;




public class CalendarPageFragment extends Fragment implements ICallBack<ArrayList<Event>> {

    private CalendarView mCalendarView;
    private BasicApi mCalendarWebApi = WebService.getInstance(getContext());
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SweetAlertDialog  pDialog;
    public CalendarPageFragment() {
    }

    public static CalendarPageFragment newInstance() {
        CalendarPageFragment fragment = new CalendarPageFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_calendar_page, container, false);

        mRecyclerView = view.findViewById(R.id.rv);
        mCalendarView = view.findViewById(R.id.calendarView);
        mProgressBar = view.findViewById(R.id.progressBar);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                DateChangeClick(new Date(i,i1,i2));
            }
        });
        DateChangeClick(new Date());
        return view;
    }

    private void DateChangeClick(Date date) {
         pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
//        mProgressBar.setVisibility(View.VISIBLE);

        mCalendarWebApi.getEventAtDate(date,this);
    }



    @Override
    public void onResponse(ArrayList<Event> item) {
        pDialog.dismissWithAnimation();
//        mProgressBar.setVisibility(View.GONE);
        MyAdapter myAdapter = new MyAdapter(getContext(),item);
        mRecyclerView.swapAdapter(myAdapter,false);
    }

    @Override
    public void onError(String err) {
        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
    }



    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {
        private Context mContext;
        private ArrayList<Event> events;
        public MyAdapter(Context context,ArrayList<Event> events){
            mContext = context;
            this.events = events;
        }
        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // each data item is just a string in this case
            private TextView mEventName,mEventDesc,mEventDate;
            private ImageView mEventImage;
            private Event mEvent;
            public ViewHolder(View view) {
                super(view);
                mEventDesc = view.findViewById(R.id.eventDescTestView);
                mEventName = view.findViewById(R.id.eventNameTextView);
                mEventDate = view.findViewById(R.id.eventDateTextView);

                mEventImage = view.findViewById(R.id.imageView);

                itemView.setOnClickListener(this);
            }

           public void bind(Event event){
               mEvent = event;
               mEventDesc.setText(event.getDescription());
               mEventName.setText(event.getName());
               mEventDate.setText(DateUtils.getRelativeDateTimeString(getContext(), event.getDate().getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0));
           }

            @Override
            public void onClick(View view) {
                Intent i = EventDetailsActivity.getIntent(getContext(),mEvent);
                startActivity(i);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View view =  LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_event, parent, false);

            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Event event = events.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }
}
