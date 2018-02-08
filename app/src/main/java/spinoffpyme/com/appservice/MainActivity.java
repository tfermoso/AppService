package spinoffpyme.com.appservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnStart;
    Button btnStop;
    Button btnBind;
    Button btnUnBind;
    Button btnGetNumber;
    TextView txtNumber;

    private MyService myService;
    private ServiceConnection serviceConnection;
    private Intent serviceIntent;
    private boolean isServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart=(Button)findViewById(R.id.btnStart);
        btnStop=(Button)findViewById(R.id.btnStop);
        btnBind=(Button)findViewById(R.id.btnBind);
        btnUnBind=(Button)findViewById(R.id.btnUnBind);
        btnGetNumber=(Button)findViewById(R.id.btnGetNumber);
        txtNumber=(TextView)findViewById(R.id.txtNumber);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnBind.setOnClickListener(this);
        btnUnBind.setOnClickListener(this);
        btnGetNumber.setOnClickListener(this);
        txtNumber.setText("000");

        Log.i("SERVICE DEMO","MainActivity thread id: "+Thread.currentThread().getId());
        serviceIntent=new Intent(this,MyService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                Log.i("SERVICE DEMO","sTART");
                startService(serviceIntent);
                break;
            case R.id.btnStop:
                stopService(serviceIntent);
                break;
            case R.id.btnBind:
                if(serviceConnection==null){
                    serviceConnection=new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName name, IBinder service) {
                            isServiceBound=true;
                            Log.i("SERVICE DEMO", "On service connected");
                            myService=((MyService.MyServiceBinder) service).getService();
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName name) {
                            isServiceBound=false;
                        }
                    };
                }
                bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btnUnBind:
                if(isServiceBound){
                    unbindService(serviceConnection);
                    isServiceBound=false;
                }
                break;
            case R.id.btnGetNumber:
                if(isServiceBound){
                    txtNumber.setText(String.valueOf(myService.getmRandomNumber()));
                }else {
                    txtNumber.setText("Service not bound");
                }
                break;
        }
    }
}
