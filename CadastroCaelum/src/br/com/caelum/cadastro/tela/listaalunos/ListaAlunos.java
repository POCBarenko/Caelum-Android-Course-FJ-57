package br.com.caelum.cadastro.tela.listaalunos;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.dao.AlunoDao;
import br.com.caelum.cadastro.helper.Sincronismo;
import br.com.caelum.cadastro.modelo.Aluno;
import br.com.caelum.cadastro.tela.formulario.Formulario;
import br.com.caelum.cadastro.tela.galeria.Galeria;
import br.com.caelum.cadastro.tela.map.Mapa;
import br.com.caelum.cadastro.tela.preferencias.Preferencias;

public class ListaAlunos extends Activity {
	private ListView listaAlunos;
	private Aluno alunoSelecionado;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista); // renderiza a tela
		listaAlunos = (ListView) findViewById(R.id.listaAlunos);

		int notificationId = getIntent().getIntExtra("notificationId", -1);
		if(notificationId != -1) {
			NotificationManager notMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notMan.cancel(notificationId);
		}
		
	}

	private void loadListaAlunos(ArrayAdapter<Aluno> adapter) {

		getListaAlunos().setAdapter(adapter);
		getListaAlunos().setOnItemClickListener(new ListaAlunosEvents(this));
		getListaAlunos().setOnItemLongClickListener(new ListaAlunosEvents(this));
	}

	// Menu principal da aplicacao
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem novo = menu.add(0, 0, Menu.NONE, R.string.menu_new);
		novo.setIcon(android.R.drawable.ic_menu_add);

		MenuItem sincronizar = menu.add(0, 1, Menu.NONE, R.string.menu_sycronize);
		sincronizar.setIcon(android.R.drawable.ic_menu_rotate);

		MenuItem galeria = menu.add(0, 2, Menu.NONE, R.string.menu_galery);
		galeria.setIcon(android.R.drawable.ic_menu_camera);

		MenuItem mapa = menu.add(0, 3, Menu.NONE, R.string.menu_map);
		mapa.setIcon(android.R.drawable.ic_menu_mapmode);

		MenuItem prefs = menu.add(0, 4, Menu.NONE, R.string.menu_preferences);
		prefs.setIcon(android.R.drawable.ic_menu_preferences);
		
		return super.onCreateOptionsMenu(menu);
	}

	// implementacao do menu principal da aplicacao
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent novo = new Intent(this, Formulario.class);
			startActivity(novo);
			break;
		case 1:
			new Sincronismo(this).sincronizar();
			break;
		case 2:
			Intent galeria = new Intent(this, Galeria.class);
			startActivity(galeria);
			break;
		case 3:
			Intent mapa = new Intent(this, Mapa.class);
			startActivity(mapa);
			break;
		case 4:
			Intent prefs = new Intent(this, Preferencias.class);
			startActivity(prefs);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// submenu dos itens da aplicacao
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.addSubMenu(Menu.NONE, 0, Menu.NONE, R.string.submenu_call);
		menu.addSubMenu(Menu.NONE, 1, Menu.NONE, R.string.submenu_sms);
		menu.addSubMenu(Menu.NONE, 2, Menu.NONE, R.string.submenu_find_map);
		menu.addSubMenu(Menu.NONE, 3, Menu.NONE, R.string.submenu_website);
		menu.addSubMenu(Menu.NONE, 4, Menu.NONE, R.string.submenu_remove);
		menu.addSubMenu(Menu.NONE, 5, Menu.NONE, R.string.submenu_send_email);
		menu.addSubMenu(Menu.NONE, 6, Menu.NONE, R.string.submenu_share);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	// implementacao dos itens do submenu da aplicacao
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent ligar = new Intent(Intent.ACTION_CALL);
			ligar.setData(Uri.parse("tel:" + getAlunoSelecionado().getTelefone()));
			startActivity(ligar);
			break;
		case 1:
			Intent sms = new Intent(Intent.ACTION_VIEW);
			sms.setData(Uri.parse("sms:" + getAlunoSelecionado().getTelefone()));
			sms.putExtra("sms_body", "Mensagem sms padrão");
			startActivity(sms);
			break;
		case 2:
			Intent geo = new Intent(Intent.ACTION_VIEW);
			geo.setData(Uri.parse("geo:0,0?z=17&q=" + getAlunoSelecionado().getEndereco()));
			startActivity(geo);
			break;
		case 3:
			Intent site = new Intent(Intent.ACTION_VIEW);
			site.setData(Uri.parse("http://" + getAlunoSelecionado().getSite()));
			startActivity(site);
			break;
		case 4:
			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Excluir").setMessage("Deseja excluir o aluno?")
					.setPositiveButton("Claro!", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							AlunoDao dao = new AlunoDao(ListaAlunos.this);
							try {
								dao.delete(getAlunoSelecionado());
							} finally {
								dao.close();
							}
							onResume();
						}
					}).setNegativeButton("Não", null).show();
			break;
		case 5:
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("message/rfc822");
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { "caelum@caelum.com.br" });
			email.putExtra(Intent.EXTRA_SUBJECT, new String[] { "Este é o titulo do email" });
			email.putExtra(Intent.EXTRA_TEXT, new String[] { "Este é o conteudo do email" });
			startActivity(Intent.createChooser(email, "Selecione a sua aplicação de email!"));
			break;
		case 6:
			Intent social = new Intent(Intent.ACTION_SEND);
			social.setType("text/plain");
			social.putExtra(Intent.EXTRA_EMAIL, new String[] { "caelum@caelum.com.br" });
			social.putExtra(Intent.EXTRA_SUBJECT, new String[] { "Este é o titulo" });
			social.putExtra(Intent.EXTRA_TEXT, new String[] { "Este é o conteudol" });
			startActivity(Intent.createChooser(social, "Selecione a sua aplicação de rede social!"));
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();

		AlunoDao dao = new AlunoDao(ListaAlunos.this);
		boolean ordenar = getSharedPreferences(Preferencias.PREFERENCIAS, MODE_PRIVATE).getBoolean(Preferencias.ORDENAR_ALFABETICAMENTE, false);
		final List<Aluno> alunos = dao.getAll(ordenar);
		dao.close();

		ArrayAdapter<Aluno> adapter = new ListaAlunosAdapter(this, android.R.layout.simple_list_item_1, alunos);

		loadListaAlunos(adapter);
	}

	public ListView getListaAlunos() {
		return listaAlunos;
	}

	public Aluno getAlunoSelecionado() {
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Aluno alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}

}