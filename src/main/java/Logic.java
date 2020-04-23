import java.util.ArrayList;

public class Logic {
    private double[] cos = new double[15];
    private double[] sin = new double[15];
    private ArrayList<Double> diffCurrent = new ArrayList<Double>();


    public void protect(){
        for (int i = 0; i < 3; i++) {
            diffCurrent.set(i, getsumm(i*5));
        }
    }

    private double getsumm(int phasa) {
        double summX = 0.;
        double summY = 0.;
        double resultCurrent = 0;
        for (int i = phasa; i < (5 + phasa); i++) {
            summX = summX + cos[i];
            summY = summY + sin[i];
            resultCurrent = Math.sqrt((Math.pow(summX, 2) + Math.pow(summY, 2)) / 2);
        }
        return resultCurrent;
    }

    public double[] getCos() {
        return cos;
    }

    public void setCos(double[] cos) {
        this.cos = cos;
    }

    public double[] getSin() {
        return sin;
    }

    public void setSin(double[] sin) {
        this.sin = sin;
    }
}
