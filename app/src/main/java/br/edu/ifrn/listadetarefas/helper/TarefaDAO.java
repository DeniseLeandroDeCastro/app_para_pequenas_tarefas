package br.edu.ifrn.listadetarefas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifrn.listadetarefas.model.Tarefa;

public class TarefaDAO implements ITarefaDAO{

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public TarefaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());
        try {
            escreve.insert(DbHelper.TABELA_TAREFAS, null, cv);
            Log.i("INFO", "Tarefa salva com sucesso!");
        } catch(Exception e) {
            Log.e("INFO", "Erro ao salvar a tarefa " + e.getMessage());
           return false;
        }
        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());
        try {
            String[] args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS, cv, "id=?", args);
            Log.i("INFO", "Tarefa atualizada com sucesso!");
        } catch(Exception e) {
            Log.e("INFO", "Erro ao atualizar a tarefa " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {
        try {
            String[] args = {tarefa.getId().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS, "id=?", args);
            Log.i("INFO", "Tarefa excluída!");
        } catch(Exception e) {
            Log.i("INFO", "Erro ao remover a tarefa! " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Método que irá recuperar as tarefas
     * e retornar as tarefas recuperadas
     * como uma lista.
     * @return
     */
    @Override
    public List<Tarefa> listar() {
        List<Tarefa> tarefas = new ArrayList<>();
        //Seleciona todos os dados da tabela
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " ;";
        Cursor c = le.rawQuery(sql, null);
        //While para percorrer o cursor, movendo sempre para o próximo item
        while(c.moveToNext()) {
            Tarefa tarefa = new Tarefa();
            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));
            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);
            /**
             * tarefas é o nome do list de tarefas criado
             * cada vez que uma tarefa for salva, ela será
             * adicionada ao list.
             */
            tarefas.add(tarefa);
        }
        //Retorna a lista com os itens salvos
        return tarefas;
    }
}
