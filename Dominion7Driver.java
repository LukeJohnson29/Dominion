
/****************************************************
 * 
 *	@ Author Luke Johnson
 * 
 * Dominion 7
 * Due Date: 03/26/2018
 * Input Files: cards.txt
 * 
 * This program will simulate the card game of Dominion.
 * Currently this program:
 * -Plays the entire game
 * -Does not allow action card specials
 */
import java.util.*;
import java.io.*;

public class Dominion7Driver
{
	// static Scanner input = new Scanner(System.in);
	static Scanner reader, input; // reader for file IO, input for user input
	public static java.io.File inFileCards; // create inFileCards
	final static int NUM_OF_PILES = 35; // constant value for number of elements in the pile array

	public static void main(String[] args) throws IOException
	{
		inFileCards = new java.io.File("cards.txt"); // set inFileCards to cards.txt
		reader = new Scanner(inFileCards);
		input = new Scanner(System.in);
		boolean gameOver = false; // used to end game
		Pile[] cards = new Pile[NUM_OF_PILES]; // array of card piles to store cards
		int[] cardCounts = new int[] { 0, 0, 0 }; // array that will be used to keep track of how many cards are generated
																// {T, V, A}

		for (int i = 0; i < NUM_OF_PILES; i++)
		{
			cards[i] = new Pile(new JunkCard(), -1);
		}

		// call functions to initialize and sort the cards into layout order
		initializeCards(cards, cardCounts);
		System.out.println("GAME START\n\n\n");
		displayBoard(cards, cardCounts);

		// instantiate both players, player one starts
		Player player1 = initializePlayer(cards);
		Player player2 = initializePlayer(cards);
		player1.setTurn(true);
		player2.setTurn(false);

		// start game
		//for (int i = 0; i < 10; i++) // used for testing, allows 10 turns
		// loop executes while gameOver is false
		 while (!gameOver)	
		{
			// execute a turn for player 1, then check for game over
			if (player1.isTurn())
			{
				System.out.println("\n#####BEGIN PLAYER 1 TURN#####\n");
				turn(player1, cards, cardCounts);
				player2.setTurn(true);
			}
			// execute a turn for player 2, then check for game over
			else
			{
				System.out.println("\n#####BEGIN PLAYER 2 TURN#####\n");
				turn(player2, cards, cardCounts);
				player1.setTurn(true);
			}

			// check for game over
			gameOver = checkGameOver(cards);
		}

		// count both player's victory points
		player1.setVicPoints(endGame(player1));
		player2.setVicPoints(endGame(player2));
		
//		System.out.println("P1 DISCARD");	FOR DEBUGGING
//		player1.getDiscard().printList();
//		System.out.println("\n\n\n\nP2 DISCARD");
//		player2.getDiscard().printList();

		// End game output
		System.out.println("\n\n\nGAME OVER");
		if (player1.getVicPoints() > player2.getVicPoints())
		{
			System.out.println("Player 1 wins!");
		}
		else if (player2.getVicPoints() > player1.getVicPoints())
		{
			System.out.println("Player 2 wins!");
		}
		else
		{
			System.out.println("Tie game");
		}

		System.out.println("Player 1 Victory Points = " + player1.getVicPoints());
		System.out.println("Player 2 Victory Points = " + player2.getVicPoints());

	}

	/************************************************************************
	 * Method moves all of a players cards to discard pile so that their victory points may be counted and a winner
	 * determined.
	 * 
	 * @param player:
	 *           player whose cards are being dealt with
	 * @return vicPoints: player's total victory points
	 */
	public static int endGame(Player player)
	{
		int vicPoints = 0; // player's end game victory points
		int handCards = player.getHand().getNumNodes(); // cards in a player's hand
		int deckCards = player.getDeck().getNumNodes(); // cards in a player's deck

		// move hand to discard
		for (int i = 0; i < handCards; i++)
		{
			player.getHand().moveNode(player.getDiscard(), 0);
		}
		// move deck to discard
		for (int i = 0; i < deckCards; i++)
		{
			player.getDeck().moveNode(player.getDiscard(), 0);
		}
		// count a player's total victory points
		vicPoints = countValue(player, "vic points");

		return vicPoints;
	}

