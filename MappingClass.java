import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
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

	public ArrayList<Result> mapping(ArrayList<Class> classList, ArrayList<Avalara> avalaraList) {
		for (Avalara avalara : avalaraList) {
			int index = 0;
			int maxPercentage = 0;

			String title = modifyString(avalara.getDescription());
			for (int i = 0; i < classList.size(); i++) {
				Class classNode = classList.get(i);
				int percentage = lock_match(classNode.getClassTitle(), title);
				if (percentage > maxPercentage) {
					maxPercentage = percentage;
					index = i;
				}

			}
			ArrayList<Commodity> commodityList = classList.get(index).getCommodityNodes();
			index = 0;
			maxPercentage = 0;
			for (int i = 0; i < commodityList.size(); i++) {
				Commodity commodityNode = commodityList.get(i);
				int percentage = lock_match(commodityNode.getCommodityTitle(), title);
				if (percentage > maxPercentage) {
					maxPercentage = percentage;
					index = i;
				}
			}
			Commodity treeCommodity = commodityList.get(index);
			Result result = new Result(avalara.getTaxCode(), avalara.getDescription(),
					treeCommodity.getCommodityTitle(), treeCommodity.getCommodityID());
			resultList.add(result);

		}
		return resultList;
	}

	public String constructResults() {
		StringBuilder stringBuilder = new StringBuilder();
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

	private static int lock_match(String s, String t) {

		int totalw = word_count(s);
		int total = 100;
		int perw = total / totalw;
		int gotperw = 0;

		if (!s.equals(t)) {

			for (int i = 1; i <= totalw; i++) {
				if (simple_match(split_string(s, i), t) == 1) {
					gotperw = ((perw * (total - 10)) / total) + gotperw;
				} else if (front_full_match(split_string(s, i), t) == 1) {
					gotperw = ((perw * (total - 20)) / total) + gotperw;
				} else if (anywhere_match(split_string(s, i), t) == 1) {
					gotperw = ((perw * (total - 30)) / total) + gotperw;
				} else {
					gotperw = ((perw * smart_match(split_string(s, i), t)) / total) + gotperw;
				}
			}
		} else {
			gotperw = 100;
		}
		return gotperw;
	}

	private static int anywhere_match(String s, String t) {
		int x = 0;
		if (t.contains(s)) {
			x = 1;
		}
		return x;
	}

	private static int front_full_match(String s, String t) {
		int x = 0;
		String tempt;
		int len = s.length();

		// ----------Work Body----------//
		for (int i = 1; i <= word_count(t); i++) {
			tempt = split_string(t, i);
			if (tempt.length() >= s.length()) {
				tempt = tempt.substring(0, len);
				if (s.contains(tempt)) {
					x = 1;
					break;
				}
			}
		}
		// ---------END---------------//
		if (len == 0) {
			x = 0;
		}
		return x;
	}

	private static int simple_match(String s, String t) {
		int x = 0;
		String tempt;
		int len = s.length();

		// ----------Work Body----------//
		for (int i = 1; i <= word_count(t); i++) {
			tempt = split_string(t, i);
			if (tempt.length() == s.length()) {
				if (s.contains(tempt)) {
					x = 1;
					break;
				}
			}
		}
		// ---------END---------------//
		if (len == 0) {
			x = 0;
		}
		return x;
	}

	private static int smart_match(String ts, String tt) {

		char[] s = new char[ts.length()];
		s = ts.toCharArray();
		char[] t = new char[tt.length()];
		t = tt.toCharArray();

		int slen = s.length;
		// number of 3 combinations per word//
		int combs = (slen - 3) + 1;
		// percentage per combination of 3 characters//
		int ppc = 0;
		if (slen >= 3) {
			ppc = 100 / combs;
		}
		// initialising an integer to store the total % this class genrate//
		int x = 0;
		// declaring a temporary new source char array
		char[] ns = new char[3];
		// check if source char array has more then 3 characters//
		if (slen < 3) {
		} else {
			for (int i = 0; i < combs; i++) {
				for (int j = 0; j < 3; j++) {
					ns[j] = s[j + i];
				}
				if (cross_full_match(ns, t) == 1) {
					x = x + 1;
				}
			}
		}
		x = ppc * x;
		return x;
	}

	/**
	 *
	 * @param s
	 * @param t
	 * @return
	 */
	private static int cross_full_match(char[] s, char[] t) {
		int z = t.length - s.length;
		int x = 0;
		if (s.length > t.length) {
			return x;
		} else {
			for (int i = 0; i <= z; i++) {
				for (int j = 0; j <= (s.length - 1); j++) {
					if (s[j] == t[j + i]) {
						// x=1 if any charecer matches
						x = 1;
					} else {
						// if x=0 mean an character do not matches and loop break out
						x = 0;
						break;
					}
				}
				if (x == 1) {
					break;
				}
			}
		}
		return x;
	}

	private static String split_string(String s, int n) {

		int index;
		String temp;
		temp = s;
		String temp2 = null;

		int temp3 = 0;

		for (int i = 0; i < n; i++) {
			int strlen = temp.length();
			index = temp.indexOf(" ");
			if (index < 0) {
				index = strlen;
			}
			temp2 = temp.substring(temp3, index);
			temp = temp.substring(index, strlen);
			temp = temp.trim();

		}
		return temp2;
	}

	private static int word_count(String s) {
		int x = 1;
		int c;
		s = s.trim();
		if (s.isEmpty()) {
			x = 0;
		} else {
			if (s.contains(" ")) {
				for (;;) {
					x++;
					c = s.indexOf(" ");
					s = s.substring(c);
					s = s.trim();
					if (s.contains(" ")) {
					} else {
						break;
					}
				}
			}
		}
		return x;
	}

	private static String modifyString(String s) {
		Pattern p = Pattern.compile(
				"\\b(&|-|I|I've|&|:|%|=|unknown|includes|required|supporters|related|including|limited|mixed|tone|entirely|Reimbursed|installation|associated|etc|measurable|paid|tangible|personal|property|stated|charges|sale|municipally|privately|owned|separately|combined|contract|option|see additional avatax system tax code information|streamed|similar|access|see additional code description|partly|covered|seen|a|about|above|across|after|again|against|all|almost|alone|along|already|also|although|always|among|an|and|another|any|anybody|anyone|anything|anywhere|are|area|areas|around|as|ask|asked|asking|asks|at|away|b|back|backed|backing|backs|be|became|because|become|becomes|been|before|began|behind|being|beings|best|better|between|big|both|but|by|By|c|containing|came|can|cannot|case|cases|certain|certainly|clear|clearly|come|could|d|did|differ|different|differently|do|does|done|down|down|download|downloaded|downed|downing|downs|during|e|each|early|either|end|ended|ending|ends| enough|even|evenly|ever|every|everybody|everyone|everything|everywhere|f|face|faces|fact|facts|far|felt|few|find|finds|first|for|four|from|full|fully|further|furthered|furthering|furthers|g|gave|general|generally|get|gets|give|given|gives|go|going|good|got|great|greater|greatest|group|grouped|grouping|groups|h|had|has|have|having|he|her|here|herself|high|high|high|higher|highest|him|himself|his|how|however|i|if|important|in|interest|interested|intended|interesting|interests|into|is|it|its|itself|j|just|k|keep|keeps|kind|knew|know|known|knows|l| large|largely|last|later|latest|least|leave|less|let|lets|like|likely|load|long|longer|longest|m|meets|made|make|making|man|many|may|me|member|members|men|might|more|most|mostly|mr|mrs|much|must|my|myself|n|necessary|need|needed|needing|needs|never|new|new|newer|newest|next|no|nobody|non|noone|not|nothing|now| nowhere|number|numbers|o|of|off|often|old|older|oldest|on|once|one|only|open|opened|opening|opens|or|order|ordered|ordering|orders|other|others|our|out|over|p|part|parted|parting|parts|per|perhaps|place|places|point|pointed|pointing|points|possible|present|previously|purchased|presented|presenting|presents|problem|problems|put|puts|q|quite|r|required|rather|really|right|right|room|rooms|s|said|same|saw|say|says|second|seconds|see|seem|seemed|seeming|seems|sees|several|shall|she|should|show|showed|showing|shows|side|sides|since|sold|small|smaller|smallest|so|some|somebody|someone|something|somewhere|state|states|still|still|such|sure|t|to|take|taken|than|that|the|their|them|then|there|therefore|these|they|thing|things|think|thinks|this|those|though|thought|thoughts|three|through|thus|to|today|together|too|took|toward|turn|turned|turning|turns|two|u|users|under|until|up|upon|us|use|used|user|uses|v|via|very|w|want|wanted|wanting|wants|was|way|ways|we|well|wells|went|were|what|when|where|whether|which|while|who|whole|whose|why|will|with|within|without|work|worked|working|works|would|x|y|year|years|yet|you|young|younger|youngest|your|yours.....)\\b\\s?");
		Matcher m = p.matcher(s);
		String modifiedString = m.replaceAll("").replaceAll("\\([^()]*\\)", "").replaceAll("[-+.^:/*,]", "");
		return modifiedString;
	}

}
