import java.util.ArrayList;

public class Fourie {
    private double[] bufferPhA = new double[80];
    private double[] bufferPhB = new double[80];
    private double[] bufferPhC = new double[80];
    private ArrayList<Double> A0 = new ArrayList<Double>();
    private ArrayList<Double> Ak1 = new ArrayList<Double>();
    private ArrayList<Double> Bk1 = new ArrayList<Double>();
    private int count = 0;
    private double[] actual_buf = new double[80];

    private SampleValues sv;
    private RMSValues rms;

    public void set(){
        for (int j = 0; j < 3; j++) {
            A0.add(0.);
            Ak1.add(0.);
            Bk1.add(0.);
        }
    }


    public void calculate() {
            int period = 20; //количество точек за период
            for (int i = 0; i < 3; i++) {
                if (i ==0) actual_buf = bufferPhA;
                if (i ==1) actual_buf = bufferPhB;
                if (i ==2) actual_buf = bufferPhC;

                double sumPh = sv.get(i+1) - actual_buf[count];

                //Алгоритм фурье постоянная составляющая + 1 гармоника

                //расчет постоянной составляющей, возникает при КЗ. В норм. режиме равна нулю -> интеграл синусоиды = 0
                double actual_A0 = A0.get(i) + sumPh / period;
                //расчет cos и sin составляющей для первой гармоники
                double actual_Ak1 =Ak1.get(i)+ 2 * (Math.cos(count * 2 * Math.PI / period) * sumPh) / period;
                double actual_Bk1 =Bk1.get(i)+ 2 * (Math.sin(count * 2 * Math.PI / period) * sumPh) / period;
                Ak1.set(i,actual_Ak1);
                Bk1.set(i,actual_Bk1);
                A0.set(i,actual_A0);
                //расчет действующего значения для 1 гармоники по cos и sin составляющей
                double Ck1 = Math.sqrt((Math.pow(actual_Ak1, 2) + Math.pow(actual_Bk1, 2)) / 2);
                double x = Math.sqrt(Math.pow(Ck1, 2) + Math.pow(actual_A0, 2));
                //суммарная составляющая
                actual_buf[count]=sv.get(i+1);
                if (i ==0) bufferPhA = actual_buf;
                if (i ==1) bufferPhB = actual_buf;
                if (i ==2) bufferPhC = actual_buf;

                //устанавливаем действующие значения
                rms.set(i+1,x);
            }
            count++;

            if (count == period) {
                count = 0;
            }
    }





    public void setSv(SampleValues sv) {
        this.sv = sv;
    }



    public void setRms(RMSValues rms) {
        this.rms = rms;
    }

}

