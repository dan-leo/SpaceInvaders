import java.util.TimerTask;

public class Update extends TimerTask {

	private DeathStar_main mk2;
	@Override
	public void run()
	{
		// System.out.printf("%d ++ ", System.currentTimeMillis() - this.scheduledExecutionTime());
		update();
		/*System.out.println(System.currentTimeMillis() - now);
		now = System.currentTimeMillis();*/
		// System.out.printf(" ++ %d\n", System.currentTimeMillis() - this.scheduledExecutionTime());
	}

	public Update(DeathStar_main mk2) {
		this.mk2 = mk2;
	}

	public void update()
	{
		mk2.pressed_keys();
		mk2.mouse();
		if (!mk2.game_over())
		{
			mk2.update_background();
		}
		else
		{
			mk2.game_over_screen();	
		}
		mk2.update_foreground();
	}

}
