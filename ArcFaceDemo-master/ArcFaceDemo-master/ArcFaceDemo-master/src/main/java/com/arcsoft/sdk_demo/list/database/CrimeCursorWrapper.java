package com.arcsoft.sdk_demo.list.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.arcsoft.sdk_demo.Crime;
import com.arcsoft.sdk_demo.list.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        int id=getInt(getColumnIndex(CrimeTable.Cols.ID));
        String title = getString(getColumnIndex(CrimeTable.Cols.MNAME));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        Crime crime;
        if (uuidString.equals("0"))
        { crime=new Crime();}
        else {
            crime = new Crime(UUID.fromString(uuidString));
            crime.setName(title);
            crime.setDate(new Date(date));
            crime.setSolved(isSolved != 0);
        }
        return crime;
    }
}