	/************************************************************************
	 * Method to check whether the game is over
	 * 
	 * @param cards:
	 *           Array of card piles. Each pile will be checked to see if it is empty
	 * @return gameOver: return value of gameOver (true or false)
	 */
	public static boolean checkGameOver(Pile[] cards)
	{
		boolean gameOver = false; // value for ending game
		int emptyPiles = 0; // number of empty piles on board

		// count empty piles
		for (int i = 0; i < NUM_OF_PILES; i++)
		{
			if (!cards[i].getCard().getCardType().equalsIgnoreCase("junk"))
			{
				if (cards[i].getNumOfCards() == 0)
				{
					emptyPiles++;
				}
			}
		}
		// More than 2 empty piles ends the game
		if (emptyPiles > 2)
		{
			gameOver = true;
		}

		return gameOver;
	}

	/************************************************************************
	 * Method executes one turn for a player
	 * 
	 * @param player:
	 *           player whose turn it is
	 * @param cards:
	 *           array of card piles
	 * @param cardCounts:
	 *           array used to output board
	 */
	public static void turn(Player player, Pile[] cards, int[] cardCounts)
	{
		// call actionPhase, buyPhase and cleanupPhase
		actionPhase(player, cards, cardCounts);
		buyPhase(player, cards, cardCounts);
		cleanupPhase(player);
		// set player's turn to false so that other player may go
		player.setTurn(false);
	}

