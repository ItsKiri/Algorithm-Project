import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Mapping {
	private ArrayList<Result> resultList;

	public Mapping() {
		resultList = new ArrayList<Result>();

	}

	public Mapping(ArrayList<Result> resultList) {

		this.resultList = resultList;
	}

	public ArrayList<Result> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<Result> resultList) {
		this.resultList = resultList;
	}

	// Need to improve the code by segmentTitle + familyTitle + classTitle +
	// commodityTitle
	public void mapping(UNSPSC tree, ArrayList<Avalara> avalaraList) {
		for (Avalara avalara : avalaraList) {
			int segmentLength = 0;
			int segmentIndex = 0;
			int familyLength = 0;
			int familyIndex = 0;
			int classLength = 0;
			int classIndex = 0;
			int commodityLength = 0;
			int commodityIndex = 0;
			for (int i = 0; i < tree.getSegmentNodes().size(); i++) {
				Segment treeSegment = tree.getSegmentNodes().get(i);
				LCS lcs = new LCS();
				if (lcs.longestCommonSubsequnce(treeSegment.getSegmentTitle(),
						avalara.getDescription()) > segmentLength) {
					segmentLength = lcs.longestCommonSubsequnce(treeSegment.getSegmentTitle(),
							avalara.getDescription());
					segmentIndex = i;
				}
			}
			Segment treeSegment = tree.getSegmentNodes().get(segmentIndex);
			for (int i = 0; i < treeSegment.getFamilyNodes().size(); i++) {
				Family treeFamily = treeSegment.getFamilyNodes().get(i);
				LCS lcs = new LCS();
				if (lcs.longestCommonSubsequnce(treeFamily.getFamilyTitle(), avalara.getDescription()) > familyLength) {
					familyLength = lcs.longestCommonSubsequnce(treeFamily.getFamilyTitle(), avalara.getDescription());
					familyIndex = i;
				}
			}
			Family treeFamily = treeSegment.getFamilyNodes().get(familyIndex);
			for (int i = 0; i < treeFamily.getClassNodes().size(); i++) {
				Class treeClass = treeFamily.getClassNodes().get(i);
				LCS lcs = new LCS();
				if (lcs.longestCommonSubsequnce(treeClass.getClassTitle(), avalara.getDescription()) > classLength) {
					classLength = lcs.longestCommonSubsequnce(treeClass.getClassTitle(), avalara.getDescription());
					classIndex = i;
				}
			}
			Class treeClass = treeFamily.getClassNodes().get(classIndex);
			for (int i = 0; i < treeClass.getCommodityNodes().size(); i++) {
				Commodity treeCommodity = treeClass.getCommodityNodes().get(i);
				LCS lcs = new LCS();
				if (lcs.longestCommonSubsequnce(treeCommodity.getCommodityTitle(),
						avalara.getDescription()) > commodityLength) {
					commodityLength = lcs.longestCommonSubsequnce(treeCommodity.getCommodityTitle(),
							avalara.getDescription());
					commodityIndex = i;
				}
			}
			Commodity treeCommodity = treeClass.getCommodityNodes().get(commodityIndex);
			Result result = new Result(avalara.getTaxCode(), avalara.getDescription(),
					treeCommodity.getCommodityTitle(), treeCommodity.getCommodityID());
			resultList.add(result);

		}

	}

	public StringBuilder constructResults() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Result result : resultList) {
			stringBuilder.append(result.toString() + "\n");
		}
		return stringBuilder;
	}

	public void print(String fileName) {
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName, true));
			outputStream.writeObject(constructResults());
			outputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
