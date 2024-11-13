/**
 * @author Enoch Jung (github.com/enochjung)
 * @file InstructionTable.java
 * @date 2024-05-05
 * @version 1.0.0
 *
 * @brief 조교가 구현한 SIC/XE 어셈블러 코드 구조 샘플
 */

package instruction;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionTable {
	/**
	 * 기계어 목록 파일을 읽어, 기계어 목록 테이블을 초기화한다.
	 * 
	 * @param instFileName 기계어 목록이 적힌 파일
	 * @throws FileNotFoundException 기계어 목록 파일이 없음.
	 * @throws IOException           파일 읽기 실패.
	 * @throws RuntimeException      잘못된 기계어 목록 테이블 파일 포맷.
	 */
	public InstructionTable(String instFileName) throws FileNotFoundException, IOException, RuntimeException {
		HashMap<String, Instruction> instMap = new HashMap<String, Instruction>();

		ArrayList<String> data = readFile(instFileName);
		Pattern pattern = Pattern.compile("..$");  // 정규 표현식: 문자열의 마지막 두 글자 매칭

		data.forEach(x -> {
			Matcher matcher = pattern.matcher(x);
			String name = "";
			if (matcher.find()) {
				name = matcher.group();
			}
			instMap.put(name, new Instruction(x));
		});

		_instructionMap = instMap;
	}

	/**
	 * 기계어 목록 테이블에서 특정 기계어를 검색한다.
	 * 
	 * @param instructionName 검색할 기계어 명칭
	 * @return 기계어 정보를 담은 <code>Optional</code>. 없을 경우 empty <code>Optional</code>.
	 */
	public Optional<Instruction> search(String instructionName) {
		return Optional.ofNullable(_instructionMap.get(instructionName));
	}

	/**
	 * 파일을 읽고, 해당 파일의 내용을 라인 단위로 저장하여 반환한다.
	 * 
	 * @param fileName 읽을 파일 명
	 * @return 파일의 내용. 라인 단위로 분리되어 있음.
	 * @throws FileNotFoundException 파일이 존재하지 않음.
	 * @throws IOException           파일 읽기 실패.
	 */
	private ArrayList<String> readFile(String fileName) throws FileNotFoundException, IOException {
		ArrayList<String> data = new ArrayList<String>();

		File file = new File(fileName);
		BufferedReader bufReader = new BufferedReader(new FileReader(file));

		String line = "";
		while ((line = bufReader.readLine()) != null){
			data.add(line);
		}

		bufReader.close();

		return data;
	}

	private final HashMap<String, Instruction> _instructionMap;
}
