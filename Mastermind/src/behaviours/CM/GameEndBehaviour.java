package behaviours.CM;

import agents.CodeMaker;
import jade.core.behaviours.OneShotBehaviour;

public class GameEndBehaviour extends OneShotBehaviour {
    CodeMaker codeMaker;

    public GameEndBehaviour(CodeMaker codeMaker) {
        this.codeMaker = codeMaker;
    }


    @Override
    public void action() {
        codeMaker.doWait(1500);
        System.out.println(codeMaker.getAID().getLocalName() + " : Game ended after " + codeMaker.getTriesNumber() +" tries");
        codeMaker.doDelete();
    }
}
