import java.util.ArrayList;

public class Vector {
    private double[] cos = new double[15];
    private double[] sin = new double[15];
    private Logic logic;



   public void getVectors(double[] x, double[] y, int numberFourie){
       // cos = [ A,A,A,A,A,B,B,B,B,B,C,C,C,C,C] (sin также)
       for (int i =0;i<15;i=i+5){
           cos[numberFourie+i] = x[i/5];
           sin[numberFourie+i] = x[i/5];
       }
       logic.setCos(cos);
       logic.setSin(sin);

   }

     public void setLogic(Logic logic) {
        this.logic = logic;
    }
}
