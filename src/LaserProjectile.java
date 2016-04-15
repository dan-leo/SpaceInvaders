import java.awt.Image;

public class LaserProjectile 
{
	private double initial_laser_pos = -0.675;
	private double laser_y = initial_laser_pos;
	private double laser_velocity = 0;
	private boolean laser_is_in_motion = true;
	private double laser_x = 0;
	private double angle;
	
//	private double temp_ship_x, temp_ship_y;
    

	// ship x, ship y, ship height and angle
	public LaserProjectile(double refresh_rate, double laser_x, double laser_y, double h, double angle)
	{
		// System.out.println(x + " " + y);
		laser_velocity = 0.0032 * refresh_rate;
//		laser_x = x + (y / 2) * Math.cos(angle);
//		laser_y = -1 + (y/2) * (1 + Math.sin(angle)) ;
		
		// laser_x = ship_x + (h/2) * Math.cos(Math.toRadians(angle));
		// laser_y = ship_y + (h/2) * Math.sin(Math.toRadians(angle));
		
		// System.out.println(Math.sqrt(Math.pow(laser_x - x, 2) + Math.pow(laser_y - y, 2)));
		
		this.laser_x = laser_x;
		this.laser_y = laser_y;
		this.angle = angle;
		
//		temp_ship_x = ship_x;
//		temp_ship_y = ship_y;
	}

	public boolean increment_laser_pos(double angle) 
	{
		laser_y = laser_y + laser_velocity * Math.sin(Math.toRadians(angle));
		laser_x = laser_x + laser_velocity * Math.cos(Math.toRadians(angle));

		if (laser_y > 1 + 0.055 || laser_x > 1 + 0.055 || laser_x < -1 - 0.055)
		{
			laser_is_in_motion = false;
		}
		else
		{
			laser_is_in_motion = true;
		}
		return laser_is_in_motion;
	}
	

	public boolean draw_laser_beam(double x, Image laser_image)
	{
		if (!increment_laser_pos(angle)) return false;
		// StdDrawModified.setPenColor(StdDrawModified.CYAN);
		// StdDrawModified.filledRectangle(laser_x, laser_y, 0.01, 0.05);
		// StdDrawModified.picture(laser_x, laser_y, "laser.png", (double)122/1500, (double)281/1500, angle - 90);
		StdDrawModified.picture(laser_x, laser_y, laser_image, (double)122/1500, (double)281/1500, angle - 90);
		
//		StdDrawModified.setPenColor(StdDrawModified.YELLOW);
//		StdDrawModified.setPenRadius(0.01);
//		StdDrawModified.point(temp_ship_x, temp_ship_y);
		
		return laser_is_in_motion;
	}
	
	public double get_laser_x()
	{
		return laser_x;
	}
	
	public double get_laser_y()
	{
		return laser_y;
	}
}