	/************************************************************************
	 * Method used to complete one action phase for a player
	 * 
	 * @param player:
	 *           player whose turn it is
	 * @param cards:
	 *           array of card piles on the board
	 * @param cardCounts:
	 *           count of each type of card, used for board output
	 */
	public static void actionPhase(Player player, Pile[] cards, int[] cardCounts)
	{
		Node head = player.getHand().getHead(); // head pointer (easier to type)
		Node current = head; // set current to head
		Card playCard; // create a card pointer
		int index; // index of a hand that player will choose
		boolean done = false; // used as a condition for action phase loop
		char choice; // lets player choose
		int actionCount = 0; // number of action cards in a player's hand
		//int count = 0; // loop iterations, for debugging
		int addCards = 0; // cards player will draw
		boolean validInput = true;	//used to verify user input is in correct range
		boolean action = true;	//used to verify that user has selected an action card

		// Set players buys and actions to 1
		player.setActions(1);
		player.setBuys(1);

		// Count action cards in a player's hand
		for (int i = 0; i < player.getHand().getNumNodes(); i++)
		{
			if (current.getCard().getCardType().equalsIgnoreCase("action"))
			{
				actionCount++;
			}
			current = current.getLink();
		}

		// Sum treasure cards in player's hand
		player.setCoin(countValue(player, "coin"));

		System.out.println("*****BEGIN ACTION PHASE*****\n\n");
		System.out.println("*****YOUR HAND*****");
		player.getHand().printList();

		while (player.getActions() > 0 && !done && actionCount > 0)
		{
			head = player.getHand().getHead();
			current = head;
			
			// Let player select card to play
			// Make sure that chosen card is an action card
			// Output error statements if necessary
			System.out.println("Which card would you like to play?");
			System.out.println(player.getHand().getNumNodes());
			index = input.nextInt();
			
			//Check for valid range of input
			if(index < 0 || index >= player.getHand().getNumNodes() )
			{
				validInput = false;
				System.out.println("Selection out of range. Please choose again.");

			}
			//If range is valid, check that selected card is action
			if(validInput)
			{
				for (int i = 0; i < index; i++)
				{
					current = current.getLink();
					System.out.println(current);
				}
				if(!current.getCard().getCardType().equalsIgnoreCase("action"))
				{
					action = false;
					current = head;
					System.out.println("That is not an action card. Please choose again.");
				}
			}
			//if range is invalid or selected card is not an action, force player to choose again.
			//repeat until both conditions are met
			while (!validInput || !action)
			{
				if(!validInput || !action)
				{
					
					index = input.nextInt();
					if(index < 0 || index >= player.getHand().getNumNodes())
					{
						System.out.println("Selection out of range. Please choose again.");
						validInput = false;
					}
					else
					{
						validInput = true;
						for (int i = 0; i < index; i++)
						{
							current = current.getLink();
						}
						if(!current.getCard().getCardType().equalsIgnoreCase("action"))
						{
							validInput = false;
							current = head;
							System.out.println("That is not an action card. Please choose again.");
						}
						else
						{
							action = true;
						}
					}
				}
			}

			// set playCard to the card chosen by user
			// decrement actions
			playCard = current.getCard();
			player.setActions(player.getActions() - 1);

			// add additional cards to hand, recount coin, move index to remove correct card
			if (((Action) playCard).getAddCard() > 0)
			{
				int modifyIndex = 0; // used to count cards added to hand
				addCards += (((Action) playCard).getAddCard());
				for (int i = 0; i < addCards; i++)
				{
					drawOne(player);
					modifyIndex++;
				}
				player.setCoin(0);
				player.setCoin(countValue(player, "coin"));
				index += modifyIndex;
			}
			// Set actions to 0 or add additional actions
			if (((Action) playCard).getAddAction() > 0)
			{
				player.setActions(0 + ((Action) playCard).getAddAction());
			}

			// Add buys if playCard adds buys
			if (((Action) playCard).getAddBuy() > 0)
			{
				player.setBuys(player.getBuys() + ((Action) playCard).getAddBuy());
			}

			// Add coin if playCard adds coin
			if (((Action) playCard).getWorth() > 0)
			{
				player.setCoin(player.getCoin() + ((Action) playCard).getWorth());
			}

			// Decrement action card counts and move action card to discard
			actionCount--;
			player.getHand().moveNode(player.getDiscard(), index);

			// Output turn information
			System.out.println("\n*****YOUR HAND*****");
			player.getHand().printList();
			System.out.printf("%-20s %5s", "Actions remaining:", player.getActions() + "\n");
			System.out.printf("%-20s %5s", "Buys:", player.getBuys() + "\n");
			System.out.printf("%-20s %5s", "Coin: ", player.getCoin() + "\n");

			// Check to see if action phase is over
			if (player.getActions() > 0 && actionCount > 0)
			{
				System.out.println("Would you like to play another card? (y/n)");
				// verify input
				do
				{
					choice = input.next().toUpperCase().charAt(0);
					if (choice != 'Y' && choice != 'N')
					{
						System.out.println("Invalid selection. Please choose again.");
					}
				} while (choice != 'Y' && choice != 'N');
				// set done to true
				if (choice == 'N')
				{
					done = true;
				}
			}
		
		}
		System.out.println("\n\n*****END OF ACTION PHASE*****");
	}

	/******************************************************************************
	 * Method to draw one card
	 * 
	 * @param player:
	 *           player whose hand and deck are being dealt with
	 */
	public static void drawOne(Player player)
	{
		// draw one card if card is available in deck
		if (player.getDeck().getNumNodes() > 0)
		{
			player.getDeck().moveNode(player.getHand(), 0);
		}
		// shuffle discard, move to deck, draw one card
		else if (player.getDiscard().getNumNodes() > 0)
		{
			int discardSize = player.getDiscard().getNumNodes();
			player.getDiscard().shuffle();

			for (int i = 0; i < discardSize; i++)
			{
				player.getDiscard().moveNode(player.getDeck(), 0);
			}
			player.getDeck().moveNode(player.getHand(), 0);
		}
		else
		{
			System.out.println("Discard and Deck are both empty.");
		}
	}

	/******************************************************************************
	 * Method to draw from a players deck
	 * 
	 * @param player:
	 *           player whose hand and deck are being dealt with
	 */
	public static void draw(Player player)
	{
		while (player.getHand().getNumNodes() < 5 && player.getDeck().getNumNodes() > 0)
		{
			player.getDeck().moveNode(player.getHand(), 0);
		}
	}

