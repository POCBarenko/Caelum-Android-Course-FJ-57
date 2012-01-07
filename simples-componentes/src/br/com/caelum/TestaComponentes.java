package br.com.caelum;

import br.com.caelum.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestaComponentes extends Activity {
	final TextView texto = (TextView) findViewById(R.form.textView);
	final Button botao = (Button) findViewById(R.form.button);
	final EditText campo = (EditText) findViewById(R.form.text);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		botao.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				texto.setText(String.format("Bem vindo %s.", campo.getText().toString()));
			}
		});
	}
}