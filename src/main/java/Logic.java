import java.util.ArrayList;

public class Logic {
    private double[] diffCurrent = new double[3];
    private double[] blkdiff = new double[3];
    private double blkSecondHarmonic;
    private Vector vectors;
    private OutputData od;
    private double beginingDiffCurrent; //Id0
    private double beginingDragCurrent; //It0
    private double coefDrag; //kt
    private boolean block_2harmonic = false;
    private double[] currentDrag = new double[3];

    public void setVectors() {
        for (int i = 0; i < 3; i++) {
            diffCurrent[i] = getsumm(i * 5, vectors.getCosFirst(), vectors.getSinFirst());
            blkdiff[i] = getsumm(i * 5, vectors.getCosSecond(), vectors.getSinSecond());
        }
        protect();

    }

    private void protect() {
        boolean blk = false;
        boolean str = false;
        boolean trip = false;
        for (int i = 0; i < 3; i++) {
//            blk = blk | blocking(blkdiff[i] / diffCurrent[i]);
            //It
            currentDrag[i] = getCurrentDrag(i * 5, vectors.getCosFirst(), vectors.getSinFirst());
            if (diffCurrent[i] > coefDrag * (currentDrag[i] - beginingDragCurrent) + beginingDiffCurrent) {
                od.setStr(true);
                if (blocking(blkdiff[i] / diffCurrent[i])) {
                    od.setBlk(false);
                    od.setTripper(true);
                    System.out.println(blkdiff[i] + " 2 harmonic");
                    System.out.println(diffCurrent[i] + " 1 harmonic");
                    System.out.println(blkdiff[i] / diffCurrent[i] + " not blocking");
                } else {
                    od.setBlk(true);
                    System.out.println(blkdiff[i] + " --------------2 harmonic");
                    System.out.println(diffCurrent[i] + "------------ 1 harmonic");
                    System.out.println(blkdiff[i] / diffCurrent[i] + "--------------------------blocking");
                }
            }
        }
        od.takeDiscreteSignals();

//        if (trip) {
//            time = time + timeStep;
//            if (time > setTime) {
//                block_2harmonic = false;
//                od.forEach(e -> e.setTripper(true));
//                time = 0;
//            }
//        }

    }

    private boolean blocking(double relation) {
        return (relation < blkSecondHarmonic);
    }

    private double getsumm(int phasa, double[] Icos, double[] Isin) {
        double summX = 0.;
        double summY = 0.;
        double resultCurrent = 0;
        for (int i = phasa; i < (5 + phasa); i++) {
            summX = summX + Icos[i];
            summY = summY + Isin[i];
        }
        resultCurrent = Math.sqrt((Math.pow(summX, 2) + Math.pow(summY, 2)) / 2);
        return resultCurrent;
    }

    private double getCurrentDrag(int phasa, double[] Icos, double[] Isin) {
        double summ = 0.;
        double currentDrag = 0;
        for (int i = phasa; i < (5 + phasa); i++) {
            summ = summ + Math.abs(Icos[i]) + Math.abs(Isin[i]);
        }
        currentDrag = summ / 2;
        return currentDrag;
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

    public void setOd(OutputData od) {
        this.od = od;
    }

    public void setBeginingDiffCurrent(double beginingDiffCurrent) {
        this.beginingDiffCurrent = beginingDiffCurrent;
    }


    public boolean isBlock_2harmonic() {
        return block_2harmonic;
    }

    public void setBeginingDragCurrent(double beginingDragCurrent) {
        this.beginingDragCurrent = beginingDragCurrent;
    }

    public void setCoefDrag(double coefDrag) {
        this.coefDrag = coefDrag;
    }
    public double[] getCurrentDrag() {
        return currentDrag;
    }


}
