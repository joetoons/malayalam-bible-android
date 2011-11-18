package com.jeesmon.malayalambible;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String DB_PATH = "/data/data/com.jeesmon.malayalambible/databases/";

	private static String DB_NAME = "malayalam-bible.db";

	private SQLiteDatabase bibleDB;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		
		try {
			createDataBase();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			this.close();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		OutputStream databaseOutputStream = new FileOutputStream(
				DB_PATH + DB_NAME);
		InputStream databaseInputStream;

		byte[] buffer = new byte[1024];
		
		int[] id = {
			R.raw.aaa,
			R.raw.aab,
			R.raw.aac,
			R.raw.aad,
			R.raw.aae,
			R.raw.aaf,
			R.raw.aag,
			R.raw.aah,
			R.raw.aai,
			R.raw.aaj,
			R.raw.aak,
			R.raw.aal,
			R.raw.aam,
			R.raw.aan,
			R.raw.aao,
			R.raw.aap,
			R.raw.aaq
		};
		
		for (int i : id) {
			databaseInputStream = myContext.getResources().openRawResource(i);
			while (databaseInputStream.read(buffer) > 0) {
				databaseOutputStream.write(buffer);
			}
			databaseInputStream.close();
		}
		
		databaseOutputStream.flush();
		databaseOutputStream.close();
	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		bibleDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READONLY);

	}

	@Override
	public synchronized void close() {

		if (bibleDB != null)
			bibleDB.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

}