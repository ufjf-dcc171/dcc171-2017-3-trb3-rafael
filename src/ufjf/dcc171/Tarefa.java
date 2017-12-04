package ufjf.dcc171;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class Tarefa {
    private String nome;
    private Integer duracao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer percentual;
    
    public Tarefa(String nome, Integer duracao, LocalDate data) {
        this.nome = nome;
        this.duracao = duracao;
        this.dataInicio = data;
        this.dataFim = data.plusDays(this.duracao);
        this.percentual = 0;
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

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public Integer getPercentual() {
        return percentual;
    }

    public void setPercentual(Integer percentual) {
        this.percentual = percentual;
    }

    @Override
    public String toString() {
        return nome;
    }
}
