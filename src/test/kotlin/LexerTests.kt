import org.junit.jupiter.api.Test
import ru.kitty.parser.Lexer

class LexerTests {

    @Test
    fun test() {
        val code = "54 + 28 - 10"
        val lexer = Lexer(code)
        val tokens = lexer.tokenize()
        tokens.forEach {
            println(it)
        }
    }
}