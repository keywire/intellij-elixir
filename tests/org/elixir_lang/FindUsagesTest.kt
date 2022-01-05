package org.elixir_lang

import com.intellij.psi.PsiPolyVariantReference
import com.intellij.usages.UsageInfo2UsageAdapter

class FindUsagesTest : PlatformTestCase() {
    override fun getTestDataPath(): String {
        return "testData/org/elixir_lang/find_usages"
    }

    fun testFunctionRecursiveDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("function_recursive_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(3, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(63, 93))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (3)
 Usages in (3)
  Call definition clause (2)
   light_idea_test_case (2)
     (2)
     function_recursive_declaration.ex (2)
      2def function([], acc), do: acc
      4def function([h | t], acc) do
  Value read (1)
   light_idea_test_case (1)
     (1)
     function_recursive_declaration.ex (1)
      5function(t, [h | acc])
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionRecursiveUsage() {
        val usages = myFixture.testFindUsagesUsingAction("function_recursive_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(3, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(63, 93))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (3)
 Usages in (3)
  Call definition clause (2)
   light_idea_test_case (2)
     (2)
     function_recursive_usage.ex (2)
      2def function([], acc), do: acc
      4def function([h | t], acc) do
  Value read (1)
   light_idea_test_case (1)
     (1)
     function_recursive_usage.ex (1)
      5function(t, [h | acc])
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionSingleClauseUnused() {
        val usages = myFixture.testFindUsagesUsingAction("function_single_clause_unused.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(1, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(26))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (1)
 Usages in (1)
  Call definition clause (1)
   light_idea_test_case (1)
     (1)
     function_single_clause_unused.ex (1)
      2def function, do: :ok
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionMultipleClausesUnused() {
        val usages = myFixture.testFindUsagesUsingAction("function_multiple_clauses_unused.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(26))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Call definition clause (2)
   light_idea_test_case (2)
     (2)
     function_multiple_clauses_unused.ex (2)
      2def function(list) when is_list(list), do: []
      3def function(map) when is_map(map), do: %{}
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionMultipleModulesDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("function_multiple_modules_declaration_target.ex", "function_multiple_modules_declaration_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(31, 50))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Call definition clause (1)
   light_idea_test_case (1)
     (1)
     function_multiple_modules_declaration_target.ex (1)
      2def declaration, do: :ok
  Value read (1)
   light_idea_test_case (1)
     (1)
     function_multiple_modules_declaration_usage.ex (1)
      3Declaration.declaration()
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionMultipleModulesUsage() {
        val usages = myFixture.testFindUsagesUsingAction("function_multiple_modules_usage_target.ex", "function_multiple_modules_usage_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(31, 50))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Call definition clause (1)
   light_idea_test_case (1)
     (1)
     function_multiple_modules_usage_declaration.ex (1)
      2def declaration, do: :ok
  Value read (1)
   light_idea_test_case (1)
     (1)
     function_multiple_modules_usage_target.ex (1)
      3Declaration.declaration()
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionImportDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("function_import_declaration_target.ex", "function_import_declaration_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(31, 60))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Call definition clause (1)
   light_idea_test_case (1)
     (1)
     function_import_declaration_target.ex (1)
      2def declaration, do: :ok
  Value read (1)
   light_idea_test_case (1)
     (1)
     function_import_declaration_usage.ex (1)
      5declaration()
""",
                usageViewTreeTextRepresentation)
    }

    fun testFunctionImportUsage() {
        myFixture.configureByFiles("function_import_usage_target.ex", "function_import_usage_declaration.ex")

        val reference = myFixture.getReferenceAtCaretPositionWithAssertion() as PsiPolyVariantReference
        assertNotNull(reference)

        val resolved = reference.multiResolve(false)
        assertEquals(2, resolved.size)

        val usageInfos = myFixture.findUsages(resolved[0].element!!)
        assertEquals(2, usageInfos.size)
        assertContainsElements( usageInfos.map { it.element!!.textOffset }, listOf(31, 60))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usageInfos)

        assertEquals("""<root> (2)
 Usages in (2)
  Call definition clause (1)
   light_idea_test_case (1)
     (1)
     function_import_usage_declaration.ex (1)
      2def declaration, do: :ok
  Value read (1)
   light_idea_test_case (1)
     (1)
     function_import_usage_target.ex (1)
      5declaration()
""",
                usageViewTreeTextRepresentation)
    }

    fun testParameterDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("parameter_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(39, 70))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Parameter declaration (1)
   light_idea_test_case (1)
     (1)
     parameter_declaration.ex (1)
      2defp function(parameter) do
  Value read (1)
   light_idea_test_case (1)
     (1)
     parameter_declaration.ex (1)
      3%{parameter: parameter}
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleRecursiveDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("module_recursive_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(10, 33))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Alias (1)
   light_idea_test_case (1)
     (1)
     module_recursive_declaration.ex (1)
      2alias Declaration
  Module definition (1)
   light_idea_test_case (1)
     (1)
     module_recursive_declaration.ex (1)
      1defmodule Declaration do
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleRecursiveUsage() {
        myFixture.configureByFile("module_recursive_usage.ex")

        val reference = myFixture.getReferenceAtCaretPositionWithAssertion() as PsiPolyVariantReference
        assertNotNull(reference)

        val resolved = reference.multiResolve(false)
        assertEquals(2, resolved.size)

        val usageInfos = myFixture.findUsages(resolved[0].element!!)
        assertEquals(2, usageInfos.size)
        assertContainsElements( usageInfos.map { it.element!!.textOffset }, listOf(10, 33))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usageInfos)

        assertEquals("""<root> (2)
 Usages in (2)
  Alias (1)
   light_idea_test_case (1)
     (1)
     module_recursive_usage.ex (1)
      2alias Declaration
  Module definition (1)
   light_idea_test_case (1)
     (1)
     module_recursive_usage.ex (1)
      1defmodule Declaration do
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleNestedRecursiveDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("module_nested_recursive_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(3, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(10, 40, 75))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (3)
 Usages in (3)
  Alias (2)
   light_idea_test_case (2)
     (2)
     module_nested_recursive_declaration.ex (2)
      2alias Parent.Declaration
      3alias Parent.{Declaration}
  Module definition (1)
   light_idea_test_case (1)
     (1)
     module_nested_recursive_declaration.ex (1)
      1defmodule Parent.Declaration do
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleMultipleModulesDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("module_multiple_modules_declaration_target.ex", "module_multiple_modules_declaration_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(10, 27))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Alias (1)
   light_idea_test_case (1)
     (1)
     module_multiple_modules_declaration_usage.ex (1)
      2alias Declaration
  Module definition (1)
   light_idea_test_case (1)
     (1)
     module_multiple_modules_declaration_target.ex (1)
      1defmodule Declaration do
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleMultipleModulesUsage() {
        myFixture.configureByFiles("module_multiple_modules_usage_target.ex", "module_multiple_modules_usage_declaration.ex")

        val reference = myFixture.getReferenceAtCaretPositionWithAssertion() as PsiPolyVariantReference
        assertNotNull(reference)

        val resolved = reference.multiResolve(false)
        assertEquals(2, resolved.size)

        val usageInfos = myFixture.findUsages(resolved[0].element!!)
        assertEquals(2, usageInfos.size)
        assertContainsElements( usageInfos.map { it.element!!.textOffset }, listOf(10, 27))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usageInfos)

        assertEquals("""<root> (2)
 Usages in (2)
  Alias (1)
   light_idea_test_case (1)
     (1)
     module_multiple_modules_usage_target.ex (1)
      2alias Declaration
  Module definition (1)
   light_idea_test_case (1)
     (1)
     module_multiple_modules_usage_declaration.ex (1)
      1defmodule Declaration do
""",
                usageViewTreeTextRepresentation)
    }

    fun testParameterUnused() {
        val usages = myFixture.testFindUsagesUsingAction("parameter_unused.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(1, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(39))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (1)
 Usages in (1)
  Parameter declaration (1)
   light_idea_test_case (1)
     (1)
     parameter_unused.ex (1)
      2defp function(_parameter) do
""",
                usageViewTreeTextRepresentation)
    }

    fun testParameterUsage() {
        val usages = myFixture.testFindUsagesUsingAction("parameter_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(39))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Parameter declaration (1)
   light_idea_test_case (1)
     (1)
     parameter_usage.ex (1)
      2defp function(parameter) do
  Value read (1)
   light_idea_test_case (1)
     (1)
     parameter_usage.ex (1)
      3%{parameter: parameter}
""",
                usageViewTreeTextRepresentation)
    }

    fun testVariableDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("variable_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(46, 99))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Value read (1)
   light_idea_test_case (1)
     (1)
     variable_declaration.ex (1)
      5variable
  Value write (1)
   light_idea_test_case (1)
     (1)
     variable_declaration.ex (1)
      3variable = Application.get_env(:variable, :key)
""",
                usageViewTreeTextRepresentation)
    }

    fun testVariableUnused() {
        val usages = myFixture.testFindUsagesUsingAction("variable_unused.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(1, usages.size)

        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(46))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (1)
 Usages in (1)
  Value write (1)
   light_idea_test_case (1)
     (1)
     variable_unused.ex (1)
      3variable = Application.get_env(:variable, :key)
""",
                usageViewTreeTextRepresentation)
    }

    fun testVariableUsage() {
        val usages = myFixture.testFindUsagesUsingAction("variable_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(46))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Value read (1)
   light_idea_test_case (1)
     (1)
     variable_usage.ex (1)
      5variable
  Value write (1)
   light_idea_test_case (1)
     (1)
     variable_usage.ex (1)
      3variable = Application.get_env(:variable, :key)
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleAttributeDeclaration() {
        val usages = myFixture.testFindUsagesUsingAction("module_attribute_declaration.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(31, 69))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Module attribute accumulate or override (1)
   light_idea_test_case (1)
     (1)
     module_attribute_declaration.ex (1)
      2@module_attribute 1
  Module attribute read (1)
   light_idea_test_case (1)
     (1)
     module_attribute_declaration.ex (1)
      4def usage, do: @module_attribute
""",
                usageViewTreeTextRepresentation)
    }

    fun testModuleAttributeUsage() {
        val usages = myFixture.testFindUsagesUsingAction("module_attribute_usage.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(2, usages.size)
        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(31, 69))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (2)
 Usages in (2)
  Module attribute accumulate or override (1)
   light_idea_test_case (1)
     (1)
     module_attribute_usage.ex (1)
      2@module_attribute 1
  Module attribute read (1)
   light_idea_test_case (1)
     (1)
     module_attribute_usage.ex (1)
      4def usage, do: @module_attribute
""",
                usageViewTreeTextRepresentation)
    }

    fun testIssue2374() {
        val usages = myFixture.testFindUsagesUsingAction("issue_2374.ex").map { it as UsageInfo2UsageAdapter }

        assertEquals(3, usages.size)

        usages.map { it.element!!.textOffset }.sorted()

        assertContainsElements( usages.map { it.element!!.textOffset }, listOf(23, 53, 53))

        val usageViewTreeTextRepresentation = myFixture.getUsageViewTreeTextRepresentation(usages.map { it.usageInfo })

        assertEquals("""<root> (3)
 Usages in (3)
  Call definition clause (3)
   light_idea_test_case (3)
     (3)
     issue_2374.ex (3)
      2def foo, do: "foo"
      3def bar, do: foo()
      4def baz, do: foo()
""",
                usageViewTreeTextRepresentation)
    }
}
