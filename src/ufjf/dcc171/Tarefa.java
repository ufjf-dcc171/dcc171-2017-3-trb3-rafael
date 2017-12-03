package ufjf.dcc171;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Tarefa {
    private String nome;
    private Integer duracao;
    private Calendar dataInicio;
    private Calendar dataFim;
    private Integer percentual;
    private boolean concluida;
    private List<Pessoa> pessoal;
    
    public Tarefa(String nome, Integer duracao, Calendar dataInicio) {
        this.nome = nome;
        this.duracao = duracao;
        this.dataInicio = dataInicio;
        setDataFim(dataInicio);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Calendar getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Calendar dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Calendar getDataFim() {
        return dataFim;
    }

    private void setDataFim(Calendar dataInicio) {
        dataInicio.add(Calendar.DATE, duracao);
        this.dataFim = dataInicio;
    }

    public Integer getPercentual() {
        return percentual;
    }

    public void setPercentual(Integer percentual) {
        this.percentual = percentual;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public List<Pessoa> getPessoal() {
        return pessoal;
    }

    public void setPessoal(List<Pessoa> pessoal) {
        this.pessoal = pessoal;
    }
}
