import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Charts {
    private ArrayList<XYSeriesCollection> datasetsAnalog = new ArrayList<XYSeriesCollection>();
    private CombinedDomainXYPlot plot;
    private JFreeChart chart;
    private JFrame frame;
    private XYSeries tempSeries;
    private double timeStep = 0.001; // шаг дискретизации при 20 т. за период
    private double currentTime = 0.0;

    public Charts(String name) {
        plot = new CombinedDomainXYPlot(new NumberAxis("Время, сек"));
        chart = new JFreeChart(name, plot);
        chart.setBorderPaint(Color.white);
        chart.setBorderVisible(true);
        chart.setBackgroundPaint(Color.white);

        frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(1024, 768);
        frame.show();
    }

    /**
     * добавление аналогового сигнала
     *
     * @param name   - имя графика
     * @param number - порядковый номер сигнала
     */
    public void createAnalogChart(String name, int number) {
//        if(charts==null) charts = new Charts(nameGraph);

        XYSeriesCollection dataset = new XYSeriesCollection();
        NumberAxis rangeAxis = new NumberAxis(name);
        rangeAxis.setAutoRangeIncludesZero(false);
        XYPlot subplot = new XYPlot(dataset, null, rangeAxis, new StandardXYItemRenderer());
        subplot.setBackgroundPaint(Color.WHITE);
        plot.add(subplot);
        subplot.setWeight(7);
        datasetsAnalog.add(dataset);
    }

    /**
     * добавление дискретного сигнала
     * @param name - имя графика
     * @param number - порядковый номер сигнала
     */

    /**
     * îáàâëßåò â óêàçàííûé àíàëîãîâûé ãðàôèê íîâûé ñèãíàë
     *
     * @param name        - ìß àíàëîãîâîãî ñèãíàëà
     * @param chartNumber - îðßäêîâûé íîìåð ñèãíàëà
     * @param number      - îðßäêîâûé íîìåð
     */
    public void addSeries(String name, int chartNumber, int number) {
        XYSeries series = new XYSeries(name);
        series.add(0.0, 0.0);
        datasetsAnalog.get(chartNumber).addSeries(series);
    }

    /**
     * òðîèò íàëîãîâûé ñèãíàë íà ãðàôèêå
     *
     * @param chart  - îðßäêîâûé íîìåð ãðàôèêà
     * @param series - îðßäêîâûé íîìåð ñèãíàëà
     * @param data   - îáàâëßåìîå çíà÷åíèå (double)
     */
    public void addAnalogData(int chart, int series, double data) {
        tempSeries = (XYSeries) datasetsAnalog.get(chart).getSeries().get(series);
        currentTime = tempSeries.getMaxX() + timeStep;
        tempSeries.add(currentTime, data);
    }

    public void setTimeStep(double timeStep) {
        this.timeStep = timeStep;
    }


}