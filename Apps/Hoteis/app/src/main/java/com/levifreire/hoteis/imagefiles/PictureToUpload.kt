package com.levifreire.hoteis.imagefiles

import okhttp3.MediaType
import java.io.File

data class PictureToUpload(val imageFile: File, val fileType: MediaType)