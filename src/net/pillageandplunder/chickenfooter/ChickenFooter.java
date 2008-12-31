package net.pillageandplunder.chickenfooter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;

public class ChickenFooter extends ListActivity {
	private ChickenDatabase mDbHelper;
	private Cursor mGamesCursor;
	private Button newGame;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        mDbHelper = new ChickenDatabase(this);
        mDbHelper.open();
        fillData();
        
        newGame = (Button)findViewById(R.id.new_game);
        newGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	long id = mDbHelper.createGame();
            	viewPlayers(id);
            	fillData();
            }
        });
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
        Cursor c = mGamesCursor;
        c.moveToPosition(position);
        viewPlayers(id);
    }
    
    private void viewPlayers(long id) {
        Intent i = new Intent(this, Players.class);
        i.putExtra("gameId", id);
        startActivity(i);
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        mGamesCursor = mDbHelper.fetchAllGames();
        startManagingCursor(mGamesCursor);

        String[] from = new String[] { "created_at" };
        int[] to = new int[] { R.id.game1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.games_row, mGamesCursor, from, to);
        setListAdapter(notes);
    }
}