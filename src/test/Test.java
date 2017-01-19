package test;

import lexer.JsonLexException;
import lexer.Lexer;
import parser.JsonParseException;
import parser.Parser;
import token.JsonToken;
import token.Token;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by shiyan on 2017/1/19.
 */
public class Test {

    public static void main(String[] argv) throws IOException,JsonLexException
            , JsonParseException {
        testLexer();
        testParser();
    }

    public static void testLexer() throws IOException, JsonLexException {
        Lexer lexer = new Lexer();
        Files.walkFileTree(Paths.get("./test"), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println("visiting directory:"+dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                if (file.getFileName().toString().matches("^([^.,/]+)\\.json$")) {
                    lexer.setPath(file.toAbsolutePath().toString());
                    System.out.print("test json:"+file + " ");
                    try {
                        Token token;
                        while ((token = lexer.scan()) != null && token.getToken() != JsonToken.EOF);
                        System.out.println("passed");
                    } catch (JsonLexException e) {
                        System.out.println("failed cause by "+e.toString());
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("open "+file+" failed");
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                System.out.println("leaving directory" + dir);
                return FileVisitResult.CONTINUE;
            }
        });
        System.out.println("done.");
    }

    public static void testParser()throws IOException,
            JsonLexException, JsonParseException {
        Parser parser = new Parser(new Lexer("./test/1.json"));

    }

}
