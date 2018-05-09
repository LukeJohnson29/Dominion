/*******************************************************
 * A linked list
 * 
 * @author Luke Johnson
 * 
 *         This class will implement playable and will be used to link nodes together to form a player's hand, discard
 *         pile and deck.
 */
public class CardList implements Playable
{

	private Node head; // head of a linked list
	private int numNodes; // number of nodes in a list

	/********************************
	 * Default Constructor
	 */
	public CardList()
	{
		this.head = null;
		this.numNodes = 0;
	}

	/****************************************************
	 * Constructor with parameters
	 * 
	 * @param head
	 *           //node passed in as head of list
	 * @param numNodes
	 *           //integer value for number of nodes in the list
	 */
	public CardList(Node head, int numNodes)
	{
		this.head = head;
		this.numNodes = numNodes;
	}


	/*********************************************************
	 * Method used to add a node to either a player's hand or deck
	 * 
	 * @param o:
	 *           Node being added to the head of a list
	 */
	@Override
	public Node addToHead(Object o)
	{
		Node newNode = ((Node)o); 
		// increment count of nodes and add a card to the top
		//Node newNode = new Node((Card) o);
		newNode.setLink(this.head);
		
		head = newNode;
		this.numNodes++;

		return head;
	}

	/*********************************************************
	 * Method used to discard a card from the player's hand to their discard pile
	 * 
	 * @param toList:
	 *           card list that a card will be moved to
	 * @param index:
	 *           index of the card to be moved from a list
	 */
	@Override
	public Node moveNode(Object toList, int index)
	{
		if (!this.isEmpty() && index < this.numNodes)
		{
			Node current = this.head;
			Node previous = current;

			if (index == 0)
			{
				this.head = current.getLink();
			}
			else
			{
				// traverse the list until the current is at the desired index

				for (int i = 0; i < index; i++)
				{
					previous = current;
					current = current.getLink();
				}
			}
			// point previous to current's link and decrement numNodes
			previous.setLink(current.getLink());
			this.numNodes--;

			// move current to a different list
			((CardList) toList).addToHead(current);
			

			return current;
		}
		else
		{
			System.out.println("Selection is not vaild or list is empty");
			return null;
		}

	}

	/******************************************************
	 * Method used to permanently delete a node
	 * 
	 * @param index:
	 *           index of the card in a player's hand that they wish to destroy
	 */
	@Override
	public void deleteNode(int index)
	{
		if (!this.isEmpty())
		{
			Node current = this.head;
			Node previous = current;

			if (index > this.numNodes)
			{
				System.out.println("Invalid selection.");
				return;
			}

			// traverse the list until current is at the desired index
			for (int i = 0; i < index; i++)
			{
				previous = current;
				current = current.getLink();
			}

			// point previous to current's link and decrement numNodes
			previous.setLink(current.getLink());
			this.numNodes--;

			System.out.println("\n" + current.getCard().getCardType() + " " + current.getCard().getCardName()
					+ " was deleted permanently! \n");
		}
		else
		{
			System.out.println("NO CARDS");
		}

	}

	/*************************************************************
	 * Method used to output a player's hand in the form of a chart
	 * 
	 * @param hand:
	 *           player's hand
	 */
	@Override
	public void printList()
	{
		if (!this.isEmpty())
		{
			// System.out.println("-------Cards in Hand-------");
			Node current = this.head;
			for (int i = 0; i < 220; i++)
			{
				System.out.print("*");
			}
			System.out.println();
			System.out.printf(" %15s %15s %12s %12s %12s %12s %12s %10s %20s", "TYPE", "NAME", "COST", "WORTH", "VIC PTS",
					"ADD CARDS", "ADD ACTIONS", "ADD BUYS", "SPECIAL" + "\n");
			for (int i = 0; i < 220; i++)
			{
				System.out.print("*");
			}
			System.out.println();
			// traverse entire list
			for (int i = 0; i < this.numNodes; i++)
			{
				// output data for any treasure cards in hand
				if (current.getCard().getCardType().equalsIgnoreCase("Treasure"))
				{
					System.out.printf("%-3s %13s %14s %10s %11s %11s %11s %13s %10s %21s", i + ")",
							current.getCard().getCardType(), current.getCard().getCardName(), current.getCard().getCost(),
							((Treasure) current.getCard()).getWorth(), "-", "-", "-", "-", "-");
				}

				// output data for any victory cards in hand
				else if (current.getCard().getCardType().equalsIgnoreCase("Victory"))
				{
					System.out.printf("%-3s %13s %14s %10s %11s %11s %11s %13s %10s %21s", i + ")",
							current.getCard().getCardType(), current.getCard().getCardName(), current.getCard().getCost(), "-",
							(((Victory) current.getCard()).getVictoryPoints()), "-", "-", "-", "-", "\n");
				}
				// output data for any action cards in hand
				else
				{
					System.out.printf("%-3s %13s %16s %8s %11s %11s %11s %13s %10s  %20s", i + ")",
							current.getCard().getCardType(), current.getCard().getCardName(), current.getCard().getCost(),
							((Action) current.getCard()).getWorth(), ((Action) current.getCard()).getVictoryPoints(),
							((Action) current.getCard()).getAddCard(), ((Action) current.getCard()).getAddAction(),
							((Action) current.getCard()).getAddBuy(), ((Action) current.getCard()).getSpecial());
				}

				current = current.getLink();
				System.out.println();
			}
		}

		else
		{
			System.out.println("NO CARDS");
		}
	}

	/**********************************************************
	 * Method used to shuffle a CardList
	 */
	@Override
	public void shuffle()
	{
		if (!this.isEmpty())
		{
			Node current = this.head; // node value used to traverse the list, starting at the head
			Node previous = current; // node value used to traverse the list, starting at the head
			int shuffleCard; // card that will be randomly selected to be moved

			// outer loop executes (numNodes * 2) times
			// inner loop used to traverse the list until it reaches a node based on a random number
			// ending node is moved to head of list
			for (int i = 0; i < this.numNodes * 2; i++)
			{
				// get random number and make sure that it is not going to be head
				do
				{
					shuffleCard = (int) (Math.random() * this.numNodes);
					// System.out.println("Random number is " + shuffleCard + "\n\n"); /*for debugging*/
				} while (shuffleCard == 0);

				// traverse list until on correct node
				for (int j = 0; j < shuffleCard; j++)
				{
					previous = current;
					current = current.getLink();
				}
				// move node to head of list
				previous.setLink(current.getLink());
				current.setLink(head);
				head = current;
				// this.printList(); /*for debugging*/
			}
			return;
		}
	}

	/**********************************************
	 * Method to check if a card list is empty
	 * 
	 * @return: returns true if list is empty or false if it is not
	 */
	public boolean isEmpty()
	{
		return this.numNodes == 0;
	}

	/***********************************************************************/

	// Getters and Setters
	public Node getHead()
	{
		return head;
	}

	public int getNumNodes()
	{
		return numNodes;
	}

	public void setHead(Node head)
	{
		this.head = head;
	}

	public void setNumNodes(int numNodes)
	{
		this.numNodes = numNodes;
	}

}
