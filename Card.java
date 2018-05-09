/*********************************
 * A card in dominion
 * 
 * @author Luke Johnson
 * 
 *         Super class for a card in the game Dominion with 3 sub classes
 * 
 * 
 *
 */
public abstract class Card
{
	private String cardType; // Type of card
	private String cardName; // Name of card
	private int cost; // Price of the card

	/*************************************************************
	 * Default constructor
	 */
	public Card()
	{
		this.cardType = "junk";
		this.cardName = "junk";
		this.cost = -1;
	}

	/*************************************************************
	 * 
	 * @param cardType
	 *           //Type of card (Action, Treasure, or Victory)
	 * @param cardName
	 *           //Name of the card
	 * @param cost
	 *           //Price of the card
	 */
	public Card(String cardType, String cardName, int cost)
	{
		this.cardType = cardType;
		this.cardName = cardName;
		this.cost = cost;
	}

	// Getters and Setters
	public String getCardType()
	{
		return cardType;
	}

	public String getCardName()
	{
		return cardName;
	}

	public int getCost()
	{
		return cost;
	}

	public void setCardType(String cardType)
	{
		this.cardType = cardType;
	}

	public void setCardName(String cardName)
	{
		this.cardName = cardName;
	}

	public void setCost(int cost)
	{
		this.cost = cost;
	}
}
