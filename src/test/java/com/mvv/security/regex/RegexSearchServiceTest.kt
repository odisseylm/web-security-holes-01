package com.mvv.security.regex

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.util.regex.Pattern


/*
 According to the article RSPEC-2631, the ReDoS issue has been handled in Java 9 and later:

 Java runtimes like OpenJDK 9+ are mitigating this problem by having additional protections in their implementation
 of regular expression evaluation. In those runtime the example above is not vulnerable.
*/
class RegexSearchServiceTest {

    @Test
    fun findMatches() {
        val content = "12"
        //val searchRegExPattern = "\\^((ab)*)+$"
        val searchRegExPattern = "\\d+"
        val result = RegexSearchService().findMatches(content, searchRegExPattern)

        assertThat(result).isNotEmpty

        println(result)
    }

    //@Test
    @RepeatedTest(10)
    fun findMatches_reDoS() {
        //val content = "abababababababababababababababababababababababababababababababababababababababababababababababababababababab"
        //val content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!"
        //val content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

        //val searchRegExPattern = "\\^((ab)*)+$"
        //val searchRegExPattern = "\\^((ab)*)+\\$"
        //val searchRegExPattern = "(ab)"
        //val searchRegExPattern = "^((ab)*)$"
        //val searchRegExPattern = "((((a+)+)+)+)+"
        //val searchRegExPattern = "((ab)*)+"

        //val content = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!"
        //val searchRegExPattern = "(.*a){11}"

        //val result = RegexSearchService().findMatches(content, searchRegExPattern)
        //println(result)

        val mr = Pattern.compile("(.*a){11}").matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!").find()
        //Pattern.compile("(.*a){11}").matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!")
    }

    //@Test
    @RepeatedTest(3)
    fun reDoS_01() {
        // It does not hang in Java 9+
        // Pattern.compile("(a|aa)+")
        //    .matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab")
        //    .matches()


        // But this nice example works only with 'matches'; with 'find' it does not work.

        // It hangs java
        //Pattern.compile("(.*a){10000}").matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!").matches()

        // It takes 2-3 secs
        Pattern.compile("(.*a){10}").matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!").matches()

        // It takes 8 secs
        // Pattern.compile("(.*a){11}").matcher("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa!").matches()
    }
}
