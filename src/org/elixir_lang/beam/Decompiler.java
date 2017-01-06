package org.elixir_lang.beam;

import com.google.common.base.Joiner;
import com.intellij.diagnostic.LogMessageEx;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.openapi.fileTypes.BinaryFileDecompiler;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.elixir_lang.beam.chunk.Atoms;
import org.elixir_lang.beam.chunk.Exports;
import org.elixir_lang.beam.chunk.exports.Export;
import org.elixir_lang.beam.decompiler.Default;
import org.elixir_lang.beam.decompiler.InfixOperator;
import org.elixir_lang.beam.decompiler.PrefixOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.elixir_lang.psi.call.name.Module.ELIXIR_PREFIX;

public class Decompiler implements BinaryFileDecompiler {
    private static final List<org.elixir_lang.beam.decompiler.MacroNameArity> MACRO_NAME_ARITY_DECOMPILER_LIST =
            new ArrayList<org.elixir_lang.beam.decompiler.MacroNameArity>();

    static {
        MACRO_NAME_ARITY_DECOMPILER_LIST.add(InfixOperator.INSTANCE);
        MACRO_NAME_ARITY_DECOMPILER_LIST.add(PrefixOperator.INSTANCE);
        MACRO_NAME_ARITY_DECOMPILER_LIST.add(Default.INSTANCE);
    }

    @NotNull
    private static CharSequence decompiled(@Nullable Beam beam) {
        StringBuilder decompiled = new StringBuilder("Decompilated Error");

        if (beam != null) {
            Atoms atoms = beam.atoms();

            if (atoms != null) {
                String moduleName = atoms.moduleName();

                if (moduleName != null) {
                    String defmoduleArgument = defmoduleArgument(moduleName);

                    decompiled = new StringBuilder(
                            "# Source code recreated from a .beam file by IntelliJ Elixir\n"
                    )
                            .append("defmodule ")
                            .append(defmoduleArgument)
                            .append(" do\n");

                    appendExports(decompiled, beam, atoms);

                    decompiled.append("end\n");
                }
            }
        }

        return decompiled;
    }

    private static void appendExports(@NotNull StringBuilder decompiled, @NotNull Beam beam, @NotNull Atoms atoms) {
        Exports exports = beam.exports();

        if (exports != null) {
            appendExports(decompiled, exports, atoms);
        }
    }

    private static void appendExports(@NotNull StringBuilder decompiled,
                                      @NotNull Exports exports,
                                      @NotNull Atoms atoms) {
        Pair<SortedMap<String, SortedMap<Integer, Export>>, SortedSet<Export>>
                exportByArityByNameNamelessExportSet = exports.exportByArityByName(atoms);
        SortedMap<String, SortedMap<Integer, Export>> exportByArityByName =
                exportByArityByNameNamelessExportSet.first;

        boolean first = true;
        for (SortedMap.Entry<String, SortedMap<Integer, Export>> nameExportByArity :
                exportByArityByName.entrySet()) {
            String name = nameExportByArity.getKey();

            SortedMap<Integer, Export> exportByArity = nameExportByArity.getValue();

            for (SortedMap.Entry<Integer, Export> arityExport : exportByArity.entrySet()) {
                if (!first) {
                    decompiled.append("\n");
                }

                @Nullable Integer arity = arityExport.getKey();

                MacroNameArity macroNameArity = new MacroNameArity(name, arity);
                appendMacroNameArity(decompiled, macroNameArity);

                first = false;
            }
        }
    }

    private static void appendMacroNameArity(@NotNull StringBuilder decompiled,
                                             @NotNull MacroNameArity macroNameArity) {
        boolean accepted = false;

        for (org.elixir_lang.beam.decompiler.MacroNameArity decompiler : MACRO_NAME_ARITY_DECOMPILER_LIST) {
            if (decompiler.accept(macroNameArity)) {
                decompiler.append(decompiled, macroNameArity);
                accepted = true;
                break;
            }
        }

        if (!accepted) {
            error(macroNameArity);
        }
    }

    private static void error(@NotNull MacroNameArity macroNameArity) {
        com.intellij.openapi.diagnostic.Logger logger = com.intellij.openapi.diagnostic.Logger.getInstance(
                Decompiler.class
        );
        String fullUserMessage = "No decompiler for MacroNameArity (" + macroNameArity +")";
        logger.error(
                LogMessageEx.createEvent(
                        fullUserMessage,
                        Joiner
                                .on("\n")
                                .join(
                                        new Throwable().getStackTrace()
                                ),
                        fullUserMessage,
                        null,
                        Collections.<Attachment>emptyList()
                )
        );
    }

    @NotNull
    public static String defmoduleArgument(String moduleName) {
        String defmoduleArgument;
        if (moduleName.startsWith(ELIXIR_PREFIX)) {
            defmoduleArgument = moduleName.substring(ELIXIR_PREFIX.length());
        } else {
            defmoduleArgument = ":" + moduleName;
        }
        return defmoduleArgument;
    }

    @NotNull
    @Override
    public CharSequence decompile(@NotNull VirtualFile virtualFile) {
        Beam beam = Beam.from(virtualFile);

        return decompiled(beam);
    }
}
