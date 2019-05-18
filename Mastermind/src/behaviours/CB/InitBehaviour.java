package behaviours.CB;

import agents.CodeBreaker;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InitBehaviour extends OneShotBehaviour {

    CodeBreaker codeBreaker;

    public InitBehaviour(CodeBreaker cb) {
        this.codeBreaker = cb;
    }

    @Override
    public void action() {
        // Wait confirmation from Code Maker to start tries
        ACLMessage message = codeBreaker.blockingReceive();
        System.out.println(message.getSender().getLocalName() + "> " + message.getContent());

    }
}
