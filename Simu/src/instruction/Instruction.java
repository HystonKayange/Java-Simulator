/**
 * @author Enoch Jung (github.com/enochjung)
 * @file Instruction.java
 * @date 2024-05-05
 * @version 1.0.0
 *
 * @brief 조교가 구현한 SIC/XE 어셈블러 코드 구조 샘플
 */

package instruction;

public class Instruction {
	/**
	 * 명령어의 operand 타입.
	 * 
	 * <ul>
	 * <li><code>OperandType.NO_OPERAND</code>: operand를 사용하지 않음 (no)
	 * <li><code>OperandType.MEMORY</code>: 메모리 주소 하나를 사용함 (m)
	 * <li><code>OperandType.REG</code>: 레지스터 하나를 사용함 (r1)
	 * <li><code>OperandType.REG1_REG2</code>: 레지스터 2개를 사용함 (r1,r2)
	 * </ul>
	 */
	public enum OperandType {
		/**
		 * operand를 사용하지 않음
		 */
		NO_OPERAND,

		/**
		 * 메모리 주소 하나를 사용함
		 */
		MEMORY,

		/**
		 * 레지스터 하나를 사용함
		 */
		REG,

		/**
		 * 레지스터 2개를 사용함
		 */
		REG1_REG2;
	}

	/**
	 * 명령어의 형식.
	 * 
	 * <ul>
	 * <li><code>Format.TWO</code>: 2형식
	 * <li><code>Format.THREE_FOUR</code>: 3 혹은 4형식
	 * </ul>
	 * 
	 * 1형식은 없음 (수행하지 않음)
	 */
	public enum Format {
		/**
		 * 2형식
		 */
		TWO,

		/**
		 * 3 혹은 4형식
		 */
		THREE_OR_FOUR
	}

	/**
	 * 기계어 목록 파일의 한 줄을 읽고, 이를 파싱하여 저장한다.
	 * 
	 * @param line 기계어 목록 파일의 한 줄
	 * @throws RuntimeException 잘못된 파일 형식.
	 */
	Instruction(String line) throws RuntimeException {
		String[] split = line.split("\t");
		if (split.length != 4)
			throw new RuntimeException("wrong instruction table file (wrong format)\n\n" + line);

		if (split[0].length() <= 6) {
			_name = split[0];
		} else {
			throw new RuntimeException("wrong instruction table file (too long instruction name)\n\n" + line);
		}

		if ("no".equals(split[1])) {
			_operandType = OperandType.NO_OPERAND;
		} else if ("m".equals(split[1])) {
			_operandType = OperandType.MEMORY;
		} else if ("r1".equals(split[1])) {
			_operandType = OperandType.REG;
		} else if ("r1,r2".equals(split[1])) {
			_operandType = OperandType.REG1_REG2;
		} else {
			throw new RuntimeException("wrong instruction table file (unrecognizable operand type)\n\n" + line);
		}

		if ("2".equals(split[2])) {
			_format = Format.TWO;
		} else if ("3/4".equals(split[2])) {
			_format = Format.THREE_OR_FOUR;
		} else {
			throw new RuntimeException("wrong instruction table file (unrecognizable instruction format)\n\n" + line);
		}

		try {
			_opcode = (byte) (Integer.parseInt(split[3], 16) &	 0xFF);
		} catch (NumberFormatException e) {
			throw new RuntimeException("wrong instruction table file (wrong opcode)\n\n" + line);
		}
	}

	/**
	 * 기계어 명칭을 반환한다.
	 * 
	 * @return 기계어 명칭
	 */
	public String getName() {
		return _name;
	}

	/**
	 * 기계어의 opcode를 반환한다.
	 *
	 * @return 기계어의 opcode
	 */
	public byte getOpcode() {
		return _opcode;
	}

	/**
	 * 기계어의 operand 타입을 반환한다.
	 * 
	 * @return 기계어의 operand 타입.
	 * 
	 *         <ul>
	 *         <li>no: <code>OperandType.NO_OPERAND</code>
	 *         <li>m: <code>OperandType.MEMORY</code>
	 *         <li>r1: <code>OperandType.REG</code>
	 *         <li>r1,r2: <code>OperandType.REG1_REG2</code>
	 *         </ul>
	 */
	public OperandType getOperandType() {
		return _operandType;
	}

	/**
	 * 기계어의 형식을 반환한다.
	 * 
	 * @return 기계어의 형식.
	 * 
	 *         <ul>
	 *         <li>2형식: <code>Format.TWO</code>
	 *         <li>3 혹은 4형식: <code>Format.THREE_FOUR</code>
	 *         </ul>
	 */
	public Format getFormat() {
		return _format;
	}

	private final String _name;
	private final OperandType _operandType;
	private final Format _format;
	private final byte _opcode;
}
