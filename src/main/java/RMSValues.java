public class RMSValues {
    private double phA;
    private double phB;
    private double phC;
    private double time;

    public void set(int phasa, double value){

        if (phasa==1) setPhA(value);
        if (phasa==2) setPhB(value);
        if (phasa==3) setPhC(value);

    }

    public double getPhA() {
        return phA;
    }

    public double getPhB() {
        return phB;
    }

    public double getPhC() {
        return phC;
    }

    public void setPhA(double phA) {
        this.phA = phA;
    }

    public void setPhB(double phB) {
        this.phB = phB;
    }

    public void setPhC(double phC) {
        this.phC = phC;
    }


    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
