public class SampleValues {
    private double phA;
    private double phB;
    private double phC;
    private double time;
    //alt+insert

    public double get(int phasa){

        if (phasa==1) {
            return  getPhA();
        }
        if (phasa==2) {
            return getPhB();
        }
        if (phasa==3) {
            return getPhC();
        } else {
            return 0;
        }


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
