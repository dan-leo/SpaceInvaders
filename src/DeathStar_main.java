/*
 * Creative Commons Licence
 * 
 * Share — copy and redistribute the material in any medium or format
 * Adapt — remix, transform, and build upon the material
 * The licensor cannot revoke these freedoms as long as you follow 
 * the license terms.
 */


import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Daniel Robinson
 * @created 2015-03-01
 * @version 0.9
 * 
 *  This is a take on space invaders of old. 
 *  This time, modern PCs should be able to handle a few extra lasers and bullets.
 */

/* TODO: 
		add name
		show score at end of level
		show high-score after game over
		save name and high score to file
		add new lasers
		add new ships
		add time spent on level to HUD
		add health to ship

		add difficulty levels
		simplicity is the ultimate sophistication
		make .jar or executable
		port to Android
		make sure it is fun!*/

public class DeathStar_main {

	// private variables
	private int FPS = 100;
	private int REFRESH_RATE_ms = 1000 / FPS; // 20 ms


	private double max_x = 1.0;
	private double min_x = -1.0;
	private double max_y = 1.0;
	private double min_y = -1.0;



	private Ship ship;
	private AllEnemies allenemies;
	private AllLasers alllasers;


	private int canvas_x = 640;
	private int canvas_y = 480;

	private int level = 1;
	private long prev_reset_time = 0;
	private long prev_next_level_time = 0;

	private boolean game_over = false;

	// temporary
	private long max_time = 0;

	private Background background;

	private long now;

	private boolean click = false;
	private boolean spacebar = false;

	int lives = level;

	long time = 0;

	public DeathStar_main() {
		StdDrawModified.setCanvasSize(canvas_x, canvas_y);
		StdDrawModified.setXscale(min_x, max_x);
		StdDrawModified.setYscale(min_y, max_y);
		ship = new Ship(REFRESH_RATE_ms);
		allenemies = new AllEnemies();
		alllasers = new AllLasers(REFRESH_RATE_ms);
		background = new Background();
		now = System.currentTimeMillis();
	}

	public void update_background()
	{
		long now0 = System.currentTimeMillis();
		draw_all_enemies_and_lasers();
		long now2 = System.currentTimeMillis() - now0;
		if (now2 > max_time && now2 < 300) max_time = now2;
		allenemies.increase_velocity();
		if (StdDrawModified.debug_time) System.out.printf(">%-5d>%-5d>>%-5d\n", now2, max_time, System.currentTimeMillis() - now0);

	}

	public void update_foreground() 
	{
		StdDrawModified.show(0);
	}

