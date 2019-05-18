package behaviours.CB;

import agents.CodeBreaker;
import agents.CodeMaker;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class StartBehaviour extends OneShotBehaviour {
    CodeBreaker codeBreaker;

    public StartBehaviour(CodeBreaker ag){
        this.codeBreaker = ag;
    }


    @Override
    public void action() {
        codeBreaker.doWait(1000);
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setContent("START GAME");
        message.addReceiver(CodeMaker.ID);
        codeBreaker.send(message);
    }
}
