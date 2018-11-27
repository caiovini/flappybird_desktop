package helper;

import javax.swing.*;
import java.awt.*;

public class Histogram extends JPanel {
  /**
    * 
   */
	private static final long serialVersionUID = 1L;

  private float[] count;
  private String[] description;
  private boolean showAsFloat;

  /** Set the count and display histogram */
  public Histogram(float[] count , String[] description , boolean showAsFloat , String chartName) {
	  
    this.count = count;
    this.description = description;
    this.showAsFloat = showAsFloat;
    this.add(createLabels(chartName));
    this.repaint();
    this.setVisible(true);
  }
  
  private JLabel createLabels(String content) {
		
	JLabel labels = new JLabel(content);
	labels.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 40));
	labels.setForeground(Color.BLACK);
	return labels;
  }

  @Override
  protected void paintComponent(Graphics g) {
    if (count == null) return; // No display if count is null

    super.paintComponent(g);

    // Find the panel size and bar width and interval dynamically
    int width = getWidth();
    int height = getHeight();
    int interval = (width - 40) / count.length;
    int individualWidth = (int)(((width - 40) / 24) * 0.60);

    // Find the maximum count. The maximum count has the highest bar
    float maxCount = 0;
    for (int i = 0; i < count.length; i++) {
      if (maxCount < count[i])
    	  maxCount = count[i];
    }

    // x is the start position for the first bar in the histogram
    int x = 30;

    // Draw a horizontal base line
    g.drawLine(10, height - 45, width - 10, height - 45);
    for (int i = 0; i < count.length; i++) {
      // Find the bar height
      int barHeight =
        (int)(((double)count[i] / (double)maxCount) * (height - 55));

      // Display a bar (i.e. rectangle)
      g.drawRect(x, height - 45 - barHeight, individualWidth,
        barHeight);

      // Display a letter under the base line
      g.drawString(description[i], x, height - 30);
      
      if(showAsFloat) {
    	  
    	  g.drawString(String.format("%.2f",  count[i]) , x, height - 45 - barHeight );
    	  
      }else {
    	  
    	  g.drawString(String.valueOf((int)(count[i])) , x, height - 45 - barHeight );
      }

      // Move x for displaying the next character
      x += interval;
    }
  }

  @Override 
  public Dimension getPreferredSize() {
    return new Dimension(300, 300);
  }
}
