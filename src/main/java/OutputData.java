import java.util.ArrayList;

public class OutputData {


    private boolean trip = false;
    private boolean str = false;
    private boolean blk = false;
    private double timeStep ;
    private double setTime;
    private double time;
    private ArrayList<Breaker> breakers = new ArrayList<Breaker>();

    public void takeDiscreteSignals(){
        if (trip) {
            time = time + timeStep;
            if (time > setTime) {
                breakers.forEach(e -> e.setState(false));
                time = 0;
            }
        }
    }


    public boolean getTripper() {
        return trip;
    }

    public void setTripper(boolean trip) {
        this.trip = trip;
    }

    public boolean getStr() {
        return str;
    }

    public void setStr(boolean str) {
        this.str = str;
    }

    public void setBlk(boolean blk) {
        this.blk = blk;
    }


    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }

    public void setSetTime(double setTime) {
        this.setTime = setTime;
    }
    public void setBreakers(ArrayList<Breaker> breakers) {
        this.breakers = breakers;
    }

    public boolean isBlk() {
        return blk;
    }
}
