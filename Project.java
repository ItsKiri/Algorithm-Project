import java.util.ArrayList;

public class Project {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadingUNSPSC readUNSPSC = new ReadingUNSPSC();
		ArrayList<Data> dataList = readUNSPSC.readCSV(".\\src\\UNSPSC_English.csv");
		System.out.println(dataList.size());
		CreateTree createTree = new CreateTree();
		createTree.insert(dataList);
		UNSPSC tree = createTree.insert(dataList);
		int segmentCount = 0;
		int familyCount = 0;
		int classCount = 0;
		ArrayList<Class> classList = new ArrayList<>();

		for (Segment treeSegment : tree.getSegmentNodes()) {
			segmentCount++;
			for (Family treeFamily : treeSegment.getFamilyNodes()) {
				familyCount++;
				for (Class treeClass : treeFamily.getClassNodes()) {
					classCount++;
					classList.add(treeClass);

				}
			}
		}
		System.out.println(segmentCount);
		System.out.println(familyCount);
		System.out.println(classCount);
		System.out.println(classList.size());

		ReadingAvalara readAvalara = new ReadingAvalara();
		ArrayList<Avalara> avalaraList = readAvalara.readCSV(".\\src\\Avalara_goods_and_services.csv");
		System.out.println(avalaraList.size());

		MappingClass mappingClass = new MappingClass();
		mappingClass.mapping(classList, avalaraList);
		mappingClass.print(".\\src\\results.csv");
	}

}
