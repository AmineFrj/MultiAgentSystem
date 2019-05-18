package agents;

import behaviours.CB.GameBehaviour;
import behaviours.CB.GameEndBehaviour;
import behaviours.CB.InitBehaviour;
import behaviours.CB.StartBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.concurrent.ThreadLocalRandom;

public class CodeBreaker extends Agent {

    private static final String END_GAME_BEHAVIOUR = "fin";
    private static final String INIT_BEHAVIOUR = "initialisation";
    private static final String GAME_BEHAVIOUR = "jeu";
    private static final String START_BEHAVIOUR = "start";

    public static AID ID = new AID("codeBreaker", AID.ISLOCALNAME);
    private int[] combination = {7,7,7,7};
    private int[] keyPegs; // White then black Pegs
    private int numIndices;

    public void setup() {
        FSMBehaviour behaviour = new FSMBehaviour(this);

        //States
        behaviour.registerFirstState(new StartBehaviour(this), START_BEHAVIOUR);
        behaviour.registerState(new InitBehaviour(this), INIT_BEHAVIOUR);
        behaviour.registerState(new GameBehaviour(this), GAME_BEHAVIOUR);
        behaviour.registerLastState(new GameEndBehaviour(this), END_GAME_BEHAVIOUR);

        //Transitions
        behaviour.registerDefaultTransition(START_BEHAVIOUR, INIT_BEHAVIOUR);
        behaviour.registerDefaultTransition(INIT_BEHAVIOUR, GAME_BEHAVIOUR);
        behaviour.registerTransition(GAME_BEHAVIOUR, GAME_BEHAVIOUR, 0);
        behaviour.registerTransition(GAME_BEHAVIOUR, END_GAME_BEHAVIOUR, 1);

        addBehaviour(behaviour);
    }

    private boolean checkExist(int[] array, int value) {
        for (int i : array)
            if (value == i) return false;
        return true;
    }

    public void printSolution(){
        System.out.print("CodeMaker> Key pegs: "+keyPegs[0]+"w|"+keyPegs[1]+"b");
        System.out.println();
    }

    public void update(){
        ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
        message.setContent(converter(combination)+"");
        message.addReceiver(CodeMaker.ID);
        this.send(message);

        /* Wait Code Maker Response */
        ACLMessage reponse = this.blockingReceive();
        if (reponse != null && reponse.getContent() != null){
                int result = Integer.parseInt(reponse.getContent());
                keyPegs = converter(result);
        }
        numIndices = keyPegs[0]+keyPegs[1];
        this.doWait(500);
        printSolution();
    }

    public int[] firstTry() {
        int rand;
        for (int i = 0; i < 4; i++) {
            rand = ThreadLocalRandom.current().nextInt(0, 6);
            while (! checkExist(combination,rand))
                rand = ThreadLocalRandom.current().nextInt(0, 6);
            combination[i] = rand;
        }
        return combination;
    }

    public void findCombination(){
        int rand = ThreadLocalRandom.current().nextInt(0, 6);
        while (! checkExist(combination,rand))
            rand = ThreadLocalRandom.current().nextInt(0, 6);
        numIndices = keyPegs[0]+keyPegs[1];
        switch (numIndices) {
            case 2:
                int x3 = combination[2],
                        x4 = combination[3],
                        x1 = combination[0];

                combination[3] = 15 - combination[0] - combination[1] - combination[2] - combination[3] - rand;
                combination[2] = rand;
                update();

                switch(numIndices){
                    case 2 :
                        combination[0]=x3;
                        combination[1]=x4;
                        update();
                        break;
                    case 3:
                        int x2 = combination[1];
                        combination[1]=x3;
                        update();

                        switch (numIndices){
                            case 2:
                                combination[0] = x2;
                                combination[1] = x4;
                                update();
                                break;
                            case 3:
                                combination[1] = x4;
                                update();
                                combination[0]=x2;
                                combination[1]=x3;
                                update();
                                if (numIndices != 4){
                                    if (numIndices == 2){
                                        combination[0]=x1;
                                        combination[1]=x4;
                                    }else {
                                        combination[0]=x4;
                                        combination[1]=x2;
                                    }
                                    update();
                                }
                                break;
                        }
                        break;
                }

                break;
            case 3:
                int x4_ = combination[3];
                combination[3] = rand;
                update();
                switch (numIndices){
                    case 2:
                        int x3_ = combination[2],
                                x2_ = combination[1];

                        combination[2] = 15 - combination[0] - combination[1] - combination[2] - combination[3] - x4_;
                        combination[3] = x4_;
                        update();

                        if (numIndices != 4){
                            combination[1] = x3_;
                            update();
                            if (numIndices != 4){
                                combination[0] = x2_;
                                update();
                            }
                        }

                        break;
                    case 3:
                        int x5_ = combination[3],
                                x2__ = combination[1],
                                x3__ = combination[2];
                        combination[3] = 15 - combination[0] - combination[1] - combination[2] - combination[3] - x4_;
                        update();
                        if (numIndices != 4){
                            combination[1] = x4_;
                            combination[3] = x5_;
                            update();
                            if (numIndices != 4){
                                combination[2] = x2__;
                                update();
                                if (numIndices != 4){
                                    combination[0] = x3__;
                                    update();
                                }
                            }
                        }
                        break;
                }
                break;
        }

    }

