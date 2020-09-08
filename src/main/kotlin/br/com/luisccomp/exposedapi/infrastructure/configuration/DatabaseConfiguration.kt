package br.com.luisccomp.exposedapi.infrastructure.configuration

import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {

    private var database: Database? = null

    @Bean
    fun database(dataSource: DataSource): Database {
        if (database == null) {
            database = Database.Companion.connect(dataSource)
        }

        return database as Database
    }

}