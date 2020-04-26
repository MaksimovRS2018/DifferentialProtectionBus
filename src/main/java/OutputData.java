import java.util.ArrayList;

public class OutputData {


    private boolean trip = false;
    private boolean str = false;
    private boolean blk = false;
    private double timeStep;
    private double setTime;
    private double time;
    private ArrayList<Breaker> breakers = new ArrayList<Breaker>();

    public void takeDiscreteSignals() {

        if (trip & !blk) { //если срабатывание, то проходим выдержку времени (20-40 миллисекунд, чтобы удостовериться)
            time = time + timeStep;
            if (time > setTime) { //проверка на выдержку времени
                breakers.forEach(e -> e.setState(false)); // приваиваем значение false для состояния выключателей
                time = 0; //сброс счетчика
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
