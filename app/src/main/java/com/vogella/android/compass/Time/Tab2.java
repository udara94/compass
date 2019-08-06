package com.vogella.android.compass.Time;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vogella.android.compass.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;


public class Tab2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab2() {
    }
    private TextView textView ;
    private Button start, pause, reset, lap, btnResume ;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private Handler handler;
    private int Seconds, Minutes, MilliSeconds ;
    private ListView listView ;
    private String[] ListElements = new String[] {  };
    private List<String> ListElementsArrayList ;
    private ArrayAdapter<String> adapter ;
    private boolean mIsStart = false;
    private LinearLayout startLayout;
    private LinearLayout pauseLayout;
   // private LinearLayout lapLayout;


    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = null;
        try {
            rootView = inflater.inflate(R.layout.fragment_tab2, container, false);

            ButterKnife.bind(this, rootView);
            textView = (TextView) rootView.findViewById(R.id.textView);
            start = (Button) rootView.findViewById(R.id.button);
            pause = (Button) rootView.findViewById(R.id.button2);
            reset = (Button) rootView.findViewById(R.id.button3);
            lap = (Button) rootView.findViewById(R.id.button4) ;
            listView = (ListView) rootView.findViewById(R.id.listview1);
            startLayout = (LinearLayout) rootView.findViewById(R.id.start_layout);
            pauseLayout = (LinearLayout) rootView.findViewById(R.id.pause_layout);
          //  lapLayout = (LinearLayout) rootView.findViewById(R.id.lap_layout);
            btnResume = (Button) rootView.findViewById(R.id.btn_resume);
            setUpUI();

        } catch (Exception ex) {
            System.out.println(ex);
        }
        setUpUI();
        return rootView;
    }

    private void setUpUI() {

        handler = new Handler() ;

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        listView.setAdapter(adapter);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                btnResume.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
               // reset.setVisibility(View.GONE);
                //lap.setVisibility(View.VISIBLE);
                toggleView(true);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("====================>puase");
                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);

                reset.setEnabled(true);
                mIsStart = false;
                btnResume.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
               // lapLayout.setVisibility(View.GONE);
                lap.setVisibility(View.GONE);
                reset.setVisibility(View.VISIBLE);



            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                textView.setText("00:00:00");

                ListElementsArrayList.clear();

                adapter.notifyDataSetChanged();
                mIsStart = false;
                toggleView(false);
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("====================>lap");
                if(mIsStart){

                    ListElementsArrayList.add(textView.getText().toString());
                    adapter.notifyDataSetChanged();
                }



            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResume.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                //lapLayout.setVisibility(View.VISIBLE);
                lap.setVisibility(View.VISIBLE);
                reset.setVisibility(View.GONE);
                startTimer();
            }
        });
    }

    private void startTimer(){
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        reset.setEnabled(false);
        mIsStart = true;
    }
    private void toggleView(boolean isStart){

        if(isStart){
            startLayout.setVisibility(View.GONE);
            pauseLayout.setVisibility(View.VISIBLE);
           // lapLayout.setVisibility(View.VISIBLE);
            lap.setVisibility(View.VISIBLE);
            reset.setVisibility(View.GONE);
        }
        else {
            startLayout.setVisibility(View.VISIBLE);
            pauseLayout.setVisibility(View.GONE);
           // lapLayout.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);
            lap.setVisibility(View.GONE);
        }
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

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            textView.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }

    };
}
