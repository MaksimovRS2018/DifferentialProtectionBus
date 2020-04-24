import java.util.ArrayList;

public class Logic {
    private double[] diffCurrent = new double[3];
    private double[] blkdiff = new double[3];
    private double blkSecondHarmonic;
    private Vector vectors;
    private ArrayList<OutputData> od;
    private double beginingDiffCurrent; //Id0
    private double beginingBrakeCurrent; //It0
    private double brakeCurrent; //It
    private double coefBrake; //kt


    public void setVectors(){
        for (int i = 0; i < 3; i++) {
            diffCurrent[i] = getsumm(i*5,vectors.getCosFirst(),vectors.getSinSecond());
            blkdiff[i] = getsumm(i*5,vectors.getCosFirst(),vectors.getSinSecond());
        }
        protect();

    }

    private void protect(){
        for (int i = 0; i < 3; i++) {
            if ((diffCurrent[i] > beginingDiffCurrent) | (diffCurrent[i]/blkdiff[i] > blkSecondHarmonic)){
                od.forEach(e->e.setTripper(true));
            }
        }
    }

    private double getsumm(int phasa, double[] cos, double[] sin) {
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


    public double[] getDiffCurrent() {
        return diffCurrent;
    }

    public Vector getVectors() {
        return vectors;
    }

    public void setVectors(Vector vectors) {
        this.vectors = vectors;
    }

    public void setBlkSecondHarmonic(double blkSecondHarmonic) {
        this.blkSecondHarmonic = blkSecondHarmonic;
    }

    public void setOd(ArrayList<OutputData> od) {
        this.od = od;
    }
}