	/*************************************************************************************
	 * Method to implement the cleanup phase in dominion. Discard a player's hand. Draw until player has 5 cards. If
	 * player's deck doesn't have 5 cards, shuffle discard pile and move it to deck. Continue drawing until player's hand
	 * has 5 cards.
	 * 
	 * @param player:
	 *           player whose turn it is
	 */
	public static void cleanupPhase(Player player)
	{
		System.out.println("\n\n*****BEGIN CLEANUP PHASE*****\n"); /* For debugging */
		// discard player's hand and draw a new hand
		discardHand(player);
		draw(player);

		// if player's hand does not have 5 cards, shuffle discard, move it to deck and continue drawing
		if (player.getHand().getNumNodes() < 5)
		{
			int discardSize = player.getDiscard().getNumNodes();
			player.getDiscard().shuffle();

			for (int i = 0; i < discardSize; i++)
			{
				player.getDiscard().moveNode(player.getDeck(), 0);
			}
			draw(player);
		}
		// reset player's actions, buys and coin
		player.setActions(0);
		player.setBuys(0);
		player.setCoin(0);
		System.out.println("*****END CLEANUP PHASE*****\n\n\n\n");
	}

	/************************************************************************
	 * Method used to count values from cards in the player's hand
	 * 
	 * @param player:
	 *           player whose card values will be counted
	 * @return value: sum of the counted values
	 */
	public static int countValue(Player player, String valueType)
	{
		int value = 0; // value that will be counted (coin, addBuys, addActions, etc.)
		int cardsInHand = player.getHand().getNumNodes(); // number of cards in player's hand
		int cardsInDiscard = player.getDiscard().getNumNodes(); // number of cards in player's discard pile
		Node handHead = player.getHand().getHead(); // get head of player's hand
		Node discardHead = player.getDiscard().getHead(); // head of player's discard

		// count coin value for the player
		if (valueType.equalsIgnoreCase("coin"))
		{
			Node current = handHead; // set current to handHead for traversing

			for (int i = 0; i < cardsInHand; i++)
			{
				if (current.getCard().getCardType().equalsIgnoreCase("treasure"))
				{
					value += ((Treasure) current.getCard()).getWorth();
				}
				current = current.getLink();
			}
		}
		// count player's victory points
		else if (valueType.equalsIgnoreCase("vic points"))
		{
			Node current = discardHead; // set current to discardHead for traversing
			// player.getDiscard().printList(); //for debug
			for (int i = 0; i < cardsInDiscard; i++)
			{
				if (current.getCard().getCardType().equalsIgnoreCase("victory"))
				{

					value += ((Victory) current.getCard()).getVictoryPoints();
				}
				else if (current.getCard().getCardType().equalsIgnoreCase("action"))
				{
					value += ((Action) current.getCard()).getVictoryPoints();
				}
				current = current.getLink();
			}
		}

		return value;
	}

