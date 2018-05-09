/*****************************************************
 * A pile of cards on the game board
 * 
 * @author Luke Johnson
 * 
 *         A class that has a set of cards from the Card class and Card's sub-classes
 *
 */
public class Pile
{
	private Card card; // a card object
	private int numOfCards; // number of cards in a pile

	/****************************************************
	 * Default Constructor. Creates default treasure card so that an error may be discovered
	 */
	public Pile()
	{
		this.card = new Treasure();
		this.numOfCards = 10;
	}

	/*****************************************************
	 * Constructor with two parameters
	 * 
	 * @param card
	 *           //a card object
	 * 
	 * @param numOfCards
	 *           //number of cards stored in a pile
	 * 
	 */
	public Pile(Card card, int numOfCards)
	{
		this.card = card;
		this.numOfCards = numOfCards;
	}

	/******************************************************
	 * Overridden toString method that returns the toString of whatever card it is called on.
	 */
	public String toString()
	{
		return card.toString();
	}

	// Getters and Setters
	public Card getCard()
	{
		return card;
	}

	public int getNumOfCards()
	{
		return numOfCards;
	}

	public void setCard(Card card)
	{
		this.card = card;
	}

	public void setNumOfCards(int numOfCards)
	{
		this.numOfCards = numOfCards;
	}
}
