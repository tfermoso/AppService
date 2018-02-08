package spinoffpyme.com.appservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by tomas on 08/02/2018.
 */

public class MyService extends Service {

    private int mRandomNumber=111;
    private boolean isRandomGeneratorOn;



    public class MyServiceBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }
    private IBinder mBinder=new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("SERVICE DEMO"," onBind");
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      Log.i("SERVICE DEMO","onStartCommand, thread id: "+Thread.currentThread().getId());
        isRandomGeneratorOn=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomGenerator();
            }
        }).start();
        return START_STICKY;
    }

    private void startRandomGenerator() {
        while(isRandomGeneratorOn){
            try {
                Thread.sleep(1000);
                mRandomNumber=new Random().nextInt(100);
                Log.i("SERVICE DEMO", "Thread id: "+Thread.currentThread().getId()+ ", Number: "+mRandomNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void stopRandomGenerator(){
        isRandomGeneratorOn=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenerator();
        Log.i("SERVICE DEMO","Thread interrupted");
    }

    public int getmRandomNumber() {
        return mRandomNumber;
    }
}
