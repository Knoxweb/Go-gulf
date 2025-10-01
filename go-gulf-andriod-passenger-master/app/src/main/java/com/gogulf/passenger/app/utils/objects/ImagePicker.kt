package com.gogulf.passenger.app.utils.objects

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.gogulf.passenger.app.R
import com.gogulf.passenger.app.utils.customcomponents.CustomAlertDialog
import com.gogulf.passenger.app.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File


object ImagePicker {

    var fileName: Uri? = null
    fun getPickImageChooserIntent(context: Context): Intent {
//        val outputFileUri = getCaptureImageOutputUri(context)
        var outputFileUri: Uri? = null
        /*     try {
                 photoFile = createImageFile()
                 // Continue only if the File was successfully created
                 if (photoFile != null) {
                     outputFileUri = FileProvider.getUriForFile(
                         this,
                         "com.mydriver.au.chauffeur.provider",
                         photoFile!!
                     )
                 }
             } catch (ex: Exception) {
                 // Error occurred while creating the File
                 displayMessage(baseContext, ex.message.toString())
             }*/

        Log.e("ImagePicker", "image $outputFileUri")
        var allIntent = ArrayList<Intent>()
        var packageManager = context.packageManager

        //collect all camera intents
        var captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            var intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.`package` = res.activityInfo.packageName
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            } else {
                Log.e("ImagePickers", " outputURi file is null.")
            }
            allIntent.add(intent)
        }

        //collect all gallery intent
        var galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        var listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            var intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.`package` = res.activityInfo.packageName
            allIntent.add(intent)
        }

        //the main intent is the last in the list
        var mainIntent = allIntent[allIntent.size - 1]
        for (intent in allIntent) {
            if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntent.remove(mainIntent)

        //Create a choose from the main intent
        var chooserIntent = Intent.createChooser(mainIntent, "Select source")
        //Add all other intent
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntent.toTypedArray())

        Log.e("RegisterActivity", "successful here")
        return chooserIntent
    }

    fun getPickImageResultUri(context: Context, data: Intent?): Uri? {

        Log.e("RegisterActivity", "getPickImageResult  $data")
        var isCamera = true
        if (data != null) {
            var action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }


        Log.e("RegisterActivity", "getCamera $isCamera")
        return if (isCamera) {
            getCaptureImageOutputUri(context)
        } else
            data?.data
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getCaptureImageOutputUri(context: Context): Uri? {
        var outputFileUri: Uri? = null
        var getImage = context.externalCacheDir
        if (getImage != null) {
//            outputFileUri = Uri.fromFile(File(getImage.path, "profile.jpg"))
            outputFileUri = FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider",
                File(getImage.path, "pickImageResult.jpeg")
            )
        }
        fileName = outputFileUri
        return outputFileUri

    }


    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun showMessageOkCancel(
        context: Context,
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        CustomAlertDialog(context).setMessage(message)
            .setPositiveText("OK", okListener)
            .setCancellable(false)
            .show()
    }

    fun findUnAskedPermissions(context: Context, wanted: ArrayList<String>): ArrayList<String> {
        var result = java.util.ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(context, perm)) {
                result.add(perm)
            }
        }
        return result
    }


    fun hasPermission(context: Context, permission: String): Boolean {
        if (canMakeSmores()) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    fun canMakeSmores(): Boolean {
        return true
    }

    fun checkPermission(applicationContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        /* val result3 = ContextCompat.checkSelfPermission(
             applicationContext,
             Manifest.permission.ACCESS_BACKGROUND_LOCATION
         )*/
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED /*&& result3 == PackageManager.PERMISSION_GRANTED*/
    }

    fun checkPermissionMedia(applicationContext: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            )
            val result1 =
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )

            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        } else {
            val result = ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            )
            return result == PackageManager.PERMISSION_GRANTED
        }
    }

    fun checkPermissionBackground(applicationContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        DebugMode.e("ImagePicker", result.toString())
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkBackgroundPermission(applicationContext: Activity) {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                applicationContext, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                10
            )
        }
    }

    fun checkPermissionAll(applicationContext: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val result2 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        )
        val result3 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
    }

    fun accessCameraDialog(activity: Context) {
        CustomAlertDialog(activity)
            .setIcon(R.drawable.ic_launcher_foreground)
            .setTitle("Update Files And Media settings")
            .setMessage("Allow us to access your files and media so you can upload the media.")
            .setPositiveText("UPDATE SETTINGS") { dialog, _ ->
                dialog.dismiss()
                settingNavigation(activity)
            }
            .setCancellable(false)
            .setNegativeText("NO THANKS") { dialog, _ ->
                showMessageOkCancel(
                    activity,
                    Constants.ERROR_MESSAGE_CAMERA_PERMISSION
                ) { d, _ ->
                    d.dismiss()
//                    settingNavigation(activity)

                }
                dialog.dismiss()
            }
            .show()
    }

    fun settingNavigation(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        // This will take the user to a page where they have to click twice to drill down to grant the permission
        context.startActivity(intent)
    }

    fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.NO_WRAP)
    }

}