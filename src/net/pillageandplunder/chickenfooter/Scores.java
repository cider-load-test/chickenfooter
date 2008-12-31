package net.pillageandplunder.chickenfooter;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Scores extends ListActivity {
	private static final int ACTIVITY_SCORE_NEW=0;
	
	private Long mPlayerId;
	private ChickenDatabase mDbHelper;
	private Cursor mScoresCursor;
	private Button newScore;
	private Button done;
	private TextView mNameText;
	private int scoresAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        
        scoresAdded = 0;
        mNameText = (TextView)findViewById(R.id.scores_player_name);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPlayerId = extras.getLong("playerId");
            mNameText.setText(extras.getString("name"));
        }
        
        mDbHelper = new ChickenDatabase(this);
        mDbHelper.open();
        fillData();
        
        newScore = (Button)findViewById(R.id.new_score);
        newScore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	newScore();
            }
        });
        
        done = (Button)findViewById(R.id.done_scoring);
        done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
				Intent mIntent = new Intent();
				mIntent.putExtra("playerId", mPlayerId);
				mIntent.putExtra("added", scoresAdded);
				setResult(RESULT_OK, mIntent);
				finish();            	
            }
        });
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
        startActivityForResult(i, ACTIVITY_SCORE_NEW);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
        case ACTIVITY_SCORE_NEW:
            int value = Integer.decode(extras.getString("value"));
            mDbHelper.createScore(mPlayerId, value);
            scoresAdded += value;
            fillData();
            break;
        }
    }    
}
