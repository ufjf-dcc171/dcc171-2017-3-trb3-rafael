package ufjf.dcc171;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAOJDBC implements TarefaDAO {
    private Connection conexao;
    private PreparedStatement inserirQuery;
    private PreparedStatement buscarQuery;
    private PreparedStatement alterarQuery;
    private PreparedStatement alterarTarefaPercentualQuery;
    private PreparedStatement alterarProjetoQuery;
    private PreparedStatement deletarQuery;
    private PreparedStatement deletarProjetoQuery;
    private PreparedStatement listarQuery;
    private PreparedStatement listarTarefaConcluidaQuery;
    private PreparedStatement listarTarefaFazerQuery;
    private PreparedStatement listarTarefaProjetoQuery;
    private final String tabela = "DCC171.tarefa";
    private PessoaTarefaProjetoDAO pessoaTarefaProjetoDAO;

    public TarefaDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirQuery = conexao.prepareStatement("INSERT INTO "+tabela+" VALUES(?,?,?,?,?,?)");
        buscarQuery = conexao.prepareStatement("SELECT * FROM "+tabela+" WHERE nome = ? AND nome_projeto = ?");
        alterarQuery = conexao.prepareStatement("UPDATE "+tabela+" SET nome = ?, duracao = ?, data_inicio = ?, data_fim = ?, percentual = ?, nome_projeto = ? WHERE nome = ? AND nome_projeto = ?");
        alterarTarefaPercentualQuery = conexao.prepareStatement("UPDATE "+tabela+" SET percentual = ? WHERE nome = ? AND nome_projeto = ?");
        alterarProjetoQuery = conexao.prepareStatement("UPDATE "+tabela+" SET nome_projeto = ? WHERE nome_projeto = ?");
        deletarQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome = ? AND nome_projeto = ?");
        deletarProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome_projeto = ?");
        listarQuery = conexao.prepareStatement("SELECT * FROM "+tabela+" ORDER BY nome ASC");
        listarTarefaConcluidaQuery = conexao.prepareStatement("SELECT * FROM "+tabela+" WHERE percentual = 100 ORDER BY nome ASC");
        listarTarefaFazerQuery = conexao.prepareStatement("SELECT * FROM "+tabela+" WHERE percentual < 100 ORDER BY nome ASC");
        listarTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM "+tabela+" WHERE nome_projeto = ? AND percentual < 100 ORDER BY nome ASC");
        pessoaTarefaProjetoDAO = new PessoaTarefaProjetoDAOJDBC();
    }

    @Override
    public void criar(Tarefa tarefa, String nomeProjeto) throws Exception {
        if(buscar(tarefa.getNome(), nomeProjeto).equals("")) {
            inserirQuery.clearParameters();
            inserirQuery.setString(1, tarefa.getNome());
            inserirQuery.setInt(2, tarefa.getDuracao());
            inserirQuery.setDate(3, Date.valueOf(tarefa.getDataInicio()));
            inserirQuery.setDate(4, Date.valueOf(tarefa.getDataFim()));
            inserirQuery.setInt(5, tarefa.getPercentual());
            inserirQuery.setString(6, nomeProjeto);
            inserirQuery.executeUpdate();
        }
    }

    @Override
    public String buscar(String nomeTarefa, String nomeProjeto) throws Exception {
        String t = "";
        buscarQuery.clearParameters();
        buscarQuery.setString(1, nomeTarefa);
        buscarQuery.setString(2, nomeProjeto);
        ResultSet resultado = buscarQuery.executeQuery();
        while (resultado.next()) {
            t = resultado.getString(1);
        }
        return t;
    }

    @Override
    public void alterar(Tarefa newTarefa, String oldNomeTarefa, String nomeProjeto) throws Exception {
        alterarQuery.clearParameters();
        alterarQuery.setString(1, newTarefa.getNome());
        alterarQuery.setInt(2, newTarefa.getDuracao());
        alterarQuery.setDate(3, Date.valueOf(newTarefa.getDataInicio()));
        alterarQuery.setDate(4, Date.valueOf(newTarefa.getDataFim()));
        alterarQuery.setInt(5, newTarefa.getPercentual());
        alterarQuery.setString(6, nomeProjeto);
        alterarQuery.setString(7, oldNomeTarefa);
        alterarQuery.setString(8, nomeProjeto);
        alterarQuery.executeUpdate();
        
        pessoaTarefaProjetoDAO.alterarTarefaPessoaTarefaProjeto(oldNomeTarefa, newTarefa.getNome(), nomeProjeto);
    }

    @Override
    public void alterarTarefaPercentual(Tarefa tarefa, String nomeProjeto) throws Exception {
        alterarTarefaPercentualQuery.clearParameters();
        alterarTarefaPercentualQuery.setInt(1, tarefa.getPercentual());
        alterarTarefaPercentualQuery.setString(2, tarefa.getNome());
        alterarTarefaPercentualQuery.setString(3, nomeProjeto);
        alterarTarefaPercentualQuery.executeUpdate();
    }

    @Override
    public void alterarProjeto(String oldNomeProjeto, String newNomeProjeto) throws Exception {
        alterarProjetoQuery.clearParameters();
        alterarProjetoQuery.setString(1, newNomeProjeto);
        alterarProjetoQuery.setString(2, oldNomeProjeto);
        alterarProjetoQuery.executeUpdate();
    }

    @Override
    public void deletar(String nomeTarefa, String nomeProjeto) throws Exception {
        deletarQuery.clearParameters();
        deletarQuery.setString(1, nomeTarefa);
        deletarQuery.setString(2, nomeProjeto);
        deletarQuery.executeUpdate();
    }

    @Override
    public void deletarProjeto(String nomeProjeto) throws Exception {
        deletarProjetoQuery.clearParameters();
        deletarProjetoQuery.setString(1, nomeProjeto);
        deletarProjetoQuery.executeUpdate();
    }

    @Override
    public List<Tarefa> listarTodos() throws Exception {
        List<Tarefa> tarefas = new ArrayList<>();
        listarQuery.clearParameters();
        ResultSet resultado = listarQuery.executeQuery();
        while (resultado.next()) {
            Tarefa t = new Tarefa(resultado.getString(1),resultado.getInt(2),new java.sql.Date(resultado.getDate(3).getTime()).toLocalDate());
            t.setPercentual(resultado.getInt(5));
            tarefas.add(t);
        }
        return tarefas;
    }

    @Override
    public List<Tarefa> buscarTarefaConcluida() throws Exception {
        List<Tarefa> tarefas = new ArrayList<>();
        listarTarefaConcluidaQuery.clearParameters();
        ResultSet resultado = listarTarefaConcluidaQuery.executeQuery();
        while (resultado.next()) {
            Tarefa t = new Tarefa(resultado.getString(1),resultado.getInt(2),new java.sql.Date(resultado.getDate(3).getTime()).toLocalDate());
            t.setPercentual(resultado.getInt(5));
            tarefas.add(t);
        }
        return tarefas;
    }

    @Override
    public List<Tarefa> buscarTarefaFazer() throws Exception {
        List<Tarefa> tarefas = new ArrayList<>();
        listarTarefaFazerQuery.clearParameters();
        ResultSet resultado = listarTarefaFazerQuery.executeQuery();
        while (resultado.next()) {
            Tarefa t = new Tarefa(resultado.getString(1),resultado.getInt(2),new java.sql.Date(resultado.getDate(3).getTime()).toLocalDate());
            t.setPercentual(resultado.getInt(5));
            tarefas.add(t);
        }
        return tarefas;
    }

    @Override
    public List<Tarefa> buscarTarefaProjeto(String nomeProjeto) throws Exception {
        List<Tarefa> tarefas = new ArrayList<>();
        listarTarefaProjetoQuery.clearParameters();
        listarTarefaProjetoQuery.setString(1, nomeProjeto);
        ResultSet resultado = listarTarefaProjetoQuery.executeQuery();
        while (resultado.next()) {
            Tarefa t = new Tarefa(resultado.getString(1),resultado.getInt(2),new java.sql.Date(resultado.getDate(3).getTime()).toLocalDate());
            t.setPercentual(resultado.getInt(5));
            tarefas.add(t);
        }
        return tarefas;
    }
}
