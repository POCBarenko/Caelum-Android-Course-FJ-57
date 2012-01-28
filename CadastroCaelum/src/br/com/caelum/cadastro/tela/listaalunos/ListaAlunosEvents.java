package br.com.caelum.cadastro.tela.listaalunos;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.tela.formulario.Formulario;

public class ListaAlunosEvents implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

	private final ListaAlunos listaAlunos;

	public ListaAlunosEvents(ListaAlunos listaAlunos) {
		this.listaAlunos = listaAlunos;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long idPosicao) {
		Intent edicao = new Intent(listaAlunos, Formulario.class);
		edicao.putExtra("alunoSelecionado", (Aluno)listaAlunos.getListaAlunos().getItemAtPosition(posicao));
		listaAlunos.startActivity(edicao);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicao, long idPosicao) {
		listaAlunos.setAlunoSelecionado((Aluno) adapterView.getItemAtPosition(posicao));
		listaAlunos.registerForContextMenu(listaAlunos.getListaAlunos());
		return false; // true nao faz callback da classe (executa apenas o longClick).
						// false faz callback (executa também o evento de click).
	}

}
