import java.util.Arrays;

public class LCS {
	int length = 0;

	public LCS() {

	}

	public LCS(int length) {

		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int longestCommonSubsequnce(String X, String Y) {
		int m = X.length();
		int n = Y.length();

		int dp[][] = new int[m][1000];

		for (int[] row : dp) {
			Arrays.fill(row, -1);
		}

		return length = lcs(X, Y, m, n, dp);
	}

	private int lcs(String X, String Y, int m, int n, int dp[][]) {
		if (m == 0 || n == 0) {
			return 0;
		}

		if (dp[m - 1][n - 1] != -1) {
			return dp[m - 1][n - 1];
		}

		if (X.charAt(m - 1) == Y.charAt(n - 1)) {
			dp[m - 1][n - 1] = 1 + lcs(X, Y, m - 1, n - 1, dp);
			return dp[m - 1][n - 1];
		} else {
			dp[m - 1][n - 1] = Math.max(lcs(X, Y, m, n - 1, dp), lcs(X, Y, m - 1, n, dp));

			return dp[m - 1][n - 1];
		}
	}

}
