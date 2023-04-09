package sat;

import java.util.ArrayList;

public class ActionFormula {
    int action;
    ArrayList<Integer> precondPosAction;
    ArrayList<Integer> effectPosAction;
    ArrayList<Integer> effectNegAction;

    public ActionFormula() {
        this.action = -1;
        this.precondPosAction = new ArrayList<>();
        this.effectPosAction = new ArrayList<>();
        this.effectNegAction = new ArrayList<>();
    }
}