	/************************************************************************
	 * Method used to complete one buy phase for a player
	 * 
	 * @param player:
	 *           player whose turn it is
	 * @param cards:
	 *           array representing the card piles on board
	 * @param cardCounts:
	 *           used for outputting cards in a chart like fashion
	 */
	public static void buyPhase(Player player, Pile[] cards, int[] cardCounts)
	{
		// int cardsInHand = player.getHand().getNumNodes(); // number of cards in a player's hand
		// int playerCoin = 0; //amount of coin that player may spend
		// int numBuys = 1; //number of buys that a player has
		char choice; // user's choice to continue buying or not
		int cardChoice; // user's choice of card
		boolean done = false; // true/false based on player's choice

		System.out.println("\n*****BEGIN BUY PHASE*****\n\n");
		// count coin in player's hand

		if (player.getCoin() == 0)
		{
			System.out.println("You have no coin.");
		}

		// loop continues executing until player has no buys, no coin or wishes to stop buying
		while (player.getBuys() > 0 && !done && player.getCoin() > 0)
		{
			displayBoard(cards, cardCounts);
			System.out.println("\n\nYOUR HAND");
			player.getHand().printList();
			System.out.println("\nYou have " + player.getBuys() + " buy(s) and " + player.getCoin() + "coin(s).");
			System.out.println("What card(s) would you like to buy?");

			// make sure all conditions are valid for player's choice of cards on board
			do
			{
				cardChoice = input.nextInt();

				if (cardChoice > NUM_OF_PILES || cardChoice < 0)
				{
					System.out.println("Choice is out of range.");
				}
				else if (cards[cardChoice].getCard().getCardType().equalsIgnoreCase("junk"))
				{
					System.out.println("Invalid choice.");
				}
				else if (player.getCoin() < cards[cardChoice].getCard().getCost())
				{
					System.out.println("You don't have enough coin. Please choose again");
				}
				else if (cards[cardChoice].getNumOfCards() == 0)
				{
					System.out.println("No cards left in that pile. Please choose again.");
				}
			} while (cardChoice > NUM_OF_PILES || cardChoice < 0
					|| cards[cardChoice].getCard().getCardType().equalsIgnoreCase("junk")
					|| player.getCoin() < cards[cardChoice].getCard().getCost() || cards[cardChoice].getNumOfCards() == 0);

			// move purchased card to player's discard pile and decrement number of cards in that pile
			// decrement player's buys and coin
			Node newNode = new Node(cards[cardChoice].getCard());
			player.getDiscard().addToHead(newNode);
			cards[cardChoice].setNumOfCards(cards[cardChoice].getNumOfCards() - 1);
			player.setBuys(player.getBuys() - 1);
			player.setCoin(player.getCoin() - cards[cardChoice].getCard().getCost());
			System.out.println("Added " + cards[cardChoice].getCard().getCardName() + " to discard pile.");

			// allow player the option of buying another card if they have coin and buys remaining
			if (player.getBuys() > 0 && player.getCoin() > 0)
			{
				System.out.println("Would you like to buy another card? (y/n)");
				do
				{
					choice = input.next().toUpperCase().charAt(0);

				} while (choice != 'Y' && choice != 'N');

				if (choice == 'N')
				{
					done = true;
				}
			}
			else
			{
				System.out.println("No buys remaining.");
			}
		}
		System.out.println("\n*****END BUY PHASE*****");
	}

	/***************************************************
	 * Method to discard all cards in a player's hand
	 * 
	 * @param player:
	 *           Player whose hand will be discarded
	 */
	public static void discardHand(Player player)
	{
		int numCards = player.getHand().getNumNodes(); // number of cards in a player's hand

		for (int i = 0; i < numCards; i++)
		{
			player.getHand().moveNode(player.getDiscard(), 0);
		}
	}

	/*********************************************************************
	 * Method used to initialize a player and give a player the correct starting cards in their deck.
	 * 
	 * @param cards:
	 *           array of cards passed in that the player's deck is generated from
	 * 
	 * @return player: return the initialized player to the call
	 */
	public static Player initializePlayer(Pile[] cards)
	{
		Player player = new Player(); // instantiate new player with default constructor
		int copperIndex = 0; // will be set to index of copper in cards
		int estateIndex = 0; // will be set to index of estate in cards

		// find index of copper and estate in cards
		for (int i = 0; i < cards.length; i++)
		{
			if (cards[i].getCard().getCardName().equalsIgnoreCase("copper"))
			{
				copperIndex = i;
			}
			else if (cards[i].getCard().getCardName().equalsIgnoreCase("estate"))
			{
				estateIndex = i;
			}
		}

		// put 7 copper and 3 estate into player's deck
		for (int i = 0; i < 7; i++)
		{
			if (i < 3)
			{
				Node newNode = new Node(cards[estateIndex].getCard());
				player.getDeck().addToHead(newNode);// player.getDeck().addToHead(cards[estateIndex].getCard());
				cards[estateIndex].setNumOfCards(cards[estateIndex].getNumOfCards() - 1);
			}
			Node newNode = new Node(cards[copperIndex].getCard());
			player.getDeck().addToHead(newNode);
			cards[copperIndex].setNumOfCards(cards[copperIndex].getNumOfCards() - 1);
		}
		// player.getDeck().printList();
		// System.out.println("\n\n");
		// shuffle player's deck and draw 5 cards
		player.getDeck().shuffle();

		for (int i = 0; i < 5; i++)
		{
			player.getDeck().moveNode(player.getHand(), 0);

		}
		return player;
	}

