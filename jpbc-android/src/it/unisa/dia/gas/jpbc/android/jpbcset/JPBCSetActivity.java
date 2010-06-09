package it.unisa.dia.gas.jpbc.android.jpbcset;

import android.app.Activity;
import android.os.Bundle;
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
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);

        super.onCreate(savedInstanceState);
        BLS bls = new BLS();
        TextView tv = new TextView(this);
        tv.setText(bls.init());
        setContentView(tv);
    }

}
