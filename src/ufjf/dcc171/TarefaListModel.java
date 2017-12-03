package ufjf.dcc171;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

public class TarefaListModel implements ListModel {
    private final List<Tarefa> tarefas;
    private final List<ListDataListener> dataListeners;
    
    public TarefaListModel(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
        this.dataListeners =  new ArrayList<>();
    }

    @Override
    public int getSize() {
        return tarefas.size();
    }

    @Override
    public Tarefa getElementAt(int index) {
        return tarefas.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        this.dataListeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        this.dataListeners.remove(l);
    }
}
