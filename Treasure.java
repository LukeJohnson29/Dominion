/******************************
 * A treasure card in Dominion
 * 
 * @author Luke Johnson
 *
 *         A subclass of Card that represents a treasure card in Dominion
 */
public class Treasure extends Card
{
	private int worth; // Value of the card
	private String special; // Special of the card

	/************************************************
	 * 
	 * @param cardType
	 *           //Type of card passed to super class
	 * @param cardName
	 *           //Name of card passed to super class
	 * @param cost
	 *           //Cost of card passed to super class
	 * @param worth
	 *           //Value of card
	 * @param special
	 *           //Special of card
	 */
	public Treasure(String cardType, String cardName, int cost, int worth, String special)
	{
		super(cardType, cardName, cost);
		this.worth = worth;
		this.special = special;
	}

	/************************************************
	 * Default constructor
	 */
	public Treasure()
	{
		super();
		this.worth = 0;
		this.special = null;
	}

	/*************************************************
	 * Overridden toString() method to output the data members for a treasure card
	 */
	public String toString()
	{
		String cardInfo = getCardType() + "\n" + getCardName() + "\n" + "Cost: " + getCost() + "\n" + "Worth: " + worth
				+ "\n";

		return cardInfo;
	}

	// Getters and Setters
	public int getWorth()
	{
		return worth;
	}

	public void setWorth(int worth)
	{
		this.worth = worth;
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
