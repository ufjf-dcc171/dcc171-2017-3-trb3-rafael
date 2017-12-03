package ufjf.dcc171;

import java.util.List;

public class Projeto {
    private List<Tarefa> tarefas;
    private String nome;
    
    public Projeto(String nome) {
        this.nome = nome;
        this.tarefas = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
