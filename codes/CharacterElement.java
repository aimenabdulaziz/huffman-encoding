/**
 * Program that creates a unique data type with character and frequency for the binary tree
 * @author Aimen Abdulaziz, Winter 2022
 */

public class CharacterElement {
    private Character character;
    private Integer frequency;

    /**
     * Create the CharacterElement
     * @param c character of the element
     * @param frequency frequency of the element
     */
    public CharacterElement(Character c, int frequency) {
        this.character = c;
        this.frequency = frequency;
    }

    /**
     * Return the character of the element
     * @return the character
     */
    public Character getCharacter() {
        return character;
    }

    /**
     * To get the frequency of the element
     * @return the frequency
     */
    public Integer getFrequency() {
        return frequency;
    }

    /**
     * toString method for the CharacterElement
     * @return the string that includes the character and the respective frequency
     */
    public String toString() {
        return character + ":" + frequency;
    }
}
