package com.susyimes.administrator.channelpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChannelPickerActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG_DATA_PROVIDER = "data provider";
    private static final String FRAGMENT_LIST_VIEW = "list view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channelpick);
        if (savedInstanceState == null) {
            /*getSupportFragmentManager().beginTransaction()
                    .add(new ExampleDataProviderFragment(), FRAGMENT_TAG_DATA_PROVIDER)
                    .commit();*/
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DraggableGridFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }

    }

}
