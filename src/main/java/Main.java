import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String fun;

        for(int i = 0; i < 11; i++) {
            if (i < 5) {
                fun = "Мг. зн." +(i+1); //"Мгн. значение " +i+"-ого фидера"
            } else if (i < 10){
                fun = "Д. зн. " +(i-4); //"Действ. значение " +i+"-ого фидера"
            } else {
                fun = "Диф. ток";
            }
            Charts.createAnalogChart(fun,i);
            Charts.addSeries("Фаза А", i, 0);
            Charts.addSeries("Фаза B", i, 1);
            Charts.addSeries("Фаза С", i, 2);
            System.out.println(i);
        }
        //график № 5 для сигнала срабатывания защиты
//        Charts.createDiscreteChart("Trip",1);
        //найдем из комтрейд файла максимальное значение действующего тока при кз в конце линии для расчета уставки ТО
        InputData inD1 = new InputData("Vkl",5);
        inD1.start();

    }
}
