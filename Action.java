/**********************************
 * An action card in Dominion
 * 
 * @author Luke Johnson
 *
 *         A subclass of Card that represents an action card in Dominion
 */
public class Action extends Card
{
	private int addCard; // Amount of extra cards to draw
	private int addAction; // Amount of extra actions
	private int addBuy; // Amount of extra buys
	private int worth; // Value of the card (extra coin)
	private int victoryPoints; // Amount of victory points
	private String special; // Card's special

	/*************************************************************
	 * Constructor with parameters
	 * 
	 * @param cardType
	 *           //Card's type, passed to super constructor
	 * @param cardName
	 *           //Name of card, passed to super constructor
	 * @param cost
	 *           //Cost of the card, passed to super constructor
	 * @param worth
	 *           //Value of card
	 * @param victoryPoints
	 *           //Amount of victory points
	 * @param addCard
	 *           //Amount of extra cards to draw
	 * @param addAction
	 *           //Amount of extra actions
	 * @param addBuy
	 *           //Amount of extra buys
	 * @param special
	 *           //Card's special
	 */
	public Action(String cardType, String cardName, int cost, int worth, int victoryPoints, int addCard, int addAction,
			int addBuy, String special)
	{
		super(cardType, cardName, cost);
		this.addCard = addCard;
		this.addAction = addAction;
		this.addBuy = addBuy;
		this.worth = worth;
		this.victoryPoints = victoryPoints;
		this.special = special;
	}

	/*****************************************************************
	 * Default constructor
	 */
	public Action()
	{
		super();
		this.worth = 0;
		this.addCard = 0;
		this.addAction = 0;
		this.addBuy = 0;
		this.victoryPoints = 0;
		this.special = null;
	}

	/******************************************************************
	 * Overridden toString() method that outputs all of the data members of an action card
	 */
	public String toString()
	{
		String cardInfo = getCardType() + "\n" + getCardName() + "\n" + "Cost: " + getCost() + "\n" + "Worth: " + worth
				+ "\n" + "Victory Points: " + victoryPoints + "\n" + "Add Card(s): " + addCard + "\n" + "Add Action(s): "
				+ addAction + "\n" + "Add Buy(s): " + addBuy + "\n" + "Special: " + special + "\n";
		return cardInfo;
	}

	// Getters and Setters
	public int getAddCard()
	{
		return addCard;
	}

	public int getAddAction()
	{
		return addAction;
	}

	public int getAddBuy()
	{
		return addBuy;
	}

	public void setAddCard(int addCard)
	{
		this.addCard = addCard;
	}

	public void setAddAction(int addAction)
	{
		this.addAction = addAction;
	}

	public void setAddBuy(int addBuy)
	{
		this.addBuy = addBuy;
	}

	public int getWorth()
	{
		return worth;
	}

	public void setWorth(int worth)
	{
		this.worth = worth;
	}

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
