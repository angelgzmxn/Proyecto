package co.edu.iub.myfirstproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.math.BigDecimal

@SpringBootApplication
@EnableScheduling
class MyFirstProjectApplication

fun main(args: Array<String>) {
    runApplication<MyFirstProjectApplication>(*args)
}
