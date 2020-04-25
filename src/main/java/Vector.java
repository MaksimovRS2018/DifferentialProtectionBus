import java.util.ArrayList;

public class Vector {
    private double[] cosFirst = new double[15];
    private double[] sinFirst = new double[15];
    private double[] cosSecond = new double[15];
    private double[] sinSecond = new double[15];

   public void getVectorsFirstAndSecondHarmonic(double[] x_harmonic1, double[] y_harmonic1, double[] x_harmonic2,
                                       double[] y_harmonic2, int numberFourie){

       for (int i =0;i<15;i=i+5){
           cosFirst[numberFourie+i] = x_harmonic1[i/5];
           sinFirst[numberFourie+i] = y_harmonic1[i/5];
           cosSecond[numberFourie+i] = x_harmonic2[i/5];
           sinSecond[numberFourie+i] = y_harmonic2[i/5];
       }

   }

    public double[] getCosFirst() {
        return cosFirst;
    }

    public double[] getSinFirst() {
        return sinFirst;
    }

    public double[] getCosSecond() {
        return cosSecond;
    }

    public double[] getSinSecond() {
        return sinSecond;
    }
}
