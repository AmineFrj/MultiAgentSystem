package behaviours.CM;

import agents.CodeBreaker;
import agents.CodeMaker;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InitBehaviour extends OneShotBehaviour {

    CodeMaker codeMaker;

    public InitBehaviour(CodeMaker codeMaker) {
        this.codeMaker = codeMaker;
    }

    @Override
    public void action() {
        codeMaker.doWait();
        ACLMessage message = codeMaker.receive();
        System.out.println(message.getSender().getLocalName() + "> " + message.getContent());

        // Define the first combination
        codeMaker.initGame();

        // Give hand to the Breaker
        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
        response.setContent("Combination chosen, you can start the first try !");
        response.addReceiver(CodeBreaker.ID);
        codeMaker.send(response);
    }
}
