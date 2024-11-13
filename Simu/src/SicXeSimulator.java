import java.io.IOException;
import instruction.InstructionTable;


public class SicXeSimulator {

    private static InstructionTable _instTable;    // 명령어 집합을 저장한다
    ObjectLoader loader;
    InstructionExecutor instExecutor;
    StepIterator steper;


    public SicXeSimulator(ResourceManager resourceManager, String fileName) throws IOException {
        // 명령어 테이블을 초기화한다
        _instTable = new InstructionTable("inst_table.txt");
        loader = new ObjectLoader(fileName, resourceManager);
        instExecutor = new InstructionExecutor(resourceManager, "F1.txt");
        steper = new StepIterator(resourceManager, _instTable);
    }
}
