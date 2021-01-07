// This is a generated file. Not intended for manual editing.
package org.elixir_lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.NavigatablePsiElement;
import com.ericsson.otp.erlang.OtpErlangObject;

public interface ElixirAnonymousFunction extends NavigatablePsiElement, Quotable {

  @NotNull
  List<ElixirEndOfExpression> getEndOfExpressionList();

  @NotNull
  ElixirStab getStab();

  @NotNull
  OtpErlangObject quote();

}
