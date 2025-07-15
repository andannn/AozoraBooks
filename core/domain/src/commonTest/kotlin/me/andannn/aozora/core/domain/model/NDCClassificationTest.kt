package me.andannn.aozora.core.domain.model

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertTrue

class NDCClassificationTest {
    @Test
    fun testNDCClassification() {
        val ndc1 = NDCClassification("1")
        assertTrue(ndc1.mainClassNum == 1)
        assertTrue(ndc1.divisionNum == null)
        assertTrue(ndc1.sectionNum == null)
        assertTrue(ndc1.ndcType == NDCType.MAIN_CLASS)

        val ndc2 = NDCClassification("12")
        assertTrue(ndc2.mainClassNum == 1)
        assertTrue(ndc2.divisionNum == 2)
        assertTrue(ndc2.sectionNum == null)
        assertTrue(ndc2.ndcType == NDCType.DIVISION)

        val ndc3 = NDCClassification("123")
        assertTrue(ndc3.mainClassNum == 1)
        assertTrue(ndc3.divisionNum == 2)
        assertTrue(ndc3.sectionNum == 3)
        assertTrue(ndc3.ndcType == NDCType.SECTION)

        assertFails {
            NDCClassification("")
        }

        assertFails {
            NDCClassification("1234")
        }
    }

    @Test
    fun testNDCClassificationInvalid() {
        assertFails { NDCClassification("1a") }
        assertFails { NDCClassification("12b") }
        assertFails { NDCClassification("123c") }
        assertFails { NDCClassification("1234d") }
        assertFails { NDCClassification("12 3") }
    }

    @Test
    fun testNDCClassificationIsChildOf() {
        val mainClass = NDCClassification("1")
        val division = NDCClassification("12")
        val section = NDCClassification("123")

        assertTrue(section.isChildOf(division))
        assertTrue(division.isChildOf(mainClass))
        assertTrue(!section.isChildOf(mainClass))
        assertTrue(!mainClass.isChildOf(division))
        assertTrue(!mainClass.isChildOf(section))
        assertTrue(!division.isChildOf(division))
        assertTrue(!section.isChildOf(section))
        assertTrue(!mainClass.isChildOf(mainClass))
    }
}
