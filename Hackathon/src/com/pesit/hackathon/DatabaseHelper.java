package com.pesit.hackathon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// Database Value
	private static final String DATABASE_NAME = "AppData";

	// Details table name
	private static final String TABLE_DETAILS = "DETAILS";

	// Details Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_VARIABLE = "variable";
	private static final String KEY_VALUE = "value";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DETAILS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_VARIABLE + " TEXT,"
				+ KEY_VALUE + " TEXT NOT NULL" + ")";
		db.execSQL(CREATE_DETAILS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);

		// Create tables again
		onCreate(db);

	}

	public void addDetails(Details detail) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, detail.getID());
		values.put(KEY_VARIABLE, detail.getVariable()); // Details Variable
		values.put(KEY_VALUE, detail.getValue()); // Details Value

		// Inserting Row
		db.insert(TABLE_DETAILS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single detail
	public Details getDetails(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_DETAILS, new String[] { KEY_ID,
				KEY_VARIABLE, KEY_VALUE }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			Log.d("Database crap", "cursor!=null");

		if (!cursor.moveToFirst()) {
			System.out.println("Null shit");
			cursor.close();
			db.close();
			return null;
		}

		Details detail = new Details(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		cursor.close();
		db.close();
		return detail;
	}

	// Updating single detail
	public int updateDetails(Details detail) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_ID, detail.getID());
		values.put(KEY_VARIABLE, detail.getVariable());
		values.put(KEY_VALUE, detail.getValue());

		int updated = db.update(TABLE_DETAILS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(detail.getID()) });
		db.close();
		// updating row
		return updated;

	}

	// Deleting single contact
	public void deleteDetails(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DETAILS, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	public void deleteDatabse() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DETAILS, "1", new String[] {});
		Log.d("Database stuff", "Database table succesfully deleted");
		db.close();
	}

	// Deleting Contact not done yet from site. If required refer to
	// http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/

}
