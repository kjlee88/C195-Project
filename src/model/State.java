package model;

public class State {
    private int stateID;
    private String stateName;
    public State(int stateID, String stateName) {
        this.stateID = stateID;
        this.stateName = stateName;
    }

    public int getStateID() {
        return stateID;
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public String toString(){
        return stateName;
    }
}
