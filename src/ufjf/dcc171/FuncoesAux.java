package ufjf.dcc171;

import java.time.LocalDate;

public class FuncoesAux {
    public static String dateFormatDDMMYYYY(LocalDate data) {
        return data.getDayOfMonth()+"/"+data.getMonthValue()+"/"+data.getYear();
    }
}
