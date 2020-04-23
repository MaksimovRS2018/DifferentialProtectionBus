import java.util.ArrayList;

public class Fourie {
    private ArrayList<Double> buff = new ArrayList<Double>();
    private ArrayList<Integer> counts = new ArrayList<Integer>();
    private ArrayList<Double> A0 = new ArrayList<Double>();
    private ArrayList<Double> Ak1 = new ArrayList<Double>();
    private ArrayList<Double> Bk1 = new ArrayList<Double>();
    private int c = 0;

    private SampleValues[]sv;
    private RMSValues[] rms;
    private double sumPh;

    public void set(){
        for (int j = 0; j < 15; j++) {
            buff.add(0.);
            A0.add(0.);
            Ak1.add(0.);
            Bk1.add(0.);
        }
        for (int j = 0; j < 5; j++) {
            counts.add(0);
        }
    }


    public void calculate() {
        for (int j = 0; j < 5; j++) {
            int period = 80; //количество точек за период
            int count = counts.get(j);
            for (int i = 0; i < 3; i++) {

                sumPh = sv[j].get(i+1) - buff.get(c);

                //Алгоритм фурье постоянная составляющая + 1 гармоника

                //расчет постоянной составляющей, возникает при КЗ. В норм. режиме равна нулю -> интеграл синусоиды = 0
                double actual_A0 = A0.get(c) + sumPh / period;
                //расчет cos и sin составляющей для первой гармоники
                double actual_Ak1 =Ak1.get(c)+ 2 * (Math.cos(count * 2 * Math.PI / period) * sumPh) / period;
                double actual_Bk1 =Bk1.get(c)+ 2 * (Math.sin(count * 2 * Math.PI / period) * sumPh) / period;
                Ak1.set(c,actual_Ak1);
                Bk1.set(c,actual_Bk1);
                A0.set(c,actual_A0);
                //расчет действующего значения для 1 гармоники по cos и sin составляющей
                double Ck1 = Math.sqrt((Math.pow(actual_Ak1, 2) + Math.pow(actual_Bk1, 2)) / 2);
                double x = Math.sqrt(Math.pow(Ck1, 2) + Math.pow(actual_A0, 2));
                //суммарная составляющая
//                localbuf[count] = sv[j].get(i+1);
                buff.set(c,sv[j].get(i+1));
                //устанавливаем действующие значения
                rms[j].set(i+1,x);
                c ++;
            }
            count++;

            if (count == period) {
                count = 0;
            }
            counts.set(j,count);


        }
        c = 0;
    }





    public void setSv(SampleValues[] sv) {
        this.sv = sv;
    }



    public void setRms(RMSValues[] rms) {
        this.rms = rms;
    }

}

