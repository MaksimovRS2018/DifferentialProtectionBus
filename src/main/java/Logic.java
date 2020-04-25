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
    private boolean block_2harmonic = false;
    private double time;
    private  double timeStep = 0.001;
    private double setTime = 0.04;

    public void setVectors(){
        for (int i = 0; i < 3; i++) {
            diffCurrent[i] = getsumm(i*5,vectors.getCosFirst(),vectors.getSinSecond());
            blkdiff[i] = getsumm(i*5,vectors.getCosSecond(),vectors.getSinSecond());
        }
        protect();

    }

    private void protect(){
        boolean blk = false;
        boolean trip = false;
        for (int i = 0; i < 3; i++) {
            blk = blk | blocking(blkdiff[i]/diffCurrent[i]);
            if (diffCurrent[i] > beginingDiffCurrent) {
                od.forEach(e->e.setStr(true));
                block_2harmonic = blk;
                if (blk) {
                    System.out.println(blkdiff[i]+ " 2 harmonic");
                    System.out.println(diffCurrent[i]+ " 1 harmonic");
                    System.out.println(blkdiff[i]/diffCurrent[i]+ "not blocking");
                    trip = true;
                } else {
                        System.out.println(blkdiff[i]+ " --------------2 harmonic");
                        System.out.println(diffCurrent[i]+ "------------ 1 harmonic");
                        System.out.println(blkdiff[i]/diffCurrent[i]+ "--------------------------blocking");
                }
            }
        }
        if (trip) {
            time = time + timeStep;
            if (time > setTime) {
                od.forEach(e -> e.setTripper(true));
                time = 0;
            }
        }


    }
    private boolean blocking(double relation){
        boolean blk = (relation < blkSecondHarmonic);
        return blk;
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

    public void setVectors(Vector vectors) {
        this.vectors = vectors;
    }

    public void setBlkSecondHarmonic(double blkSecondHarmonic) {
        this.blkSecondHarmonic = blkSecondHarmonic;
    }

    public void setOd(ArrayList<OutputData> od) {
        this.od = od;
    }

    public void setBeginingDiffCurrent(double beginingDiffCurrent) {
        this.beginingDiffCurrent = beginingDiffCurrent;
    }

    public void setBeginingBrakeCurrent(double beginingBrakeCurrent) {
        this.beginingBrakeCurrent = beginingBrakeCurrent;
    }

    public void setBrakeCurrent(double brakeCurrent) {
        this.brakeCurrent = brakeCurrent;
    }

    public void setCoefBrake(double coefBrake) {
        this.coefBrake = coefBrake;
    }

    public boolean isBlock_2harmonic() {
        return block_2harmonic;
    }
}
