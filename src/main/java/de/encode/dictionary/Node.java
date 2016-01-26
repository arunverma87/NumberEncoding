/**
 *
 */
package de.encode.dictionary;

import java.util.List;
import java.util.Map;

/**
 * Interface for Character node of Tree
 *
 * @author arunv
 */
public interface Node {

	/**
	 * Get parent node.
	 *
	 * @return Parent Node
	 */
	Node getParent();

	/**
	 * Set parent node.
	 *
	 * @param Node
	 */
	void setParent(Node node);

	/**
	 * Get all children nodes.
	 *
	 * @return Children nodes
	 */
	Map<Character, Node> getChildren();

	/**
	 *
	 * @return
	 */
	boolean getIsWord();

	/**
	 *
	 * @return
	 */
	void setIsWord(boolean isword);

	/**
	 * get HasChild varible
	 *
	 * @return boolean
	 */
	boolean getHasChild();

	/**
	 * Set hasChild property.
	 *
	 * @param boolean
	 */
	void setHasChild(boolean haschild);

	/**
	 * Add node as a child to current node.
	 *
	 * @param Node
	 *
	 */
	void addChild(Character key, Node node);

	/**
	 * Get child node of current Node.
	 *
	 * @param k
	 *            character
	 * @return Node
	 */
	Node getChildNode(char k);

	/**
	 * Get child nodes of current Node(To handle Umlaut Characters)
	 *
	 * @param k
	 *            character
	 * @return List of Node objects
	 */
	List<Node> getChildNodes(char k);


	/**
	 * To convert in to String of WordNode
	 *
	 * @return String
	 */
	String toString();
}
