package il.ac.tau.cs.sw1.ex3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class BigramModel {
	public static final int MAX_VOCABULARY_SIZE = 14500;
	public static final String VOC_FILE_SUFFIX = ".voc";
	public static final String COUNTS_FILE_SUFFIX = ".counts";
	public static final String SOME_NUM = "some_num";
	public static final int ELEMENT_NOT_FOUND = -1;

	String[] mVocabulary;
	int[][] mBigramCounts;

	// DO NOT CHANGE THIS !!!
	public void initModel(String fileName) throws IOException {
		mVocabulary = buildVocabularyIndex(fileName);
		mBigramCounts = buildCountsArray(fileName, mVocabulary);
	}

	public static boolean old_word(String word, String[] vocabulary) {
		for (String st : vocabulary) {
			if (word != null && word.equals(st)) {
				return true;
			}
		}
		return false;
	}

	// ترجع true لكلمة فيها أحرف، أو رقم كامل (تُستخدم للفلترة فقط)
	public static boolean is_word(String word) {
		if (word == null || word.isEmpty()) return false;
		boolean hasLetter = false;
		for (char c : word.toCharArray()) {
			if (Character.isLetter(c)) hasLetter = true;
			else if (!Character.isLetterOrDigit(c)) return false; // رموز غريبة
		}
		return hasLetter || is_num(word);
	}

	public static boolean is_num(String word) {
		if (word == null || word.isEmpty()) return false;
		for (char chr : word.toCharArray()) {
			if (!Character.isDigit(chr)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public String[] buildVocabularyIndex(String fileName) throws IOException {
		String[] vocabulary = new String[MAX_VOCABULARY_SIZE];
		int size = 0;

		File fromFile = new File(fileName);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFile));
		String line;
		while ((line = bufferedReader.readLine()) != null && size < MAX_VOCABULARY_SIZE) {
			String[] words = line.split("\\s+");
			for (String raw : words) {
				if (!is_word(raw)) continue;
				String word = raw.toLowerCase();
				if (is_num(word)) word = SOME_NUM;
				if (!old_word(word, Arrays.copyOf(vocabulary, size))) {
					vocabulary[size++] = word;
					if (size == MAX_VOCABULARY_SIZE) break;
				}
			}
		}
		bufferedReader.close();
		return Arrays.copyOf(vocabulary, size);
	}

	/*
	 * @post: mVocabulary = prev(mVocabulary)
	 * @post: mBigramCounts = prev(mBigramCounts)
	 */
	public int[][] buildCountsArray(String fileName, String[] vocabulary) throws IOException {
		int v = vocabulary.length;
		int[][] bigramCounts = new int[v][v];

		// خريطة كلمة -> فهرس لتسريع الوصول
		HashMap<String, Integer> index = new HashMap<>(v * 2);
		for (int i = 0; i < v; i++) index.put(vocabulary[i], i);

		File fromFile = new File(fileName);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fromFile));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			String[] words = line.split("\\s+");
			for (int k = 0; k + 1 < words.length; k++) {
				String w1 = words[k].toLowerCase();
				String w2 = words[k + 1].toLowerCase();
				if (is_num(w1)) w1 = SOME_NUM;
				if (is_num(w2)) w2 = SOME_NUM;

				Integer i = index.get(w1);
				Integer j = index.get(w2);
				if (i != null && j != null) {
					bigramCounts[i][j]++;
				}
			}
		}
		bufferedReader.close();
		return bigramCounts;
	}

	/*
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: fileName is a legal file path
	 */
	public void saveModel(String fileName) throws IOException {
		BufferedWriter vocWriter = new BufferedWriter(new FileWriter(fileName + VOC_FILE_SUFFIX));
		int n = mVocabulary.length;
		// السطر الأول: الحجم فقط (ليطابق loadModel)
		vocWriter.write(Integer.toString(n));
		vocWriter.newLine();
		for (int i = 0; i < n; i++) {
			vocWriter.write(i + "," + mVocabulary[i]);
			vocWriter.newLine();
		}
		vocWriter.close();

		BufferedWriter counWriter = new BufferedWriter(new FileWriter(fileName + COUNTS_FILE_SUFFIX));
		for (int k = 0; k < n; k++) {
			for (int j = 0; j < n; j++) {
				if (mBigramCounts[k][j] > 0) {
					counWriter.write(k + "," + j + ":" + mBigramCounts[k][j]);
					counWriter.newLine();
				}
			}
		}
		counWriter.close();
	}

	/*
	 * @pre: fileName is a legal file path
	 */
	public void loadModel(String fileName) throws IOException { // Q - 4
		BufferedReader vocReader = new BufferedReader(new FileReader(fileName + VOC_FILE_SUFFIX));
		int vocabSize = Integer.parseInt(vocReader.readLine().trim());
		mVocabulary = new String[vocabSize];
		for (int i = 0; i < vocabSize; i++) {
			String line = vocReader.readLine();
			String[] parts = line.split(",", 2);
			int index = Integer.parseInt(parts[0].trim());
			mVocabulary[index] = parts[1].trim();
		}
		vocReader.close();

		BufferedReader countsReader = new BufferedReader(new FileReader(fileName + COUNTS_FILE_SUFFIX));
		mBigramCounts = new int[vocabSize][vocabSize];
		String line;
		while ((line = countsReader.readLine()) != null) {
			String[] parts = line.split("[:,]");
			int index1 = Integer.parseInt(parts[0].trim());
			int index2 = Integer.parseInt(parts[1].trim());
			int count = Integer.parseInt(parts[2].trim());
			mBigramCounts[index1][index2] = count;
		}
		countsReader.close();
	}

	/*
	 * @pre: word is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post: $ret = -1 if word is not in vocabulary, otherwise $ret = the index of word in vocabulary
	 */
	public int getWordIndex(String word) {  // Q - 5
		if (word == null) return -1;
		for (int i = 0; i < mVocabulary.length; i++) {
			if (word.equals(mVocabulary[i])) return i;
		}
		return -1;
	}

	/*
	 * @pre: word1, word2 are in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post: $ret = the count for the bigram <word1, word2>. if one of the words does not
	 * exist in the vocabulary, $ret = 0
	 */
	public int getBigramCount(String word1, String word2) { //  Q - 6
		int index1 = getWordIndex(word1);
		int index2 = getWordIndex(word2);
		if ((index1 == -1) || (index2 == -1)) {
			return 0;
		}
		return mBigramCounts[index1][index2];
	}

	/*
	 * @pre word in lowercase, and is in mVocabulary
	 * @pre: the method initModel was called (the language model is initialized)
	 * @post $ret = the word with the lowest vocabulary index that appears most fequently after word (if a bigram starting with
	 * word was never seen, $ret will be null
	 */
	public String getMostFrequentProceeding(String word) { //  Q - 7
		int n = mVocabulary.length;
		int max_val = 0;
		int index3 = getWordIndex(word);
		if (index3 == -1) {
			return null;
		}
		int mostfreindex = -1;
		for (int y = 0; y < n; y++) {
			if (mBigramCounts[index3][y] > max_val) {
				max_val = mBigramCounts[index3][y];
				mostfreindex = y;
			}
		}
		if (mostfreindex >= 0) {
			return mVocabulary[mostfreindex];
		}
		return null;
	}

	/* @pre: sentence is in lowercase
	 * @pre: the method initModel was called (the language model is initialized)
	 * @pre: each two words in the sentence are are separated with a single space
	 * @post: if sentence is is probable, according to the model, $ret = true, else, $ret = false
	 */
	public boolean isLegalSentence(String sentence) {  //  Q - 8
		if (sentence == null || sentence.isEmpty()) return false;
		String[] words = sentence.split("\\s+");
		for (int i = 0; i < words.length - 1; i++) {
			int index1 = getWordIndex(words[i]);
			int index2 = getWordIndex(words[i + 1]);
			if ((index1 == -1) || (index2 == -1)) {
				return false;
			}
			if (mBigramCounts[index1][index2] == 0) {
				return false;
			}
		}
		return true;
	}

	/*
	 * @pre: arr1.length = arr2.legnth
	 * post if arr1 or arr2 are only filled with zeros, $ret = 0, otherwise calcluates CosineSim
	 */
	public static boolean is_0(int arr[]) {
		for (int num : arr) {
			if (num != 0) return false;
		}
		return true;
	}

	public static double calcCosineSim(int[] arr1, int[] arr2) { //  Q - 9
		if (arr1 == null || arr2 == null || arr1.length != arr2.length) return 0.0;
		if ((is_0(arr1)) || (is_0(arr2))) {
			return 0.0;
		}
		long Numerator = 0;
		double Denominator1 = 0;
		double Denominator2 = 0;
		for (int i = 0; i < arr1.length; i++) {
			Numerator += (long) arr1[i] * arr2[i];
			Denominator1 += (double) arr1[i] * arr1[i];
			Denominator2 += (double) arr2[i] * arr2[i];
		}
		return Numerator / (Math.sqrt(Denominator1) * Math.sqrt(Denominator2));
	}

	/*
	 * @pre: word is in vocabulary
	 * @pre: the method initModel was called (the language model is initialized),
	 * @post: $ret = w implies that w is the word with the largest cosineSimilarity(vector for word, vector for w) among all the
	 * other words in vocabulary
	 */
	public String getClosestWord(String word) { //  Q - 10
		int n = mVocabulary.length;
		double max_smi = -1.0;
		int ind_of_max = -1;
		int index = getWordIndex(word);

		if (index == -1) {
			return null;
		}
		if (n == 1) {
			return word;
		}

		for (int i = 0; i < n; i++) {
			if (i == index) continue; // لا نقارن مع نفس الكلمة
			double PossibleVal = calcCosineSim(mBigramCounts[index], mBigramCounts[i]);
			if (PossibleVal > max_smi) {
				max_smi = PossibleVal;
				ind_of_max = i;
			}
		}
		return (ind_of_max != -1) ? mVocabulary[ind_of_max] : null;
	}
}
