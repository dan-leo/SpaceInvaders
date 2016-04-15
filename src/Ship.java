
public class Ship {
	
	private double ship_width = 0.3;
	private double ship_height = 0.3;
	private double ship_x;
//	private double ship_y = -0.85;
	private double ship_y = -1 + ship_height/2;
	
	private double angle = 90;
	private double angular_velocity;
	private double max_angle = 180;
	private double min_angle = 0;
	
	private double horiz_velocity;
	
	private double x = 0;
	
	private double max_x = 1.0;
	private double min_x = -1.0;
	
	public Ship (int REFRESH_RATE_ms)
	{
		angular_velocity = 0.75 * REFRESH_RATE_ms;
		horiz_velocity = 0.0032 * REFRESH_RATE_ms;
	}
	
	public void draw_spacecraft()
	{
		this.ship_x = x;
		// StdDrawModified.picture(ship_x,  ship_y,  "spacecraft.png", ship_width, ship_height, angle - 90.0);
		StdDrawModified.picture(ship_x,  ship_y,  "fighter-01.png", ship_width, ship_height, angle - 90.0);
		StdDrawModified.setPenColor(StdDrawModified.WHITE);
//		StdDrawModified.setPenRadius(0.01);
//		StdDrawModified.point(ship_x, ship_y);
	}
	
	public double get_angle()
	{
		return angle;
	}
	
	public double get_x()
	{
		return ship_x;
	}
	
	public double get_y()
	{
		return ship_y;
	}
	
	public double get_ship_width()
	{
		return ship_width;
	}
	
	public double get_ship_height()
	{
		return ship_height;
	}
	
	public void move_left() {
		x -= horiz_velocity;
		if (x < min_x) {
			x = min_x;
		}
	}

	public void move_right() {
		x += horiz_velocity;
		if (x > max_x) {
			x = max_x;
		}
	}

	public void rotate_left() {
		angle += angular_velocity;
		if (angle > max_angle) {
			angle = max_angle;
		}
	}

	public void rotate_right() {
		angle -= angular_velocity;
		if (angle < min_angle) {
			angle = min_angle;
		}
	}
	
	public void set_angle(double angle)
	{
		this.angle = angle;
		if (angle < min_angle) {
			this.angle = min_angle;
		}
		if (angle > max_angle) {
			this.angle = max_angle;
		}
	}

}
