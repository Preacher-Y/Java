package Tutorial1;
public class StringBuilderClass{
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        // Using StringBuilder to build a string
        sb.append("Hello");
        sb.append(" World");
        // Convert StringBuilder to String
        String result = sb.toString();
        System.out.println(result);
        
    }
}
