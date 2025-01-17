/*
 *  Copyright (c) 2021 David Allison <davidallisongithub@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ichi2.anki.lint.rules

import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestFile.JavaTestFile
import com.android.tools.lint.checks.infrastructure.TestLintTask.*
import org.intellij.lang.annotations.Language
import org.junit.Test

class JavaNonPublicNonStaticFieldDetectorTest {
    companion object {
        @Language("JAVA")
        private val BADLY_NAMED_VARIABLE = "public class Xx { " +
            "/**" +
            "*/" +
            "private int withCommentShows;" +
            "public int publicIsFine = 1; " +
            "private static int staticIsFine = 1; " +
            "private int ad = 1; " +
            "private int mad = 1; " +
            "private int a = 1; " +
            "private int m = 1; " +
            "private String mOk;" +
            "" +
            " static Animation slide(int type, int duration, int offset) {" +
            "   int i = 5;" +
            "}" +
            "}"

        @Language("kotlin")
        private val BADLY_NAMED_VARIABLE_KOTLIN = """
               class Xx { /***/private val withCommentShows: Int = -1
               val publicIsFine: Int = 1
               private val ad: Int = 1
               private val mad: Int = 1
               private val a : Int = 1
               private val m: Int = 1
               private val mOk: String
               }
        """.trimIndent()
    }

    @Test
    fun showsErrorForNullable() {
        lint()
            .allowMissingSdk()
            .allowCompilationErrors()
            .files(JavaTestFile.create(BADLY_NAMED_VARIABLE))
            .issues(NonPublicNonStaticJavaFieldDetector.ISSUE)
            .run()
            .expectErrorCount(5)
            .check({ })
    }

    @Test
    fun kotlin_no_errors() {
        // #9377 - Kotlin explicitly should not follow our style guide
        lint()
            .allowMissingSdk()
            .allowCompilationErrors()
            .files(TestFile.KotlinTestFile.create(BADLY_NAMED_VARIABLE_KOTLIN))
            .issues(NonPublicNonStaticJavaFieldDetector.ISSUE)
            .run()
            .expectClean()
    }
}
