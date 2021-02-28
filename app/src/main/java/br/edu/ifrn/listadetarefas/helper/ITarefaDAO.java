package br.edu.ifrn.listadetarefas.helper;

import java.util.List;

import br.edu.ifrn.listadetarefas.model.Tarefa;

/**
 * Interface onde serão criados os
 * método do CRUD. A implementação
 * desses métodos criados aqui é feita
 * na classe TarefaDAO.
 */

public interface ITarefaDAO {

    //Método salvar
    public boolean salvar(Tarefa tarefa);

    //Método atualizar
    public boolean atualizar(Tarefa tarefa);

    //Método deletar
    public boolean deletar(Tarefa tarefa);

    //Método para listar as tarefas
    public List<Tarefa> listar();
}
