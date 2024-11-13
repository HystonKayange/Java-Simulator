import instruction.Instruction;
import instruction.InstructionTable;

import java.util.Iterator;

public class StepIterator implements Iterator<String> {
    ResourceManager _resourceManager;
    InstructionTable _instTable;
    String objectCode;

    public StepIterator(ResourceManager resourceManager, InstructionTable instructionTable){
        _resourceManager = resourceManager;
        _instTable = instructionTable;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    // PC 값을 기준으로 계속해서 받아들인다
    @Override
    public String next() {
        int index = _resourceManager.PC;
        String tmp = _resourceManager.memory.substring(index, index + 2);
        int opcode = Integer.parseInt(tmp, 16);
        Instruction inst;
        String result = "";

        opcode = opcode & 0b11111100;
        tmp = String.format("%02X", opcode);

        inst = _instTable.search(tmp).get();

        if(inst.getFormat() == Instruction.Format.TWO){
            result = _resourceManager.memory.substring(index, index + 4);
            index += 4;
        }else if(inst.getFormat() == Instruction.Format.THREE_OR_FOUR){
            int ebit = _resourceManager.memory.substring(index + 2, index + 3).charAt(0);
            ebit = ebit & 0b0001;
            if(ebit == 1){ // 5형식
                result = _resourceManager.memory.substring(index, index + 8);
                index += 8;
            }else {
                result = _resourceManager.memory.substring(index, index + 6);
                index += 6;
            }
        }

        _resourceManager.PC = index;
        objectCode = result;
        return objectCode;
    }
}
