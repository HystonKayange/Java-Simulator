import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ObjectLoader {

    public static HashMap<String, Integer> ExternalSymbolTable;

    private int PROGADDR;
    private int CSADDR;
    private int CSLTH;
    private boolean firstCheck;
    private  int programLength;

    // 생성자로 실행시킨다
    public ObjectLoader(String fileName, ResourceManager resourceManager) throws IOException {
        firstCheck = true;
        programLength = 0;
        ExternalSymbolTable = new HashMap<>();
        String objectCode = readFile(fileName);
        pass2(objectCode, resourceManager);
    }

    // file을 읽으면서 pass1을 동시에 진행한다
    private String readFile(String fileName) throws FileNotFoundException, IOException {
        StringBuilder data = new StringBuilder();
        File file = new File(fileName);
        BufferedReader bufReader = new BufferedReader(new FileReader(file));
        String line = "";

        PROGADDR = (Integer) 0;
        CSADDR = PROGADDR;
        CSLTH = (Integer) 0;

        while ((line = bufReader.readLine()) != null){
            if(line.isEmpty())
                continue;

            data.append(line);
            data.append(System.lineSeparator());
            pass1(line);
        }
        bufReader.close();

        return data.toString();
    }

    // ESTAB만드는 pass1 진행
    private void pass1(String s){
        // Header 경우
        if(s.charAt(0) == 'H'){
            CSADDR += CSLTH;
            ExternalSymbolTable.put(s.substring(1, 7), CSADDR);
            CSLTH = Integer.valueOf(s.substring(12), 16);
        } // Define 경우
        else if(s.charAt(0) == 'D'){
            int loop = 1;
            while((loop + 12) <= s.length()){
                String key = s.substring(loop, loop + 6);
                int value = Integer.parseInt(s.substring(loop + 6, loop + 12), 16);
                ExternalSymbolTable.put(key, CSADDR + value);
                loop += 12;
            }
        }

    }
    // 메모리에 로드하면서 Modify를 수정해준다. --메모리 두칸이 한바이트이므로 2를 곱해준다
    private void pass2(String objectCode, ResourceManager resourceManager) throws IOException {
        int CSADDR = 0;
        // StringReader와 BufferedReader 사용
        try (BufferedReader reader = new BufferedReader(new StringReader(objectCode))) {
            String line;
            StringBuilder memoryBuilder = new StringBuilder(resourceManager.memory);
            while ((line = reader.readLine()) != null) {

                if(line.charAt(0) == 'H') {
                    CSADDR = ExternalSymbolTable.get(line.substring(1, 7)) * 2;
                    programLength += Integer.parseInt(line.substring(13), 16);
                    if(firstCheck){
                        resourceManager.programName = line.substring(1, 7);
                        resourceManager.programStartAddress = line.substring(7, 13);
                        firstCheck = false;
                    }
                }
                else if(line.charAt(0) == 'T') {
                    int start = CSADDR + Integer.parseInt(line.substring(1, 7), 16) * 2;
                    String text = line.substring(9);
                    memoryBuilder.replace(start, start + text.length(), text);
                }
                else if(line.charAt(0) == 'M'){
                    int length = Integer.parseInt(line.substring(7, 9), 16);
                    int start = 0;
                    if(length % 2 == 0){
                        start = CSADDR + 2 * Integer.parseInt(line.substring(1, 7), 16);
                    }else{
                        start = CSADDR + 2 * Integer.parseInt(line.substring(1, 7), 16) + 1;
                    }
                    String key = String.format("%-6s", line.substring(10));

                    int orgin = Integer.parseInt(memoryBuilder.substring(start, start + length), 16);
                    int amender = ExternalSymbolTable.get(key);
                    int result = 0;

                    if(line.charAt(9) == '+'){
                        result = orgin + amender;
                    }else if(line.charAt(9) == '-'){
                        result = orgin - amender;
                    }
                    String changed = String.format("%0" + length +"X", result);
                    memoryBuilder.replace(start, start + length, changed);
                }
            }
            resourceManager.memory = memoryBuilder.toString();
            resourceManager.programLength = String.format("%06X", programLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
