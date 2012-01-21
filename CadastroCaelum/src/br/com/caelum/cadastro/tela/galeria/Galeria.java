package br.com.caelum.cadastro.tela.galeria;

import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.R.galeria;
import br.com.caelum.cadastro.R.layout;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.tela.formulario.Formulario;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;

/**
 * Dica: use F6 para transformar o mouse em scroll
 */

public class Galeria extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.galeria);

	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPictures();
	}

	private void loadPictures() {
		AlunoDao dao = new AlunoDao(this);
		List<Aluno> alunos = dao.getAll();
		dao.close();

		Gallery galeria = (Gallery) findViewById(R.galeria.galeria);
		GaleriaAlunosAdapter adapter = new GaleriaAlunosAdapter(alunos, this);
		galeria.setAdapter(adapter);

		galeria.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
				Aluno aluno = (Aluno) adapter.getItemAtPosition(position);
				Intent intent = new Intent(Galeria.this, Formulario.class);
				intent.putExtra(Formulario.ALUNO_SELECIONADO, aluno);
				startActivity(intent);
			}

		});
	}

}
