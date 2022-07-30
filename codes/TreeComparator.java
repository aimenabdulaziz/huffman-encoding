import java.util.Comparator;
import java.util.Objects;

/**
 * Program for the comparator of the binary trees
 * @author Aimen Abdulaziz, Winter 2022
 */

/**
 * Compare the frequency of the trees
 */
public class TreeComparator implements Comparator<BinaryTree<CharacterElement>> {
    public int compare(BinaryTree<CharacterElement> tree1, BinaryTree<CharacterElement> tree2){
        if (tree1.data.getFrequency() < tree2.data.getFrequency()){
            return -1;
        }
        else if (Objects.equals(tree1.data.getFrequency(), tree2.data.getFrequency())){
            return 0;
        }
        else{
            return 1;
        }
    }
}