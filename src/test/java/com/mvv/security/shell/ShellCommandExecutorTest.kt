package com.mvv.security.shell

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText


class ShellCommandExecutorTest {
    private val homeDir = System.getProperty("user.home")
    private val tempFile = Path.of("${homeDir}/projects/web-security-holes-01/temp/file-to-delete.txt")

    @Test
    fun processFile_byCommandExecutor() {
        CommandExecutor().processFile("/etc/hosts")
    }

    @Test
    fun processFile_byShellCommandExecutor() {
        ShellCommandExecutor().processFile("/etc/hosts")
    }

    @Test
    fun processFile_attemptToInjectCommand_expectToFail() {
        makeSureTempFileExists(tempFile)

        assertThat(tempFile).exists()

        assertThatCode { CommandExecutor().processFile("/etc/hosts && rm $tempFile && ls ~") }
            .hasMessageContaining("Error of processing file.")

        assertThat(tempFile).exists()
    }

    @Test
    fun processFile_attemptToInjectCommand_expectToFail_02() {
        makeSureTempFileExists(tempFile)

        assertThat(tempFile).exists()

        assertThatCode { CommandExecutor().processFile("/etc/hosts; rm $tempFile") }
            .hasMessageContaining("Error of processing file.")

        assertThat(tempFile).exists()
    }

    @Test
    fun processFile_attemptToInjectShellCommand() {
        makeSureTempFileExists(tempFile)

        assertThat(tempFile).exists()

        ShellCommandExecutor().processFile("/etc/hosts && rm $tempFile && ls ~")

        assertThat(tempFile).doesNotExist()
    }

    @Test
    fun processFile_attemptToInjectShellCommand_02() {
        makeSureTempFileExists(tempFile)

        assertThat(tempFile).exists()

        ShellCommandExecutor().processFile("/etc/hosts; rm $tempFile; ls /etc/hosts ")
        //ShellCommandExecutor().processFile("/etc/hosts; rm $tempFile")

        assertThat(tempFile).doesNotExist()
    }
}


@Suppress("SameParameterValue")
private fun makeSureTempFileExists(fileToDelete: Path) {
    if (!fileToDelete.exists()) {
        fileToDelete.parent.createDirectories()
        fileToDelete.writeText("")
    }
}
