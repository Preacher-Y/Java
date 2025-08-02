package Tutorial1Stringbuilder;
public class StringBuilderClass{
    public static void main(String[] args) {
        // Using StringBuilder to build a string
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" World");
        // Convert StringBuilder to String
        String result = sb.toString();
        int age=32;
        // String Formatting
        System.out.println(String.format("%s ,this world is for people with age of %d",result,age));
        // Notice i changed the println to printf
        System.out.printf("%s ,this world is for people with age of %d",result,age);
    }
}
