public class Breaker {

    private boolean state = true; // true - включен

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }

}
