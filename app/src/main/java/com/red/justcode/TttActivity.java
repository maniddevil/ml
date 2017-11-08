package com.red.justcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.red.justcode.learning.TestTraining;


public class TttActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);

        //MANI For testing purpose, others can comment below code
        TestTraining tt = new TestTraining();
        tt.dummyTest(this);
        tt.neuralNetworkTests();
        //MANI End
    }
}
