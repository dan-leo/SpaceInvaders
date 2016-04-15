import java.awt.Image;


public class Background {
	
	private int max = 32;
	private Image[] background;
	private double x = 0, y;
	private double default_y = 3;
	private int current;

	public Background() {
		background = new Image[max];
		for (int i = 0; i < background.length; i++)
		{
			background[i] = StdDrawModified.getImage("backgrounds/space-wallpapers-" + i + ".jpg");
		}
		current = 0;
		y = default_y;
	}
	
	private Image get(int index){
		return background[index];
	}
	
	public void draw()
	{
		StdDrawModified.picture(x, y, get(current), 2, 8);
	}
	
	// returns false if past three quarter mark
	public boolean update()
	{
		if (y > -3)
		{
			y -= 0.003;
		}
		return y > -3;
	}
	
	public void next()
	{
		current++;
		current %= max;
		y = default_y;
	}
	
	public boolean three_quarter_mark()
	{
		return y < -2.5;
	}

}
