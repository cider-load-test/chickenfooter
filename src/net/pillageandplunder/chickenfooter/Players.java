package net.pillageandplunder.chickenfooter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Players extends ListActivity {
	private static final int ACTIVITY_PLAYER_NEW=0;
	private static final int ACTIVITY_PLAYER_SCORES=1;
	
	private Long mGameId;
	private ChickenDatabase mDbHelper;
	private Cursor mPlayersCursor;
	private Button newPlayer;

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
        
        newPlayer = (Button)findViewById(R.id.new_player);
        newPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	newPlayer();
            }
        });
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
        Cursor c = mPlayersCursor;
        c.moveToPosition(position);
        editPlayer(id, c.getString(1));
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
    
    private void editPlayer(long id, String name) {
        Intent i = new Intent(this, Scores.class);
        i.putExtra("playerId", id);
        i.putExtra("name", name);
        startActivityForResult(i, ACTIVITY_PLAYER_SCORES);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
        case ACTIVITY_PLAYER_NEW:
            String name = extras.getString("name");
            mDbHelper.createPlayer(mGameId, name);
            fillData();
            break;
        case ACTIVITY_PLAYER_SCORES:
        	int scoresAdded = extras.getInt("added");
        	long playerId = extras.getLong("playerId");
        	mDbHelper.addToPlayerScore(playerId, scoresAdded);
            fillData();
            break;
        }
    }    
}
