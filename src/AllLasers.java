import java.awt.Image;
import java.io.File;
import java.util.ArrayList;


public class AllLasers {

	private LaserProjectile missile;
	private ArrayList<LaserProjectile> missiles;

	private Image laser_image = StdDrawModified.getImage("laser.png");

	int REFRESH_RATE_ms;

	int counter = 0;
	int between_lasers = 30;

	public AllLasers(int refresh_rate) {
		missiles = new ArrayList<LaserProjectile>();
		REFRESH_RATE_ms = refresh_rate;
	}

	// ship x, ship y, ship height and angle
	public void add_laser(double ship_x, double ship_y, double h, double angle, int level, boolean click)
	{
		int temp_between_lasers = between_lasers/level;
		if (temp_between_lasers < 1)
		{
			temp_between_lasers = 1;
		}
		
		if (counter == temp_between_lasers || click)
		{
			play_sound();
			for (int i = 0; i < level; i++)
			{
				if (level == 1)
				{
					double laser_x = ship_x + (h/2) * Math.cos(Math.toRadians(angle));
					double laser_y = ship_y + (h/2) * Math.sin(Math.toRadians(angle));
					missile = new LaserProjectile(REFRESH_RATE_ms, laser_x, laser_y, h, angle);
					missiles.add(missile);
					
				}
				else
				{
					double calc_angle = (angle - 15) + 30 * i / (level - 1);
					double laser_x = ship_x + (h/2) * Math.cos(Math.toRadians(calc_angle));
					double laser_y = ship_y + (h/2) * Math.sin(Math.toRadians(calc_angle));
					missile = new LaserProjectile(REFRESH_RATE_ms, laser_x, laser_y, h, calc_angle);
					missiles.add(missile);
				}
			}
			counter = 0;
		}
		click = false;
		counter++;
	}

	public void draw(double x)
	{
		// draws laser beams
		for (int i = 0; i < missiles.size(); i++) 
		{
			if (!missiles.get(i).draw_laser_beam(x, laser_image)) {
				missiles.remove(i); 
			}
		}
	}

	public ArrayList<LaserProjectile> get_all()
	{
		return missiles;
	}
	
	public void play_sound()
	{
		new Thread(
	            new Runnable() {
	                public void run() {
	                    try {
	                        // PLAY AUDIO CODE
	                    	File file = new File("laser_sounds/LASER-4.wav");
	                    	Sound.playSoundFile(file);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }).start();
	}
}
