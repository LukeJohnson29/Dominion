/****************************************
 * A victory card in Dominion
 * 
 * @author Luke Johnson
 *
 *         A subclass of Card that represents a victory card
 * 
 */
public class Victory extends Card
{
	private int victoryPoints; // victory points for instance of victory card
	private String special; // special for

	/*********************************************
	 * 
	 * @param cardType
	 *           //Card type passed to super class
	 * @param cardName
	 *           //Card name passed to super class
	 * @param cost
	 *           //Cost of card passed to super class
	 * @param victoryPoints
	 *           //Amount of victory points the card is worth
	 * @param special
	 *           //special for card
	 */
	public Victory(String cardType, String cardName, int cost, int victoryPoints, String special)
	{
		super(cardType, cardName, cost); // parent class constructor
		this.victoryPoints = victoryPoints;
		this.special = special;
	}

	/*********************************************
	 * Default constructor
	 */
	public Victory()
	{
		super();
		this.victoryPoints = 0;
		this.special = null;
	}

	/**********************************************
	 * Overridden to string method that prints victory card data members
	 */
	public String toString()
	{
		String cardInfo = getCardType() + "\n" + getCardName() + "\n" + "Cost: " + getCost() + "\n" + "Victory Points: "
				+ victoryPoints + "\n";
		return cardInfo;
	}

	// Getters and Setters
	public int getVictoryPoints()
	{
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints)
	{
		this.victoryPoints = victoryPoints;
	}

	public String getSpecial()
	{
		return special;
	}

	public void setSpecial(String special)
	{
		this.special = special;
	}
}
