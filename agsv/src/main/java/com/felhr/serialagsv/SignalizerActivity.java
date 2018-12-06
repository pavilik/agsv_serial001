package com.felhr.serialagsv;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SignalizerActivity extends AppCompatActivity {

    RecyclerViewSignalizerAdapter adapter;
    private UsbService usbService;
    Handler mHandler2;

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();

            usbService.setHandler(mHandler2);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalizer);
        mHandler2 = new MyHandler2(this);
        Button testBtn = findViewById(R.id.testbutton);

        ArrayList<SignalizerItem> signalizerItemArrayList = new ArrayList<>();


        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    signalizerItemArrayList.stream().filter(signalizerItem ->signalizerItem.getNumberSignalizer()%2==0).collect(Collectors.toList());
//                }
                ///
                signalizerItemArrayList.clear();

                for (int i = 1; i <= 255; i++) {

                    SignalizerItem signalizerItem = new SignalizerItem(i, false, 1);
                    signalizerItemArrayList.add(signalizerItem);
                }

                adapter.changeInformationSignazer(signalizerItemArrayList);
                adapter.notifyDataSetChanged();

            }
        });

        // Заполнение начального состояния сигнализаторов и заполнение списка
        for (int i = 1; i <= 255; i++) {
            SignalizerItem signalizerItem = new SignalizerItem(i, false, 0);
            signalizerItemArrayList.add(signalizerItem);
        }
        //конец заполнения начального состояния
        // data to populate the RecyclerView with
        // String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};
        //String[] data2 = { "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};


        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        int numberOfColumns = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new RecyclerViewSignalizerAdapter(this, signalizerItemArrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        bindService(new Intent(this, UsbService.class), usbConnection, Context.BIND_AUTO_CREATE);
        // startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }


    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private class MyHandler2 extends Handler {
        private final WeakReference<SignalizerActivity> mActivity;

        public MyHandler2(SignalizerActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    Log.i("TAG", "Активити 2 данные: "+ data);
                    dataComPortAnalyses(msg);
                    //  mActivity.get().display.append(data);
                    //TODO
                    //обработка сообщения от сервиса
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    // метод анализа данных пришедших из порта, изменение данных в адаптере
public void dataComPortAnalyses (Message msg){
//       byte[] data = msg.obj.toString().getBytes();
//       for (int i=0; i<data.length;i++)
//       {
//          byte unobyte = data[i];
//
//
//       }
    String dataBuffer = msg.obj.toString();

    if (dataBuffer.contains(new String(new byte []{0,8,18})))
    {
        Log.i("TAG", "Активити 2 данные: сошлись )))");

    }





}

}
