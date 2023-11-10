package puzzles.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.TentOrGrassCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class TentOrGrassCaseRuleTest {

    private static final TentOrGrassCaseRule RULE = new TentOrGrassCaseRule();
    private static TreeTent treetent;
 
    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * empty 3x3 TreeTent puzzle
     * Tests TentOrGrassCaseRule on UNKOWN tile
     * at (0,0)
     * 
     * checks 2 cases are created
     * checks first case is TENT tile
     * checks second case is GRASS tile
     * checks other cells have not been modified
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void TentOrTreeTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TentOrGrassCaseRule/TestPuzzle", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell testing_cell = board.getCell(0, 0);
        ArrayList<Board> cases = RULE.getCases(board, testing_cell);

        // assert correct number of cases created
        Assert.assertEquals(2, cases.size());

        // expected boards
        TreeTentCell changed_cell;

        // case1 (TENT case)
        TreeTentBoard case1 = ((TreeTentBoard) transition.getBoard()).copy();
        changed_cell = case1.getCell(0, 0);
        changed_cell.setData(TreeTentType.TENT);
        Assert.assertTrue(cases.contains((TreeTentBoard) case1));

        // case2 (GRASS case)
        TreeTentBoard case2 = ((TreeTentBoard) transition.getBoard()).copy();
        changed_cell = case2.getCell(0, 0);
        changed_cell.setData(TreeTentType.GRASS);
        Assert.assertTrue(cases.contains((TreeTentBoard) case2));

        // checks other cells have not been modified
        TreeTentCell original_cell;
        TreeTentCell case_cell;

        for (int w =0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (w == 0 && h ==0) {
                    continue;
                }
                original_cell = board.getCell(w, h);
                case_cell = case1.getCell(w, h);
                Assert.assertEquals(original_cell, case_cell);

                case_cell = case2.getCell(w, h);
                Assert.assertEquals(original_cell, case_cell);
            }
        }
    }
}



