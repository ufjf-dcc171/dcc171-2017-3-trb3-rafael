package ufjf.dcc171;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PainelExibeTodasTarefas extends javax.swing.JFrame {
    private TarefaDAO tarefaDAO;
    
    public PainelExibeTodasTarefas(List<Tarefa> tarefas) {
        initComponents();
        
        lblTarefaDataFim.setText("");
        lblTarefaDataInicio.setText("");
        lblTarefaDuracao.setText("");
        lblTarefaNome.setText("");
        lblTarefaPercentual.setText("");
        pgrTarefaPercentual.setValue(0);
        
        lstTodasTarefas.setModel(new TarefaListModel(tarefas));
        
        lstTodasTarefas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Tarefa selTarefa = lstTodasTarefas.getSelectedValue();
                lblTarefaDataFim.setText(dateFormatDDMMYYYY(selTarefa.getDataFim()));
                lblTarefaDataInicio.setText(dateFormatDDMMYYYY(selTarefa.getDataInicio()));
                lblTarefaDuracao.setText(selTarefa.getDuracao().toString() + " dias");
                lblTarefaNome.setText(selTarefa.getNome());
                lblTarefaPercentual.setText(selTarefa.getPercentual().toString());
                pgrTarefaPercentual.setValue(selTarefa.getPercentual());
            }
        });
    }
    
    private String dateFormatDDMMYYYY(LocalDate data) {
        return data.getDayOfMonth()+"/"+data.getMonthValue()+"/"+data.getYear();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstTodasTarefas = new JList<>(new DefaultListModel<>());
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTarefaNome = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTarefaDataInicio = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblTarefaDataFim = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblTarefaDuracao = new javax.swing.JLabel();
        pgrTarefaPercentual = new javax.swing.JProgressBar();
        lblTarefaPercentual = new javax.swing.JLabel();

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lstTodasTarefas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstTodasTarefas);

        jLabel1.setText("Informações");

        jLabel2.setText("Nome:");

        lblTarefaNome.setText("jLabel4");

        jLabel5.setText("Data de início:");

        lblTarefaDataInicio.setText("jLabel6");

        jLabel7.setText("Data de término:");

        lblTarefaDataFim.setText("jLabel8");

        jLabel9.setText("Duração:");

        lblTarefaDuracao.setText("jLabel10");

        lblTarefaPercentual.setText("jlabel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTarefaNome)
                                    .addComponent(lblTarefaDataInicio)
                                    .addComponent(lblTarefaDataFim)
                                    .addComponent(lblTarefaDuracao))))
                        .addContainerGap(56, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(pgrTarefaPercentual, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTarefaPercentual)
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lblTarefaNome))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(lblTarefaDataInicio))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lblTarefaDataFim))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(lblTarefaDuracao))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblTarefaPercentual, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                            .addComponent(pgrTarefaPercentual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 96, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTarefaDataFim;
    private javax.swing.JLabel lblTarefaDataInicio;
    private javax.swing.JLabel lblTarefaDuracao;
    private javax.swing.JLabel lblTarefaNome;
    private javax.swing.JLabel lblTarefaPercentual;
    private javax.swing.JList<Tarefa> lstTodasTarefas;
    private javax.swing.JProgressBar pgrTarefaPercentual;
    // End of variables declaration//GEN-END:variables
}