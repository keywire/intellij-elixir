package org.elixir_lang.elixir_flex_lexer.group_heredoc_end.quote;

import com.intellij.psi.tree.IElementType;
import org.elixir_lang.elixir_flex_lexer.TokenTest;

import java.io.IOException;

/**
 * Created by luke.imhoff on 9/12/14.
 */
public abstract class Test extends TokenTest {
    @Override
    protected void reset(CharSequence charSequence) throws IOException {
        CharSequence fullCharSequence = promoter() + '\n' + charSequence;
        super.reset(fullCharSequence);
        // consume promoter
        flexLexer.advance();
        // consume '\n'
        flexLexer.advance();
    }

    protected abstract IElementType fragmentType();
    protected abstract String promoter();
    protected abstract IElementType terminatorType();

    public void tripleDoubleQuotes() throws IOException {
        reset("\"\"\"");
    }

    public void tripleSingleQuotes() throws IOException {
        reset("'''");
    }
}
