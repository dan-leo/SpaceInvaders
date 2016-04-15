import java.awt.Image;

public class Enemies {

	private double enemy_width = 0.35;
	private double enemy_height = 0.35;

	private double starting_x;
	private double starting_y;
	private boolean[] alive;

	private int[] hitpoints;

	private int number_of_enemies;

	private double x_velocity;
	private double y_velocity;
	private boolean moving_horizontally = true;
	private double prev_starting_y;

	private double distance_between;

	private int level = 1;

	public Enemies(int nr_enemies, int index, int level)
	{
		number_of_enemies = nr_enemies;
		alive = new boolean[number_of_enemies];
		hitpoints = new int[number_of_enemies];
		distance_between = 1.5 / number_of_enemies;
		for (int i = 0; i < number_of_enemies; i++)
		{
			if (level < 8)
			{
				alive[i] = true;
				hitpoints[i] = level - 1;
			}
			else
			{
				alive[i] = true;
				hitpoints[i] = (int)(0.05 * (level - 1) + 6);
				System.out.println((int)(0.05 * (level - 1) + 6));
			}
		}
		/*x_velocity = 0.01;
		y_velocity = 0.0075 / 2;*/
		x_velocity = 0.00075;
		y_velocity = 0.00065;
		set_starting_x(index);
		starting_y = 1 - (enemy_height/2);
		starting_y += index * enemy_height;
		this.level = level;
	}

	public boolean check_if_starting_y_lower_than_original_y()
	{
		if (starting_y <= 1 - enemy_height/2)
		{
			return true;
		}
		return false;
	}

	private void set_starting_x(int index)
	{
		double diff_between_min_max = max_starting_x() - min_starting_x();
		if (index % 2 == 0)
		{
			starting_x = (Math.random() * diff_between_min_max)/2 + min_starting_x();
		}
		else
		{
			starting_x = (Math.random() * diff_between_min_max)/2 + diff_between_min_max/2 + min_starting_x();
		}
	}

	private double max_starting_x()
	{
		return 1 - ((number_of_enemies - 1) * distance_between + enemy_width/2);
	}

	private double min_starting_x()
	{
		return -1 + enemy_width/2;
	}

	public boolean draw_enemies(Ship ship, Image enemy_image)
	{
		double temp_x = starting_x;
		double temp_y = starting_y;

		if (moving_horizontally)
		{
			prev_starting_y = starting_y;
			for (int i = 0; i < number_of_enemies; i++ ){
				if (alive[i])
				{
					double e_x = temp_x + i * distance_between;

					StdDrawModified.picture(e_x, temp_y, enemy_image, enemy_width, enemy_height, 0);

					// this is to draw the health bars
					double percentage_health = (double)hitpoints[i] / level;

					if (percentage_health < 1)
					{
						double bar_x = e_x + (enemy_width/2) * (percentage_health - 1);
						double bar_y = temp_y + (enemy_height/2) * (1 - 0.05);
						StdDrawModified.setPenColor(StdDrawModified.GREEN);

						if (percentage_health < 0.25)
						{
							StdDrawModified.setPenColor(StdDrawModified.RED);
						}
						else if (percentage_health < 0.5)
						{
							StdDrawModified.setPenColor(StdDrawModified.YELLOW);
						}
						StdDrawModified.filledRectangle(bar_x, bar_y, percentage_health * enemy_width / 2, 0.05 * enemy_height / 2);
					}


					// StdDrawModified.picture(temp_x + i * distance_between, temp_y, "spaceship.gif", enemy_width, enemy_height);
				}
			}	
			starting_x += x_velocity;

			if (x_of_enemy(number_of_enemies - 1) + (enemy_width/2) > 1 || x_of_enemy(0) - (enemy_width/2) < -1)
			{
				x_velocity = -x_velocity;
				moving_horizontally = false;
			}
		}
		else
		{
			starting_y -= y_velocity;
			if (prev_starting_y - starting_y >= enemy_height)
			{
				moving_horizontally = true;
			}
			for (int i = 0; i < number_of_enemies; i++ ){
				if (alive[i])
				{
					StdDrawModified.picture(temp_x + i * distance_between, temp_y, enemy_image, enemy_width, enemy_height);
				}
			}	

		}
		return check_ship_enemy_collision(ship);
	}

