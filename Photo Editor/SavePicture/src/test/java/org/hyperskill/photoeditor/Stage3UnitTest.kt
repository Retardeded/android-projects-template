package org.hyperskill.photoeditor

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import android.widget.Button
import android.widget.ImageView
import org.junit.Assert.*
import org.robolectric.Shadows.shadowOf
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


@RunWith(RobolectricTestRunner::class)
class Stage3UnitTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java)
    val activity = activityController.setup().get()

    @Test
    fun testShouldCheckSaveButtonExist() {
        val btnSave = activity.findViewById<Button>(R.id.btnSave)
        val message = "does view with id \"btnSave\" placed in activity?"
        assertNotNull(message, btnSave)
    }

    @Test
    fun testShouldCheckSomeNewBitmapIsCreated() {
        val btnSave = activity.findViewById<Button>(R.id.btnSave)
        val ivPhoto = activity.findViewById<ImageView>(R.id.ivPhoto)
        val bitmap2 = (ivPhoto.getDrawable() as BitmapDrawable).bitmap
        val cr = activity.contentResolver
        val output = ByteArrayOutputStream()
        val crs = shadowOf(cr)
        val uri = Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString() + "/1")
        var message2 = uri.toString()
        crs.registerOutputStream(uri, output)
        btnSave.performClick()
        crs.registerInputStream(uri, ByteArrayInputStream(output.toByteArray()))
        val bitmap = cr.openInputStream(uri!!).use(BitmapFactory::decodeStream)!!
        assertEquals(message2, bitmap2.width, bitmap.width)
    }

}
