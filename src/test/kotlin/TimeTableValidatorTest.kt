import com.alameyo.furiouscinema.inputvalidation.InputValidationException
import com.alameyo.furiouscinema.inputvalidation.TimeTableValidator
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class TimeTableValidatorTest {
    private val timeTableValidator = TimeTableValidator()

    @Test
    fun `Valid input - should not throw exception`() {
        val validInput =
            """{
                "date": "2021-11-11",
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
                  "startHour": "15:10",
                  "endHour": "16:50",
                  "price": "22"
                }
              ]
            }""".trim()

        assertDoesNotThrow { timeTableValidator.validate(validInput) }
    }

    @Test
    fun `If input cannot be parsed to JsonObject - should throw InputValidationException`() {
        val notParsableJson =
            """{{{{
                "date": "2021-11-11",;
                "timeSlots": [
                {
                  "room": 4,,
                  "movieId": "tt0232500",
                  "startHour": "12:40",;;
                  "endHour": "14:50",
                  "price": "22"
                }
              ]
            }""".trim()
        assertThrows(InputValidationException::class.java) { timeTableValidator.validate(notParsableJson) }
    }

    @Test
    fun `If input have invalid date - should throw InputValidationException`() {
        val invalidDateJson =
            """{
                "date": "20-11-11",
                "timeSlots": [
                {
                  "room": 4,
                  "movieId": "tt0232500",
                  "startHour": "12:40",
                  "endHour": "14:50",
                  "price": "22"
                }
              ]
            }""".trim()
        assertThrows(InputValidationException::class.java) { timeTableValidator.validate(invalidDateJson) }
    }

    @Test
    fun `Room is not parsable to Integer - should throw InputValidationException`() {
        val invalidDateJson =
            """{
                "date": "2021-11-11",
                "timeSlots": [
                {
                  "room": "test",
                  "movieId": "tt0232500",
                  "startHour": "12:40",
                  "endHour": "14:50",
                  "price": "22"
                }
              ]
            }""".trim()
        assertThrows(InputValidationException::class.java) { timeTableValidator.validate(invalidDateJson) }
    }

    @Test
    fun `MovieIds does not conform format - should throw InputValidationException`() {
        val invalidMovieIds =
            """{
                "date": "2021-11-11",
                "timeSlots": [
                {
                  "room": 4,
                  "movieId": "tt023250032141",
                  "startHour": "12:40",
                  "endHour": "14:50",
                  "price": "22"
                },
                {
                  "room": 4,
                  "movieId": "tt0",
                  "startHour": "15:10",
                  "endHour": "16:50",
                  "price": "22"
                }
              ]
            }""".trim()
        assertThrows(InputValidationException::class.java) { timeTableValidator.validate(invalidMovieIds) }
    }
}
