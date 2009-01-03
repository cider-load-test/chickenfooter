package net.pillageandplunder.chickenfooter;

import java.sql.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChickenDatabase {
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private final Context mCtx;

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 1;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table games   (_id integer primary key autoincrement, created_at date);");
			db.execSQL("create table players (_id integer primary key autoincrement, name varchar(20), total_score integer, game_id integer);");
			db.execSQL("create table scores  (_id integer primary key autoincrement, value integer, player_id integer);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public ChickenDatabase(Context ctx) {
		this.mCtx = ctx;
	}

	public ChickenDatabase open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createGame() {
		ContentValues values = new ContentValues();
		Date date = new Date(System.currentTimeMillis());
		values.put("created_at", date.toString());

		return mDb.insert("games", null, values);
	}

    public boolean deleteGame(long gameId) {
    	Cursor c;
    	long playerId;
    	if (mDb.delete("games", "_id = " + gameId, null) > 0) {
    		// delete players associated with this game
    		c = mDb.query("players", new String[] { "_id" }, 
    				"game_id = " + gameId, null, null, null, null);
    		for (int i = 0; i < c.getCount(); i++) {
    			c.moveToPosition(i);
    			playerId = c.getLong(0);
    			mDb.delete("scores", "player_id = " + playerId, null);
    		}
    		mDb.delete("players", "game_id = " + gameId, null);
    		return true;
    	}
    	return false;
    }
    
	public Cursor fetchAllGames() {
		return mDb.query("games", new String[] { "_id", "created_at" },
				null, null, null, null, "_id");
	}

	public long createPlayer(long gameId, String name) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("game_id", gameId);
		values.put("total_score", 0);

		return mDb.insert("players", null, values);
	}
	
	public boolean addToPlayerScore(long playerId, int score) {
		Cursor player = fetchPlayer(playerId);
		int currentScore = player.getInt(2);
		
        ContentValues args = new ContentValues();
        args.put("_id", playerId);
        args.put("total_score", currentScore + score);
        
        return mDb.update("players", args, "_id =" + playerId, null) > 0;
	}
	
    public Cursor fetchPlayer(long playerId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, "players", new String[] { "_id", "name", 
                		"total_score" }, "_id =" + playerId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

	public Cursor fetchAllPlayers(Long gameId) {
		return mDb.query("players", new String[] { "_id", "name", "total_score" },
				"game_id = ?", new String[] { gameId.toString() }, null, null, "total_score");
	}
	
	public long createScore(long playerId, int value) {
		ContentValues values = new ContentValues();
		values.put("value", value);
		values.put("player_id", playerId);

		long retval = mDb.insert("scores", null, values);
		if (retval >= 0)
			addToPlayerScore(playerId, value);
		return retval;
	}
	
	public Cursor fetchAllScores(Long playerId) {
		return mDb.query("scores", new String[] { "_id", "value" },
				"player_id = ?", new String[] { playerId.toString() }, null, null, "_id");
	}
}
