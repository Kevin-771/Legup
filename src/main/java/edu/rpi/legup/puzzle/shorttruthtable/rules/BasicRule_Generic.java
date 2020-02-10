package edu.rpi.legup.puzzle.shorttruthtable.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableBoard;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCell;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableCellType;
import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import edu.rpi.legup.model.rules.ContradictionRule;

import java.awt.*;
import java.util.List;

public abstract class BasicRule_Generic extends BasicRule {

    final ContradictionRule correspondingContradictionRule;

    public BasicRule_Generic(String ruleName, String description, String imageName, ContradictionRule contraRule){
        super(ruleName, description, imageName);
        this.correspondingContradictionRule = contraRule;
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement element){

        //Check that the puzzle element is no unknown
        ShortTruthTableBoard board = (ShortTruthTableBoard) transition.getBoard();
        ShortTruthTableCell cell = (ShortTruthTableCell) board.getPuzzleElement(element);

        System.out.println("Check Basic Rule Generic: "+cell);

        if(!cell.isAssigned()){
            System.out.println("  Only assigned cells");
            return "Only assigned cells are allowed for basic rules";
        }

        //check that it is assigned to the right value
        ShortTruthTableBoard originalBoard = (ShortTruthTableBoard) transition.getParents().get(0).getBoard();

        //Use this board to check what would happen if the cell what the oppisite value
        ShortTruthTableBoard testBoard = originalBoard.copy();
        ((ShortTruthTableCell) testBoard.getPuzzleElement(element)).setType(cell.getType().getNegation());

        System.out.println("BAsicRule_Generic Full Debug");
        for (int y = 0; y < board.getHeight(); y+=2)
            for (int x = 0; x < board.getWidth(); x++){
                System.out.println("Cell  "+board.getCell(x, y));
                System.out.println("State "+((ShortTruthTableCell)board.getCell(x, y)).getStatementRefference());
                System.out.println();
            }
        System.out.println("\n\n\n\n");

        //see if there is a contradiction
        String checkContradiction = correspondingContradictionRule.checkContradictionAt(testBoard, element);
        ShortTruthTableStatement s = cell.getStatementRefference();
        System.out.println(cell);
        System.out.println(s);
        ShortTruthTableStatement parent = s.getParentStatement();
        ShortTruthTableCell parentCell = parent.getCell();
        String checkContradictionParent = correspondingContradictionRule.checkContradictionAt(testBoard, parentCell);

        if(checkContradiction==null || checkContradictionParent==null)
            return null;

        return "Oppisite Contradiction Failed";

    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node short truth table board used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}