package net.pillageandplunder.chickenfooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
import android.widget.EditText;

public class PlayerNew extends Activity {
	private EditText mNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_new);
		setTitle("New player");

		mNameText = (EditText) findViewById(R.id.new_player_name);
		mNameText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode != KeyEvent.KEYCODE_ENTER)
					return false;

				Intent mIntent = new Intent();
				mIntent.putExtra("name", mNameText.getText().toString());
				setResult(RESULT_OK, mIntent);
				finish();
				return true;
			}
		});
	}

}
