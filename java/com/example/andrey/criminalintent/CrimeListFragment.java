package com.example.andrey.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by PC on 18.02.2018.
 */

public class CrimeListFragment extends Fragment {
    private static final String KEY_SAVED_SUBTITLE_VISIBLE = "com.andrey.criminalintent.mSubtitleVisible";
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int currentListElement=0;
    private boolean mSubtitleVisible;
    private TextView mEmptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmptyView = view.findViewById(R.id.empty_view);
        if(savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(KEY_SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        Log.d(" ", "onCreate() CrimeListFragment");
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        //String subtitle = getString(R.string.subtitle_format, crimeCount);
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);
        if(!mSubtitleVisible) {
            subtitle = null;
        }
        /*if (mSubtitleVisible) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.getSupportActionBar().setSubtitle(subtitle);
        }*/
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if(activity.getSupportActionBar() == null) {
                Log.d("test", "No support actionbar");
            }
            else {
                activity.getSupportActionBar().setSubtitle(subtitle);
            }

        /*AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                Crime crime = new Crime();
                //TODO: create "startActivityForResult" to return answer if the crime was created (changed)
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                //Intent intent = CrimePagerActivity.newIntent2(getActivity());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;
        private int number;
        private SimpleDateFormat dateFormatMaster;
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved_img);
            dateFormatMaster = new SimpleDateFormat("EEEE, d MMM y, HH:mm");
        }
        public void bind(Crime crime, int numb){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(dateFormatMaster.format(mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            number = numb;
        }

        @Override
        public void onClick(View view) {
            //setPositionToUpdate(number);
            setPositionToUpdate(this.getAdapterPosition());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

    private void updateUI() {
        Log.d("CrimeListFragment", "updateUI()");
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);

        }else{
            /*if(mAdapter.mCrimes.size()==0) {
                //mEmptyView.setVisibility(View.VISIBLE);
                mCrimeRecyclerView.setVisibility(View.GONE);
            }
            else{
                //mEmptyView.setVisibility(View.GONE);
                mCrimeRecyclerView.setVisibility(View.VISIBLE);
            }*/
            //Log.d("CrimeListFragment", Integer.toString(mAdapter.mCrimes.size()));
            //CrimeLab crimeLab = CrimeLab.get(getActivity());
            //List<Crime> crimes = crimeLab.getCrimes();
            //mAdapter = new CrimeAdapter(crimes);
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
            mEmptyView.setVisibility(mAdapter.mCrimes.size() > 0? View.GONE : View.VISIBLE);

            //mAdapter.notifyItemChanged(currentListElement); //too long update & error when delete item
        }
        updateSubtitle();
    }
    private void setPositionToUpdate(int position) {
        currentListElement = position;
    }
}
