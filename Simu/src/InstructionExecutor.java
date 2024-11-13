import java.io.*;
import java.util.Objects;

public class InstructionExecutor {

    ResourceManager _resourceManager;
    int rdIndex;
    int wdIndex;
    String rdSentence;
    String wdSentence;
    boolean endOfProgram;

    public InstructionExecutor(ResourceManager resourceManager, String fileName) {
        rdIndex = 0;
        wdIndex = 0;
//        rdSentence = "NICE ON SONNY! NICE ON SON! NICE ON SONNY! LET A HAVE A ONE!";
        rdSentence = rdFile(fileName);
        wdSentence = "";
        endOfProgram = false;
        _resourceManager = resourceManager;
    }

    public void execute(StepIterator steper) {
        String code = steper.objectCode;
        String log = "";
        if(!code.isEmpty())
            _resourceManager.instLog = code;
        int k = Integer.parseInt(code, 16);
        boolean nBit, iBit, xBit, pBit, eBit;
        String memory, opcode = "";
        char r1, r2;
        int memoryLocation = 0;
        int num, num2; // 레지스터 값 계산

        opcode = String.format("%" + code.length() * 4 + "s", Integer.toBinaryString(k)).replace(' ', '0');
        r1 = code.charAt(2);
        r2 = code.charAt(3);
        memory = code.substring(3);
        nBit = opcode.charAt(6) == '1'; iBit = opcode.charAt(7) == '1'; xBit = opcode.charAt(8) == '1';
        pBit = opcode.charAt(10) == '1'; eBit = opcode.charAt(11) == '1';
        opcode = opcode.substring(0, 6);

        StringBuilder memoryBuilder = new StringBuilder(_resourceManager.memory);
        switch (opcode){
            case "000101":  // case STL
                log = "STL";
                memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                memoryBuilder.replace(memoryLocation, memoryLocation + 6, String.format("%06X", _resourceManager.L));
                break;
            case "010010":  // case JSUB
                log = "JSUB";
                _resourceManager.L = _resourceManager.PC;
                _resourceManager.PC = 2 * Integer.parseInt(memory, 16);
                memoryLocation = _resourceManager.PC;
                break;
            case "101101":  // case CLEAR
                log = "CLEAR";
                num = r1 - '0';
                switch (num){
                    case 0: _resourceManager.A = 0; break;
                    case 1: _resourceManager.X = 0; break;
                    case 2: _resourceManager.L = 0; break;
                    case 3: _resourceManager.B = 0; break;
                    case 4: _resourceManager.S = 0; break;
                    case 5: _resourceManager.T = 0; break;
                    case 6: _resourceManager.F = 0; break;
                    case 8: _resourceManager.PC = 0; break;
                    case 9: _resourceManager.SW = 0; break;
                }
                break;
            case "000000":  // case LDA
                log = "LDA";
                if(!nBit & iBit){
                    _resourceManager.A = Integer.parseInt(memory, 16);
                    memoryLocation= _resourceManager.A * 2;
                }else{
                    memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                    _resourceManager.A = Integer.parseInt(memoryBuilder.substring(memoryLocation, memoryLocation+6), 16);
                }

                break;
            case "011101":  // case LDT
                log = "LDT";
                if(eBit){
                    memoryLocation = 2 * (Integer.parseInt(memory, 16));
                    _resourceManager.T = Integer.parseInt(memoryBuilder.substring(memoryLocation, memoryLocation+6), 16);
                }else{
                    memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                    _resourceManager.T = Integer.parseInt(memoryBuilder.substring(memoryLocation, memoryLocation+6), 16);
                }
                break;
            case "010100":  // case LDCH
                log = "LDCH";
                memoryLocation = 2 * (Integer.parseInt(memory, 16) + _resourceManager.X);
                _resourceManager.A = Integer.parseInt(memoryBuilder.substring(memoryLocation, memoryLocation+2), 16);
                break;
            case "110111":  // case WD
                log = "WD";
                wdDevice((char)_resourceManager.A);
                memoryLocation = 2 * (Integer.parseInt(memory, 16) + _resourceManager.PC/2);
                System.out.println("wdSentence : " + wdSentence);
                break;
            case "111000":  // case TD
                log = "TD";
                memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                _resourceManager.usingDevice = memoryBuilder.substring(memoryLocation, memoryLocation + 2);
                _resourceManager.SW = 1;
                break;
            case "001100":  // case JEQ
                log = "JEQ";
                if(_resourceManager.SW == 0){
                    if(memory.charAt(0) == 'F'){
                        _resourceManager.PC = 2 * (_resourceManager.PC/2 - (Integer.parseInt("1000", 16) - Integer.parseInt(memory, 16)) );
                        memoryLocation = _resourceManager.PC;
                    }
                    else{
                        _resourceManager.PC = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                        memoryLocation = _resourceManager.PC;
                    }
                }else{
                    if(memory.charAt(0) == 'F'){
                        memoryLocation = 2 * (_resourceManager.PC/2 - (Integer.parseInt("1000", 16) - Integer.parseInt(memory, 16)) );
                    }
                    else{
                        memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                    }
                }
                break;
            case "001111":  // case J
                log = "J";
                if(nBit & !iBit){
                    memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16) );
                    _resourceManager.PC = Integer.parseInt(memoryBuilder.substring(memoryLocation, memoryLocation+6),16);
                    endOfProgram = true;
                    _resourceManager.endOfProgram = true;
                }else{
                    if(memory.charAt(0) == 'F')
                        _resourceManager.PC = 2 * (_resourceManager.PC/2 - (Integer.parseInt("1000", 16) - Integer.parseInt(memory, 16)) );
                    else
                        _resourceManager.PC = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                }
                break;
            case "110110":  // case RD
                log = "RD";
                memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                _resourceManager.A = rdDevice();
                break;
            case "001010":  // case COMP
                log = "COMP";
                num = Integer.parseInt(memory, 16);
                if(_resourceManager.A == num){
                    _resourceManager.SW = 0;
                }else{
                    _resourceManager.SW = 1;
                }
                break;
            case "101000":  // case COMPR
                log = "COMPR";
                num = r1 - '0';
                switch (num){
                    case 0: num = _resourceManager.A; break;
                    case 1: num = _resourceManager.X; break;
                    case 2: num = _resourceManager.L; break;
                    case 3: num = _resourceManager.B; break;
                    case 4: num = _resourceManager.S; break;
                    case 5: num = _resourceManager.T; break;
                    case 6: num = _resourceManager.F; break;
                    case 8: num = _resourceManager.PC; break;
                    case 9: num = _resourceManager.SW; break;
                }
                num2 = r2 - '0';
                switch (num2){
                    case 0: num2 = _resourceManager.A; break;
                    case 1: num2 = _resourceManager.X; break;
                    case 2: num2 = _resourceManager.L; break;
                    case 3: num2 = _resourceManager.B; break;
                    case 4: num2 = _resourceManager.S; break;
                    case 5: num2 = _resourceManager.T; break;
                    case 6: num2 = _resourceManager.F; break;
                    case 8: num2 = _resourceManager.PC; break;
                    case 9: num2 = _resourceManager.SW; break;
                }
                if(num == num2){
                    _resourceManager.SW = 0;
                }else{
                    _resourceManager.SW++;
                }
                break;
            case "000011":  // case STA
                log = "STA";
                if(eBit){
                    memoryLocation = 2 * (Integer.parseInt(memory, 16));
                    memoryBuilder.replace(memoryLocation,memoryLocation + 6, String.format("%06X", _resourceManager.A));
                }else{
                    memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                    memoryBuilder.replace(memoryLocation,memoryLocation + 6, String.format("%06X", _resourceManager.A));
                }
                break;
            case "010101":  // case STCH
                log = "STCH";
                if(eBit){
                    memoryLocation = 2 * (Integer.parseInt(memory, 16) + _resourceManager.X);
                    memoryBuilder.replace(memoryLocation,memoryLocation + 2, String.format("%02X", _resourceManager.A));
                }else{
                    memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16) + _resourceManager.X);
                    memoryBuilder.replace(memoryLocation,memoryLocation + 2, String.format("%02X", _resourceManager.A));
                }
                break;
            case "000100":  // case STX
                log = "STX";
                if(eBit){
                    memoryLocation = 2 * (Integer.parseInt(memory, 16));
                    memoryBuilder.replace(memoryLocation,memoryLocation + 6, String.format("%06X", _resourceManager.X));
                }else{
                    memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                    memoryBuilder.replace(memoryLocation,memoryLocation + 6, String.format("%06X", _resourceManager.X));
                }
                break;
            case "010011":  //case RSUB
                log = "RSUB";
                _resourceManager.PC = _resourceManager.L;
                _resourceManager.usingDevice = "";
                break;
            case "101110":  //case TIXR
                log = "TIXR";
                _resourceManager.X += 1;
                num = r1 - '0';
                switch (num){
                    case 0: num = _resourceManager.A; break;
                    case 1: num = _resourceManager.X; break;
                    case 2: num = _resourceManager.L; break;
                    case 3: num = _resourceManager.B; break;
                    case 4: num = _resourceManager.S; break;
                    case 5: num = _resourceManager.T; break;
                    case 6: num = _resourceManager.F; break;
                    case 8: num = _resourceManager.PC; break;
                    case 9: num = _resourceManager.SW; break;
                }
                if(num == _resourceManager.X)
                    _resourceManager.SW = 0;
                else if(num < _resourceManager.X)
                    _resourceManager.SW++;
                else if(num > _resourceManager.X)
                    _resourceManager.SW = -1;
                break;
            case "001110":  //case JLT
                log = "JLT";
                if(_resourceManager.SW < 0){
                    if(memory.charAt(0) == 'F'){
                        _resourceManager.PC = 2 * (_resourceManager.PC/2 - (Integer.parseInt("1000", 16) - Integer.parseInt(memory, 16)) );
                        memoryLocation = _resourceManager.PC;
                    }
                    else{
                        _resourceManager.PC = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                        memoryLocation = _resourceManager.PC;
                    }
                }else{
                    if(memory.charAt(0) == 'F'){
                        memoryLocation = 2 * (_resourceManager.PC/2 - (Integer.parseInt("1000", 16) - Integer.parseInt(memory, 16)) );
                    }
                    else{
                        memoryLocation = 2 * (_resourceManager.PC/2 + Integer.parseInt(memory, 16));
                    }
                }
                break;
        }
        _resourceManager.memory = memoryBuilder.toString();
        _resourceManager.targetAddress = memoryLocation;
        if(!log.isEmpty())
            _resourceManager.logMessage = log;
    }

    public String rdFile(String filePath){
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int character;
            while ((character = br.read()) != -1) {
                text.append((char)character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public char rdDevice(){
        try{
            return rdSentence.charAt(rdIndex++);
        }catch(Exception e){
            return 0;
        }
    }

    public void wdDevice(char c){
        wdSentence += c;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("outDevice.txt"))) {
            bw.write(wdSentence);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
