package br.edu.ifrn.listadetarefas.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrn.listadetarefas.R;
import br.edu.ifrn.listadetarefas.adapter.TarefaAdapter;

import br.edu.ifrn.listadetarefas.helper.RecyclerItemClickListener;
import br.edu.ifrn.listadetarefas.helper.TarefaDAO;
import br.edu.ifrn.listadetarefas.model.Tarefa;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurar recyclerView
        recyclerView = findViewById(R.id.recyclerView);

        //Adicionar evento de clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Recuperar a tarefa para edição
                                Tarefa tarefaSelecionada = listaTarefas.get(position);

                                //Envia a tarefa para a tela adicionar tarefa
                                Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                                intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //Recupera a tarefa para poder deletar
                                tarefaSelecionada = listaTarefas.get(position);
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                //Configura título e mensagem
                                dialog.setTitle("Confirmar exclusão:");
                                dialog.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + " ?");
                                //Configura o botão para confirmar a exclusão da tarefa
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                        //Verificar se é possível deletar a tarefa
                                        if(tarefaDAO.deletar(tarefaSelecionada)) {
                                            carregarListaTarefas();
                                            Toast.makeText(getApplicationContext(),
                                                    "Tarefa excluída!",
                                                    Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Erro ao excluir a tarefa!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                //Configura o botão para cancelar a exclusão
                                dialog.setNegativeButton("Não", null);
                                //Exibir dialog
                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        //Clique do FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }
    //Configurar o recyclerView para carregar a lista de tarefas (pega o recyclerview e monta as tarefas)
    public void carregarListaTarefas() {
        //Listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        /**
         * Exibe uma lista de tarefas no Recyclerview
         */

        //Configurar um adapter
        tarefaAdapter = new TarefaAdapter(listaTarefas);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);
    }
    //Método do ciclo de vida de uma activity
    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}