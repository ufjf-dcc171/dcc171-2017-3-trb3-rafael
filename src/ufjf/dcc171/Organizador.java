package ufjf.dcc171;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Organizador extends JFrame {
    private List<Projeto> dtProjetos;
    private ProjetoDAO projetoDAO;
    private TarefaDAO tarefaDAO;
    private PessoaDAO pessoaDAO;
    
    public Organizador(List<Projeto> projetos) {
        initComponents();
        
        dtProjetos = projetos;
        lstProjetos.setModel(new ProjetoListModel(dtProjetos));
        
        lstProjetos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                clearAllTarefaFields();
                clearAllPessoaFields();
                Projeto selProjeto = lstProjetos.getSelectedValue();
                List<Tarefa> tarefas = null;
                lstPessoas.setModel(new DefaultListModel<>());
                
                try {
                    tarefaDAO = new TarefaDAOJDBC();
                    tarefas = tarefaDAO.buscarTarefaProjeto(selProjeto.getNome());
                } catch (Exception ex) {
                    Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(tarefas != null) {
                    lstTarefas.setModel(new TarefaListModel(tarefas));
                }
            }
        });
        
        lstTarefas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                clearAllPessoaFields();
                Tarefa selTarefa = lstTarefas.getSelectedValue();
                List<Pessoa> pessoas = null;
                
                txtTarefaNome.setText(selTarefa.getNome());
                txtTarefaDuracao.setText(selTarefa.getDuracao().toString());
                txtTarefaDataInicio.setText(FuncoesAux.dateFormatDDMMYYYY(selTarefa.getDataInicio()));
                pgrTarefa.setValue(selTarefa.getPercentual());
                txtTarefaPercentual.setText(selTarefa.getPercentual().toString());
                
                try {
                    pessoaDAO = new PessoaDAOJDBC();
                    pessoas = pessoaDAO.listarPessoaTarefaProjeto(lstProjetos.getSelectedValue().getNome(), selTarefa.getNome());
                } catch (Exception ex) {
                    Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                if(pessoas != null) {
                    lstPessoas.setModel(new PessoaListModel(pessoas));
                }
            }
        });
        
        lstPessoas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Pessoa selPessoa = lstPessoas.getSelectedValue();

                txtPessoaNome.setText(selPessoa.getNome());
                txtPessoaEmail.setText(selPessoa.getEmail());
            }
        });
        
        // ---------------  Botoes de acao  ------------------------------------------------
        
        btnNovoProjeto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeProjeto = JOptionPane.showInputDialog(null, "Digite o nome do novo projeto:", "Novo Projeto", JOptionPane.PLAIN_MESSAGE);
        
                if(nomeProjeto != null) try {
                    projetoDAO = new ProjetoDAOJDBC();
                    projetoDAO.criar(nomeProjeto);
                    dtProjetos = projetoDAO.listarTodos();
                    lstProjetos.setModel(new ProjetoListModel(dtProjetos));
                    lstTarefas.setModel(new DefaultListModel<>());
                } catch (Exception ex) {
                    Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btnExcluirProjeto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lstProjetos.getSelectedValue() != null) {
                    try {
                        pessoaDAO = new PessoaDAOJDBC();
                        pessoaDAO.deletarProjeto(lstProjetos.getSelectedValue().getNome());
                        lstPessoas.setModel(new DefaultListModel<>());
                        tarefaDAO = new TarefaDAOJDBC();
                        tarefaDAO.deletarProjeto(lstProjetos.getSelectedValue().getNome());
                        lstTarefas.setModel(new DefaultListModel<>());
                        projetoDAO = new ProjetoDAOJDBC();
                        projetoDAO.deletar(lstProjetos.getSelectedValue().getNome());
                        clearAllTarefaFields();
                        clearAllPessoaFields();
                        lstProjetos.setModel(new ProjetoListModel(projetoDAO.listarTodos()));
                    } catch (Exception ex) {
                        Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        btnNovaTarefa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtTarefaDataInicio.getText().isEmpty() && !txtTarefaDuracao.getText().isEmpty() && !txtTarefaNome.getText().isEmpty() && lstProjetos.getSelectedValue() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate localDateInicio = LocalDate.parse(txtTarefaDataInicio.getText(), formatter);
                    Tarefa tarefa = new Tarefa(txtTarefaNome.getText(), Integer.parseInt(txtTarefaDuracao.getText()), localDateInicio);
                    if(txtTarefaPercentual.getText().isEmpty()) tarefa.setPercentual(0);
                    else tarefa.setPercentual(Integer.parseInt(txtTarefaPercentual.getText()));
                    
                    try {
                        tarefaDAO = new TarefaDAOJDBC();
                        tarefaDAO.criar(tarefa, lstProjetos.getSelectedValue().getNome());
                        lstTarefas.setModel(new TarefaListModel(tarefaDAO.buscarTarefaProjeto(lstProjetos.getSelectedValue().getNome())));
                    } catch (Exception ex) {
                        Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        btnRemoverTarefa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lstTarefas.getSelectedValue() != null && lstProjetos.getSelectedValue() != null) {
                    try {
                        pessoaDAO = new PessoaDAOJDBC();
                        pessoaDAO.deletarTarefa(lstProjetos.getSelectedValue().getNome(), lstTarefas.getSelectedValue().getNome());
                        lstPessoas.setModel(new PessoaListModel(pessoaDAO.listarPessoaTarefaProjeto(lstProjetos.getSelectedValue().getNome(), lstTarefas.getSelectedValue().getNome())));
                        tarefaDAO = new TarefaDAOJDBC();
                        tarefaDAO.deletar(lstTarefas.getSelectedValue().getNome(), lstProjetos.getSelectedValue().getNome());
                        clearAllTarefaFields();
                        lstTarefas.setModel(new TarefaListModel(tarefaDAO.buscarTarefaProjeto(lstProjetos.getSelectedValue().getNome())));
                    } catch (Exception ex) {
                        Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        btnInicioHoje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtTarefaDataInicio.setText(FuncoesAux.dateFormatDDMMYYYY(LocalDate.now()));
            }
        });
        
        btnNovaPessoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtPessoaNome.getText().isEmpty() && !txtPessoaEmail.getText().isEmpty() && lstTarefas.getSelectedValue() != null && lstProjetos.getSelectedValue() != null) {
                    Pessoa pessoa = new Pessoa(txtPessoaNome.getText(), txtPessoaEmail.getText());
                    try {
                        pessoaDAO = new PessoaDAOJDBC();
                        pessoaDAO.criar(pessoa, lstTarefas.getSelectedValue().getNome(), lstProjetos.getSelectedValue().getNome());
                        lstPessoas.setModel(new PessoaListModel(pessoaDAO.listarPessoaTarefaProjeto(lstProjetos.getSelectedValue().getNome(), lstTarefas.getSelectedValue().getNome())));
                    } catch (Exception ex) {
                        Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }
        });
        
        btnRemoverPessoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lstPessoas.getSelectedValue() != null && lstTarefas.getSelectedValue() != null && lstProjetos.getSelectedValue() != null) {
                    try {
                        pessoaDAO = new PessoaDAOJDBC();
                        pessoaDAO.deletar(lstProjetos.getSelectedValue().getNome(), lstTarefas.getSelectedValue().getNome(), lstPessoas.getSelectedValue().getNome());
                        clearAllPessoaFields();
                        lstPessoas.setModel(new PessoaListModel(pessoaDAO.listarPessoaTarefaProjeto(lstProjetos.getSelectedValue().getNome(), lstTarefas.getSelectedValue().getNome())));
                    } catch (Exception ex) {
                        Logger.getLogger(Organizador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    
        // ---------------  Botoes de relatorio  -------------------------------------------

        btnTodasTarefas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Tarefa> tarefas = null;
                
                try {
                    tarefaDAO = new TarefaDAOJDBC();
                    tarefas = tarefaDAO.listarTodos();
                } catch (Exception ex) {
                    Logger.getLogger(PainelExibeTodasTarefas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                PainelExibeTodasTarefas prtt = new PainelExibeTodasTarefas(tarefas);
                prtt.setVisible(true);
                prtt.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            }
        });

        btnTarefasConcluidas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Tarefa> tarefas = null;
                
                try {
                    tarefaDAO = new TarefaDAOJDBC();
                    tarefas = tarefaDAO.buscarTarefaConcluida();
                } catch (Exception ex) {
                    Logger.getLogger(PainelExibeTodasTarefas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                PainelExibeTodasTarefas prtt = new PainelExibeTodasTarefas(tarefas);
                prtt.setVisible(true);
                prtt.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            }
        });
        
        btnTarefasFazer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Tarefa> tarefas = null;
                
                try {
                    tarefaDAO = new TarefaDAOJDBC();
                    tarefas = tarefaDAO.buscarTarefaFazer();
                } catch (Exception ex) {
                    Logger.getLogger(PainelExibeTodasTarefas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                PainelExibeTodasTarefas prtt = new PainelExibeTodasTarefas(tarefas);
                prtt.setVisible(true);
                prtt.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            }
        });
    }
    
    //----------------  Funcoes auxiliares  --------------------------------------------

    private void clearAllTarefaFields() {
        txtTarefaNome.setText("");
        txtTarefaDuracao.setText("");
        txtTarefaDataInicio.setText("");
        txtTarefaPercentual.setText("");
        pgrTarefa.setValue(0);
    }
    
    private void clearAllPessoaFields() {
        txtPessoaNome.setText("");
        txtPessoaEmail.setText("");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTarefaDuracao = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTarefaDataInicio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPessoaNome = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtPessoaEmail = new javax.swing.JTextField();
        btnNovoProjeto = new javax.swing.JButton();
        btnTarefasConcluidas = new javax.swing.JButton();
        btnTodasTarefas = new javax.swing.JButton();
        btnExcluirProjeto = new javax.swing.JButton();
        btnTarefasFazer = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        pgrTarefa = new javax.swing.JProgressBar();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstProjetos = new JList<>(new DefaultListModel<>());
        jScrollPane5 = new javax.swing.JScrollPane();
        lstTarefas = new JList<>(new DefaultListModel<>());
        jScrollPane6 = new javax.swing.JScrollPane();
        lstPessoas = new JList<>(new DefaultListModel<>());
        btnNovaPessoa = new javax.swing.JButton();
        btnRemoverPessoa = new javax.swing.JButton();
        btnNovaTarefa = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtTarefaNome = new javax.swing.JTextField();
        btnRemoverTarefa = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        btnInicioHoje = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        txtTarefaPercentual = new javax.swing.JTextField();

        jCheckBox1.setText("jCheckBox1");

        jLabel11.setText("Tarefa concluída");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Projetos");

        jLabel2.setText("Tarefas");

        jLabel3.setText("Pessoal");

        jLabel4.setText("Informações");

        jLabel5.setText("Duração");

        txtTarefaDuracao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarefaDuracaoActionPerformed(evt);
            }
        });

        jLabel6.setText("Início");

        txtTarefaDataInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarefaDataInicioActionPerformed(evt);
            }
        });

        jLabel8.setText("Dados");

        jLabel9.setText("Nome");

        jLabel10.setText("Email");

        txtPessoaEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPessoaEmailActionPerformed(evt);
            }
        });

        btnNovoProjeto.setText("Novo projeto");

        btnTarefasConcluidas.setText("Tarefas concluídas");
        btnTarefasConcluidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTarefasConcluidasActionPerformed(evt);
            }
        });

        btnTodasTarefas.setText("Exibir todas as tarefas");
        btnTodasTarefas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTodasTarefasActionPerformed(evt);
            }
        });

        btnExcluirProjeto.setText("Excluir projeto");

        btnTarefasFazer.setText("Tarefas a fazer");

        jButton7.setText("Tarefas disponíveis para início");

        jButton8.setText("Tarefas necessárias");

        pgrTarefa.setValue(0);

        lstProjetos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(lstProjetos);

        lstTarefas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(lstTarefas);

        lstPessoas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(lstPessoas);

        btnNovaPessoa.setText("Adicionar pessoa");

        btnRemoverPessoa.setText("Remover pessoa");

        btnNovaTarefa.setText("Adicionar tarefa");

        jButton13.setText("Salvar tarefa");

        jLabel12.setText("Nome");

        btnRemoverTarefa.setText("Remover tarefa");

        jButton14.setText("Salvar pessoa");

        btnInicioHoje.setText("Hoje");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnExcluirProjeto, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(btnNovoProjeto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTarefasFazer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTarefasConcluidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pgrTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTarefaPercentual)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtPessoaNome))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnRemoverTarefa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(txtTarefaDataInicio)
                                            .addComponent(txtTarefaNome, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel5)
                                            .addComponent(txtTarefaDuracao)
                                            .addComponent(btnInicioHoje, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                                    .addComponent(btnNovaTarefa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                    .addComponent(btnTodasTarefas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPessoaEmail)
                                    .addComponent(btnNovaPessoa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(btnRemoverPessoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnInicioHoje, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(txtTarefaDataInicio))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTarefaDuracao, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTarefaNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addComponent(btnNovaTarefa))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton13)
                            .addComponent(jButton8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnRemoverTarefa)
                                    .addComponent(pgrTarefa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTarefaPercentual)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPessoaNome, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPessoaEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnNovaPessoa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRemoverPessoa))
                            .addComponent(jScrollPane6)))
                    .addComponent(jScrollPane4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTodasTarefas)
                    .addComponent(btnTarefasConcluidas)
                    .addComponent(btnNovoProjeto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExcluirProjeto)
                    .addComponent(btnTarefasFazer)
                    .addComponent(jButton7))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTarefaDuracaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarefaDuracaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTarefaDuracaoActionPerformed

    private void txtTarefaDataInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarefaDataInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTarefaDataInicioActionPerformed

    private void btnTarefasConcluidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTarefasConcluidasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTarefasConcluidasActionPerformed

    private void btnTodasTarefasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTodasTarefasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTodasTarefasActionPerformed

    private void txtPessoaEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPessoaEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPessoaEmailActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcluirProjeto;
    private javax.swing.JButton btnInicioHoje;
    private javax.swing.JButton btnNovaPessoa;
    private javax.swing.JButton btnNovaTarefa;
    private javax.swing.JButton btnNovoProjeto;
    private javax.swing.JButton btnRemoverPessoa;
    private javax.swing.JButton btnRemoverTarefa;
    private javax.swing.JButton btnTarefasConcluidas;
    private javax.swing.JButton btnTarefasFazer;
    private javax.swing.JButton btnTodasTarefas;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<Pessoa> lstPessoas;
    private javax.swing.JList<Projeto> lstProjetos;
    private javax.swing.JList<Tarefa> lstTarefas;
    private javax.swing.JProgressBar pgrTarefa;
    private javax.swing.JTextField txtPessoaEmail;
    private javax.swing.JTextField txtPessoaNome;
    private javax.swing.JTextField txtTarefaDataInicio;
    private javax.swing.JTextField txtTarefaDuracao;
    private javax.swing.JTextField txtTarefaNome;
    private javax.swing.JTextField txtTarefaPercentual;
    // End of variables declaration//GEN-END:variables
}
