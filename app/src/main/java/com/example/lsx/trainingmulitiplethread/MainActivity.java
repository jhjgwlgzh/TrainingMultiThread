package com.example.lsx.trainingmulitiplethread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView mImageview;
    private Button mLoadimagebutton;
    private Button mToastbutton;
    private ProgressBar mProgressbar;
    private Handler mHandle = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mProgressbar.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mProgressbar.setProgress((int) msg.obj);
                    break;
            }
            mProgressbar.setVisibility(View.VISIBLE);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageview = (ImageView) findViewById(R.id.Main_Activity_Image_View);
        mLoadimagebutton = (Button) findViewById(R.id.Main_Activity_Load_Image_Button);
        mToastbutton = (Button) findViewById(R.id.Main_Activity_Toast_Button);
        mProgressbar = (ProgressBar) findViewById(R.id.activity_main_progress_bar);
        mToastbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "XXXXXXXXXXXXXXX", Toast.LENGTH_SHORT).show();
            }
        });
        mLoadimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadImageTask().execute();
            }
        });
    }

    private void showimage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                sleep();
                mImageview.post(new Runnable() {
                    @Override
                    public void run() {
                        Message msg =new Message();
                        msg.what=0;
                        mHandle.sendMessage(msg);
                        for(int i=1;i<11;i++){
                            sleep();
                            Message msg2=new Message();
                            msg.what=0;
                            mHandle.sendMessage(msg);
                            msg2.what=1;
                            msg2.what=i*10;
                            mHandle.sendMessage(msg2);



                        }
                    }

                });
            }
        }).start();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class LoadImageTask extends AsyncTask<Void, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            for (int i = 0; i <11 ; i++) {
                sleep();
                publishProgress(i*10);
            }
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            Log.d(TAG, "Thread: "+Thread.currentThread().getName());
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageview.setImageBitmap(bitmap);
            Log.d(TAG, "Thread: "+Thread.currentThread().getName());
            mProgressbar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressbar.setProgress(values[0]);
        }
    }
}
