/*************************************************
 * A node object
 * 
 * @author Luke Johnson
 * 
 *         The nodes will hold card values and a pointer to the next card or null. This class will be used with a linked
 *         list to represent a hand and a deck.
 */
public class Node
{
	private Card card; // a card object held in the node
	private Node link; // points to the next node or to null

	/********************************************************
	 * Default Constructor
	 */
	public Node()
	{
		this.card = null;
		this.link = null;
	}

	/********************************************************
	 * Constructor with card parameter
	 * 
	 * @param card: a card object that will be held in the node
	 */
	public Node(Card card)
	{
		this.card = card;
		this.link = null;
	}

	// Getters and Setters
	public Card getCard()
	{
		return card;
	}

	public Node getLink()
	{
		return link;
	}

	public void setCard(Card card)
	{
		this.card = card;
	}

	public void setLink(Node link)
	{
		this.link = link;
	}
}