    public void findPermutations(){
        switch (keyPegs[1]){
            case 0:
                permuteCaseZero();
                break;
            case 1:
                permuteCaseOne();
                break;
            case 2:
                permuteCaseTwo();
                break;
        }
    }

    private void permuteCaseZero(){
        int tmp = combination[0], tmp2;
        combination[0] = combination[1];
        combination[1] = tmp;
        tmp = combination[2];
        combination[2] = combination[3];
        combination[3] = tmp;
        update();
        switch (keyPegs[1]){
            case 0:
                tmp = combination[0];
                combination[0] = combination[2];
                combination[2] = combination[3];
                combination[3] = tmp;
                update();
                switch (keyPegs[1]){
                    case 0:
                        tmp = combination[0];
                        tmp2 = combination[1];
                        combination[0] = combination[2];
                        combination[1] = tmp;
                        combination[2] = combination[3];
                        combination[3] = tmp2;
                        update();
                        break;
                    case 1:
                        tmp = combination[1];
                        combination[1] = combination[2];
                        combination[2] = combination[3];
                        combination[3] = tmp;
                        update();
                        if (keyPegs[1]!=4){
                            tmp = combination[0];
                            combination[0] = combination[1];
                            combination[1] = tmp;
                            tmp = combination[2];
                            combination[2] = combination[3];
                            combination[3] = tmp;
                            update();
                        }
                        break;
                    case 2:
                        tmp = combination[1];
                        combination[1] = combination[2];
                        combination[2] = tmp;
                        update();
                        break;
                }
                break;
            case 1:
                tmp = combination[0];
                combination[0] = combination[2];
                combination[2] = tmp;
                update();
                break;
            case 2:
                tmp = combination[0];
                combination[0] = combination[3];
                combination[3] = tmp;
                update();
                if (keyPegs[1] != 4) {
                    if (keyPegs[1] == 0) {
                        tmp = combination[0];
                        tmp2 = combination[1];
                        combination[0] = combination[3];
                        combination[1] = combination[2];
                        combination[2] = tmp2;
                        combination[3] = tmp;
                        update();
                    } else {
                        tmp = combination[0];
                        tmp2 = combination[1];
                        combination[0] = combination[3];
                        combination[1] = tmp;
                        combination[3] = tmp2;
                        update();
                    }
                }
                break;
        }
    }

