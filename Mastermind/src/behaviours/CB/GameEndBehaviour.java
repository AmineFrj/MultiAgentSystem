package behaviours.CB;

import agents.CodeBreaker;
import jade.core.behaviours.OneShotBehaviour;

public class GameEndBehaviour extends OneShotBehaviour {
    CodeBreaker codeBreaker;

    public GameEndBehaviour(CodeBreaker codeBreaker) {
        this.codeBreaker = codeBreaker;
    }

    @Override
    public void action() {
        System.out.println(codeBreaker.getAID().getLocalName() + " : Game ended !"); //+ codeMaker.getTriesNumber() +" tries");
        codeBreaker.doDelete();
    }
}
