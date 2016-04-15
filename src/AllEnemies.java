import java.awt.Image;
import java.io.File;
import java.util.ArrayList;


public class AllEnemies {

	private ArrayList<Enemies> rows_of_enemies;
	private int initial_total_columns_of_enemies = 5;
	private int total_rows_of_enemies = 1;
	private int total_columns_of_enemies = 5;

	private double vertical_velocity;
	private double horizontal_velocity;

	private double initial_vertical_velocity;
	private double initial_horizontal_velocity;

	private long enemies_killed;
	private long total_enemies_killed;

	private int level = 1;                
	private long last_explosion = 0;    

	//	private Image enemy_image = StdDrawModified.getImage("spaceship.gif");
	// private Image enemy_image = StdDrawModified.getImage("aliensprite.png");
	private Image[] enemy_images;

	int max = 32;


	public AllEnemies() {
		enemy_images = new Image[max];
		for (int i = 0; i < enemy_images.length; i++)
		{
			enemy_images[i] = StdDrawModified.getImage("enemy_sprites/e" + (i + 1) + ".png");			
		}


		enemies_killed = 0;
		rows_of_enemies = new ArrayList<Enemies>();
		for (int i = 0; i < total_rows_of_enemies; i++)
		{
			rows_of_enemies.add(new Enemies(total_columns_of_enemies, i, level));
		}

		initial_vertical_velocity = rows_of_enemies.get(0).get_y_velocity();
		initial_horizontal_velocity = rows_of_enemies.get(0).get_x_velocity();

		vertical_velocity = initial_vertical_velocity;
		horizontal_velocity = initial_horizontal_velocity;
	}

	public void check_for_collisions(ArrayList<LaserProjectile> missiles)
	{
		for (int j = 0; j < rows_of_enemies.size(); j++)
		{
			for (int i = 0; i < missiles.size(); i++) 
			{
				// if there is a collision between a laser and all the enemies in a row
				int op_code = rows_of_enemies.get(j).check_for_collision(missiles.get(i).get_laser_x(), missiles.get(i).get_laser_y());
				if (op_code == 1)
				{
					missiles.remove(i);
					enemies_killed++;
					total_enemies_killed++;
					if (System.currentTimeMillis() - last_explosion > 500)
					{
						play_sound();
						last_explosion = System.currentTimeMillis();
					}
				}
				if (op_code == 2)
				{
					missiles.remove(i);
				}
			}
			if (rows_of_enemies.get(j).all_dead())
			{
				rows_of_enemies.remove(j);
			}
		}
	}

	// sets velocities, adds, removes enemies
	public void update(boolean three_quarter_mark)
	{
		for (int j = 0; j < rows_of_enemies.size(); j++)
		{
			rows_of_enemies.get(j).set_vertical_velocity((vertical_velocity));
			rows_of_enemies.get(j).set_horizontal_velocity((horizontal_velocity));
			if (rows_of_enemies.get(j).all_dead())
			{
				rows_of_enemies.remove(j);
			}
		}

		// add a row of enemies
		if (!three_quarter_mark)
		{
			if (!rows_of_enemies.isEmpty())
			{
				if (rows_of_enemies.get(rows_of_enemies.size() - 1).check_if_starting_y_lower_than_original_y())
				{
					rows_of_enemies.add(new Enemies(total_columns_of_enemies, 1, level));
				}
			}
			else
			{
				rows_of_enemies.add(new Enemies(total_columns_of_enemies, rows_of_enemies.size() + 1, level));
			}
		}
	}

	public void increase_velocity()
	{
		vertical_velocity += 0.000001;
		horizontal_velocity += 0.000001;
	}

	public void reset()
	{
		for (int i = 0; i < rows_of_enemies.size(); i++)
		{
			rows_of_enemies.get(i).reset(i);
		}
		vertical_velocity = initial_vertical_velocity;
		horizontal_velocity = initial_horizontal_velocity;
	}

	public void next_level_reset(int level)
	{
		// System.out.println(horizontal_velocity + " " + vertical_velocity);
		horizontal_velocity = initial_horizontal_velocity + level*0.001;
		vertical_velocity = initial_vertical_velocity + level*0.001;
		this.level = level;
		total_columns_of_enemies = initial_total_columns_of_enemies + level - 1;
		enemies_killed = 0;
		rows_of_enemies.clear();
	}

	// draw enemies and return true if ship collides with any enemies.
	public boolean it_is_the_end(Ship ship)
	{
		for (int j = 0; j < rows_of_enemies.size(); j++)
		{
			if(rows_of_enemies.get(j).draw_enemies(ship, enemy_images[(level - 1) % max]))
			{
				return true;
			}
		}
		return false;
	}

	public boolean all_dead()
	{
		return rows_of_enemies.isEmpty();
	}

	public long enemies_killed()
	{
		return enemies_killed;
	}

	public long total_enemies_killed()
	{
		return total_enemies_killed;
	}

	public void play_sound()
	{
		new Thread(
				new Runnable() {
					public void run() {
						try {
							// PLAY AUDIO CODE
							File file = new File("boom.wav");
							Sound.playSoundFile(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
	}

}