	public void pressed_keys() {
		if (!game_over)
		{
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_Z)) {
				ship.move_left();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_C)) {
				ship.move_right();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_B)) {
				ship.rotate_left();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_M)) {
				ship.rotate_right();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_LEFT)) {
				ship.move_left();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_RIGHT)) {
				ship.move_right();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_R) && StdDrawModified.isKeyPressed(KeyEvent.VK_T)) {
				reset();
			}
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_H) && StdDrawModified.isKeyPressed(KeyEvent.VK_J) && StdDrawModified.isKeyPressed(KeyEvent.VK_X)) {
				next_level();
			}

			if (StdDrawModified.isKeyPressed(KeyEvent.VK_SPACE) && !StdDrawModified.mousePressed()) {
				alllasers.add_laser(ship.get_x(), ship.get_y(), ship.get_ship_height(), ship.get_angle(), level, spacebar);
				spacebar = false;
			}
			else
			{
				spacebar = true;
			}
		}
		else
		{
			if (StdDrawModified.isKeyPressed(KeyEvent.VK_ENTER)) {
				reset();
			}
		}
		if (StdDrawModified.isKeyPressed(KeyEvent.VK_Q)) {
			System.exit(0);
		}
	}    

	public void mouse()     
	{   
		if (StdDrawModified.mousePressed() && !game_over)
		{
			double mouse_angle = Math.toDegrees(Math.atan((StdDrawModified.mouseY() - ship.get_y() ) / (StdDrawModified.mouseX() - ship.get_x())));
			if (mouse_angle < 0)
			{         
				double temp = Math.abs(mouse_angle + 90);
				mouse_angle = 90 + temp;
			}
			alllasers.add_laser(ship.get_x(), ship.get_y(), ship.get_ship_height(), mouse_angle, level, click);
			ship.set_angle(mouse_angle);
			click = false;
		}
		else
		{
			click = true;
		}
	}

	private void next_level()
	{
		if (System.currentTimeMillis() - prev_next_level_time > 500) {
			level++;
			background.next();
			allenemies.next_level_reset(level);
			alllasers.get_all().clear();
			lives = level;
			prev_next_level_time = System.currentTimeMillis();
		}

	}

	private void reset()
	{
		game_over = false;
		max_time = 0;
		if (System.currentTimeMillis() - prev_reset_time > 1000) {
			// enemies.reset();
			// allenemies.reset();
			level = 1;
			ship = new Ship(REFRESH_RATE_ms);
			allenemies = new AllEnemies();
			alllasers = new AllLasers(REFRESH_RATE_ms);
			background = new Background();
			now = System.currentTimeMillis();
			prev_reset_time = System.currentTimeMillis();
		}
	}

	private void welcome_screen() 
	{
		StdDrawModified.picture(0, 0, "background.jpg", 3, 2);
		StdDrawModified.setFont(new Font("Britannic Bold", Font.BOLD, 20));
		StdDrawModified.setPenColor(StdDrawModified.RED);
		StdDrawModified.text(0, 0.5, "Welcome to the Dark Side!");

		StdDrawModified.setPenColor(StdDrawModified.ORANGE);
		StdDrawModified.text(0, -0.7, "Left / Right Arrow Keys, or Z and C to move");
		StdDrawModified.setPenColor(StdDrawModified.GREEN);
		StdDrawModified.text(0, -0.8, "B and M to rotate");
		StdDrawModified.setPenColor(StdDrawModified.YELLOW);
		StdDrawModified.text(0, -0.9, "Spacebar or mouse to shoot!");
		StdDrawModified.setPenColor(StdDrawModified.WHITE);
		StdDrawModified.text(-1 + 0.15, 1 - 0.05, "Q to quit");
	}

	public void game_over_screen() {
		StdDrawModified.clear(StdDrawModified.BLACK);
		StdDrawModified.setPenColor(StdDrawModified.RED);
		StdDrawModified.text(0, 0, "Game Over!");
		StdDrawModified.setPenColor(StdDrawModified.YELLOW);
		StdDrawModified.text(0, -0.2, "Return to the light you must");
		StdDrawModified.setPenColor(StdDrawModified.BOOK_LIGHT_BLUE);
		StdDrawModified.text(0, -0.4, "Press enter to restart");
		text();
	}

	private void text() {
		StdDrawModified.setPenColor(StdDrawModified.GREEN);
		StdDrawModified.text(-1 + 0.15, 1 - 0.05, "Level: " + level);
		StdDrawModified.setPenColor(StdDrawModified.ORANGE);
		StdDrawModified.text(-1 + 0.2, -1 + 0.05, "Killed: " + allenemies.enemies_killed());
		StdDrawModified.setPenColor(StdDrawModified.RED);
		StdDrawModified.text(-1 + 0.6, -1 + 0.05, "Total: " + allenemies.total_enemies_killed());
		StdDrawModified.setPenColor(StdDrawModified.BOOK_LIGHT_BLUE);
		if (!game_over) time = System.currentTimeMillis() - now;
		StdDrawModified.text(1 - 0.2, -1 + 0.05, time + "ms");
		StdDrawModified.setPenColor(StdDrawModified.MAGENTA);
		StdDrawModified.text(1 - 0.15, 1 - 0.1, "Lives: " + lives);
	}

	private void draw_all_enemies_and_lasers()
	{
		long[] now = new long[10];

		if (StdDrawModified.debug_time) now[0] = System.currentTimeMillis();

		background.draw();
		background.update();

		if (StdDrawModified.debug_time) now[1] = System.currentTimeMillis();

		if (allenemies.all_dead())
		{
			next_level();
		}

		if (StdDrawModified.debug_time) now[2] = System.currentTimeMillis();

		// draws space craft
		ship.draw_spacecraft();
		text();

		if (StdDrawModified.debug_time) now[3] = System.currentTimeMillis();

		// draws enemies as well, and if it is the end, then game over.
		if (allenemies.it_is_the_end(ship)) {
			lives--;
			System.out.println(lives);
			if (lives == 0)
			{
				game_over_screen();
				game_over = true;
			}
		}

		if (StdDrawModified.debug_time) now[4] = System.currentTimeMillis();

		// draws laser beams and checks for collisions.
		if (!alllasers.get_all().isEmpty()) 
		{
			alllasers.draw(ship.get_x());
			now[5] = System.currentTimeMillis();

			// check for collisions here
			allenemies.check_for_collisions(alllasers.get_all());

			if (StdDrawModified.debug_time) now[6] = System.currentTimeMillis();

		}

		// sets velocities, adds, removes enemies
		allenemies.update(background.three_quarter_mark());
		if (StdDrawModified.debug_time) now[7] = System.currentTimeMillis();

		for (int i = 1; i < 7; i++)
		{
			if (StdDrawModified.debug_time) System.out.printf("%-3d; ", now[i] - now[i - 1]);
		}

	}

	public boolean game_over()
	{
		return game_over;
	}

	public long refresh_rate()
	{
		return REFRESH_RATE_ms;
	}



	public static void main(String[] args) {
		DeathStar_main ds = new DeathStar_main();
		TimerTask main_task = new Update(ds);
		TimerTask music = new Music();
		Timer timer = new Timer(true);
		Timer music_timer = new Timer(true);

		ds.welcome_screen();

		while (!StdDrawModified.hasNextKeyTyped()) {
		}

		timer.scheduleAtFixedRate(main_task, 0, ds.refresh_rate());
		music_timer.scheduleAtFixedRate(music, 0, 1000);

	}

}