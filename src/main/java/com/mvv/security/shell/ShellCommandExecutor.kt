package com.mvv.security.shell

import java.io.IOException
import java.io.UncheckedIOException
import java.util.concurrent.TimeUnit
import kotlin.text.Charsets.UTF_8


class CommandExecutor {

    fun processFile(file: String) {
        val p = Runtime.getRuntime().exec("cat $file")

        val outOutput = String(p.inputStream.readAllBytes(), UTF_8)
        val errOutput = String(p.errorStream.readAllBytes(), UTF_8)

        val result = p.waitFor()

        if (outOutput.isNotBlank()) println(outOutput)
        if (errOutput.isNotBlank()) println(errOutput)

        if (result != 0) throw UncheckedIOException(IOException("Error of processing file."))
    }
}

class ShellCommandExecutor {

    fun processFile(file: String) {

        println("/bin/sh -c ls $file")

        // Shell command injection does not work for such simple usage because it does NOT work at all :-)
        // It happens because command line string is parsed to array and seems 'sh' (or 'bash') uses only 1st param (in our case it is 'ls' without params).
        //val p = Runtime.getRuntime().exec("/bin/sh -c ls $file")

        // Of course for the same reason this also does not work at all.
        //val p = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "cat", file))

        // To use shell (sh/bash) properly we need to path full command as 2nd shell's param.
        // And injection works fine in such case.
        val p = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "cat $file"))
        //val p = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", "ls $file"))

        val outOutput = String(p.inputStream.readAllBytes(), UTF_8)
        val errOutput = String(p.errorStream.readAllBytes(), UTF_8)

        val result: Int = try { p.waitFor(5, TimeUnit.SECONDS); p.exitValue() } catch (_: Exception) { -1 }

        if (outOutput.isNotBlank()) println(outOutput)
        if (errOutput.isNotBlank()) println(errOutput)

        if (result != 0) throw UncheckedIOException(IOException("Error of processing file."))
    }
}
