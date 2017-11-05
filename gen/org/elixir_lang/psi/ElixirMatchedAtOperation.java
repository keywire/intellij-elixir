// This is a generated file. Not intended for manual editing.
package org.elixir_lang.psi;

import com.ericsson.otp.erlang.OtpErlangObject;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ElixirMatchedAtOperation extends ElixirMatchedExpression, AtNonNumericOperation {

  @NotNull
  ElixirAtPrefixOperator getAtPrefixOperator();

  @Nullable
  ElixirMatchedExpression getMatchedExpression();

  @Nullable
  PsiReference getReference();

  @NotNull
  String moduleAttributeName();

  @Nullable
  Quotable operand();

  @NotNull
  Operator operator();

  @NotNull
  OtpErlangObject quote();

}