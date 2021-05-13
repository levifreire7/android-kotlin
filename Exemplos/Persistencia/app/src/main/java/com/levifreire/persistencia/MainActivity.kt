package com.levifreire.persistencia

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.levifreire.persistencia.databinding.ActivityMainBinding
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRead.setOnClickListener {
            btnReadClick()
        }
        binding.btnSave.setOnClickListener {
            btnSaveClick()
        }
    }

    private fun btnReadClick() {
        val type = binding.rgType.checkedRadioButtonId
        when (type) {
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternal(true)
            R.id.rbExternalPublic -> loadFromExternal(false)
        }
    }

    private fun btnSaveClick() {
        val type = binding.rgType.checkedRadioButtonId
        when (type) {
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternal(true)
            R.id.rbExternalPublic -> saveToExternal(false)
        }
    }

    private fun saveToInternal() {
        try {
            val fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE)
            save(fos)
        } catch (e: Exception) {
            Log.e("NGVL", "Erro ao salvar o arquivo", e)
        }
    }

    private fun saveToExternal(privateDir: Boolean) {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val myDir = getExternalDir(privateDir)
            try {
                if (myDir?.exists() == false) {
                    myDir.mkdir()
                }
                val txtFile = File(myDir, "arquivo.txt")
                if (!txtFile.exists()) {
                    txtFile.createNewFile()
                }
                val fos = FileOutputStream(txtFile)
                save(fos)
            } catch (e: IOException) {
                Log.d("NGVL", "Erro ao salvar arquivo", e)
            }
        } else {
            Log.e("NGVL", "Não é possível escrever no SD Card")
        }
    }

    private fun save(fos: FileOutputStream) {
        val lines = TextUtils.split(binding.edtText.text.toString(), "\n")
        val writer = PrintWriter(fos)

        for (line in lines)
            writer.println(line)

        writer.flush()
        writer.close()
        fos.close()
    }

    private fun getExternalDir(privateDir: Boolean) =
        // SDCard/Android/data/pacote.da.app/files
        if (privateDir) getExternalFilesDir(null)
        // SDCard/DCIM
        else Environment.getExternalStorageDirectory()

    private fun loadFromInternal() {
        try {
            val fis = openFileInput("arquivo.txt")
            load(fis)
        } catch (e: Exception) {
            Toast.makeText(this, "Ainda não foi salvo nenhum arquivo", Toast.LENGTH_LONG).show()
            Log.e("NGVL", "Erro ao carregar o arquivo", e)
        }
    }

    private fun loadFromExternal(privateDir: Boolean) {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state ||
            Environment.MEDIA_MOUNTED_READ_ONLY == state
        ) {
            val myDir = getExternalDir(privateDir)
            if (myDir?.exists() == true) {
                val txtFile = File(myDir, "arquivo.txt")
                if (txtFile.exists()) {
                    try {
                        txtFile.createNewFile()
                        val fis = FileInputStream(txtFile)
                        load(fis)
                    } catch (e: IOException) {
                        Log.d("NGVL", "Erro ao carregar arquivo", e)
                    }
                }
            }
        } else {
            Log.e("NGVL", "SD Card indisponível")
        }
    }

    private fun load(fis: FileInputStream) {
        val reader = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()

        do {
            val line = reader.readLine() ?: break
            if (sb.isNotEmpty()) sb.append('\n')
            sb.append(line)
        } while (true)

        reader.close()
        fis.close()
        binding.txtText.text = sb.toString()
    }
}