package behaviours.CB;

import agents.CodeBreaker;
import agents.CodeMaker;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class GameBehaviour extends OneShotBehaviour {

    private CodeBreaker codeBreaker;

    public GameBehaviour(CodeBreaker codeBreaker) {
        this.codeBreaker = codeBreaker;
    }

    @Override
    public void action() {
        int[] fTry = codeBreaker.firstTry();
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.setContent(codeBreaker.converter(fTry)+"");
        message.addReceiver(CodeMaker.ID);
        codeBreaker.send(message);

        /* Wait for the CodeMaker response */
        codeBreaker.doWait(1000);
        ACLMessage response = codeBreaker.blockingReceive();
        if (response != null && response.getContent() != null) {
            int result = Integer.parseInt(response.getContent());
            codeBreaker.setKeyPegs(codeBreaker.converter(result));
            codeBreaker.printSolution();
            codeBreaker.findCombination();
            codeBreaker.findPermutations();
        }
    }

    @Override
    public int onEnd(){
        return 1;
    }

}
