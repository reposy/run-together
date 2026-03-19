package sypg.runtogether

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RunTogetherApplication

fun main(args: Array<String>) {
    runApplication<RunTogetherApplication>(*args)
}
