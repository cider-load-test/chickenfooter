package net.pillageandplunder.chickenfooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayerNew extends Activity {
	private EditText mNameText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_new);

		mNameText = (EditText) findViewById(R.id.new_player_name);
		Button confirmButton = (Button) findViewById(R.id.new_player_confirm);

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent mIntent = new Intent();
				mIntent.putExtra("name", mNameText.getText().toString());
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}

}
