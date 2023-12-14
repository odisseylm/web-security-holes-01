package com.mvv.test

import org.assertj.core.api.SoftAssertions


fun useAssertJSoftAssertions(block: SoftAssertions.() -> Unit) {
    val a = SoftAssertions()
    a.block()
    a.assertAll()
}
