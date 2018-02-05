package readInesData;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.TableView.TableCell;
import javax.swing.text.TableView.TableRow;

@SuppressWarnings("serial")
public class mainWindow extends JFrame {

	final int ROW_COMBOBOX = 0;
	
	private JPanel contentPane;
	private static JTextField outputField;
	
	private JButton startButton;
	
	private JCheckBox outputinInputFolder;
	private DefaultListModel<String> InputListModel;
	
	private addInput InputFrame;
	
	private JTextArea console;
	private JScrollPane consoleSP;
	
	private mainWindow mainFrame = this;
	private JTable table;
	private ArrayList<JComboBox<String>> variableBoxes;
	private ArrayList<DataVariables> variables;
	
	private JTable table_1;
	
	public mainWindow() {
		setTitle("Dateitrennsystem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setMinimumSize(new Dimension(500, 500));
		setLocationRelativeTo(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setContinuousLayout(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JPanel inputOutputArea = new JPanel();
		splitPane.setLeftComponent(inputOutputArea);
		inputOutputArea.setLayout(new BorderLayout(0, 0));
		
		JSplitPane inputOutputSplit = new JSplitPane();
		inputOutputSplit.setContinuousLayout(true);
		inputOutputSplit.setResizeWeight(0.5);
		inputOutputArea.add(inputOutputSplit, BorderLayout.CENTER);
		
		JPanel outputPanel = new JPanel();
		inputOutputSplit.setRightComponent(outputPanel);
		outputPanel.setBackground(Color.WHITE);
		outputPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel mainOutputPanel = new JPanel();
		mainOutputPanel.setBackground(Color.WHITE);
		outputPanel.add(mainOutputPanel, BorderLayout.NORTH);
		mainOutputPanel.setLayout(new BorderLayout(0, 0));
		
		outputField = new JTextField();
		mainOutputPanel.add(outputField, BorderLayout.CENTER);
		outputField.setColumns(10);
		
		JButton button = new JButton("durchsuchen...");
		mainOutputPanel.add(button, BorderLayout.EAST);
		
		JLabel lblOutput = new JLabel(" Output:");
		mainOutputPanel.add(lblOutput, BorderLayout.NORTH);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				File f = new File(outputField.getText());
				if (f.exists()) fc.setCurrentDirectory(f);
				
				fc.showSaveDialog(null);
				if (fc.getSelectedFile() != null) outputField.setText(fc.getSelectedFile().getPath());
			}
		});
		
		JPanel inputPanel = new JPanel();
		inputOutputSplit.setLeftComponent(inputPanel);
		inputPanel.setBackground(Color.WHITE);
		inputPanel.setLayout(new BorderLayout(0, 0));
		
		InputFrame = new addInput();
		InputListModel = new DefaultListModel<String>();
		
		JList<String> InputList = new JList<String>(InputListModel);
		InputList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent event) {
				File[] inputs = getListFiles(InputListModel);
				
				if (outputinInputFolder!= null) {
					if (outputinInputFolder.isSelected() && InputList.getSelectedIndex() != -1) {
						File f = inputs[InputList.getSelectedIndex()];
						
						outputField.setText(f.getPath());
					} else if (InputList.getSelectedIndex() == -1) outputField.setText("");
				}
			}
		});
		InputList.setVisibleRowCount(3);
		JScrollPane InputListScroller = new JScrollPane(InputList);
		inputPanel.add(InputListScroller, BorderLayout.CENTER);
		
		outputinInputFolder = new JCheckBox("selber Ordner wie ausgew\u00E4hlte Datei");
		outputinInputFolder.setBackground(Color.WHITE);
		outputinInputFolder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (outputinInputFolder.isSelected()) {
					if (InputList.getSelectedIndex() != -1) outputField.setText(InputListModel.getElementAt(InputList.getSelectedIndex()));
					outputField.setEditable(false);
					button.setEnabled(false);
				} else {
					outputField.setEditable(true);
					button.setEnabled(true);
				}
			}
		});
		outputPanel.add(outputinInputFolder, BorderLayout.SOUTH);
		
		JLabel lblInput = new JLabel(" Input:");
		inputPanel.add(lblInput, BorderLayout.NORTH);
		
		JButton btnDurchsuchen = new JButton("Bearbeiten");
		btnDurchsuchen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<String> elementList = new ArrayList<String>(Arrays.asList(getListElements(InputListModel)));
				
				InputFrame.setElements(elementList);
				InputFrame.showOpenDialog(mainFrame);
			}
		});
		inputPanel.add(btnDurchsuchen, BorderLayout.SOUTH);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(1.0);
		splitPane_1.setContinuousLayout(true);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		JPanel settingsArea = new JPanel();
		splitPane_1.setLeftComponent(settingsArea);
		settingsArea.setLayout(new BorderLayout(5, 5));
		
		JPanel label = new JPanel();
		settingsArea.add(label, BorderLayout.NORTH);
		label.setLayout(new BorderLayout(0, 0));
		
		JLabel lblEinstellungen = new JLabel("Einstellungen:");
		label.add(lblEinstellungen);
		
		JButton button_1 = new JButton("?");
		button_1.setToolTipText("Dokumentation ueber regulaere Audruecke");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					URI url = new URI("https://www.kompf.de/java/regex.html");
					
					Desktop.getDesktop().browse(url);
				} catch (URISyntaxException | IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		label.add(button_1, BorderLayout.EAST);
		
		JPanel checkBoxArea = new JPanel();
		settingsArea.add(checkBoxArea, BorderLayout.EAST);
		checkBoxArea.setBackground(Color.WHITE);
		checkBoxArea.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		checkBoxArea.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel monthLength = new JPanel();
		panel.add(monthLength);
		monthLength.setLayout(new BorderLayout(0, 0));
		
		JSpinner spinner = new JSpinner();
		monthLength.add(spinner, BorderLayout.CENTER);
		spinner.setModel(new SpinnerNumberModel(30, 1, 31, 1));
		
		JLabel lblMonatslnge = new JLabel("Monatsl\u00E4nge:");
		monthLength.add(lblMonatslnge, BorderLayout.NORTH);
		
		JPanel monthBegin = new JPanel();
		panel.add(monthBegin);
		monthBegin.setLayout(new BorderLayout(0, 0));
		
		JLabel lblMonatsbegin = new JLabel("Monatsbegin:");
		monthBegin.add(lblMonatsbegin, BorderLayout.NORTH);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setToolTipText("Wochentag am Monatsbegin");
		comboBox.setMaximumRowCount(7);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"}));
		comboBox.setSelectedIndex(0);
		monthBegin.add(comboBox, BorderLayout.CENTER);
		
		JPanel generateDiagram = new JPanel();
		panel.add(generateDiagram);
		generateDiagram.setLayout(new BorderLayout(0, 0));
		
		JCheckBox chckbxDiagrammeGenerieren = new JCheckBox("Diagramme generieren");
		generateDiagram.add(chckbxDiagrammeGenerieren);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setContinuousLayout(true);
		splitPane_2.setResizeWeight(0.8);
		settingsArea.add(splitPane_2, BorderLayout.CENTER);
		
		JPanel tablePanel = new JPanel();
		splitPane_2.setLeftComponent(tablePanel);
		tablePanel.setLayout(new BorderLayout(0, 0));
		
		table = new JTable();
		int[] notReordering = new int[] {0};
		table.setColumnModel(new reorderingTableColumnModel(notReordering));
		table.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
			
			@Override
			public void columnSelectionChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void columnRemoved(TableColumnModelEvent e) {
				
			}
			
			@Override
			public void columnMoved(TableColumnModelEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void columnMarginChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void columnAdded(TableColumnModelEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});;
		table.setColumnSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"Tabellenkopf"},
				{"Tabellenwert (wiederholend)"},
			},
			new String[] {
				""
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return (column > 0);
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane tableScroller = new JScrollPane(table);
		tablePanel.add(tableScroller, BorderLayout.CENTER);
		
		JPanel tableSettings = new JPanel();
		tablePanel.add(tableSettings, BorderLayout.SOUTH);
		tableSettings.setLayout(new BorderLayout(0, 0));
		
		variables = new ArrayList<DataVariables>();
		
		JButton btnNeueSpalte = new JButton("neue Spalte");
		btnNeueSpalte.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				JComboBox<String> box = new JComboBox<String>();
				variableBoxes.add(box);
				updateVariableNames(variables, variableBoxes);
				
				model.addColumn("");
				TableColumn column = table.getColumnModel().getColumn(model.getColumnCount()-1);
				column.setCellEditor(new ComboBoxCellEditor(box, ROW_COMBOBOX, 1));
				
				
			}
		});
		tableSettings.add(btnNeueSpalte, BorderLayout.NORTH);
		
		JLabel lblTabelle = new JLabel("Tabelle");
		tablePanel.add(lblTabelle, BorderLayout.NORTH);
		
		JPanel variablePanel = new JPanel();
		splitPane_2.setRightComponent(variablePanel);
		variablePanel.setLayout(new BorderLayout(0, 0));
		
		table_1 = new JTable();
		table_1.setTableHeader(null);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				""
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		variablePanel.add(table_1);
		
		JPanel variableSettings = new JPanel();
		variablePanel.add(variableSettings, BorderLayout.SOUTH);
		variableSettings.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton addVariable = new JButton("hinzuf\u00FCgen");
		variableSettings.add(addVariable);
		
		JButton btnEntfernen = new JButton("entfernen");
		variableSettings.add(btnEntfernen);
		
		JLabel lblVariablen = new JLabel("Variablen");
		variablePanel.add(lblVariablen, BorderLayout.NORTH);
		variableBoxes = new ArrayList<JComboBox<String>>();
		
		JPanel startArea = new JPanel();
		
		splitPane_1.setRightComponent(startArea);
		startArea.setLayout(new BorderLayout(0, 0));
		
		console = new JTextArea();
		console.setEditable(false);
		console.setRows(3);
		
		consoleSP = new JScrollPane(console);
		startArea.add(consoleSP, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		startArea.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.setLayout(new BorderLayout(0, 0));
		
		startButton = new JButton("Start");
		buttonPanel.add(startButton);
		
		JButton clearConsole = new JButton ("leere Konsole");
		buttonPanel.add(clearConsole, BorderLayout.SOUTH);
		clearConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearConsole();
			}
		});
	}
	
	public void updateVariableNames(ArrayList<DataVariables> variables, ArrayList<JComboBox<String>> box) {
		if (variables != null && box != null) {
			for (JComboBox<String> cb : box) {
				cb.removeAllItems();
				
				for (int i = 0; i < variables.size(); i++) {
					cb.addItem(variables.get(i).getName());
				}
			}
		}
	}
	
	public static File[] convertArrayListToArray (ArrayList<File> f) {
		File[] files = new File[f.size()];
		
		for (int i = 0; i < files.length; i++) files[i] = f.get(i);
		
		return files;
	}
	
	public void clearConsole() {
		console.setText("");
	}
	
	public void printConsole(String nextLine) {
		console.setText(console.getText() + nextLine + "\n");
	}
	
	private static ArrayList<File> getFilesFromPath(String[] filePaths) {
		ArrayList<File> files = new ArrayList<File>();
		
		for (int i = 0; i < filePaths.length; i++) {
			files.add(new File(filePaths[i]));
		}
		
		return files;
	}
	
	private static File[] getListFiles(ListModel<String> model) {
		String[] paths = getListElements(model);
		File[] files = new File[paths.length];
		
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(paths[i]);
		}
		
		return files;
	}
	
	private static String[] getListElements (ListModel<String> model) {
		String[] str = new String[model.getSize()];
		
		for (int i = 0; i < model.getSize(); i++) {
			str[i] = model.getElementAt(i);
		}
		
		return str;
	}

	public void updateInput() {
		ArrayList<String> str = InputFrame.getElements();
		InputListModel.removeAllElements();
		
		for (String s : str) if (s!="") InputListModel.addElement(s);
	}
	
	private void enableAllChildren(Container c, boolean value) {
		for (Component comp : getAllComponents(c)) comp.setEnabled(value);
	}
	
	private static List<Component> getAllComponents(Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
	}
	
	public class reorderingTableColumnModel extends DefaultTableColumnModel {
		
		int[] notReordering;
		
		public reorderingTableColumnModel(int[] notReordering) {
			super();
			
			this.notReordering = notReordering;
		}
		
		@Override
		public void moveColumn(int columnIndex, int newIndex) {
			for (int i : notReordering) if (i==columnIndex) return;
			super.moveColumn(columnIndex, newIndex);
		}
	}
	
	public class ComboBoxCellEditor extends DefaultCellEditor {

		JComboBox<String> comboBoxes;
		JComponent mainEditor;
		int rowPosition;
		int startingColumnPosition;
		
	    /**
	     * Creates a new ComboBoxCellEditor.
	     * 
	     * @param comboBox the comboBox that should be used as the cell editor.
	     */
	    public ComboBoxCellEditor(final JComboBox<String> comboBox, int rowPosition, int startingColumnPosition) {
	    	super(comboBox);
	    	comboBoxes = comboBox;
	    	this.rowPosition = rowPosition;
	    	this.startingColumnPosition = startingColumnPosition;
	    }
	    
	    @Override
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	    	if (row == rowPosition && column-startingColumnPosition >= 0) {
	    		super.editorComponent = comboBoxes;
	    	}
	    	else {
	    		super.editorComponent = outputField;
	    	}
			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	    }
	}
}
