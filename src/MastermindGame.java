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

        ArrayList<String> duplicateList = (ArrayList) colorList.clone();
        for(int i = 0; i < codeLength; i++) {
            String colorChoice = duplicateList.get(random.nextInt(duplicateList.size()));
            this.secretCode.add(colorChoice);
            if(!duplicatesAllowed) {
                duplicateList.remove(colorChoice);
            }
        }


        // initializes colorMap
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
    // original code
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

        ArrayList<String> guessDup = new ArrayList<String>();
        for(String color : guess) {
            guessDup.add(color);
        }
        
        for(int i = 0; i < guessDup.size(); i++) {
            if(guessDup.get(i).equals(duplicateCode.get(i))) {
                returnArray.add("red");
                guessDup.set(i,"c");
                duplicateCode.set(i, "p");
            }
        }

        for(int i = 0; i < guessDup.size(); i++) {
            for(int j = 0; j < duplicateCode.size(); j++) {
                if(guessDup.get(i).equals(duplicateCode.get(j))) {
                    returnArray.add("white");
                    guessDup.set(i, "r");
                    duplicateCode.set(j, "k");
                }
            }
        }


        // fills rest of code with blanks //
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
    // helpful for testing
    public static String printList(ArrayList<String> list) {
        String returnString = "";
        for(int i = 0; i < list.size(); i++) {
            returnString += (list.get(i) + " ");
        }
        return returnString;
    }

    public static ArrayList<String> resetGuess(ArrayList<String> guessList) {
        ArrayList<String> emptyList = new ArrayList<String>(Arrays.asList());
        for(int i = 0; i < guessList.size(); i++) {
            emptyList.add(null);
        }
        return emptyList;
    }
}