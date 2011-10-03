import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;

/**  Skeleton for Mahjongg2D. 
 *   GUI has a menu bar, a status area, and a 2d playing area.
 *   The GUI will display the game and handle user interaction. 
 * @author J. Dalbey
 * @version 9/24/2011
*/
public class Mahjongg2D extends JFrame implements ActionListener
{
    /* Main components of the GUI */
    // DO NOT CHANGE ANY OF THE GUI COMPONENT DECLARATIONS IN THIS SECTION
    private String[] columns = {"", "", "", "", "", "", "", "", "", "", "", "", };
    private JTable table;
    private JMenuBar menuBar;
    private JMenu mnuGame;
    private JMenuItem[] mnuItems;    
    private JLabel lblStatus = new JLabel();
    private ImageIcon background;

    /* The game board */
    private Object[][] myBoard; 
	private static final int kBoardWidth = 12;
	private static final int kBoardHeight = 8;

    /* Square dimensions in pixels */
    private static final int kTileWidth = 58;
    private static final int kTileHeight = 78;

    
    /** Create a GUI.
     * Will use the System Look and Feel when possible.
     */
    public Mahjongg2D()
    {
        super();
        try
        {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }

    }


    /** Place all the Swing widgets in the frame of this GUI.
     * @post the GUI is visible.  
     */
    public void layoutGUI()
    {
        loadImages();
        newGame();
        table = new JTable(this.myBoard, this.columns)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer( renderer, row, column);
                // We want renderer component to be
                // transparent so background image is visible
                if ( c instanceof JComponent)
                    ((JComponent)c).setOpaque(false);
                return c;
            }

            // Override paint so as to show the table background
            public void paint( Graphics g )
            {
                // paint an image in the table background
                if (background != null)
                {
                    g.drawImage( background.getImage(), 0, 0, null, null );
                }
                // Now let the paint do its usual work
                super.paint(g);
            }
            
            // Make the table cells not editable
            public boolean isCellEditable(int row,int column)
            {  
                return false;  
            }              
        }
        ; // end table def
       
        TableColumn column = null;
        // Does the board exist?
        if (this.myBoard != null)
        {
            // Set the dimensions for each column in the board to match the image */
            for (int index = 0; index < kBoardWidth; index++)
            {
                column = table.getColumnModel().getColumn(index);
                column.setMaxWidth(kTileWidth);
                column.setMinWidth(kTileWidth);
            }
        }

        // Define the layout manager that will control order of components
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        // Create the menu options
        layoutMenus();
        
        // Create a panel for the status information
        JPanel statusPane = new JPanel();
        statusPane.add(lblStatus);
        lblStatus.setName("Status");
        statusPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(statusPane);

        // Define the characteristics of the table that shows the game board        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setRowHeight(kTileHeight);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(table);

        // Define the mouse listener that will handle player's clicks.
        table.addMouseListener(myMouseListener);
        
        // And handle window closing events
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        }
        );

    } // end layout
    
    private void layoutMenus()
    {
        // Add a menubar
        menuBar = new javax.swing.JMenuBar();
        mnuGame = new JMenu("Game");     
        menuBar.add(mnuGame);
        mnuItems = new JMenuItem[9];  // allocate space for 9 menu items      
        
        // Create the Restart menu item
        mnuItems[0] = new JMenuItem("Restart");
        mnuItems[0].setAccelerator(KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        mnuItems[0].addActionListener(this);
        mnuGame.add(mnuItems[0]);

        setJMenuBar(menuBar);   // tell the frame which menu bar to use
    }

    /* Listener to respond to mouse clicks on the table */
    private MouseAdapter myMouseListener = new MouseAdapter()
    {
        public void mouseReleased(MouseEvent ev)
        {
            int col = table.getSelectedColumn();
            int row = table.getSelectedRow();
            // call methods to handle player's click
            repaint();
        }
    };
    
    protected void loadImages()
    {
        // load background image
        background = new ImageIcon( 
                    Toolkit.getDefaultToolkit().getImage(
                        this.getClass().getResource("img/" + "bkgd.jpg")));
        // Load tile images here
    }

	protected void newGame()
	{
		this.myBoard = new Tile[this.kBoardWidth][this.kBoardHeight];
		for (int width = 0; width < this.kBoardWidth; width++)
		{
			for (int height = 0; height < this.kBoardHeight; height++)
			{
				this.myBoard[width][height] = new Tile(Tile.Suit.Bamboo, 1);
			}
		}
	}

    
    /** Handle button clicks
     * @param e The result of what was clicked
     */
    public void actionPerformed(ActionEvent e) 
    {
        // Does the user want to restart the current game?
        if ("Restart".equals(e.getActionCommand()))
        {
            // call restart method
        }
        repaint();
    }
   
    // Local main to launch the GUI
    public static void main(String[] args)
    {
        // Create the GUI 
        Mahjongg2D frame = new Mahjongg2D();

        frame.layoutGUI();   // do the layout of widgets
               
        // Make the GUI visible and available for user interaction
        frame.pack();
        frame.setVisible(true);
    }
    
}  // end class

class Tile
{
	public enum Suit {Bamboo, Dots, Characters};

	private Suit suit;
	private int rank;

	public Tile(Suit suit, int rank)
	{
		this.suit = suit;
		this.rank = rank;
	}
}
