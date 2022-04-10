import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadingAvalara {
	private ArrayList<Avalara> avalaraList = new ArrayList<>();

	public ReadingAvalara() {

	}

	public ReadingAvalara(ArrayList<Avalara> avalaraList) {

		this.avalaraList = avalaraList;
	}

	public ArrayList<Avalara> getAvalaraList() {
		return avalaraList;
	}

	public void setAvalaraList(ArrayList<Avalara> avalaraList) {
		this.avalaraList = avalaraList;
	}

	public ArrayList<Avalara> readCSV(String filename) {
		ArrayList<String> list = new ArrayList<>();

		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			reader.readLine();
			String line = null;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
			for (String row : list) {
				String[] strings = row.trim().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				for (int i = 0; i < strings.length; i++) {
					if (strings[i].startsWith("\""))
						strings[i] = strings[i].substring(1, strings[i].length());
					if (strings[i].endsWith("\""))
						strings[i] = strings[i].substring(0, strings[i].length() - 1);
					strings[i] = strings[i].replaceAll("\"\"", "\"");
				}

				if (strings.length >= 2 && strings[0] != "") {
					Avalara avalara = new Avalara(strings[0], strings[1]);
					avalaraList.add(avalara);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avalaraList;

	}

}
