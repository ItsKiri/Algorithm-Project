import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappingClass {
	private ArrayList<Result> resultList;

	public MappingClass() {
		resultList = new ArrayList<Result>();

	}

	public MappingClass(ArrayList<Result> resultList) {

		this.resultList = resultList;
	}

	public ArrayList<Result> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<Result> resultList) {
		this.resultList = resultList;
	}

	public void mapping(ArrayList<Class> classList, ArrayList<Avalara> avalaraList, UNSPSC tree) {
		for (Avalara avalara : avalaraList) {

			int maxPercentage = 0;
			ArrayList<Class> sameClass = new ArrayList<>();

			String title = modifyString(avalara.getDescription()).toLowerCase();
			for (int i = 0; i < classList.size(); i++) {
				Class classNode = classList.get(i);
				int percentage = commonWordsCount(title, classNode.getClassTitle().toLowerCase());
				if (percentage > maxPercentage) {
					sameClass.clear();
					sameClass.add(classNode);
					maxPercentage = percentage;

				} else if (percentage == maxPercentage)
					sameClass.add(classNode);

			}
			int sameClassIndex = 0;
			int sameCommodityIndex = 0;
			maxPercentage = 0;
			for (int i = 0; i < sameClass.size(); i++) {
				Class sameClassNode = sameClass.get(i);
				ArrayList<Commodity> commodityList = sameClassNode.getCommodityNodes();

				for (int j = 0; j < commodityList.size(); j++) {
					Commodity commodityNode = commodityList.get(j);
					title = commodityModification(title);

					int percentage = 0;

					percentage = commonWordsCount(title, commodityNode.getCommodityTitle().toLowerCase());

					if (percentage > maxPercentage) {
						maxPercentage = percentage;
						sameClassIndex = i;
						sameCommodityIndex = j;
					}
				}

			}

			if (maxPercentage == 0) {
				title = modifyString(avalara.getDescription()).toLowerCase();
				int segmentIndex = 0;
				int familyIndex = 0;
				int classIndex = 0;
				int commodityIndex = 0;

				for (int i = 0; i < tree.getSegmentNodes().size(); i++) {
					Segment treeSegment = tree.getSegmentNodes().get(i);
					for (int j = 0; j < treeSegment.getFamilyNodes().size(); j++) {
						Family treeFamily = treeSegment.getFamilyNodes().get(j);
						for (int k = 0; k < treeFamily.getClassNodes().size(); k++) {
							Class treeClass = treeFamily.getClassNodes().get(k);
							for (int l = 0; l < treeClass.getCommodityNodes().size(); l++) {

								Commodity treeCommodity = treeClass.getCommodityNodes().get(l);
								String fullTitle = treeSegment.getSegmentTitle() + " " + treeFamily.getFamilyTitle()
										+ " " + treeClass.getClassTitle() + " " + treeCommodity.getCommodityTitle();

								int percentage = commonWordsCount(title, fullTitle.toLowerCase());
								if (percentage > maxPercentage) {
									maxPercentage = percentage;
									segmentIndex = i;
									familyIndex = j;
									classIndex = k;
									commodityIndex = l;

								}
							}
						}
					}
				}
				Commodity correctCommodity = tree.getSegmentNodes().get(segmentIndex).getFamilyNodes().get(familyIndex)
						.getClassNodes().get(classIndex).getCommodityNodes().get(commodityIndex);
				Result result = new Result(avalara.getTaxCode(), title, correctCommodity.getCommodityTitle(),
						correctCommodity.getCommodityID());
				resultList.add(result);

			} else {
				Class correctClass = sameClass.get(sameClassIndex);
				Commodity treeCommodity = correctClass.getCommodityNodes().get(sameCommodityIndex);
				Result result = new Result(avalara.getTaxCode(), title, treeCommodity.getCommodityTitle(),
						treeCommodity.getCommodityID());
				resultList.add(result);

			}

		}

	}

	public String constructResults() {
		StringBuilder stringBuilder = new StringBuilder();
		String categories = "Commodity Title,"+"Commodity ID,"+"Avalara Tax Code,"+"Avalara Description\n";
		stringBuilder.append(categories);
		for (Result line : resultList) {
			stringBuilder.append(line.toString() + "\n");
		}
		return stringBuilder.toString();
	}

	public void print(String fileName) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			out.write(constructResults());
			out.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String commodityModification(String avalara) {
		String modified = avalara;
		if (modified.indexOf("clothing accessories ") != -1)
			modified = modified.replaceAll("clothing accessories ", "").trim();
		if (modified == "" || modified.length() == 0)
			return "empty";

		return modified;
	}

	private String addSynonyms(String s) {

		if (s.toLowerCase().contains("digital")) {
			s = s + " electrical print";
		}
		if (s.toLowerCase().contains("digital audio works")) {
			s = s + " integrated circuits workstation";
		}
		if (s.toLowerCase().contains("digital books")) {
			s = s + " Business use papers";
		}
		if (s.toLowerCase().contains("computer software")) {
			s = s + " licensing rental or leasing service";
		}
		if (s.toLowerCase().contains("system software")) {
			s = s + " maintenance and support";
		}
		if (s.toLowerCase().contains("cloud")) {
			s = s + " infrastructure";
		}
		if (s.toLowerCase().contains("gift")) {
			s = s + " Novelty certificate";
		}
		if (s.toLowerCase().contains("digital image")) {
			s = s + " printers Audio visual equipment";
		}
		if (s.toLowerCase().contains("digital magazines")) {
			s = s + " Electronic publications";
		}
		if (s.toLowerCase().contains("mailing lists")) {
			s = s + " services";
		}
		if (s.toLowerCase().contains("movies")) {
			s = s + " entertainment theatre Film";
		}
		if (s.toLowerCase().contains("streaming")) {
			s = s + " equipment";
		}
		if (s.toLowerCase().contains("newspaper")) {
			s = s + " publication";
		}
		if (s.toLowerCase().contains("photograph")) {
			s = s + " art";
		}
		if (s.toLowerCase().contains("media")) {
			s = s + " storage";
		}
		if (s.toLowerCase().contains("vehicle")) {
			s = s + " transport";
		}
		if (s.toLowerCase().contains("international")) {
			s = s + " air freight";
		}
		if (s.toLowerCase().contains("concert")) {
			s = s + " performance";
		}
		if (s.toLowerCase().contains("adult")) {
			s = s + " club night";
		}
		if (s.toLowerCase().contains("coupons")) {
			s = s + " paper";
		}
		if (s.toLowerCase().contains("sales") || s.toLowerCase().contains("fees")) {
			s = s + " tax";
		}
		if (s.toLowerCase().contains("plant")) {
			s = s + " tree shrub";
		}
		if (s.toLowerCase().contains("containers")) {
			s = s + " storage equipment";
		}
		if (s.toLowerCase().contains("drink")) {
			s = s + " alcohol ";
		}
		if (s.toLowerCase().contains("tire")) {
			s = s + " tube ";
		}
		if (s.toLowerCase().contains("bicycle")) {
			s = s + " vehicle ";
		}
		if (s.toLowerCase().contains("books")) {
			s = s + " printed ";
		}
		if (s.toLowerCase().contains("battery")) {
			s = s + " cell ";
		}

		if (s.toLowerCase().contains("carpet")) {
			s = " rugs mat " + s;
		}
		if (s.toLowerCase().contains("child")) {
			s = " infant toddler baby " + s;
		}
		if (s.toLowerCase().contains("and related products")) {
			s = s.replaceAll("and related products", "accessories");
		}
		if (s.toLowerCase().contains("ear")) {
			s = " hearing " + s;
		}
		if (s.toLowerCase().contains("foot lets")) {
			s = " hoisery socks " + s;
		}
		if (s.toLowerCase().contains("sheepskin")) {
			s = " leather " + s;
		}
		if (s.toLowerCase().contains("fur")) {
			s = " leather sheep" + s;
		}
		if (s.toLowerCase().contains("golf clothing")) {
			s = " gloves " + s;
		}
		if (s.toLowerCase().contains("insole")) {
			s = " Ergonomic " + s;
		}
		if (s.toLowerCase().contains("pantyhose") || s.toLowerCase().contains("socks and stocking")) {
			s = " hoisery " + s;
		}
		if (s.toLowerCase().contains("poncho")) {
			s = " coat jacket " + s;
		}
		if (s.toLowerCase().contains("rainwear") || s.toLowerCase().contains("protective")) {
			s = " safety " + s;
		}
		if (s.toLowerCase().contains("scarves") || s.toLowerCase().contains("suspender")
				|| s.toLowerCase().contains("handkerchiefs") || s.toLowerCase().contains("umbrellas")
				|| s.toLowerCase().contains("sweatbands")) {
			s = " clothing " + s;
		}
		if (s.toLowerCase().contains("underwear")) {
			s = " undergarment " + s;
		}
		if (s.toLowerCase().contains("aqua")) {
			s = " water " + s;
		}
		if (s.toLowerCase().contains("shoe")) {
			s = " footwear " + s;
		}
		if (s.toLowerCase().contains("blanket")) {
			s = " night " + s;
		}
		if (s.toLowerCase().contains("costume")) {
			s = " drama play " + s;
		}
		if (s.toLowerCase().contains("briefcase")) {
			s = " business " + s;
		}
		if (s.toLowerCase().contains("cosmetic") || s.toLowerCase().contains("hair")
				|| s.toLowerCase().contains("shower")) {
			s = " bath body " + s;
		}
		if (s.toLowerCase().contains("handbag")) {
			s = " purse bag" + s;
		}
		if (s.toLowerCase().contains("sunglass")) {
			s = " vision eye" + s;
		}
		if (s.toLowerCase().contains("wallet")) {
			s = " cash " + s;
		}
		if (s.toLowerCase().contains("wet")) {
			s = " scuba " + s;
		}
		if (s.toLowerCase().contains("belt") || s.toLowerCase().contains("buckle")) {
			s = " sewing " + s;
		}
		if (s.toLowerCase().contains("mask")) {
			s = " respiration protection " + s;
		}
		if (s.toLowerCase().contains("helmet")) {
			s = " head protection " + s;
		}
		if (s.toLowerCase().contains("safety")) {
			s = " protection equipment " + s;
		}
		if (s.toLowerCase().contains("gloves")) {
			s = " apparel " + s;
		}
		if (s.toLowerCase().contains("ski")) {
			s = " skiing snow " + s;
		}
		if (s.toLowerCase().contains("athletic")) {
			s = " sports " + s;
		}
		if (s.toLowerCase().contains("swim")) {
			s = " surf " + s;
		}
		if (s.toLowerCase().contains("footwear")) {
			s = " shoes " + s;
		}

		return s;

	}

	private String removeStrings(String s) {

		s = s.replaceAll("\\((business-to-business)\\) ?", "");
		s = s.replaceAll("\\((business-to-customer)\\) ?", "");
		return s;
	}

	private String modifyString(String s) {

		String cleanedString = removeStrings(s);
		String synonymString = addSynonyms(cleanedString);
		String modified = synonymString.replaceAll("[()]", "").replaceAll("[-+.^:/*,]", " ");
		Pattern p = Pattern.compile(
				"\\b(&|I|I've|&|:|%|=|unknown|includes|required|supporters|related|including|limited|mixed|tone|entirely|Reimbursed|installation|associated|etc|measurable|paid|tangible|personal|property|stated|charges|sale|municipally|privately|owned|separately|combined|contract|option|see additional avatax system tax code information|streamed|similar|access|see additional code description|partly|covered|seen|a|about|above|across|after|again|against|all|almost|alone|along|already|also|although|always|among|an|and|another|any|anybody|anyone|anything|anywhere|are|area|areas|around|as|ask|asked|asking|asks|at|away|b|back|backed|backing|backs|be|became|because|become|becomes|been|before|began|behind|being|beings|best|better|between|big|both|but|by|By|c|containing|came|can|cannot|case|cases|certain|certainly|clear|clearly|come|could|d|did|differ|different|differently|do|does|done|down|down|download|downloaded|downed|downing|downs|during|e|each|early|either|end|ended|ending|ends| enough|even|evenly|ever|every|everybody|everyone|everything|everywhere|f|face|faces|fact|facts|far|felt|few|find|finds|first|for|four|from|full|fully|further|furthered|furthering|furthers|g|gave|general|generally|get|gets|give|given|gives|go|going|good|got|great|greater|greatest|group|grouped|grouping|groups|h|had|has|have|having|he|her|here|herself|high|high|high|higher|highest|him|himself|his|how|however|i|if|important|in|interest|interested|intended|interesting|interests|into|is|it|its|itself|j|just|k|keep|keeps|kind|knew|know|known|knows|l| large|largely|last|later|latest|least|leave|less|let|lets|like|likely|load|long|longer|longest|m|meets|made|make|making|man|many|may|me|member|members|men|might|more|most|mostly|mr|mrs|much|must|my|myself|n|necessary|need|needed|needing|needs|never|new|new|newer|newest|next|no|nobody|non|noone|not|nothing|now| nowhere|number|numbers|o|of|off|often|old|older|oldest|on|once|one|only|open|opened|opening|opens|or|order|ordered|ordering|orders|other|others|our|out|over|p|part|parted|parting|parts|per|perhaps|place|places|point|pointed|pointing|points|possible|present|previously|purchased|presented|presenting|presents|problem|problems|put|puts|q|quite|r|required|rather|really|right|right|room|rooms|s|said|same|saw|say|says|second|seconds|see|seem|seemed|seeming|seems|sees|several|shall|she|should|show|showed|showing|shows|side|sides|since|sold|small|smaller|smallest|so|some|somebody|someone|something|somewhere|state|states|still|still|such|sure|t|to|take|taken|than|that|the|their|them|then|there|therefore|these|they|thing|things|think|thinks|this|those|though|thought|thoughts|three|through|thus|to|today|together|too|took|toward|turn|turned|turning|turns|two|u|users|under|until|up|upon|us|use|used|user|uses|v|via|very|w|want|wanted|wanting|wants|was|way|ways|we|well|wells|went|were|what|when|where|whether|which|while|who|whole|whose|why|will|with|within|without|work|worked|working|works|would|x|y|year|years|yet|you|young|younger|youngest|your|yours.....)\\b\\s?");
		Matcher m = p.matcher(modified);
		String modifiedString = m.replaceAll("");
		return modifiedString;
	}

	private int commonWordsCount(String unspsc, String avalara) {

		unspsc = unspsc.trim();
		avalara = avalara.trim();
		if (unspsc.length() == 0 || unspsc == "" || avalara.length() == 0 || avalara == "")
			return 0;

		int count = 0;
		Set<String> set = new HashSet<>();
		String[] strings = unspsc.split(" ");
		for (String string : strings) {
			if (!set.contains(string) && !set.contains(string + "s")) {
				set.add(string);
				set.add(string + "s");
			}

		}
		for (String string : set) {
			if (avalara.indexOf(string) != -1 || (avalara + "s").indexOf(string) != -1)
				count++;
		}

		return count;
	}

}
