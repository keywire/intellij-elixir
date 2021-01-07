package org.elixir_lang.elixir_flex_lexer;

import com.intellij.psi.TokenType;
import org.elixir_lang.ElixirFlexLexer;
import org.elixir_lang.TokenTypeState;
import org.elixir_lang.psi.ElixirTypes;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * See https://github.com/KronicDeth/intellij-elixir/issues/196
 */
@RunWith(Parameterized.class)
public class SigilPromoterTest extends Test {
    /*
     * Fields
     */

    private CharSequence identifierCharSequence;
    private List<TokenTypeState> tokenTypeStates;

    /*
     * Constructors
     */

    public SigilPromoterTest(CharSequence identifierCharSequence, List<TokenTypeState> tokenTypeStates) {
        this.identifierCharSequence = identifierCharSequence;
        this.tokenTypeStates = tokenTypeStates;
    }

    /*
     * Static Methods
     */

    @Parameterized.Parameters(
            name = "\"{0}\" parses"
    )
    public static Collection<Object[]> generateData() {
        return Arrays.asList(new Object[][]{
                        {
                                "def f () do\nend",
                                Arrays.asList(
                                        new TokenTypeState(ElixirTypes.IDENTIFIER_TOKEN, ElixirFlexLexer.AFTER_UNQUALIFIED_IDENTIFIER),
                                        new TokenTypeState(TokenType.WHITE_SPACE, ElixirFlexLexer.AFTER_IDENTIFIER_WHITE_SPACE),
                                        new TokenTypeState(ElixirTypes.IDENTIFIER_TOKEN, ElixirFlexLexer.AFTER_UNQUALIFIED_IDENTIFIER),
                                        new TokenTypeState(TokenType.WHITE_SPACE, ElixirFlexLexer.AFTER_IDENTIFIER_WHITE_SPACE),
                                        new TokenTypeState(ElixirTypes.OPENING_PARENTHESIS, ElixirFlexLexer.MULTILINE_WHITE_SPACE_MAYBE),
                                        new TokenTypeState(ElixirTypes.CLOSING_PARENTHESIS, ElixirFlexLexer.ADDITION_OR_SUBTRACTION_MAYBE),
                                        new TokenTypeState(TokenType.WHITE_SPACE, ElixirFlexLexer.ADDITION_OR_SUBTRACTION_MAYBE),
                                        new TokenTypeState(ElixirTypes.DO, ElixirFlexLexer.KEYWORD_PAIR_OR_MULTILINE_WHITE_SPACE_MAYBE),
                                        new TokenTypeState(TokenType.WHITE_SPACE, ElixirFlexLexer.YYINITIAL),
                                        new TokenTypeState(ElixirTypes.END, ElixirFlexLexer.KEYWORD_PAIR_MAYBE)
                                )
                        },
                        {
                                "def f (~) do\nend",
                                Arrays.asList(
                                        new TokenTypeState(ElixirTypes.IDENTIFIER_TOKEN, ElixirFlexLexer.AFTER_UNQUALIFIED_IDENTIFIER),
                                        new TokenTypeState(TokenType.WHITE_SPACE, ElixirFlexLexer.AFTER_IDENTIFIER_WHITE_SPACE),
                                        new TokenTypeState(ElixirTypes.IDENTIFIER_TOKEN, ElixirFlexLexer.AFTER_UNQUALIFIED_IDENTIFIER),
                                        new TokenTypeState(TokenType.WHITE_SPACE, ElixirFlexLexer.AFTER_IDENTIFIER_WHITE_SPACE),
                                        new TokenTypeState(ElixirTypes.OPENING_PARENTHESIS, ElixirFlexLexer.MULTILINE_WHITE_SPACE_MAYBE),
                                        new TokenTypeState(ElixirTypes.TILDE, ElixirFlexLexer.SIGIL),
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.SIGIL), // )
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.SIGIL), // space
                                        new TokenTypeState(ElixirTypes.INTERPOLATING_SIGIL_NAME, ElixirFlexLexer.NAMED_SIGIL), // d
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.NAMED_SIGIL), // o
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.NAMED_SIGIL), // \n
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.NAMED_SIGIL), // e
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.NAMED_SIGIL), // n
                                        new TokenTypeState(TokenType.BAD_CHARACTER, ElixirFlexLexer.NAMED_SIGIL) // d
                                )
                        }
                }
        );
    }

    /*
     * Instance Methods
     */

    @org.junit.Test
    public void sigilPromoter() {
        start(identifierCharSequence);

        for (TokenTypeState tokenTypeState: tokenTypeStates) {
            assertEquals(tokenTypeState.tokenType, lexer.getTokenType());

            lexer.advance();

            assertEquals(tokenTypeState.state, lexer.getState());
        }

        lexer.advance();

        assert lexer.getTokenType() == null;
    }
}
