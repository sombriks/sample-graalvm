package sample.graalvm

import io.javalin.Javalin
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.jupiter.api.Test

class AppTest2 {

    @Test
    fun `should have a javalin instance`() {
        val app = App()
        assertThat(app.javalin, IsInstanceOf(Javalin::class.java))
    }
}
