package com.example.munseongsu.helloworld;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FrameLayout wheel;                                              //바퀴
    ArrayList<ImageView> carImgArr = new ArrayList<ImageView>();            //관람차 이미지뷰 배열
    ArrayList<ImageView> vomitCarImgArr = new ArrayList<ImageView>();       //관람차(토 이미지뷰 배열
    ImageView startBtn;                                             //시작버튼
    ImageView stopBtn;                                              //스톱버튼
    ImageView arrowUp;                                              //속도업
    ImageView arrowDown;                                            //속도다운
    TextView speedNumber;                                           //속도 텍뷰
    int speed = 1;                                                  //속도 값
    int angle = 0;                                                  //각도
    int i = 0;                                                      //반복문 사용
    boolean isStarted = false;                                      //회전시작 여부
    Handler rotationThreadHandler;                                  //회전 쓰레드 핸들러

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET 바퀴
        wheel = (FrameLayout) findViewById(R.id.wheel);

        //GET 시작/종료/스피드업/스피드다운/스피드숫자
        startBtn = (ImageView) findViewById(R.id.start_btn);
        stopBtn = (ImageView) findViewById(R.id.stop_btn);
        arrowUp = (ImageView) findViewById(R.id.arrow_up);
        arrowDown = (ImageView) findViewById(R.id.arrow_down);
        speedNumber = (TextView) findViewById(R.id.speed);

        //GET 관람차 이미지뷰
        carImgArr.add((ImageView) findViewById(R.id.green_car));        //0: green_car
        carImgArr.add((ImageView)findViewById(R.id.sky_car));           //1: sky_car
        carImgArr.add((ImageView)findViewById(R.id.violet_car));        //2: violet_car
        carImgArr.add((ImageView)findViewById(R.id.red_car));           //3: red_car
        carImgArr.add((ImageView) findViewById(R.id.orange_car));       //4: orange_car
        carImgArr.add((ImageView) findViewById(R.id.yellow_car));        //5: yellow_car

        //GET 관람차(승객이 토하는) 이미지뷰
        vomitCarImgArr.add((ImageView) findViewById(R.id.green_car_vomit));        //0: green_car
        vomitCarImgArr.add((ImageView)findViewById(R.id.sky_car_vomit));           //1: sky_car
        vomitCarImgArr.add((ImageView)findViewById(R.id.violet_car_vomit));        //2: violet_car
        vomitCarImgArr.add((ImageView)findViewById(R.id.red_car_vomit));           //3: red_car
        vomitCarImgArr.add((ImageView) findViewById(R.id.orange_car_vomit));       //4: orange_car
        vomitCarImgArr.add((ImageView) findViewById(R.id.yellow_car_vomit));        //5: yellow_car

        //클릭 이벤트 리스너 등록
        startBtn.setOnClickListener(btnOnClickListener);
        stopBtn.setOnClickListener(btnOnClickListener);
        arrowUp.setOnClickListener(btnOnClickListener);
        arrowDown.setOnClickListener(btnOnClickListener);

    }

    //클릭 이벤트 리스너
    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_btn :
                    stopBtn.setVisibility(View.VISIBLE);
                    isStarted = true;
                    startRotation();
                    break;
                case R.id.stop_btn :
                    stopBtn.setVisibility(View.GONE);
                    isStarted = false;
                    i=0;    //회전 반복문 변수 초기화
                    rotationThreadHandler.removeMessages(0);
                    stopVomitAll();
                    break;
                case R.id.arrow_up :
                    if(speed < 10) speed++;     //생명을 위해 최대 스피드 10으로 제한
                    speedNumber.setText(String.valueOf(speed));
                    if(speed > 4) {
                        if(isStarted) vomitAll();
                    }
                    break;
                case R.id.arrow_down :
                    if(speed > 1) speed--;  //속도에 마이너스는 없음
                    speedNumber.setText(String.valueOf(speed));
                    if(speed < 5) {
                        if(isStarted) stopVomitAll();
                    }
                    break;
            }
        }
    };

    //회전 시작!
    public void startRotation(){
        rotationThreadHandler = new Handler();

        for (; i<=3000 ;i++) {   //최대 약 8분
            rotationThreadHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    angle += speed;
                    wheel.setRotation(angle);
                    rotationCars(-angle);
                    rotationVomitCars(-angle);
                }

            }, 100 * i);
        }
    }

    //모든 카 회전
    public void rotationCars(int angleVal){
        for(int i=0; i<carImgArr.size(); i++){
            carImgArr.get(i).setRotation(angleVal);
        }
    }

    //모든 카(구토) 회전
    public void rotationVomitCars(int angleVal){
        for(int i=0; i<vomitCarImgArr.size(); i++){
            vomitCarImgArr.get(i).setRotation(angleVal);
        }
    }

    //모든 카 승객 구토
    public void vomitAll(){
        for(int i=0; i<vomitCarImgArr.size(); i++){
            vomitCarImgArr.get(i).setVisibility(View.VISIBLE);
        }
    }

    //모든 카 승객 구토 멈춤
    public void stopVomitAll(){
        for(int i=0; i<vomitCarImgArr.size(); i++){
            Log.i("size", vomitCarImgArr.size()+"");
            vomitCarImgArr.get(i).setVisibility(View.GONE);
        }
    }
}
