package net.pillageandplunder.chickenfooter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Scores extends ListActivity {
	private static final int ACTIVITY_SCORE_NEW=0;
    private static final int INSERT_ID = Menu.FIRST;
	
	private Long mPlayerId;
	private String mPlayerName;
	private ChickenDatabase mDbHelper;
	private Cursor mScoresCursor;
	private TextView mNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        
        mNameText = (TextView)findViewById(R.id.scores_player_name);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPlayerId = extras.getLong("playerId");
            mPlayerName = extras.getString("name");
            mNameText.setText(mPlayerName);
        }
        
        mDbHelper = new ChickenDatabase(this);
        mDbHelper.open();
        fillData();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem mi = menu.add(0, INSERT_ID, 0, R.string.add_score);
        mi.setIcon(android.R.drawable.ic_menu_add);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            newScore();
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void fillData() {
        // Get all of the notes from the database and create the item list
        mScoresCursor = mDbHelper.fetchAllScores(mPlayerId);
        startManagingCursor(mScoresCursor);

        String[] from = new String[] { "value" };
        int[] to = new int[] { R.id.score_value1 };
        
        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter scores =
            new SimpleCursorAdapter(this, R.layout.scores_row, mScoresCursor, from, to);
        setListAdapter(scores);
    }
    
    private void newScore() {
        Intent i = new Intent(this, ScoreNew.class);
        i.putExtra("name", mPlayerName);
        startActivityForResult(i, ACTIVITY_SCORE_NEW);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_OK)
        	return;
        
        Bundle extras = intent.getExtras();
        switch(requestCode) {
        case ACTIVITY_SCORE_NEW:
        	if (extras != null) {
        		int value = Integer.decode(extras.getString("value"));
        		mDbHelper.createScore(mPlayerId, value);
        		fillData();
        	}
            break;
        }
    }    
}
