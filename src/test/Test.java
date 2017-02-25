package test;

import lexer.FileUtils;
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
 * Contact: shiyan233@hotmail.com
 *          saltyx.github.io
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
                    lexer.resetSource(file.toAbsolutePath().toString(), Lexer.READ_MODE.READ_FROM_FILE);
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
        System.out.print("testing lexer from string");
        lexer.resetSource("{\"213\":111}", Lexer.READ_MODE.READ_FROM_STRING);
        Token token;
        while ((token = lexer.scan()) != null && token.getToken() != JsonToken.EOF);
        System.out.println(" passed");
    }

    public static void testParser()throws IOException,
            JsonLexException, JsonParseException {
        Parser parser = new Parser(new Lexer(new FileUtils("./test/1.json")));

    }

}
