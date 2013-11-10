package smartorder.presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import smartorder.entities.FoodOrder;
import smartorder.entities.OrderDetail;
import smartorder.entities.Product;

public class SalesReportChart {
	public static final int LAST_7_DAYS = 7;
	public static final int LAST_30_DAYS = 30;
	public static final int YESTERDAY = 1;

	private DefaultCategoryDataset ordersTrend;
	private DefaultCategoryDataset salesTrend;
	private DefaultPieDataset productDistribution;
	private List<FoodOrder> orders;
	private int productID;

	private int parentWidth;

	public SalesReportChart(int productID, List<FoodOrder> orders,
			int parentWidth) {
		this.orders = orders;
		this.productID = productID;
		this.parentWidth = parentWidth;
	}

	public JPanel createReportPanel() {
		// create dataset use for 2 charts
		createDataset();

		// create charts
		ChartPanel ordersChart = new ChartPanel(createBarChart(ordersTrend,
				"Orders Trend", "Date", "Orders"));
		ChartPanel salesChart = new ChartPanel(createBarChart(salesTrend,
				"Sales Trend", "Date", "Sales"));
		ChartPanel pieChart = new ChartPanel(createPieChart(
				productDistribution, "Rate Distribution"));

		ordersChart.setBorder(new LineBorder(ManagementFrame.DEFAULT_COLOR));
		salesChart.setBorder(new LineBorder(ManagementFrame.DEFAULT_COLOR));

		// create bar chart container (panel)
		JPanel leftPanel = new JPanel(new GridLayout(2, 1));
		leftPanel.add(ordersChart);
		leftPanel.add(salesChart);
		// create pie chart container
		JPanel rightPanel = new JPanel(new BorderLayout());
		rightPanel.add(pieChart, BorderLayout.CENTER);

		JPanel chartPanel = new JPanel(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, rightPanel);
		split.setDividerLocation((parentWidth / 2));
		chartPanel.add(split, BorderLayout.CENTER);

		return chartPanel;
	}

	private JFreeChart createPieChart(PieDataset dataset, String title) {
		final JFreeChart chart = ChartFactory.createPieChart3D(title, // chart
																		// title
				dataset, // data
				true, // include legend
				true, false);

		// customize this chart...
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);

		return chart;
	}

	private JFreeChart createBarChart(CategoryDataset dataset, String title,
			String domainLabel, String rangeLabel) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(title, // chart
																	// title
				domainLabel, // domain axis label
				rangeLabel, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);

		// customize the domain axis...
		chart.getCategoryPlot().getDomainAxis()
				.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		// customize the range axis...
		if (rangeLabel.equals("Orders")) {
			final NumberAxis rangeAxis = (NumberAxis) chart.getCategoryPlot()
					.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			rangeAxis.setAutoRangeIncludesZero(true);
		}

		return chart;
	}

	/**
	 * initialize data sets
	 */
	private void createDataset() {
		ordersTrend = new DefaultCategoryDataset();
		salesTrend = new DefaultCategoryDataset();
		productDistribution = new DefaultPieDataset();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (FoodOrder fo : orders) {
			for (OrderDetail od : fo.getDetails()) {
				Product p = od.getProduct();
				String name = p.getProductName();

				// update data set of SalesTrend and OrdersTrend Chart
				if (p.getProductID() == productID) {
					double sale = od.getPrice() * od.getQuantity();
					try {
						ordersTrend.incrementValue(od.getQuantity(), name,
								sdf.format(fo.getOrderDate()));
						salesTrend.incrementValue(sale, name,
								sdf.format(fo.getOrderDate()));
					} catch (UnknownKeyException e) {
						ordersTrend.addValue(od.getQuantity(), name,
								sdf.format(fo.getOrderDate()));
						salesTrend.addValue(sale, name,
								sdf.format(fo.getOrderDate()));
					}
				}

				// update data set of Distribution chart
				try {
					double currentValue = productDistribution.getValue(name)
							.doubleValue();
					productDistribution.setValue(name,
							currentValue + od.getQuantity());
				} catch (UnknownKeyException e) {
					productDistribution.setValue(name, od.getQuantity());
				}
			}
		}
	}

}
