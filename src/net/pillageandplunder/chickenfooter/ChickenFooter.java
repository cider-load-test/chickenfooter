package net.pillageandplunder.chickenfooter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;

public class ChickenFooter extends ListActivity {
    private static final int INSERT_ID = Menu.FIRST;
    
	private ChickenDatabase mDbHelper;
	private Cursor mGamesCursor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
                
        mDbHelper = new ChickenDatabase(this);
        mDbHelper.open();
        fillData();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
        Cursor c = mGamesCursor;
        c.moveToPosition(position);
        viewPlayers(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi = menu.add(0, INSERT_ID, 0, R.string.new_game);
        mi.setIcon(android.R.drawable.ic_menu_add);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            newGame();
            return true;
        }
        
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void newGame() {
    	mDbHelper.createGame();
    	fillData();
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