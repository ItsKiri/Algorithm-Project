import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadingUNSPSC {
	private ArrayList<Data> dataList = new ArrayList<>();

	public ReadingUNSPSC(ArrayList<Data> dataList) {
		this.dataList = dataList;
	}

	public ReadingUNSPSC() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Data> getDatalist() {
		return dataList;
	}

	public void setDatalist(ArrayList<Data> dataList) {
		this.dataList = dataList;
	}

	private boolean isInt(String string) {
		for (char c : string.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Data> readCSV(String filename) {
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
				for1: for (int i = 0; i < strings.length - 1; i++) {
					if (strings[i].length() == 8 && isInt(strings[i])) {
						if (i + 2 >= strings.length)
							break for1;
						int segmentID = Integer.parseInt(strings[i]);
						String segmentTitle = strings[i + 1];
						for (int j = i + 1; j < strings.length - 1; j++) {
							if (strings[j].length() == 8 && isInt(strings[j])) {
								if (j + 2 >= strings.length)
									break for1;
								int familyID = Integer.parseInt(strings[j]);
								String familyTitle = strings[j + 1];
								for (int k = j + 1; k < strings.length - 1; k++) {
									if (strings[k].length() == 8 && isInt(strings[k])) {
										if (k + 2 >= strings.length)
											break for1;
										int classID = Integer.parseInt(strings[k]);
										String classTitle = strings[k + 1];
										for (int l = k + 1; l < strings.length - 1; l++) {
											if (strings[l].length() == 8 && isInt(strings[l])) {
												int commodityID = Integer.parseInt(strings[l]);
												String commodityTitle = strings[l + 1];
												Data data = new Data(segmentTitle, segmentID, familyTitle, familyID,
														classTitle, classID, commodityTitle, commodityID);
												dataList.add(data);
											}
										}
									}
								}
							}
						}
					}
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataList;
	}
}