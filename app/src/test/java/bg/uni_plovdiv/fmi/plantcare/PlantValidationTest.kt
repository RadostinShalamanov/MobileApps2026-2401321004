package bg.uni_plovdiv.fmi.plantcare

import bg.uni_plovdiv.fmi.plantcare.domain.PlantValidation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlantValidationTest {

    @Test
    fun name_blankOrNullIsInvalid() {
        assertFalse(PlantValidation.isValidName(null))
        assertFalse(PlantValidation.isValidName("   "))
        assertTrue(PlantValidation.isValidName("Монстера"))
    }

    @Test
    fun interval_validRange() {
        assertEquals(1, PlantValidation.parseInterval("1"))
        assertEquals(7, PlantValidation.parseInterval(" 7 "))
        assertEquals(365, PlantValidation.parseInterval("365"))
    }

    @Test
    fun interval_invalidValues() {
        assertNull(PlantValidation.parseInterval(null))
        assertNull(PlantValidation.parseInterval(""))
        assertNull(PlantValidation.parseInterval("abc"))
        assertNull(PlantValidation.parseInterval("0"))
        assertNull(PlantValidation.parseInterval("366"))
        assertNull(PlantValidation.parseInterval("-3"))
    }
}