package com.levifreire.persistencia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.levifreire.persistencia.databinding.ActivityMainBinding
import permissions.dispatcher.RuntimePermissions
import java.io.*
import java.lang.StringBuilder
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied

@RuntimePermissions
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

        binding.btnOpenPref.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }

        binding.btnReadPref.setOnClickListener {
            readPrefs()
        }
    }

    private fun readPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val city =
            prefs.getString(getString(R.string.pref_city), getString(R.string.pref_city_default))
        val socialNetwork = prefs.getString(
            getString(R.string.pref_social_network),
            getString(R.string.pref_social_network_default)
        )
        val messages = prefs.getBoolean(getString(R.string.pref_messages), false)
        val msg = String.format(
            "%s = %s\n%s = %s\n%s = %s",
            getString(R.string.title_city), city,
            getString(R.string.title_social_network), socialNetwork,
            getString(R.string.title_messages), messages.toString()
        )
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showDeniedForExternal() {
        Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
    }

    private fun btnReadClick() {
        val type = binding.rgType.checkedRadioButtonId
        when (type) {
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternalWithPermissionCheck(true)
            R.id.rbExternalPublic -> loadFromExternalWithPermissionCheck(false)
        }
    }

    private fun btnSaveClick() {
        val type = binding.rgType.checkedRadioButtonId
        when (type) {
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternalWithPermissionCheck(true)
            R.id.rbExternalPublic -> saveToExternalWithPermissionCheck(false)
        }
    }

    private fun saveToInternal() {
        try {
            val fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE)
            save(fos)
            binding.edtText.setText("")
        } catch (e: Exception) {
            Log.e("NGVL", "Erro ao salvar o arquivo", e)
        }
    }

    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun saveToExternal(privateDir: Boolean) {
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
                binding.edtText.setText("")
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

    @NeedsPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    fun loadFromExternal(privateDir: Boolean) {
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