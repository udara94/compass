package com.vogella.android.compass.Time;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vogella.android.compass.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;


public class Tab3 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String[] secondsArray;
    private String[] minutesArray;
    private String[] hourArray;
    private String mParam1;
    private String mParam2;
    private NumberPicker mHourPicker, mMinutesPicker, mSecondsPicker ;
    private TextView txtHours, txtMinutes, txtSeconds;
    private Handler handler;
    private Runnable runnable;
    private long mSeconds = 0;
    private long mHours = 0;
    private long mMinutes = 0;
    private long totalMilliSeconds =0;
    private Button btnStart, btnPause, btnCancel, btnResume;
    private LinearLayout startLayout;
    private LinearLayout pauseLayout, numPickLayout;
    private CountDownTimer waitTimer;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private int decrementValue = 0;


    private OnFragmentInteractionListener mListener;

    public Tab3() {
    }


    public static Tab3 newInstance(String param1, String param2) {
        Tab3 fragment = new Tab3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

            ButterKnife.bind(this, rootView);


            mHourPicker = (NumberPicker) rootView.findViewById(R.id.hour_picker);
            mMinutesPicker = (NumberPicker) rootView.findViewById(R.id.minutes_picker);
            mSecondsPicker = (NumberPicker) rootView.findViewById(R.id.seconds_picker);

            txtHours = (TextView) rootView.findViewById(R.id.txt_hour);
            txtMinutes = (TextView) rootView.findViewById(R.id.txt_minutes);
            txtSeconds = (TextView) rootView.findViewById(R.id.txt_seconds);

            btnStart = (Button) rootView.findViewById(R.id.btn_start);
            btnPause = (Button) rootView.findViewById(R.id.btn_pause);
            btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
            btnResume = (Button) rootView.findViewById(R.id.btn_resume);

            startLayout = (LinearLayout) rootView.findViewById(R.id.start_layout);
            pauseLayout = (LinearLayout) rootView.findViewById(R.id.pause_layout);
            numPickLayout = (LinearLayout) rootView.findViewById(R.id.num_picker_layout);

            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

           // btnPause.setVisibility(View.VISIBLE);
            //btnResume.setVisibility(View.GONE);
            initializeData();


        } catch (Exception ex) {
            System.out.println(ex);
        }

        return rootView;
    }

    private void initializeData(){

        ArrayList<String> secondList = new ArrayList<>();
        ArrayList<String> hourList = new ArrayList<>();
        ArrayList<String> minutesList = new ArrayList<>();

        for(int i = 0 ; i<= 60; i++){

            if(i<10){
                String num = "0"+Integer.toString(i);
                secondList.add(num);
                minutesList.add(num);
            }else {
                String num = Integer.toString(i);
                secondList.add(num);
                minutesList.add(num);
            }


        }
        for(int i = 0; i< 100; i++){
            String num = Integer.toString(i);
            hourList.add(num);
        }
        //converting list to array
        secondsArray = new String[secondList.size()];
        secondsArray = secondList.toArray(secondsArray);

        minutesArray = new String[minutesList.size()];
        minutesArray = minutesList.toArray(minutesArray);

        hourArray = new String[hourList.size()];
        hourArray = hourList.toArray(hourArray);

        mSecondsPicker.setMinValue(0);
        mSecondsPicker.setMaxValue(secondsArray.length-1);
        mSecondsPicker.setDisplayedValues(secondsArray);

        mSecondsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                String val = secondsArray[newVal];
                txtSeconds.setText(val);
                mSeconds = Long.parseLong(val);
            }
        });

        mMinutesPicker.setMinValue(0);
        mMinutesPicker.setMaxValue(minutesArray.length-1);
        mMinutesPicker.setDisplayedValues(minutesArray);

        mMinutesPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                String val = minutesArray[newVal];
                txtMinutes.setText(val);
                mMinutes = Long.parseLong(val);
            }
        });

        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(hourArray.length-1);
        mHourPicker.setDisplayedValues(hourArray);

        mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                String val = hourArray[newVal];
                txtHours.setText(val);
                mHours = Long.parseLong(val);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCounter();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeTimer();
            }
        });
    }

    private void startCounter() {
        totalMilliSeconds = (mSeconds * 1000)+ (mMinutes * 60 * 1000)+ (mHours * 60 * 60 * 1000);
        //decrementValue = totalMilliSeconds/100;
        pStatus = (int)totalMilliSeconds;
        progressBar.setMax((int)totalMilliSeconds);
       // countDownStart();
        System.out.println("===================>decrementValue"+decrementValue );
        if(totalMilliSeconds != 0){
            countDouwnTimer();
            toggleButtonSet(true);

        }

    }

    private void toggleButtonSet(boolean status){
        if(status){
            numPickLayout.setVisibility(View.GONE);
            startLayout.setVisibility(View.GONE);
            pauseLayout.setVisibility(View.VISIBLE);
        }else {
            numPickLayout.setVisibility(View.VISIBLE);
            startLayout.setVisibility(View.VISIBLE);
            pauseLayout.setVisibility(View.GONE);
            resetTimer();
        }
    }

    private void resetTimer(){
        txtHours.setText("00");
        txtMinutes.setText("00");
        txtSeconds.setText("00");
        totalMilliSeconds = 0;
        mHours = 0;
        mMinutes = 0;
        mSeconds = 0;
        //pStatus = 0;
    }

    private void countDouwnTimer(){

        waitTimer = new CountDownTimer(totalMilliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                //called every 300 milliseconds, which could be used to
                //send messages or some other action
                long futureDate = new Date().getTime()+ totalMilliSeconds;
                long currentDate = new Date().getTime();

                long diff = futureDate - currentDate;

                long hours = diff / (60 * 60 * 1000);

                diff -= hours * (60 * 60 * 1000);
                long minutes = diff / (60 * 1000);

                diff -= minutes * (60 * 1000);
                long seconds = diff / 1000;

                txtHours.setText("" + String.format("%02d", hours));
                txtMinutes.setText("" + String.format("%02d", minutes));
                txtSeconds.setText("" + String.format("%02d", seconds));


                    progressBar.setProgress(pStatus);
                    pStatus-= 1000;
                totalMilliSeconds = totalMilliSeconds -1000;
                System.out.println("===================>totalMilliSeconds"+totalMilliSeconds );
                System.out.println("===================>pStatus"+pStatus );
            }


            public void onFinish() {
               toggleButtonSet(false);
                progressBar.setProgress(pStatus);
               System.out.println("===================>finished");
            }
        }.start();
    }


    private void cancelTimer(){
        if(waitTimer!=null){
            waitTimer.cancel();
            waitTimer=null;
        }
        toggleButtonSet(false);
    }
    private void pauseTimer(){
        waitTimer.cancel();
        btnPause.setVisibility(View.GONE);
        btnResume.setVisibility(View.VISIBLE);
    }
    private void resumeTimer(){
        waitTimer.start();
        btnPause.setVisibility(View.VISIBLE);
        btnResume.setVisibility(View.GONE);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
