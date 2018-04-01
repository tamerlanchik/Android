package com.example.andrey.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by PC on 18.02.2018.
 */

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}

