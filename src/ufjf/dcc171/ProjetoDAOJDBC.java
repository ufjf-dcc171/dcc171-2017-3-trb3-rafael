package ufjf.dcc171;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAOJDBC implements ProjetoDAO {
    private Connection conexao;
    private PreparedStatement inserirQuery;
    private PreparedStatement alterarQuery;
    private PreparedStatement deletarQuery;
    private PreparedStatement listarQuery;
    private final String tabela = "DCC171.projeto";

    public ProjetoDAOJDBC() throws Exception {
        conexao = ConnectionDAO.connection();
        inserirQuery = conexao.prepareStatement("INSERT INTO "+tabela+"(nome) VALUES(?)");
        deletarQuery = conexao.prepareStatement("DELETE FROM "+tabela+" WHERE nome = ?");
        listarQuery = conexao.prepareStatement("SELECT nome FROM "+tabela+" ORDER BY nome ASC");
    }

    @Override
    public void criar(String nomeProjeto) throws Exception {
        inserirQuery.clearParameters();
        inserirQuery.setString(1, nomeProjeto);
        inserirQuery.executeUpdate();
    }

    @Override
    public void deletar(String nomeProjeto) throws Exception {
        deletarQuery.clearParameters();
        deletarQuery.setString(1, nomeProjeto);
        deletarQuery.executeUpdate();    }

    @Override
    public List<Projeto> listarTodos() throws Exception {
        List<Projeto> projetos = new ArrayList<>();
        listarQuery.clearParameters();
        ResultSet resultado = listarQuery.executeQuery();
        while (resultado.next()) {
            Projeto p = new Projeto(resultado.getString(1));
            projetos.add(p);
        }
        return projetos;
    }
}
