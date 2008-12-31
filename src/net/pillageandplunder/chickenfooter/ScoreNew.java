package net.pillageandplunder.chickenfooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ScoreNew extends Activity {
	private EditText mValueText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_new);

		mValueText = (EditText) findViewById(R.id.new_score_value);
		Button confirmButton = (Button) findViewById(R.id.new_score_confirm);

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent mIntent = new Intent();
				mIntent.putExtra("value", mValueText.getText().toString());
				setResult(RESULT_OK, mIntent);
				finish();
			}
		});
	}

}
