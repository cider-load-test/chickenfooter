package net.pillageandplunder.chickenfooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class ScoreNew extends Activity {
	private EditText mValueText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_new);
		
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	setTitle("Add score for " + extras.getString("name"));
        }

		mValueText = (EditText) findViewById(R.id.new_score_value);
		mValueText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode != KeyEvent.KEYCODE_ENTER)
					return false;

				Intent mIntent = new Intent();
				mIntent.putExtra("value", mValueText.getText().toString());
				setResult(RESULT_OK, mIntent);
				finish();
				return true;
			}
		});
	}

}