	/*****************************************************************************************
	 * Method that is used to display all of the cards on the table in the form of a chart. The number that is output to
	 * the left of the card is the number that will be used to select the card.
	 * 
	 * @param cards
	 *           //array of card piles that holds the card objects and the count of cards remaining
	 * @param cardCount
	 *           //array with three elements which represent how many of each card were instantiated {T, V, A}
	 * 
	 */
	public static void displayBoard(Pile[] cards, int[] cardCounts)
	{
		System.out.printf("%60s", "----------Cards Available on Board----------\n");
		for (int i = 0; i < 220; i++)
		{
			System.out.print("*");
		}
		System.out.println();
		System.out.printf(" %15s %15s %15s %12s %12s %12s %12s %12s %10s %20s", "TYPE", "NAME", "QUANTITY", "COST",
				"WORTH", "VIC PTS", "ADD CARDS", "ADD ACTIONS", "ADD BUYS", "SPECIAL" + "\n");
		for (int i = 0; i < 220; i++)
		{
			System.out.print("*");
		}
		System.out.println();
		for (int i = 0; i < NUM_OF_PILES; i++)
		{
			// only output card data for non junk cards
			if (!cards[i].getCard().getCardType().equalsIgnoreCase("junk"))
			{
				// print the treasure cards
				if (cards[i].getCard().getCardType().equalsIgnoreCase("Treasure"))
				{
					System.out.printf("%-3s %13s %14s %12s %13s %11s %11s %10s %13s %10s %21s", i + ")",
							cards[i].getCard().getCardType(), cards[i].getCard().getCardName(), cards[i].getNumOfCards(),
							cards[i].getCard().getCost(), ((Treasure) cards[i].getCard()).getWorth(), "-", "-", "-", "-",
							"-" + "\n");

					// print a new line on last treasure card output
					if ((i + 1) == cardCounts[0])
					{
						System.out.println();
					}
				}

				// output data for victory cards
				else if (cards[i].getCard().getCardType().equalsIgnoreCase("Victory"))
				{
					System.out.printf("%-3s %13s %14s %12s %13s %11s %11s %10s %13s %10s %20s", i + ")",
							cards[i].getCard().getCardType(), cards[i].getCard().getCardName(), cards[i].getNumOfCards(),
							cards[i].getCard().getCost(), "-", (((Victory) cards[i].getCard()).getVictoryPoints()), "-", "-",
							"-", "-", "\n");
					System.out.println();

					// output new line if on last victory card
					if ((i + 1) == cardCounts[1] + 10)
					{
						System.out.println();
					}
				}

				// output data for action cards
				else
				{
					System.out.printf("%-3s %13s %16s %10s %13s %11s %11s %10s %13s %10s %20s", i + ")",
							cards[i].getCard().getCardType(), cards[i].getCard().getCardName(), cards[i].getNumOfCards(),
							cards[i].getCard().getCost(), ((Action) cards[i].getCard()).getWorth(),
							((Action) cards[i].getCard()).getVictoryPoints(), ((Action) cards[i].getCard()).getAddCard(),
							((Action) cards[i].getCard()).getAddAction(), ((Action) cards[i].getCard()).getAddBuy(),
							((Action) cards[i].getCard()).getSpecial() + "\n", "\n\n");
				}
			}
		}
	}

