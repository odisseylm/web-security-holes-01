package com.mvv.security.xxe

import org.junit.jupiter.api.Test


class UtilsTest {

    @Test
    fun test_printXml() {
        val xml = safeDocumentBuilder().toXml("<r></r>")
        xml.printXml()
    }
}
