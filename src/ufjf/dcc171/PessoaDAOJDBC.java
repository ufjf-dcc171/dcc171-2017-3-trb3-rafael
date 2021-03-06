package ufjf.dcc171;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAOJDBC implements PessoaDAO {
    private Connection conexao;
    private PreparedStatement inserirQuery;
    private PreparedStatement buscarQuery;
    private PreparedStatement alterarQuery;
    private PreparedStatement deletarQuery;
    private PreparedStatement listarQuery;
    private PreparedStatement listarPessoaProjetoQuery;
    private PreparedStatement listarPessoaTarefaProjetoQuery;
    private final String tabela = "DCC171.pessoa";
    private final String tabelaPTP = "DCC171.projeto_tarefa_pessoa";
    private PessoaTarefaProjetoDAO pessoaTarefaProjetoDAO;

    public PessoaDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirQuery = conexao.prepareStatement("INSERT INTO " + tabela + " VALUES(?,?)");
        buscarQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " WHERE nome = ?");
        alterarQuery = conexao.prepareStatement("UPDATE " + tabela + " SET nome = ?, email = ? WHERE nome = ? AND email = ?");
        deletarQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome = ?");
        listarQuery = conexao.prepareStatement("SELECT * FROM " + tabela + " ORDER BY nome ASC");
        listarPessoaProjetoQuery = conexao.prepareStatement("SELECT * FROM (SELECT " + tabela + ".* FROM " + tabela + " JOIN (SELECT * FROM " + tabelaPTP + " WHERE nome_projeto = ?) AS resultado1 ON resultado1.nome_pessoa = " + tabela + ".nome) AS resultado2");
        listarPessoaTarefaProjetoQuery = conexao.prepareStatement("SELECT * FROM (SELECT " + tabela + ".* FROM " + tabela + " JOIN (SELECT * FROM " + tabelaPTP + " WHERE nome_projeto = ? AND nome_tarefa = ?) AS resultado1 ON resultado1.nome_pessoa = " + tabela + ".nome) AS resultado2");
        pessoaTarefaProjetoDAO = new PessoaTarefaProjetoDAOJDBC();
    }

    @Override
    public void criar(Pessoa pessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        List<Pessoa> pessoas = buscaPessoa(pessoa);
        if(pessoas.isEmpty() && pessoaTarefaProjetoDAO.inserirTabelaPTP(pessoa.getNome(), nomeTarefa, nomeProjeto)) {
            inserirQuery.clearParameters();
            inserirQuery.setString(1, pessoa.getNome());
            inserirQuery.setString(2, pessoa.getEmail());
            inserirQuery.executeUpdate();
        } else {
            for(Pessoa p : pessoas) {
                if(p.getNome().equals(pessoa.getNome()) && p.getEmail().equals(pessoa.getEmail())) {
                    pessoaTarefaProjetoDAO.inserirTabelaPTP(pessoa.getNome(), nomeTarefa, nomeProjeto);
                }
            }
        }
    }

    @Override
    public Pessoa buscar(Pessoa pessoa) throws Exception {
        buscaPessoa(pessoa);

        return pessoa;
    }

    @Override
    public void alterar(Pessoa oldPessoa, Pessoa newPessoa) throws Exception {
        alterarQuery.clearParameters();
        alterarQuery.setString(1, newPessoa.getNome());
        alterarQuery.setString(2, newPessoa.getEmail());
        alterarQuery.setString(3, oldPessoa.getNome());
        alterarQuery.setString(4, oldPessoa.getEmail());
        alterarQuery.executeUpdate();
        
        alterarPessoaTarefaProjeto(oldPessoa.getNome(), newPessoa.getNome());
    }

    @Override
    public void deletar(String nomeProjeto, String nomeTarefa, String nomePessoa) throws Exception {
        deletarPessoa(nomePessoa, nomeTarefa, nomeProjeto);
    }

    @Override
    public void deletarTarefa(String nomeProjeto, String nomeTarefa) throws Exception {
        List<Pessoa> pessoas = listaPessoas(nomeProjeto, nomeTarefa);
        for(Pessoa p : pessoas) {
            deletarPessoa(p.getNome(), nomeTarefa, nomeProjeto);
        }
    }

    @Override
    public void deletarProjeto(String nomeProjeto) throws Exception {
        List<Pessoa> pessoas = listaPessoasProjeto(nomeProjeto);
        for(Pessoa p : pessoas) {
            pessoaTarefaProjetoDAO.deletarProjeto(nomeProjeto, p);
            
            if(pessoaTarefaProjetoDAO.buscaPessoaTabelaProjeto(p.getNome()).equals("")) {
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

    public void alterarPessoaTarefaProjeto(String oldPessoaNome, String newPessoaNome) throws Exception {
        pessoaTarefaProjetoDAO.alterarPessoaTarefaProjeto(oldPessoaNome, newPessoaNome);
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
    
    private void deletarPessoa(String nomePessoa, String nomeTarefa, String nomeProjeto) throws Exception {
        pessoaTarefaProjetoDAO.deletarPessoa(nomePessoa, nomeTarefa, nomeProjeto);
        
        if(pessoaTarefaProjetoDAO.buscaPessoaTabelaProjeto(nomePessoa).equals("")) {
            deletarQuery.clearParameters();
            deletarQuery.setString(1, nomePessoa);
            deletarQuery.executeUpdate();
        }
    }
    
    private List<Pessoa> buscaPessoa(Pessoa pessoa) throws Exception {
        List<Pessoa> pessoas = new ArrayList<>();
        buscarQuery.clearParameters();
        buscarQuery.setString(1, pessoa.getNome());
        ResultSet resultado = buscarQuery.executeQuery();
        while (resultado.next()) {
            Pessoa p = new Pessoa(resultado.getString(1), resultado.getString(2));
            pessoas.add(p);
        }
        return pessoas;
    }
}
