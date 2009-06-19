package net.pillageandplunder.chickenfooter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Players extends ListActivity {
	private static final int ACTIVITY_PLAYER_NEW = 0;
	private static final int ACTIVITY_PLAYER_SCORES = 1;
	private static final int ACTIVITY_PLAYER_NEW_SCORE = 2;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

	
	private Long mGameId;
	private Long mPlayerId;
	private ChickenDatabase mDbHelper;
	private Cursor mPlayersCursor;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.players);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mGameId = extras.getLong("gameId");
        }
        
        mDbHelper = new ChickenDatabase(this);
        mDbHelper.open();
        fillData();
        
        ListView lv = getListView();
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        	@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
        		onListItemLongClick(v, pos, id);
				return false;
			}
		}); 
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
        Cursor c = mPlayersCursor;
        c.moveToPosition(position);
        newScore(id, c.getString(1));
    }

    protected void onListItemLongClick(View v, int position, long id) {
        Cursor c = mPlayersCursor;
        c.moveToPosition(position);
        playerScores(id, c.getString(1));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi = menu.add(0, INSERT_ID, 0, R.string.new_player);
        mi.setIcon(android.R.drawable.ic_menu_add);
        
        mi = menu.add(0, DELETE_ID, 0, R.string.delete_game);
        mi.setIcon(android.R.drawable.ic_menu_delete);
        
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            newPlayer();
            return true;
        case DELETE_ID:
        	deleteGame();
        	return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        mPlayersCursor = mDbHelper.fetchAllPlayers(mGameId);
        startManagingCursor(mPlayersCursor);

        String[] from = new String[] { "name", "total_score" };
        int[] to = new int[] { R.id.player_name1, R.id.player_score1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter players =
            new SimpleCursorAdapter(this, R.layout.players_row, mPlayersCursor, from, to);
        setListAdapter(players);    	
    }
    
    private void newPlayer() {
        Intent i = new Intent(this, PlayerNew.class);
        startActivityForResult(i, ACTIVITY_PLAYER_NEW);
    }
    
    private void playerScores(long id, String name) {
        Intent i = new Intent(this, Scores.class);
        i.putExtra("playerId", id);
        i.putExtra("name", name);
        startActivityForResult(i, ACTIVITY_PLAYER_SCORES);
    }
    
    private void deleteGame() {
    	mDbHelper.deleteGame(mGameId);
    	finish();
    }
    
    private void newScore(long id, String name) {
    	Intent i = new Intent(this, ScoreNew.class);
    	i.putExtra("name", name);
    	mPlayerId = id;
        startActivityForResult(i, ACTIVITY_PLAYER_NEW_SCORE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Bundle extras = intent == null ? null : intent.getExtras();
        switch(requestCode) {
        case ACTIVITY_PLAYER_NEW:
        	if (extras != null) {
        		String name = extras.getString("name");
        		mDbHelper.createPlayer(mGameId, name);
        		fillData();
        	}
            break;
        case ACTIVITY_PLAYER_SCORES:
    		fillData();
            break;
        case ACTIVITY_PLAYER_NEW_SCORE:
        	if (extras != null) {
        		try {
            		int value = Integer.decode(extras.getString("value"));
            		mDbHelper.createScore(mPlayerId, value);
            		fillData();
        		}
        		catch (NumberFormatException e) {
        		}
        	}
            break;
        }        
    }    
}