    private void permuteCaseOne(){
        int tmp = combination[0], tmp2;
        combination[0] = combination[1];
        combination[1] = tmp;
        update();
        switch (keyPegs[1]){
            case 0:
                tmp = combination[0];
                combination[0] = combination[1];
                combination[1] = combination[3];
                combination[3] = combination[2];
                combination[2] = tmp;
                update();
                switch (keyPegs[1]){
                    case 0:
                        tmp = combination[0];
                        tmp2 = combination[1];
                        combination[0] = combination[3];
                        combination[1] = combination[2];
                        combination[2] = tmp2;
                        combination[3] = tmp;
                        update();
                        if (keyPegs[1]!=4){
                            if (keyPegs[1]==0){
                                tmp = combination[0];
                                tmp2 = combination[1];
                                combination[0] = combination[2];
                                combination[1] = combination[3];
                                combination[2] = tmp;
                                combination[3] = tmp2;
                                update();
                            }else {
                                tmp = combination[2];
                                combination[0] = combination[1];
                                combination[1] = combination[2];
                                combination[2] = tmp;
                                update();
                            }
                        }
                        break;
                    case 1:
                        tmp = combination[1];
                        combination[1] = combination[3];
                        combination[3] = combination[2];
                        combination[2] = tmp;
                        update();
                        if (keyPegs[1] != 4){
                            tmp = combination[0];
                            tmp2 = combination[1];
                            combination[0] = combination[2];
                            combination[1] = combination[3];
                            combination[2] = tmp;
                            combination[3] = tmp2;
                            update();
                        }
                        if (keyPegs[1] != 4){
                            tmp = combination[0];
                            combination[0] = combination[1];
                            combination[1] = tmp;
                            tmp = combination[2];
                            combination[2] = combination[3];
                            combination[3] = tmp;
                            update();
                        }
                        break;
                }
                break;
            case 2:
                tmp = combination[0];
                combination[0] = combination[3];
                combination[3] = tmp;
                update();
                switch (keyPegs[1]){
                    case 0:
                        tmp = combination[0];
                        tmp2 = combination[1];
                        combination[0] = combination[3];
                        combination[1] = combination[2];
                        combination[2] = tmp2;
                        combination[3] = tmp;
                        update();
                        break;
                    case 1:
                        tmp = combination[0];
                        combination[0] = combination[3];
                        combination[3] = combination[1];
                        combination[1] = tmp;
                        update();
                        if (keyPegs[1] != 4){
                            tmp = combination[0];
                            tmp2 = combination[1];
                            combination[0] = combination[2];
                            combination[1] = combination[3];
                            combination[2] = tmp;
                            combination[3] = tmp2;
                            update();
                        }
                        break;
                }
                break;
        }
    }

    private void permuteCaseTwo(){
        int tmp, tmp2;
        tmp = combination[0];
        combination[0] = combination[1];
        combination[1] = tmp;
        update();
        switch (keyPegs[1]){
            case 0 :
                tmp = combination[0];
                combination[0] = combination[1];
                combination[1] = tmp;
                tmp = combination[2];
                combination[2] = combination[3];
                combination[3] = tmp;
                update();
                break;
            case 1:
                tmp = combination[0];
                combination[0] = combination[1];
                combination[1] = combination[3];
                combination[3] = tmp;
                update();
                switch (keyPegs[1]){
                    case 0:
                        tmp = combination[0];
                        tmp2 = combination[1];
                        combination[0] = combination[2];
                        combination[1] = combination[3];
                        combination[2] = tmp;
                        combination[3] = tmp2;
                        update();
                        break;
                    case 1:
                        tmp = combination[1];
                        combination[1] = combination[2];
                        combination[2] = combination[3];
                        combination[3] = tmp;
                        update();
                        if (keyPegs[1] != 4){
                            tmp = combination[0];
                            tmp2 = combination[1];
                            combination[0] = combination[3];
                            combination[1] = combination[2];
                            combination[2] = tmp2;
                            combination[3] = tmp;
                            update();
                        }
                        break;
                }
                break;
        }
    }

    public int converter(int[] tab){
        return tab[0]*1000+tab[1]*100+tab[2]*10+tab[3];
    }

    public int[] converter(int num){
        int[] tab = new int[2];
        tab[0] = num/10; num -= tab[0]*10;
        tab[1] = num;
        return tab;
    }

    public int[] getCombination() {
        return combination;
    }

    public void setCombination(int[] combination) {
        this.combination = combination;
    }

    public int[] getKeyPegs() {
        return keyPegs;
    }

    public void setKeyPegs(int[] keyPegs) {
        this.keyPegs = keyPegs;
    }

    public int getNumIndeces() {
        return numIndices;
    }

    public void setNumIndeces(int numIndeces) {
        this.numIndices = numIndeces;
    }
}
