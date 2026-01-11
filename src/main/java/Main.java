import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
  private static final Map<Character, String> TOKEN = Map.ofEntries(
          Map.entry('(', "LEFT_PAREN"),
          Map.entry(')', "RIGHT_PAREN"),
          Map.entry('{', "LEFT_BRACE"),
          Map.entry('}', "RIGHT_BRACE"),
          Map.entry(',', "COMMA"),
          Map.entry('.', "DOT"),
          Map.entry('-', "MINUS"),
          Map.entry('+', "PLUS"),
          Map.entry(';', "SEMICOLON"),
          Map.entry('/', "SLASH"),
          Map.entry('*', "STAR")
  );

  private static final Map<String, String> KEYWORDS = Map.ofEntries(
          Map.entry("and", "AND"),
          Map.entry("class", "CLASS"),
          Map.entry("else", "ELSE"),
          Map.entry("false", "FALSE"),
          Map.entry("for", "FOR"),
          Map.entry("fun", "FUN"),
          Map.entry("if", "IF"),
          Map.entry("nil", "NIL"),
          Map.entry("or", "OR"),
          Map.entry("print", "PRINT"),
          Map.entry("return", "RETURN"),
          Map.entry("super", "SUPER"),
          Map.entry("this", "THIS"),
          Map.entry("true", "TRUE"),
          Map.entry("var", "VAR"),
          Map.entry("while", "WHILE")
  );

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.err.println("Logs from your program will appear here!");

    if (args.length < 2) {
      System.err.println("Usage: ./your_program.sh tokenize <filename>");
      System.exit(1);
    }

    String command = args[0];
    String filename = args[1];

    if (!command.equals("tokenize")) {
      System.err.println("Unknown command: " + command);
      System.exit(1);
    }

    String fileContents = "";
    try {
      fileContents = Files.readString(Path.of(filename));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(1);
    }

     //TODO: Uncomment the code below to pass the first stage

     if (fileContents.length() > 0) {
       int line = 1;
       List<String> errors = new ArrayList<>();
       List<String> tokens = new ArrayList<>();

       for (int i = 0; i < fileContents.length(); i++) {
         char c = fileContents.charAt(i);

         if (c == '\n') {
           line++;
           continue;
         }

         if (c == '=') {
           if (i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
             tokens.add("EQUAL_EQUAL == null");
             i++;
           } else {
             tokens.add("EQUAL = null");
           }
           continue;
         }

         if (c == '!'){
           if(i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
             tokens.add("BANG_EQUAL != null");
             i++;
           } else {
             tokens.add("BANG ! null");
           }
           continue;
         }

         if (c == '<'){
           if(i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
             tokens.add("LESS_EQUAL <= null");
             i++;
           } else {
             tokens.add("LESS < null");
           }
           continue;
         }

         if (c == '>'){
           if(i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
             tokens.add("GREATER_EQUAL >= null");
             i++;
           } else {
             tokens.add("GREATER > null");
           }
           continue;
         }

         if(c == '/'){
           if(i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '/') {
             while( i < fileContents.length() && fileContents.charAt(i) != '\n') {
               i++;
             }
             line++;
             continue;
           }
         }

         //String 처리
         if( c == '"'){
           StringBuilder builder = new StringBuilder();
           int startLine = line;
           i++;

           boolean isString = false;

           while(i<fileContents.length()) {
             char ch = fileContents.charAt(i);

             if(ch == '"'){
               isString = true;
               break;
             }

             if(ch == '\n')
               line++;

             builder.append(ch);
             i++;
           }

           if(!isString) {
             errors.add("[line " + startLine + "] Error: Unterminated string.");
             break;
           }
           tokens.add("STRING " + "\"" + builder.toString() + "\" " + builder.toString());
           continue;
         }

         // NUMBER 처리
         if(Character.isDigit(c)) {
           int start = i;
           boolean hasDot = false;

           while(i<fileContents.length()) {
             char ch = fileContents.charAt(i);

             if(Character.isDigit(ch)) {
               i++;
               continue;
             }

             if(ch == '.' && !hasDot) {
               hasDot = true;
               i++;
               continue;
             }
             break;
           }
           tokens.add("NUMBER " + fileContents.substring(start, i) + " " + Double.parseDouble(fileContents.substring(start, i)));
           i--;
           continue;
         }

         //Identifiers
         if(Character.isLetter(c) || c == '_') {
           int start = i;

           while(i<fileContents.length()) {
             char ch = fileContents.charAt(i);

             if(Character.isLetterOrDigit(ch) || ch == '_') {
               i++;
             }else{
               break;
             }
           }

           String keyword = fileContents.substring(start, i);
           String type = KEYWORDS.getOrDefault(keyword, "IDENTIFIER");
           tokens.add(type + " " + keyword + " null");

           i--;
           continue;
         }


         //TOKEN 문자
         String tokenType = TOKEN.get(c);
         if (tokenType != null) {
           tokens.add(tokenType + " " + c + " null");
           continue;
         }

         if (!Character.isWhitespace(c)) {
           errors.add("[line " + line + "] Error: Unexpected character: " + c);
         }
       }
       for(String error: errors) {
         System.err.println(error);
       }
       for(String token: tokens) {
         System.out.println(token);
       }
       System.out.println("EOF  null");

       if(!errors.isEmpty())
         System.exit(65);
       else
         System.exit(0);

     } else {
       System.out.println("EOF  null"); // Placeholder, replace this line when implementing the scanner
     }
  }
}
