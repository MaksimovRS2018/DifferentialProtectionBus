import java.util.ArrayList;

public class Fourie {
    private double[] bufferPhA = new double[80];
    private double[] bufferPhB = new double[80];
    private double[] bufferPhC = new double[80];
    private double[] A0 = new double[3]; //3 - 3 phases
    public double[] Ak1 = new double[3];
    public double[] Bk1 = new double[3];
    public double[] Ak2 = new double[3];
    public double[] Bk2 = new double[3];
    private ArrayList<double[]> buff = new ArrayList<double[]>();
    private int count = 0;
    private SampleValues sv;
    private RMSValues rms;
    private Vector vector;
    private int number;

    public void set(){
        buff.add(bufferPhA);
        buff.add(bufferPhB);
        buff.add(bufferPhC);
    }
    Fourie(int number){
        this.number = number;
    }


    public void calculate() {
        int period = 20; //количество точек за период
        for (int i = 0; i < 3; i++) { //3 - 3 phases
                double[] actual_buf = buff.get(i);
                double sumPh = sv.get(i+1) - actual_buf[count];
                //Алгоритм фурье: постоянная составляющая + 1 гармоника
                //расчет постоянной составляющей, возникает при КЗ. В норм. режиме равна нулю -> интеграл синусоиды = 0
                A0[i] = A0[i] + sumPh / period;
                //расчет cos и sin составляющей для первой гармоники
                Ak1[i] =Ak1[i]+ 2 * (Math.cos(count * 2 * Math.PI / period) * sumPh) / period;
                Bk1[i] =Bk1[i]+ 2 * (Math.sin(count * 2 * Math.PI / period) * sumPh) / period;
                //расчет ортогональных составляющих 2 гармоники для блокировки
                Ak2[i] =Ak2[i]+ 2 * (Math.cos(count * 2 * 2 * Math.PI / period) * sumPh) / period;
                Bk2[i] =Bk2[i]+ 2 * (Math.sin(count * 2 * 2 * Math.PI / period) * sumPh) / period;
                //расчет действующего значения для 1 гармоники по cos и sin составляющей
                double Ck1 = Math.sqrt((Math.pow(Ak1[i], 2) + Math.pow(Bk1[i], 2)) / 2);
                double x = Math.sqrt(Math.pow(Ck1, 2) + Math.pow(A0[i], 2));
//                System.out.println("Действубщее значение "+(number+1)+"-ого Фурье = "+x +" фаза = "+(i+1));
                //суммарная составляющая
                actual_buf[count]=sv.get(i+1);
                buff.set(i, actual_buf);
                //устанавливаем действующие значения для построения графика
                rms.set(i+1,x);
            }
            count++;
            if (count == period) {
                count = 0;
            }

            vector.getVectorsFirstAndSecondHarmonic(Ak1,Bk1,Ak2,Bk2,number);

    }


    public void setSv(SampleValues sv) {
        this.sv = sv;
    }

    public void setRms(RMSValues rms) {
        this.rms = rms;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }
}

