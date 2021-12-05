package org.hyperskill.photoeditor

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.widget.ImageView
import com.google.android.material.slider.Slider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows


@RunWith(RobolectricTestRunner::class)
class Stage5UnitTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java)
    val activity = activityController.setup().get()
    val marginError = 3

    @Test
    fun testShouldCheckSliderExist() {
        val slSaturation = activity.findViewById<Slider>(R.id.slSaturation)

        val message = "does view with id \"slSaturation\" placed in activity?"
        assertNotNull(message, slSaturation)

        val message2 = "\"slider saturation\" should have proper stepSize attribute"
        assertEquals(message2, slSaturation.stepSize, 10f)

        val message3 = "\"slider saturation\" should have proper valueFrom attribute"
        assertEquals(message3, slSaturation.valueFrom, -250f)

        val message4 = "\"slider saturation\" should have proper valueTo attribute"
        assertEquals(message4, slSaturation.valueTo, 250f)

        val message5 = "\"slider saturation\" should have proper initial value"
        assertEquals(message5, slSaturation.value, 0f)
    }

    @Test
    fun testShouldCheckGammaSliderExist() {

        val slGamma = activity.findViewById<Slider>(R.id.slGamma)

        val message = "does view with id \"slGamma\" placed in activity?"
        assertNotNull(message, slGamma)

        val message2 = "\"slider gamma\" should have proper stepSize attribute"
        assertEquals(message2, slGamma.stepSize, 0.2f)

        val message3 = "\"slider gamma\" should have proper valueFrom attribute"
        assertEquals(message3, slGamma.valueFrom, 0.2f)

        val message4 = "\"slider gamma\" should have proper valueTo attribute"
        assertEquals(message4, slGamma.valueTo, 4f)

        val message5 = "\"slider gamma\" should have proper initial value"
        assertEquals(message5, slGamma.value, 1f)
    }

    @Test
    fun testShouldCheckSliderNotCrashingByDefault() {
        val slSaturation = activity.findViewById<Slider>(R.id.slSaturation)
        val ivPhoto = activity.findViewById<ImageView>(R.id.ivPhoto)
        slSaturation.value += slSaturation.stepSize
        val bitmap = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        val message2 = "is \"ivPhoto\" not empty and no crash occurs while swiping slider?"
        assertNotNull(message2, bitmap)
        slSaturation.value -= slSaturation.stepSize
    }

    @Test
    fun testShouldCheckDefaultBitmapEdit() {
        val slBrightness = activity.findViewById<Slider>(R.id.slBrightness)
        val slContrast = activity.findViewById<Slider>(R.id.slContrast)
        val slSaturation = activity.findViewById<Slider>(R.id.slSaturation)
        val slGamma = activity.findViewById<Slider>(R.id.slGamma)
        val ivPhoto = activity.findViewById<ImageView>(R.id.ivPhoto)
        var img0 = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        var RGB0 = img0?.let { singleColor(it,80, 90) }

        runBlocking() {
            slBrightness.value += slBrightness.stepSize
            slContrast.value += slContrast.stepSize * 4
            slContrast.value += slContrast.stepSize
            slSaturation.value += slSaturation.stepSize * 10
            slSaturation.value += slSaturation.stepSize * 5
            slGamma.value -= slGamma.stepSize * 2

            Shadows.shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(200)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
        }

        val img2 = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        val RGB2 = singleColor(img2, 60, 70)
        val message2 = "val0 ${RGB0} val2 ${RGB2}"
        if (RGB0 != null) {
            assertTrue(message2,Math.abs(110-RGB2.first) <= marginError)
            assertTrue(message2,Math.abs(244-RGB2.second) <= marginError)
            assertTrue(message2,Math.abs(255-RGB2.third) <= marginError)
        }
    }

    @Test
    fun testShouldCheckDefaultBitmapEdit2() {
        val slBrightness = activity.findViewById<Slider>(R.id.slBrightness)
        val slContrast = activity.findViewById<Slider>(R.id.slContrast)
        val slSaturation = activity.findViewById<Slider>(R.id.slSaturation)
        val ivPhoto = activity.findViewById<ImageView>(R.id.ivPhoto)
        val slGamma = activity.findViewById<Slider>(R.id.slGamma)
        var img0 = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        var RGB0 = img0?.let { singleColor(it, 80, 90) }

        runBlocking() {
            slGamma.value += slGamma.stepSize * 5
            slSaturation.value += slSaturation.stepSize * 5
            slBrightness.value += slBrightness.stepSize
            slBrightness.value += slBrightness.stepSize
            slContrast.value -= slContrast.stepSize

            Shadows.shadowOf(Looper.getMainLooper()).idle()
            Thread.sleep(200)
            Shadows.shadowOf(Looper.getMainLooper()).idle()
        }

        val img2 = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        val RGB2 = singleColor(img2, 80, 90)
        val message2 = "val0 ${RGB0} val2 ${RGB2}"
        if (RGB0 != null) {
            assertTrue(message2,Math.abs(79-RGB2.first) <= marginError)
            assertTrue(message2,Math.abs(131-RGB2.second) <= marginError)
            assertTrue(message2,Math.abs(195-RGB2.third) <= marginError)
        }
    }

    fun singleColor(source: Bitmap, x0:Int, y0:Int): Triple<Int, Int, Int> {
        val width = source.width
        val height = source.height
        val pixels = IntArray(width * height)
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height)

        var index: Int
        var R: Int
        var G: Int
        var B: Int
        var y = x0
        var x = y0

        index = y * width + x
        // get color
        R = Color.red(pixels[index])
        G = Color.green(pixels[index])
        B = Color.blue(pixels[index])

        return  Triple(R,G,B)
    }

    fun createBitmap(): Bitmap {
        val width = 200
        val height = 100
        val pixels = IntArray(width * height)
        // get pixel array from source

        var R: Int
        var G: Int
        var B: Int
        var index: Int

        for (y in 0 until height) {
            for (x in 0 until width) {
                // get current index in 2D-matrix
                index = y * width + x
                // get color
                R = x % 100 + 40
                G = y % 100 + 80
                B = (x+y) % 100 + 120

                pixels[index] = Color.rgb(R,G,B)

            }
        }
        // output bitmap
        val bitmapOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        bitmapOut.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmapOut
    }
}