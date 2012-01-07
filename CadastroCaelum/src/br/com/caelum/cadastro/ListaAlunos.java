package br.com.caelum.cadastro;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaAlunos extends Activity {
	private ListView listaAlunos;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista); // renderiza a tela

		List<String> alunos = Arrays.asList("Cláudio", "André", "Rafael", "Benedita");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alunos);

		loadListaAlunos(adapter);

	}

	private void loadListaAlunos(ArrayAdapter<String> adapter) {
		listaAlunos = (ListView) findViewById(R.id.listaAlunos);

		listaAlunos.setAdapter(adapter);

		listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long idPosicao) {
				Toast.makeText(ListaAlunos.this, "Você clicou na posição " + posicao, Toast.LENGTH_SHORT).show();
			}
		});

		listaAlunos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicao, long idPosicao) {
				registerForContextMenu(listaAlunos);
				return false; // true nao faz callback da classe (executa apenas o longClick).
								// false faz callback (executa também o evento de click).
			}
		});
	}

	//Menu principal da aplicacao
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem novo = menu.add(0, 0, 0, "Novo");
		novo.setIcon(android.R.drawable.ic_menu_add);

		MenuItem sincronizar = menu.add(0, 1, 0, "Sincronizar");
		sincronizar.setIcon(android.R.drawable.ic_menu_rotate);

		MenuItem galeria = menu.add(0, 2, 0, "Galeria");
		galeria.setIcon(android.R.drawable.ic_menu_camera);

		MenuItem mapa = menu.add(0, 3, 0, "Mapa");
		mapa.setIcon(android.R.drawable.ic_menu_mapmode);

		return super.onCreateOptionsMenu(menu);
	}

	
	//implementacao do menu principal da aplicacao
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Toast.makeText(this, "Novo", Toast.LENGTH_SHORT).show();
			Intent novo = new Intent(this, Formulario.class);
			startActivity(novo);
			break;
		case 1:
			Toast.makeText(this, "Sincronizar", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(this, "Galeria", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(this, "Mapa", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	//submenu dos itens da aplicacao
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.addSubMenu(0, 0, 0, "Ligar");
		menu.addSubMenu(0, 1, 0, "Enviar SMS");
		menu.addSubMenu(0, 2, 0, "Achar no Mapa");
		menu.addSubMenu(0, 3, 0, "Navegar no Site");
		menu.addSubMenu(0, 4, 0, "Excluir");
		menu.addSubMenu(0, 5, 0, "Enviar email");
		menu.addSubMenu(0, 6, 0, "Compartilhar");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	//implementacao dos itens do submenu da aplicacao
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Toast.makeText(this, "Ligar", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			Toast.makeText(this, "Enviar SMS", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(this, "Achar no Mapa", Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(this, "Navegar no Site", Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(this, "Excluir", Toast.LENGTH_SHORT).show();
			break;
		case 5:
			Toast.makeText(this, "Enviar email", Toast.LENGTH_SHORT).show();
			break;
		case 6:
			Toast.makeText(this, "Compartilhar", Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);
	}
}