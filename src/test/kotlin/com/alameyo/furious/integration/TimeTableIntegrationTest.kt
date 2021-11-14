package com.alameyo.furious.integration

import com.alameyo.furious.TestingHttpClient
import com.alameyo.furiouscinema.Application
import com.alameyo.furiouscinema.asJsonObject
import com.mongodb.client.MongoDatabase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [Application::class])
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["dev"])
//@Disabled("Before enabling run application and database")
class TimeTableIntegrationTest(@Autowired private val database: MongoDatabase) {
    val testingHttpClient = TestingHttpClient()

    @Value("\${spring.security.user.name}")
    private lateinit var user: String

    @Value("\${spring.security.user.password}")
    private lateinit var password: String
    val authenticationPair by lazy { user to password }

    @Test
    @Suppress("LongMethod")
    fun `Add timeTable with 2 timeSlots - should return 201 first time, and 200 after overwriting it, timeTable should have 2 timeSlots`() {
        val timeTable1 =
            """{
                  "date": "2021-11-14",
                  "timeSlots": [
                    {
                      "room": 4,
                      "movieId": "tt0232500",
                      "startHour": "12:40",
                      "endHour": "14:50",
                      "price": "22"
                    },
                    {
                      "room": 4,
                      "movieId": "tt0232500",
                      "startHour": "15:40",
                      "endHour": "22:50",
                      "price": "22"
                    }
                  ]
                }""".trim().asJsonObject()

        val timeTable2 =
            """{
                  "date": "2021-11-14",
                  "timeSlots": [
                    {
                      "room": 3,
                      "movieId": "tt0232500",
                      "startHour": "12:40",
                      "endHour": "14:50",
                      "price": "10"
                    },
                    {
                      "room": 2,
                      "movieId": "tt0232500",
                      "startHour": "15:40",
                      "endHour": "22:50",
                      "price": "10"
                    }
                  ]
                }""".trim().asJsonObject()

        val response1 = testingHttpClient.get("/timetable/2021-11-14")
        assertTrue(response1?.statusCode() == 404, "No timetable should be found for given day")
        assertTrue(response1?.body().isNullOrBlank(), "No timetable should be found for given day")

        val response2 = testingHttpClient.put("/timetable", timeTable1, authenticationPair)
        assertTrue(response2?.statusCode() == 201, "Response code should be 201 when timetable submitted first time")

        val response3 = testingHttpClient.get("/timetable/2021-11-14")
        assertTrue(response3?.body()?.asJsonObject().toString() == timeTable1.toString(), "Should respond with timetable that was provided")


        val response4 = testingHttpClient.put("/timetable", timeTable2, authenticationPair)
        assertTrue(response4?.statusCode() == 200, "Response code should be 200 after timetable was overwritten")

        val response5 = testingHttpClient.get("/timetable/2021-11-14")
        assertTrue(response5?.body()?.asJsonObject().toString() == timeTable2.toString(), "Should respond with timetable that was used for replacement")
    }

    @Test
    fun `Submit invalid timetable with time conflict - should return 400 and timetable should not be saved`() {
        val timeTableWithConflict =
            """{
                  "date": "2021-11-14",
                  "timeSlots": [
                    {
                      "room": 4,
                      "movieId": "tt0232500",
                      "startHour": "12:40",
                      "endHour": "14:50",
                      "price": "22"
                    },
                    {
                      "room": 4,
                      "movieId": "tt0232500",
                      "startHour": "13:40",
                      "endHour": "16:50",
                      "price": "22"
                    }
                  ]
                }""".trim().asJsonObject()

        val response1 = testingHttpClient.get("/timetable/2021-11-14")
        assertTrue(response1?.statusCode() == 404, "No timetable should be found for given day")
        assertTrue(response1?.body().isNullOrBlank(), "No timetable should be found for given day")

        val response2 = testingHttpClient.put("/timetable", timeTableWithConflict, authenticationPair)
        assertTrue(response2?.statusCode() == 400, "Response code should be 400 as incorrect timetable json was submitted")

        val response3 = testingHttpClient.get("/timetable/2021-11-14")
        assertTrue(response3?.statusCode() == 404, "Submitting timetable failed so still no timetable should be found for given day")
        assertTrue(response3?.body().isNullOrBlank(), "Submitting timetable failed so still no timetable should be found for given day")
    }

    @Test
    fun `Put with incorrect credentials - should return 401`() {
        val dummyJson =
            """{"Dummy": "Json" }""".trim().asJsonObject()
        val response = testingHttpClient.put("/timetable", dummyJson, "RandomUser" to "IDontKnowPassword")
        assertTrue(response?.statusCode() == 401, "Response code should be 401 as this user is not authenticated")
    }

    @AfterEach
    fun cleanUp() {
        val timeTablesCollection = database.getCollection("timeTables")
        timeTablesCollection.drop()
    }
}
