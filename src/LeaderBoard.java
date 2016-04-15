import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;


public class LeaderBoard {
	
	private HashMap<String, Integer> table;

	public LeaderBoard() {
		table = new HashMap<String, Integer>();
		
	}
	
	public HashMap<String, Integer> get()
	{
		return table;
	}
	
	@SuppressWarnings("rawtypes")
	public Iterator sort()
	{
		Map<String, Integer> map = new TreeMap<String, Integer>(table);
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		return iterator;
	}
	
	@SuppressWarnings("rawtypes")
	public void draw()
	{
		Iterator it = sort();
		while(it.hasNext())
		{
			Map.Entry me = (Map.Entry)it.next();
			System.out.println(me.getKey() + " " + me.getValue());
		}
	}
	
	public int[] values()
	{
		Collection<Integer> values = table.values();
		return null;
	}

	public static void main(String[] args)
	{
		File file = new File("highscores.txt");
		// System.out.println(new File("highscores.txt").exists());
		Scanner sc = null;
		LeaderBoard lb = new LeaderBoard();
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (sc.hasNextLine())
		{
			String name = sc.next();
			int score = sc.nextInt();
			System.out.println(name + " " + score);
			lb.get().put(name, score);
		}
		
		lb.get().put("Josh", 240);
		lb.draw();
		
		
	}
}
