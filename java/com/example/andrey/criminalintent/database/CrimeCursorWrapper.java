package com.example.andrey.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.andrey.criminalintent.Crime;

import java.util.Date;
import java.util.UUID;

import static com.example.andrey.criminalintent.database.CrimeDbSchema.*;

/**
 * Created by PC on 24.03.2018.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECT));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved !=0);
        crime.setSuspect(suspect);

        return crime;
    }
}
