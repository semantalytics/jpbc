package it.unisa.dia.gas.jpbc.android.jpbcset;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class JPBCSetActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        final BLS bls = new BLS();

        ((TextView) findViewById(R.id.result)).setText(bls.init());

        ((Button) findViewById(R.id.bench)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ((TextView) findViewById(R.id.result)).setText("Benchmarking...");
                ((TextView) findViewById(R.id.result)).setText(bls.init());
            }
        });
    }

}
