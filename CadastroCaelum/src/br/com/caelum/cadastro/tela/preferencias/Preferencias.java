package br.com.caelum.cadastro.tela.preferencias;

import br.com.caelum.cadastro.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class Preferencias extends Activity {
	public static final String PREFERENCIAS = "Preferencias";
	public static final String ORDENAR_ALFABETICAMENTE = "ordenarAlfabeticamente";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferencias);
		
		final SharedPreferences sp = getSharedPreferences(PREFERENCIAS, MODE_PRIVATE);
		boolean externo = sp.getBoolean(ORDENAR_ALFABETICAMENTE, false);
		
		final CheckBox checkBox = (CheckBox) findViewById(R.preferencias.ordemAlfabetica);
		checkBox.setChecked(externo);
		
		Button salvar = (Button) findViewById(R.preferencias.gravarPreferencias);
		salvar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor ed = sp.edit();
				ed.putBoolean(ORDENAR_ALFABETICAMENTE, checkBox.isChecked());
				ed.commit();
				finish();
			}
		});
		
	}
}
