package it.unisa.dia.gas.jpbc.android.benchmark;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class JPBCBenchmarkActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "JPBCBenchmarkActivity";

    protected AndroidBenchmark androidBenchmark;

    protected boolean running = false;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(it.unisa.dia.gas.jpbc.android.jpbcset.R.layout.main);

        this.androidBenchmark = new AndroidBenchmark(10);

        ((TextView) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.status)).setText("");
        findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.benchmark).setOnClickListener(this);

        Log.i(TAG, "onCreate.finished");
    }

    public void onClick(View view) {
        Log.i(TAG, "onClick");

        if (running) {
            ((TextView) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.status)).setText("Stopping...");

            running = false;
            stopBenchmark();
        } else {
            findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.progress).setVisibility(View.VISIBLE);
            ((TextView) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.status)).setText("Benchmarking...");
            ((Button) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.benchmark)).setText("Stop");

            running = true;
            benchmark();
        }

        Log.i(TAG, "onClick.finished");
    }


    protected void benchmark() {
        Thread t = new Thread() {
            public void run() {
                Benchmark benchmark = androidBenchmark.benchmark(new String[]{
                        "it/unisa/dia/gas/jpbc/android/jpbcset/benchmark/curves/a.properties"
                });

                //Send update to the main thread
                messageHandler.sendMessage(
                        Message.obtain(messageHandler, (benchmark != null) ? 0 : 1, benchmark));

            }
        };
        t.start();
    }

    protected void stopBenchmark() {
        androidBenchmark.stop();
    }


    // Instantiating the Handler associated with the main thread.
    private Handler messageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ((TextView) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.status)).setText("Benchmark Completed!");
                    ((Button) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.benchmark)).setText("Benchmark");
                    findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.progress).setVisibility(View.INVISIBLE);

                    Log.i(TAG, ((Benchmark) msg.obj).toHTML());
                    break;
                case 1:
                    ((TextView) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.status)).setText("Benchmark Stopped!");
                    ((Button) findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.benchmark)).setText("Benchmark");
                    findViewById(it.unisa.dia.gas.jpbc.android.jpbcset.R.id.progress).setVisibility(View.INVISIBLE);
                    break;
            }
        }

    };
}
