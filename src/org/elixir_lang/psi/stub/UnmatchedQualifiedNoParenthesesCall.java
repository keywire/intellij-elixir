package org.elixir_lang.psi.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import org.elixir_lang.psi.ElixirUnmatchedQualifiedNoParenthesesCall;
import org.elixir_lang.psi.stub.call.Stub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class UnmatchedQualifiedNoParenthesesCall extends Stub<ElixirUnmatchedQualifiedNoParenthesesCall> {
    public UnmatchedQualifiedNoParenthesesCall(
            StubElement parent,
            @NotNull IStubElementType elementType,
            @Nullable StringRef resolvedModuleName,
            @Nullable StringRef resolvedFunctionName,
            int resolvedFinalArity,
            boolean hasDoBlockOrKeyword,
            @NotNull StringRef name,
            @NotNull Collection<StringRef> canonicalNameCollection
    ) {
        super(
                parent,
                elementType,
                resolvedModuleName,
                resolvedFunctionName,
                resolvedFinalArity,
                hasDoBlockOrKeyword,
                name,
                canonicalNameCollection
        );
    }

    public UnmatchedQualifiedNoParenthesesCall(
            StubElement parent,
            @NotNull IStubElementType elementType,
            @Nullable String resolvedModuleName,
            @Nullable String resolvedFunctionName,
            int resolvedFinalArity,
            boolean hasDoBlockOrKeyword,
            @NotNull String name,
            @NotNull Collection<String> canonicalNameCollection
    ) {
        super(
                parent,
                elementType,
                resolvedModuleName,
                resolvedFunctionName,
                resolvedFinalArity,
                hasDoBlockOrKeyword,
                name,
                canonicalNameCollection
        );
    }
}
