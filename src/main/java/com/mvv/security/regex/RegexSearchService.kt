package com.mvv.security.regex

import java.util.stream.Collectors


class RegexSearchService {

    fun findMatches(content: String, searchRegExStr: String): List<Int> {
        val p = java.util.regex.Pattern.compile(searchRegExStr.trim { it <= ' ' })
        val matcher = p.matcher(content)

        return matcher.results().map { it.start() }.collect(Collectors.toList())
    }
}
