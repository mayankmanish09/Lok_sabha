package assignment2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.filter.VisibilityFilter;
import prefuse.action.layout.AxisLabelLayout;
import prefuse.action.layout.AxisLayout;
import prefuse.controls.Control;
import prefuse.controls.ControlAdapter;
import prefuse.controls.ToolTipControl;
import prefuse.data.Table;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.query.ListQueryBinding;
import prefuse.data.query.NumberRangeModel;
import prefuse.data.query.RangeQueryBinding;
import prefuse.data.query.SearchQueryBinding;
import prefuse.render.AxisRenderer;
import prefuse.render.Renderer;
import prefuse.render.RendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.render.AbstractShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.UpdateListener;
import prefuse.util.ui.JFastLabel;
import prefuse.util.ui.JRangeSlider;
import prefuse.util.ui.JSearchPanel;
import prefuse.util.ui.UILib;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.visual.expression.VisiblePredicate;
import prefuse.visual.sort.ItemSorter;

public class plot_attendance extends JPanel {

	static Table tb=new Table();

	public static void main(String[] args) throws DataIOException {

		UILib.setPlatformLookAndFeel();

		JFrame f = gen();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	public static JFrame gen() throws DataIOException {
		// load the data
		CSVTableReader c = new CSVTableReader();
		tb = c.readTable("/inputdup.csv");
		int rows=tb.getRowCount();

		Table t = new Table();
		t.addColumn("Name", String.class);
		t.addColumn("Age", int.class);
		t.addColumn("Attendance", int.class);
		t.addColumn("Party", String.class);
		t.addColumn("State", String.class);
		t.addColumn("count", int.class);

		t.addRows(rows);
		for(int i=0;i<rows;i++){
			t.set(i, 0, tb.getString(i,0));
			t.set(i, 1, tb.getString(i,10));
			int temp=Integer.parseInt((tb.getString(i, 14).split("[%]+"))[0]);
			t.set(i, 2, temp);
			t.set(i, 3, tb.getString(i, 6));
			t.set(i, 4, tb.getString(i, 4));
			t.set(i, 5, i);
		}

		JFrame frame = new JFrame("ATTENDANCE of all MP's");
		frame.setContentPane(new plot_attendance(t));
		frame.pack();
		return frame;
	}

	// ------------------------------------------------------------------------

	private String m_title = "NvsS";
	private JFastLabel m_details;

	private Visualization m_vis;
	private Display m_display;
	private Rectangle2D m_dataB = new Rectangle2D.Double();
	private Rectangle2D m_xlabB = new Rectangle2D.Double();
	private Rectangle2D m_ylabB = new Rectangle2D.Double();

	public plot_attendance(Table t) {
		super(new BorderLayout());

		// --------------------------------------------------------------------
		// STEP 1: setup the visualized data

		final Visualization vis = new Visualization();
		m_vis = vis;

		final String group = "by_state";

		VisualTable vt = vis.addTable(group, t);

		// add a new column containing a label string showing
		// candidate name, party, state, age, and attendance
		vt.addColumn("label",
				"CONCAT([Name], ' ; Age: ', FORMAT([Age],0),' ; Attendance: ', FORMAT([Attendance],0),'%', ' ; ', [Party])");

		vis.setRendererFactory(new RendererFactory() {
			AbstractShapeRenderer sr = new ShapeRenderer(13);
			Renderer arY = new AxisRenderer(Constants.LEFT, Constants.TOP);
			Renderer arX = new AxisRenderer(Constants.CENTER, Constants.FAR_BOTTOM);

			public Renderer getRenderer(VisualItem item) {
				return item.isInGroup("ylab") ? arY :
					item.isInGroup("xlab") ? arX : sr;
			}
		});

		// --------------------------------------------------------------------
		// STEP 2: create actions to process the visual data

		// set up dynamic queries, search set

		SearchQueryBinding searchQ   = new SearchQueryBinding(vt, "State");
		SearchQueryBinding searchQ1   = new SearchQueryBinding(vt, "Party");

		// construct the filtering predicate
		AndPredicate filter = new AndPredicate(searchQ.getPredicate());
		filter.add(searchQ1.getPredicate());

		// set up the actions
		AxisLayout xaxis = new AxisLayout(group, "count", Constants.X_AXIS, VisiblePredicate.TRUE); 
		AxisLayout yaxis = new AxisLayout(group, "Attendance", Constants.Y_AXIS, VisiblePredicate.TRUE);

		yaxis.setRangeModel(new NumberRangeModel(0, 100, 0, 100));

		xaxis.setLayoutBounds(m_dataB);
		yaxis.setLayoutBounds(m_dataB);

		AxisLabelLayout ylabels = new AxisLabelLayout("ylab", yaxis, m_ylabB);
		AxisLabelLayout xlabels = new AxisLabelLayout("xlab", xaxis, m_xlabB);
		vis.putAction("xlabels", xlabels);

		int[] palette = new int[] {
				ColorLib.rgb(255,150,150),ColorLib.gray(0), 
				ColorLib.rgb(255,0,0),ColorLib.rgb(0,255,0),
				ColorLib.rgb(150,150,255),
				ColorLib.rgb(0,0,255),ColorLib.gray(200),
		};
		DataColorAction color = new DataColorAction(group, "Party",
				Constants.ORDINAL, VisualItem.STROKECOLOR, palette);

		int[] shapes = new int[]
				{ Constants.SHAPE_RECTANGLE, Constants.SHAPE_DIAMOND,Constants.SHAPE_STAR, Constants.SHAPE_ELLIPSE };
		DataShapeAction shape = new DataShapeAction(group, "Party", shapes);

		ActionList draw = new ActionList();
		draw.add(color);
		draw.add(shape);
		draw.add(xaxis);
		draw.add(yaxis);
		draw.add(ylabels);
		draw.add(new RepaintAction());
		vis.putAction("draw", draw);

		ActionList update = new ActionList();
		update.add(new VisibilityFilter(group, filter));
		update.add(xaxis);
		update.add(yaxis);
		update.add(ylabels);
		update.add(new RepaintAction());
		vis.putAction("update", update);

		UpdateListener lstnr = new UpdateListener() {
			public void update(Object src) {
				vis.run("update");
			}
		};
		filter.addExpressionListener(lstnr);

		// --------------------------------------------------------------------
		// STEP 3: set up a display and ui components to show the visualization

		m_display = new Display(vis);
		
		m_display.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		m_display.setSize(1700,900);
		m_display.setHighQuality(true);
		m_display.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				displayLayout();
			}
		});
		displayLayout();

		m_details = new JFastLabel(m_title);
		m_details.setPreferredSize(new Dimension(75,20));
		m_details.setVerticalAlignment(SwingConstants.BOTTOM);

		ToolTipControl ttc = new ToolTipControl("label");
		Control hoverc = new ControlAdapter() {
			public void itemEntered(VisualItem item, MouseEvent evt) {
				if ( item.isInGroup(group) ) {
					item.setFillColor(item.getStrokeColor());
					item.setStrokeColor(ColorLib.rgb(0,0,0));
					item.getVisualization().repaint();
				}
			}
			public void itemExited(VisualItem item, MouseEvent evt) {
				if ( item.isInGroup(group) ) {
					item.setFillColor(item.getEndFillColor());
					item.setStrokeColor(item.getEndStrokeColor());
					item.getVisualization().repaint();
				}
			}
		};
		m_display.addControlListener(ttc);
		m_display.addControlListener(hoverc);


		// --------------------------------------------------------------------        
		// STEP 4: launching the visualization

		this.addComponentListener(lstnr);

		// set up search box
		JSearchPanel searcher = searchQ.createSearchPanel();
		searcher.setLabelText("State: ");
		searcher.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));

		JSearchPanel searcher1 = searchQ1.createSearchPanel();
		searcher1.setLabelText("Party: ");
		searcher1.setBorder(BorderFactory.createEmptyBorder(5,5,5,0));

		// create dynamic queries
		Box radioBox = new Box(BoxLayout.X_AXIS);
		radioBox.add(Box.createHorizontalStrut(5));
		radioBox.add(searcher);
		radioBox.add(Box.createHorizontalGlue());
		radioBox.add(Box.createHorizontalStrut(5));
		radioBox.add(Box.createHorizontalStrut(16));

		Box radioBox1 = new Box(BoxLayout.X_AXIS);
		radioBox1.add(Box.createHorizontalStrut(5));
		radioBox1.add(searcher1);
		radioBox1.add(Box.createHorizontalGlue());
		radioBox1.add(Box.createHorizontalStrut(5));
		radioBox1.add(Box.createHorizontalStrut(16));

		vis.run("draw");
		vis.run("xlabels");

		add(m_display, BorderLayout.CENTER);
		add(radioBox, BorderLayout.NORTH);
		add(radioBox1, BorderLayout.SOUTH);
		UILib.setColor(this, ColorLib.getColor(255,255,255), Color.GRAY);
		UILib.setFont(radioBox, FontLib.getFont("Tahoma", 20));
		UILib.setFont(radioBox1, FontLib.getFont("Tahoma", 20));
		m_details.setFont(FontLib.getFont("Tahoma", 18));
	}

	public void displayLayout() {
		int paddingLeft = 15;
		int paddingTop = 15;
		int paddingRight = 30;
		int paddingBottom = 15;

		int axisWidth = 50;
		int axisHeight = 10;

		Insets i = m_display.getInsets();

		int left = i.left + paddingLeft;
		int top = i.top + paddingTop;
		int innerWidth = m_display.getWidth() - i.left - i.right - paddingLeft- paddingRight;
		int innerHeight = m_display.getHeight() - i.top - i.bottom - paddingTop- paddingBottom;

		m_dataB.setRect(left + axisWidth, top, innerWidth - axisWidth, innerHeight - axisHeight);
		m_xlabB.setRect(left + axisWidth, top + innerHeight - axisHeight, innerWidth - axisWidth, axisHeight);
		m_ylabB.setRect(left, top, innerWidth + paddingRight, innerHeight - axisHeight);

		m_vis.run("update");
		m_vis.run("xlabels");
	}

} // end of class ScatterPlot
