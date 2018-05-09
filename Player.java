/************************************************************
 * A player
 * 
 * @author Luke Johnson
 * 
 *         This class represents one player in the game of Dominion.
 *
 */

public class Player
{
	private CardList hand; // player's hand
	private CardList deck; // player's deck
	private CardList discard; // player's discard pile
	private int buys;	//player's buys
	private int actions;	//player's actions
	private int coin;	//player's coin
	private boolean turn; // true/false for whether it's the player's turn or not
	private int vicPoints;

	/******************************************
	 * Constructor with hand, deck, discard, and turn parameters
	 * 
	 * @param hand:
	 *           player's hand
	 * @param deck:
	 *           player's deck to draw from
	 * @param discard:
	 *           player's discard pile
	 * @param turn:
	 *           true/false value for determines if it is player's turn to go
	 */
	public Player(CardList hand, CardList deck, CardList discard, boolean turn)
	{
		this.hand = hand;
		this.deck = deck;
		this.discard = discard;
		this.turn = turn;
	}

	/*************************************************************
	 * Constructor that allows for player to have initial deck and true/false value for player's turn or not.
	 * 
	 * @param deck:
	 *           player's deck/starting deck
	 * @param turn:
	 *           true/false to determine whether it is the player's turn
	 */
	public Player(CardList deck, boolean turn)
	{
		this.hand = new CardList();
		this.deck = deck;
		this.discard = new CardList();
		this.turn = turn;
	}

	/***************************************
	 * Default constructor
	 */
	public Player()
	{
		this.hand = new CardList();
		this.deck = new CardList();
		this.discard = new CardList();
		this.turn = false;
	}

	/*************************************
	 * Getters and Setters
	 */
	public CardList getHand()
	{
		return hand;
	}

	public void setHand(CardList hand)
	{
		this.hand = hand;
	}

	public CardList getDeck()
	{
		return deck;
	}

	public void setDeck(CardList deck)
	{
		this.deck = deck;
	}

	public CardList getDiscard()
	{
		return discard;
	}

	public void setDiscard(CardList discard)
	{
		this.discard = discard;
	}

	public boolean isTurn()
	{
		return turn;
	}

	public void setTurn(boolean turn)
	{
		this.turn = turn;
	}

	public int getBuys()
	{
		return buys;
	}

	public void setBuys(int buys)
	{
		this.buys = buys;
	}

	public int getActions()
	{
		return actions;
	}

	public void setActions(int actions)
	{
		this.actions = actions;
	}

	public int getCoin()
	{
		return coin;
	}

	public void setCoin(int coin)
	{
		this.coin = coin;
	}

	public int getVicPoints()
	{
		return vicPoints;
	}

	public void setVicPoints(int vicPoints)
	{
		this.vicPoints = vicPoints;
	}

}