	public double get_y_velocity()
	{
		return y_velocity;
	}

	public void set_vertical_velocity(double y_velocity)
	{
		this.y_velocity = y_velocity;
	}

	public double get_x_velocity()
	{
		return x_velocity;
	}

	public void set_horizontal_velocity(double x_velocity)
	{
		if (this.x_velocity > 0)
		{
			this.x_velocity = x_velocity;
		}
		else
		{
			this.x_velocity = -x_velocity;
		}
	}

	public int check_for_collision(double laser_x, double laser_y)
	{
		for (int enemies_ct = 0; enemies_ct < number_of_enemies; enemies_ct++)
		{
			if (alive[enemies_ct])
			{
				if (laser_y < 1)
				{
					if (laser_x > x_of_enemy(enemies_ct) - (distance_between/2) && laser_x < x_of_enemy(enemies_ct) + (distance_between/2))
					{
						if (laser_y > y_of_enemy(0) - (enemy_height/2) && laser_y < y_of_enemy(0) + (enemy_height/2))
						{
							if (hitpoints[enemies_ct] > 0)
							{
								hitpoints[enemies_ct]--;
								return 2;
							}
							else
							{
								alive[enemies_ct] = false;
								return 1;
							}
						}
					}
				}
			}
		}
		return 0;
	}

	public int check_ship_and_enemy_collision(double ship_x, double ship_y)
	{
		for (int enemies_ct = 0; enemies_ct < number_of_enemies; enemies_ct++)
		{
			if (alive[enemies_ct])
			{
				if (ship_y < 1)
				{
					if (ship_x > x_of_enemy(enemies_ct) - (distance_between/2) && ship_x < x_of_enemy(enemies_ct) + (distance_between/2))
					{
						if (ship_y > y_of_enemy(0) - (enemy_height/2) && ship_y < y_of_enemy(0) + (enemy_height/2))
						{
							alive[enemies_ct] = false;
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	private double x_of_enemy(int index)
	{
		return starting_x + distance_between * index;
	}

	private double y_of_enemy(int index)
	{
		return starting_y + distance_between * index;
	}

	public void reset(int index)
	{
		set_starting_x(index);
		starting_y = 1 - (enemy_height/2);
		starting_y += index * enemy_height;
		moving_horizontally = true;
		x_velocity = 0.00075;
		y_velocity = 0.00065;
		for (int i = 0; i < number_of_enemies; i++)
		{
			alive[i] = true;
		}
	}

	private boolean check_ship_enemy_collision(Ship ship)
	{
		int top_left_corner = check_ship_and_enemy_collision(ship.get_x() - ship.get_ship_width()/2, ship.get_y() + ship.get_ship_height()/2);
		int top_right_corner = check_ship_and_enemy_collision(ship.get_x() + ship.get_ship_width()/2, ship.get_y() + ship.get_ship_height()/2);
		if (top_left_corner != 0)
		{
			return true;
		}
		else if (top_right_corner != 0)
		{
			return true;
		}
		for (int i = 0; i < number_of_enemies; i++)
		{
			if (alive[i])
			{
				if (starting_y - enemy_height/2 < -1)
				{
					alive[i] = false;
					return true;
				}
			}
		}
		return false;
	}

	public boolean all_dead()
	{
		for (int i = 0; i < number_of_enemies; i++)
		{
			if (alive[i])
			{
				return false;
			}
		}
		return true;
	}

	public boolean moving_downwards()
	{
		return !moving_horizontally;
	}
}
