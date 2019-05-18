package agents;

import behaviours.CM.GameBehaviour;
import behaviours.CM.GameEndBehaviour;
import behaviours.CM.InitBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;

import java.util.concurrent.ThreadLocalRandom;

public class CodeMaker extends Agent {

    private static final String INIT_BEHAVIOUR = "initialisation";
    private static final String GAME_BEHAVIOUR = "jeu";
    private static final String END_GAME_BEHAVIOUR = "fin";

    public static AID ID = new AID("codeMaker", AID.ISLOCALNAME);
    private int[] combination = {7,7,7,7}; //codeCombination
    private short triesNumber = 0;
    boolean found;

    public void setup() {
        FSMBehaviour behaviour = new FSMBehaviour(this);

        //Etats
        behaviour.registerFirstState(new InitBehaviour(this), INIT_BEHAVIOUR);
        behaviour.registerState(new GameBehaviour(this), GAME_BEHAVIOUR);
        behaviour.registerLastState(new GameEndBehaviour(this), END_GAME_BEHAVIOUR);

        //Transitions
        behaviour.registerDefaultTransition(INIT_BEHAVIOUR, GAME_BEHAVIOUR);
        behaviour.registerTransition(GAME_BEHAVIOUR, GAME_BEHAVIOUR, 0);
        behaviour.registerTransition(GAME_BEHAVIOUR, END_GAME_BEHAVIOUR, 1);

        addBehaviour(behaviour);

    }

    public short getTriesNumber() {
        return triesNumber;
    }

    public void setTriesNumber(short triesNumber) {
        this.triesNumber = triesNumber;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public void initGame(){
        //define the combination
        int rand;
        for (int i = 0; i < 4; i++) {
            rand = ThreadLocalRandom.current().nextInt(0, 6);
            while (! checkExist(combination,rand))
                rand = ThreadLocalRandom.current().nextInt(0, 6);
            combination[i] = rand;
        }
        System.out.print("\t\t|");System.out.print(combination[0]);System.out.print(combination[1]);System.out.print(combination[2]);System.out.print(combination[3]);System.out.print("|\n");
    }

    private boolean checkExist(int[] array, int value) {
        for (int i : array)
            if (value == i) return false;
        return true;
    }

    public int[] check(int[] tryCombin){
        int[] res = new int[2];
        res[0] = this.checkWhite(tryCombin);
        res[1] = this.checkBlack(tryCombin);
        if (res[1]==4) found = true;
        return res;
    }

    private int checkWhite(int[] tryCombin){
        int c = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i!=j && tryCombin[i] == combination[j])
                    c++;
            }
        }
        return c;
    }

    private int checkBlack(int[] tryCombin){
        int c = 0;
        for (int i = 0; i < 4; i++) {
            if (tryCombin[i] == combination[i])
                c++;
        }
        return c;
    }

    public int converter(int[] tab){
        return tab[0]*10+tab[1];
    }

    public int[] converter(int num){
        int[] tab = new int[4];
        tab[0] = num/1000; num -= tab[0]*1000;
        tab[1] = num/100; num -= tab[1]*100;
        tab[2] = num/10; num -= tab[2]*10;
        tab[3] = num;
        return tab;
    }

    public void addTry(){
        triesNumber++;
    }
}