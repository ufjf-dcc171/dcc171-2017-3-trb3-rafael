package ufjf.dcc171;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAOJDBC implements PessoaDAO {

    private Connection conexao;
    private PreparedStatement inserirQuery;
    private PreparedStatement inserirProjetoTarefaPessoaQuery;
    private PreparedStatement buscarQuery;
    private PreparedStatement buscarPessoaTarefaProjetoQuery;
    private PreparedStatement alterarQuery;
    private PreparedStatement deletarQuery;
    private PreparedStatement deletarPessoaProjetoQuery;
    private PreparedStatement deletarPessoaTarefaProjetoQuery;
    private PreparedStatement listarQuery;
    private PreparedStatement listarPessoaProjetoQuery;
    private PreparedStatement listarPessoaTarefaProjetoQuery;
    private final String tabela = "DCC171.pessoa";
    private final String tabelaPTP = "DCC171.projeto_tarefa_pessoa";

    public PessoaDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirQuery = conexao.prepareStatement("INSERT INTO " + tabela + " VALUES(?,?)");
        inserirProjetoTarefaPessoaQuery = conexao.prepareStatement("INSERT INTO " + tabelaPTP + " VALUES(?,?,?)");
        buscarQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " WHERE nome = ? AND email = ?");
        buscarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM " + tabelaPTP + " WHERE nome_pessoa = ?");
        deletarQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome = ?");
        deletarPessoaProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabelaPTP+" WHERE nome_projeto = ? AND nome_pessoa = ?");
        deletarPessoaTarefaProjetoQuery = conexao.prepareStatement("DELETE FROM "+tabelaPTP+" WHERE nome_projeto = ? AND nome_tarefa = ? AND nome_pessoa = ?");
        listarQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " ORDER BY nome ASC");
        listarPessoaProjetoQuery = conexao.prepareStatement("SELECT * FROM (SELECT " + tabela + ".* FROM " + tabela + " JOIN (SELECT * FROM " + tabelaPTP + " WHERE nome_projeto = ?) AS resultado1 ON resultado1.nome_pessoa = " + tabela + ".nome) AS resultado2");
        listarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM (SELECT " + tabela + ".* FROM " + tabela + " JOIN (SELECT * FROM " + tabelaPTP + " WHERE nome_projeto = ? AND nome_tarefa = ?) AS resultado1 ON resultado1.nome_pessoa = " + tabela + ".nome) AS resultado2");

    }

    @Override
    public void criar(Pessoa pessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        if(buscaPessoa(pessoa) == null) {
            inserirQuery.clearParameters();
            inserirQuery.setString(1, pessoa.getNome());
            inserirQuery.setString(2, pessoa.getEmail());
            inserirQuery.executeUpdate();
        }
        
        inserirTabelaPTP(pessoa.getNome(), nomeTarefa, nomeProjeto);
    }

    @Override
    public Pessoa buscar(Pessoa pessoa) throws Exception {
        buscaPessoa(pessoa);

        return pessoa;
    }

    @Override
    public void alterar(Pessoa pessoa) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletar(String nomeProjeto, String nomeTarefa, String nomePessoa) throws Exception {
        deletaPessoa(nomePessoa, nomeTarefa, nomeProjeto);
    }

    @Override
    public void deletarTarefa(String nomeProjeto, String nomeTarefa) throws Exception {
        List<Pessoa> pessoas = listaPessoas(nomeProjeto, nomeTarefa);
        for(Pessoa p : pessoas) {
            deletaPessoa(p.getNome(), nomeTarefa, nomeProjeto);
        }
    }

    @Override
    public void deletarProjeto(String nomeProjeto) throws Exception {
        List<Pessoa> pessoas = listaPessoasProjeto(nomeProjeto);
        for(Pessoa p : pessoas) {
            deletarPessoaProjetoQuery.clearParameters();
            deletarPessoaProjetoQuery.setString(1, nomeProjeto);
            deletarPessoaProjetoQuery.setString(2, p.getNome());
            deletarPessoaProjetoQuery.executeUpdate();
            
            if(buscaPessoaTabelaProjeto(p.getNome()) == "") {
                deletarQuery.clearParameters();
                deletarQuery.setString(1, p.getNome());
                deletarQuery.executeUpdate();
            }
        }
    }

    @Override
    public List<Pessoa> listarTodos() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Pessoa> listarPessoaTarefaProjeto(String nomeProjeto, String nomeTarefa) throws Exception {
        return listaPessoas(nomeProjeto, nomeTarefa);
    }

    private void inserirTabelaPTP(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        inserirProjetoTarefaPessoaQuery.clearParameters();
        inserirProjetoTarefaPessoaQuery.setString(1, nomeProjeto);
        inserirProjetoTarefaPessoaQuery.setString(2, nomeTarefa);
        inserirProjetoTarefaPessoaQuery.setString(3, nomePessoa);
        inserirProjetoTarefaPessoaQuery.executeUpdate();
    }
    
    private List<Pessoa> listaPessoas(String nomeProjeto, String nomeTarefa) throws Exception {
        List<Pessoa> pessoas = new ArrayList<>();
        listarPessoaTarefaProjetoQuery.clearParameters();
        listarPessoaTarefaProjetoQuery.setString(1, nomeProjeto);
        listarPessoaTarefaProjetoQuery.setString(2, nomeTarefa);
        ResultSet resultado = listarPessoaTarefaProjetoQuery.executeQuery();
        while (resultado.next()) {
            Pessoa p = new Pessoa(resultado.getString(1), resultado.getString(2));
            pessoas.add(p);
        }
        return pessoas;
    }
    
    private List<Pessoa> listaPessoasProjeto(String nomeProjeto) throws Exception {
        List<Pessoa> pessoas = new ArrayList<>();
        listarPessoaProjetoQuery.clearParameters();
        listarPessoaProjetoQuery.setString(1, nomeProjeto);
        ResultSet resultado = listarPessoaProjetoQuery.executeQuery();
        while (resultado.next()) {
            Pessoa p = new Pessoa(resultado.getString(1), resultado.getString(2));
            pessoas.add(p);
        }
        return pessoas;
    }
    
    private void deletaPessoa(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        deletarPessoaTarefaProjetoQuery.clearParameters();
        deletarPessoaTarefaProjetoQuery.setString(1, nomeProjeto);
        deletarPessoaTarefaProjetoQuery.setString(2, nomeTarefa);
        deletarPessoaTarefaProjetoQuery.setString(3, nomePessoa);
        deletarPessoaTarefaProjetoQuery.executeUpdate();
        
        if(buscaPessoaTabelaProjeto(nomePessoa) == "") {
            deletarQuery.clearParameters();
            deletarQuery.setString(1, nomePessoa);
            deletarQuery.executeUpdate();
        }
    }
    
    private Pessoa buscaPessoa(Pessoa pessoa) throws Exception {
        Pessoa p = null;
        buscarQuery.clearParameters();
        buscarQuery.setString(1, pessoa.getNome());
        buscarQuery.setString(2, pessoa.getEmail());
        ResultSet resultado = buscarQuery.executeQuery();
        while (resultado.next()) {
            p = new Pessoa(resultado.getString(1), resultado.getString(2));
        }
        return p;
    }
    
    private String buscaPessoaTabelaProjeto(String nomePessoa) throws Exception {
        String nome = "";
        buscarPessoaTarefaProjetoQuery.clearParameters();
        buscarPessoaTarefaProjetoQuery.setString(1, nomePessoa);
        ResultSet resultado = buscarPessoaTarefaProjetoQuery.executeQuery();
        while (resultado.next()) {
            nome = resultado.getString(1);
        }
        return nome;
    }
}