	/******************************************************************************
	 * Method reads in the cards text file and initializes all of the cards in the game. It has one parameter, cards, and
	 * no return value.
	 * 
	 * @param cards
	 *           //array that represents the card piles in the game
	 * @param cardCounts
	 *           // array of 3 elements that will be used to keep track of how many cards are generated {T, V, A}
	 */
	public static void initializeCards(Pile[] cards, int[] cardCounts)
	{
		String cardType; // card's type
		String cardName; // card's name
		String special; // card's special
		String junk; // junk catcher for integer input
		boolean readNext = true; // condition used to continue or exit loop while cards are being read in
		int cost; // cost of card
		int worth; // value of card
		int victoryPoints;// card's victory points
		int addCard; // number of cards to add to hand
		int addAction; // actions added by card
		int addBuy; // number of buys added
		int numOfCards; // number of a specific card per game
		// int pileNumber = 0; // initial pile number
		// read in first card type and convert it to upper case
		cardType = reader.nextLine();
		cardType = cardType.toUpperCase();

		// Loop continues until readNext is false
		while (readNext)
		{
			// set card name
			cardName = reader.nextLine().toUpperCase();

			// create victory cards
			if (cardType.equalsIgnoreCase("Victory"))
			{
				// System.out.println("in victory"); //For debugging
				// read in data from text file and set variables
				numOfCards = reader.nextInt();
				cost = reader.nextInt();
				victoryPoints = reader.nextInt();
				junk = reader.nextLine();
				special = reader.nextLine();

				// create instance of victory card and add it to gameBoard[]
				Victory card = new Victory(cardType, cardName, cost, victoryPoints, special);
				// System.out.println(card.toString());
				cards[cardCounts[1] + 10] = new Pile(card, numOfCards);
				cardCounts[1]++;
			}

			// create treasure cards
			else if (cardType.equalsIgnoreCase("Treasure"))
			{
				// System.out.println("in treasure"); //For debugging
				// read in data from text file and set variables
				numOfCards = reader.nextInt();
				cost = reader.nextInt();
				worth = reader.nextInt();
				junk = reader.nextLine();
				special = reader.nextLine();

				// create instance of treasure card and add it to gameBoard[]
				Treasure card = new Treasure(cardType, cardName, cost, worth, special);
				// System.out.println(card.toString());
				cards[cardCounts[0]] = new Pile(card, numOfCards);
				cardCounts[0]++;
			}

			// Create action cards
			else if (cardType.equalsIgnoreCase("Action"))
			{
				// System.out.println("in action"); //For debugging

				// read in data from text file and set variables
				numOfCards = reader.nextInt();
				cost = reader.nextInt();
				worth = reader.nextInt();
				victoryPoints = reader.nextInt();
				addCard = reader.nextInt();
				addAction = reader.nextInt();
				addBuy = reader.nextInt();
				junk = reader.nextLine();
				special = reader.nextLine();

				// create instance of action card and add it to gameBoard[]
				Action card = new Action(cardType, cardName, cost, worth, victoryPoints, addCard, addAction, addBuy,
						special);
				// System.out.println(card.toString());
				cards[cardCounts[2] + 20] = new Pile(card, numOfCards);
				cardCounts[2]++;
			}

			// set card type
			cardType = reader.nextLine();

			// loop insures card type is valid and end of file has not been hit
			while ((!cardType.equalsIgnoreCase("action")) && (!cardType.equalsIgnoreCase("victory"))
					&& (!cardType.equalsIgnoreCase("treasure")) && (!cardType.equalsIgnoreCase("Done")))
			{
				// check if card type is junk input
				if (cardType.equals(""))
				{
					cardType = reader.nextLine();
				}
				// check for end of file
				if (cardType.equals("*"))
				{
					readNext = false;
					cardType = "done";
				}
				else
				{
					// do nothing
				}
			}
			// convert card type to upper case
			cardType = cardType.toUpperCase();

		}
	}
}

/************************************************************************
 * Part 1 Problems: Writing the code was not a problem at all. The problem that I had was that I forgot to catch the
 * junk input after reading in an integer from the file and it was skipping every other card. Once I figured out that I
 * was missing that it was an easy fix.
 * 
 * Part 2 Problems: None
 * 
 * Part 3 Problems: The only problem I had was that I had to change the way my output works and comment out my sort to
 * make the program work with the new specifications that we were given. I had only allowed for the original cards in
 * the text file.
 * 
 * Part 4 Problems: None
 * 
 * Part 5 Problems: None
 * 
 * Part 6 Problems: None
 * 
 * Part 7 Problems: None
 * 
 */