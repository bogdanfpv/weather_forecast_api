import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.lang.Object;
import java.text.ParseException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Weather {
    public static void httpGET_AND_GRAPH(Map<String, String> map, String owm_api_key) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create("http://api.openweathermap.org/data/2.5/forecast?lat="+map.get("lat")+"&lon="+map.get("lon")+"&appid="+owm_api_key))
        .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray list = jsonObject.getJSONArray("list");
            List<Date> dates = new ArrayList<>();
            List<Double> temperatures = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String dt_txt = item.getString("dt_txt");
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt_txt);
                    dates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                double tempInKelvin = item.getJSONObject("main").getDouble("temp");
                double tempInCelsius = tempInKelvin - 273.15;
                temperatures.add(tempInCelsius);
            }

            TimeSeries series = new TimeSeries("Temperature");
            for (int i = 0; i < temperatures.size(); i++) {
                series.add(new Minute(dates.get(i)), temperatures.get(i));
            }
            TimeSeriesCollection dataset = new TimeSeriesCollection();
            dataset.addSeries(series);


            JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Prévision météo pour " + /*jsonObject.getJSONObject("city").get("name")*/ Geocode.city_name
            , "5 jours / 3 heures"
            , "Température"
            , dataset);

            XYPlot plot = (XYPlot) chart.getPlot();
            BasicStroke stroke = new BasicStroke(2.0f);
            plot.getRenderer().setSeriesStroke(0, stroke);
            plot.getRenderer().setSeriesStroke(0, stroke);
            plot.getRenderer().setSeriesPaint(0, Color.BLUE);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.GREEN);

            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame frame = new JFrame();
            frame.getContentPane().add(chartPanel, BorderLayout.CENTER);
            frame.setSize(640, 480);
            frame.setVisible(true);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static String countryCodeToEmoji(String code) {

        // offset between uppercase ASCII and regional indicator symbols
        int OFFSET = 127397;
    
        // validate code
        if(code == null || code.length() != 2) {
            return "";
        }
    
        //fix for uk -> gb
        if (code.equalsIgnoreCase("uk")) {
            code = "gb";
        }
    
        // convert code to uppercase
        code = code.toUpperCase();
    
        StringBuilder emojiStr = new StringBuilder();
    
        //loop all characters
        for (int i = 0; i < code.length(); i++) {
            emojiStr.appendCodePoint(code.charAt(i) + OFFSET);
        }
    
        // return emoji
        return emojiStr.toString();
    }
}