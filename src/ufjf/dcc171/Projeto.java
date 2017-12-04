package ufjf.dcc171;

import java.util.List;

public class Projeto {
    private String nome;
    
    public Projeto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
