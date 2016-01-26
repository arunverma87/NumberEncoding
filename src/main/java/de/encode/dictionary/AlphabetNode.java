/**
 *
 */
package de.encode.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains All Characters of Dictionary in a tree structure start from root.
 *
 * @author arunv
 */
public class AlphabetNode implements Node {

	private Node parentNode;
	// Containing all child objects and making tree
	private Map<Character, Node> children;
	// is whole Word
	private boolean isWholeWord;
	// Just to confirm, it has any child objects or not?
	private boolean hasChild;
	// Value of node
	private char ch;

	public AlphabetNode(char ch) {
		this.ch = ch;
		children = new HashMap<Character, Node>();
		isWholeWord = false;
		hasChild = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#getParent()
	 */
	@Override
	public Node getParent() {
		return parentNode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#setParent(de.encode.dictionary.Node)
	 */
	@Override
	public void setParent(Node node) {
		parentNode = node;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#getIsWord()
	 */
	@Override
	public boolean getIsWord() {
		return isWholeWord;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#setIsWord(boolean)
	 */
	@Override
	public void setIsWord(boolean isWord) {
		isWholeWord = isWord;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#setHasChild(boolean)
	 */
	@Override
	public void setHasChild(boolean haschild) {
		hasChild = haschild;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#getHasChild()
	 */
	@Override
	public boolean getHasChild() {
		return hasChild;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#getChildren()
	 */
	@Override
	public Map<Character, Node> getChildren() {
		return children;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#getChildNode(char)
	 */
	@Override
	public Node getChildNode(char key) {
		if (children.get(key) != null)
			return children.get(key);
		else
			// else return null
			return null;
	}

	/* (non-Javadoc)
	 * @see de.encode.dictionary.Node#getChildNodes(char)
	 */
	@Override
	public List<Node> getChildNodes(char key) {
		List<Node> listofChild = new ArrayList<Node>();
		// Handling Umlaut Character here.
		if (children.get(key) != null) {
			listofChild.add(children.get(key));
			// Also check umlaut charcter exist in current node and if exist,
			// again check children of umlaut character.
			if (children.get('"') != null) {
				// if not null get child object of umlaut character
				if (children.get('"').getChildNode(key) != null)
					listofChild.add(children.get('"').getChildNode(key));
			}
		} else if (children.get('"') != null) {
			// if not null get child object of umlaut character
			if (children.get('"').getChildNode(key) != null)
				listofChild.add(children.get('"').getChildNode(key));
		} else {
			// else return null
			return null;
		}
		return listofChild;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (parentNode == null) {
			return "";
		}
		return parentNode.toString() + String.valueOf(this.ch);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.encode.dictionary.Node#insertNode(de.encode.dictionary.Node)
	 */
	@Override
	public void addChild(Character key, Node node) {
		children.put(key, node);
	}

}
