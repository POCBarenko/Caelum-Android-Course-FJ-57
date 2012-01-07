package br.com.caelum;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestaComponentes extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final TextView viewer = (TextView) findViewById(R.form.viewer);
		final Button botao = (Button) findViewById(R.form.button);
		final EditText name = (EditText) findViewById(R.form.name);

		botao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewer.setText(getApplicationContext().getString(R.string.form_viewer_msg, name.getText()));
			}
		});
	}
}