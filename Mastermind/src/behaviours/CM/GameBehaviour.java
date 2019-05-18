package behaviours.CM;

import agents.CodeBreaker;
import agents.CodeMaker;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class GameBehaviour extends OneShotBehaviour {

    private CodeMaker codeMaker;
    private int stop;

    public GameBehaviour(CodeMaker codeMaker) {
        this.codeMaker = codeMaker;
    }

    @Override
    public void action() {
        //Wait for the try
        ACLMessage message = codeMaker.blockingReceive();
        int[] combination = null;
        int combinationMSG = 0;
        try{
            combinationMSG = Integer.parseInt(message.getContent());
            combination = codeMaker.converter(combinationMSG);
        } catch(Exception ex){
            System.out.println("Erreur : Incorrect Message");
            codeMaker.doDelete();
        }
        codeMaker.doWait(1200);
        System.out.println("CodeBreaker> try: " + combinationMSG);
        //Response
        stop = codeMaker.getTriesNumber();
        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
        response.setContent(codeMaker.converter(codeMaker.check(combination))+"");
        response.addReceiver(CodeBreaker.ID);
        codeMaker.send(response);
        codeMaker.addTry();
    }

    @Override
    public int onEnd(){
        if (codeMaker.isFound() || codeMaker.getTriesNumber()==10)
            return 1;
        return 0;
    }

}
