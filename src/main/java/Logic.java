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
    private double[] currentDrag = new double[3];

    public void setVectors() {
        for (int i = 0; i < 3; i++) {
            //диф ток по 1 гармонике
            diffCurrent[i] = getsumm(i * 5, vectors.getCosFirst(), vectors.getSinFirst());
            //диф ток по 2 гармонике, для блокировки
            blkdiff[i] = getsumm(i * 5, vectors.getCosSecond(), vectors.getSinSecond());
        }
        //запуск логики
        protect();

    }

    private void protect() {
        boolean str = false;
        boolean blk = false;
        boolean trip = false;
        for (int i = 0; i < 3; i++) {
            //It
            currentDrag[i] = getCurrentDrag(i * 5, vectors.getCosFirst(), vectors.getSinFirst());
            if (diffCurrent[i] > coefDrag * (currentDrag[i] - beginingDragCurrent) + beginingDiffCurrent) {
                str = str | true;
                //проверка на блокировку по 2 гармонике
                if (blocking(blkdiff[i] / diffCurrent[i])) {
                    blk = blk | false;
                    trip = trip | true;
                } else {
                    blk = blk | true;
                }
            }
        }
        od.setBlk(blk);
        od.setTripper(trip);
        od.setStr(str);
        //прием дискретных сигналов от логики
        od.takeDiscreteSignals();

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
