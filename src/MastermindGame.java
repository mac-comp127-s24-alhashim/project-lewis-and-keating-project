import java.util.*;
import java.util.Random;



public class MastermindGame {
    
    public ArrayList<String> secretCode = new ArrayList<String>(Arrays.asList());
    public boolean duplicatesAllowed;
    public int codeLength;

    public static final ArrayList<String> colorList = new ArrayList<String>(Arrays.asList("red","green","blue","yellow","brown","orange","black","white"));
    public static final HashMap<String, int[]> colorMap = new HashMap<>();

    


    public Random random = new Random();

    


    // constructor for MastermindGame, also randomly generates code of codeLength
    // only allows duplicates of colors within the code if duplicatesAllowed is true
    public MastermindGame(boolean duplicatesAllowed, int codeLength) {
        this.duplicatesAllowed = duplicatesAllowed;
        this.codeLength = codeLength;

        ArrayList<String> duplicateList = colorList;
        for(int i = 0; i < codeLength; i++) {
            String colorChoice = duplicateList.get(random.nextInt(duplicateList.size()));
            this.secretCode.add(colorChoice);
            if(!duplicatesAllowed) {
                duplicateList.remove(colorChoice);
            }
        }


        // initializes colorMap -- might delete later, took like five seconds anyway//
        colorMap.put("red", new int[]{195,40,40,230});
        colorMap.put("green", new int[]{70,180,90,230});
        colorMap.put("blue", new int[]{85,140,230,230});
        colorMap.put("yellow", new int[]{240,220,80,230});
        colorMap.put("brown", new int[]{65,40,30,230});
        colorMap.put("orange", new int[]{250,160,50,230});
        colorMap.put("black", new int[]{25,20,30,230});
        colorMap.put("white", new int[]{230,225,235,230});

    }

    public ArrayList<String> getSecretCode() {
        return this.secretCode;
    }

    // this might not be that important but if we need to do something
    // with the secret code and we don't wanna risk changing the
    // original code, this could be helpful
    private ArrayList<String> getDuplicateOfSecretCode() {
        ArrayList<String> copy = new ArrayList<String>();
        for(String color : this.secretCode) {
            copy.add(color);
        }

        return copy;
    }


    // checks secret code and returns a list of red pegs, white pegs, and empty pegs (blanks)
    public ArrayList<String> checkSecretCode(ArrayList<String> guess) {

        // makes sure the guess is the same length 
        // maybe when we implement the buttons this could be useful
        if(guess.size() != secretCode.size()) {
            return null;
        }

        ArrayList<String> returnArray = new ArrayList<String>(Arrays.asList());
        ArrayList<String> duplicateCode = getDuplicateOfSecretCode();
        
        for(int i = 0; i < guess.size(); i++) {
            if(guess.get(i).equals(duplicateCode.get(i))) {
                returnArray.add("red");
                guess.set(i,"c");
                duplicateCode.set(i, "p");
            }
        }

        for(int i = 0; i < guess.size(); i++) {
            for(int j = 0; j < duplicateCode.size(); j++) {
                if(guess.get(i).equals(duplicateCode.get(j))) {
                    returnArray.add("white");
                    guess.set(i, "r");
                    duplicateCode.set(j, "k");
                }
            }
        }


        // quick thingy to fill the rest with blanks 
        // we might not need to use this when we build the UI
        int howMuchIsLeft = guess.size() - returnArray.size();
        for(int i = 0; i < howMuchIsLeft; i++) {
            returnArray.add("blank");
        }
        
        return returnArray;
    }


    // toString to make sure we can check on the code
    public String toString() {
        String returnString = "Secret code:  ";
        for(int i = 0; i < this.codeLength; i++) {
            returnString += (secretCode.get(i) + " ");
        }
        return returnString;
    }

    // basically a toString for if we need to check a code other than the secret one
    public static String printList(ArrayList<String> list) {
        String returnString = "";
        for(int i = 0; i < list.size(); i++) {
            returnString += (list.get(i) + " ");
        }
        return returnString;
    }
}