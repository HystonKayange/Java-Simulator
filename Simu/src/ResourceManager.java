public class ResourceManager {

    public int A;
    public int X;
    public int L;
    public int PC;
    public int SW;

    public int B;
    public int S;
    public int T;
    public int F;

    public String memory;
    // -------------------------------------
    public String fileName;
    public String usingDevice;
    public String instLog;
    public String logMessage;

    public String programName;
    public String programStartAddress;
    public String programEndAddress;
    public String programLength;
    // -------------------------------------
    public int memoryStartAddress;
    public int targetAddress;
    // -------------------------------------
    public boolean endOfProgram;

    public ResourceManager() {
        A = 0;
        X = 0;
        L = 0;
        PC = 0;
        SW = 0;
        B = 0;
        S = 0;
        T = 0;
        F = 0;
        memory = "-".repeat(20000);
        fileName = "";
        instLog = "";
        logMessage = "";

        usingDevice = "";
        programName = "";
        programStartAddress = "";
        programEndAddress = "";
        programLength = "";
        memoryStartAddress = 0;
        targetAddress = 0;
        endOfProgram = false;
    }

    public void printResource(){
        System.out.println("< A:"+String.format("%X", A)+
                            ", X:"+String.format("%X", X)+
                            ", L:"+String.format("%X", L/2)+
                            ", PC:"+String.format("%X", PC/2)+
                            ", SW:"+String.format("%X", SW) +
                            ", B:"+String.format("%X", B) +
                            ", S:"+String.format("%X", S) +
                            ", T:"+String.format("%X", T) +
                            ", F:"+String.format("%X >", F));


    }

    
}
