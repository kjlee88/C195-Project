package model;

public class State {
    private int stateID;
    private String stateName;

    /**
     * Constructor for State object
     * @param stateID stateID
     * @param stateName stateName
     */
    public State(int stateID, String stateName) {
        this.stateID = stateID;
        this.stateName = stateName;
    }

    /**
     *
     * @return stateID
     */
    public int getStateID() {
        return stateID;
    }

    /**
     *
     * @return Division
     */
    public String getStateName() {
        return stateName;
    }

    /**
     *
     * @return overrides State object and displays in Division
     */
    @Override
    public String toString(){
        return stateName;
    }
}
