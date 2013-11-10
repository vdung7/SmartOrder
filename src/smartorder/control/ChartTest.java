package smartorder.control;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class ChartTest extends JFrame {

	public ChartTest() {
		setTitle("Test Chart (Using JFreeChart");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(500, 270));

		final CategoryDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final JFreeChart chart2 = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		final ChartPanel chartPanel2 = new ChartPanel(chart2);
		final JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(chartPanel);
		panel.add(chartPanel2);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(panel);
	}

	private CategoryDataset createDataset() {
		DefaultCategoryDataset res = new DefaultCategoryDataset();

		String product = "Pizza";

		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (int i = 0; i < 30; i++) {
			cal.add(Calendar.DATE, -1);
			Date date = cal.getTime();

			int value = (int) ((Math.random() * 10) + 1);

			res.setValue(value, product, sdf.format(date));
		}
		cal.add(Calendar.DATE, -1);
		Date date = cal.getTime();
		System.out.println(date);
		res.setValue(1, product, sdf.format(date));
		try {
			res.incrementValue(3, product, sdf.format(date));
		} catch (UnknownKeyException e) {
			res.setValue(1, product, sdf.format(date));
		}
		return res;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart("Chart Demo", // chart
																			// title
				"Date", // domain axis label
				"Order", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);
		chart.getCategoryPlot().getDomainAxis()
				.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		// customise the range axis...
		final NumberAxis rangeAxis = (NumberAxis) chart.getCategoryPlot()
				.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);

		return chart;
	}

	public static void main(String[] args) {
		new ChartTest().setVisible(true);
	}
}
