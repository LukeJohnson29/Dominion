/********************************************************
 * An interface to be used by linked lists
 * 
 * @author Luke Johnson
 * 
 *         This interface will be used by the cardList class
 *
 */
public interface Playable
{
	public abstract Object addToHead(Object o); // add a node to top of list

	public abstract Object moveNode(Object o, int index); // move a node

	public abstract void deleteNode(int index); // delete a node from list

	public abstract void shuffle(); // shuffle a linked list

	public abstract void printList(); // output the list
	
	public abstract boolean isEmpty();	//check if the list is empty

}
