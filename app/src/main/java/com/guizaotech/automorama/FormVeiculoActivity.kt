package com.guizaotech.automorama

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.view.ContextMenu
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.guizaotech.automorama.database.AutomoramaDatabase
import com.guizaotech.automorama.helpers.CarregaImagem
import com.guizaotech.automorama.helpers.Codigos_Activity
import com.guizaotech.automorama.helpers.HelperVeiculo
import com.guizaotech.automorama.modelo.Veiculo
import com.guizaotech.automorama.repository.VeiculosRepository
import com.guizaotech.automorama.viewModel.FormVeiculosViewModel
import com.guizaotech.automorama.viewModel.factory.FormVeiculosViewModelFactory
import kotlinx.android.synthetic.main.activity_form_veiculo.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.Serializable


class FormVeiculoActivity : AppCompatActivity(), Codigos_Activity {

    var caminhoFoto: String? = ""
    private val codigoCamera = 1
    private val codigoGaleria = 2
    private val codigoPermissaoMemoria = 3

    private val viewModel by lazy {
        val repository = VeiculosRepository(AutomoramaDatabase.getInstance(this).getRoomVeiculoDAO())
        val factory = FormVeiculosViewModelFactory(repository)
        val provedor = ViewModelProviders.of(this, factory)
        provedor.get(FormVeiculosViewModel::class.java)
    }


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_veiculo)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val helper = HelperVeiculo(this)

        val intent = intent
        var veiculo = intent.getSerializableExtra("editarVeiculo")
        //val veiculoPosicao = intent.getIntExtra("veiculoPosicao", -1)

        if (savedInstanceState != null) {
            configuraImagem(savedInstanceState)
        }

        if (veiculo != null) {
            veiculo = veiculo as Veiculo
            helper.preencheVeiculo(veiculo)
            if (veiculo.caminhoImagem != "") {
                floatRemover.visibility = View.VISIBLE
            }
        } else {
            veiculo = Veiculo()
        }

        floatRemover.setOnClickListener {
            removeImagem(helper)
        }

        placa.filters = arrayOf<InputFilter>(InputFilter.AllCaps())

        floatCamera.setOnClickListener {
            this.openContextMenu(floatCamera)
        }

        registerForContextMenu(floatCamera)

        btSalvar.setOnClickListener {
            btSalvar.isClickable = false
            val ehValido = helper.ehValido()
            if (ehValido){
                runBlocking {
                    salva(helper, veiculo)
                }
            }

        }
    }

    private suspend fun salva(
        helper: HelperVeiculo,
        veiculo: Serializable?,
    ) {
        val veiculoNovo: Veiculo = helper.getVeiculo(veiculo = veiculo as Veiculo)
        viewModel.salva(veiculo = veiculoNovo).observe(this, Observer {
            if (it.erro == null) {
                finish()
            } else {
                Toast.makeText(
                    this,
                    it.erro,
                    Toast.LENGTH_SHORT
                ).show()
                btSalvar.isClickable = true
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun removeImagem(helper: HelperVeiculo) {
        caminhoFoto = null
        imageVeiculo.setImageResource(R.drawable.ic_car_list)

        helper.carregaImagem(caminhoFoto)
        floatRemover.visibility = View.INVISIBLE
    }

    private fun configuraImagem(savedInstanceState: Bundle) {
        if (savedInstanceState.getString("imagem") != null) {
            caminhoFoto = savedInstanceState.getString("imagem")
            val helperImagem = CarregaImagem()
            val imagem = helperImagem.giraImagem(caminhoFoto.toString(), 500, 500)
            imageVeiculo.setImageBitmap(imagem)
            imageVeiculo.scaleType = ImageView.ScaleType.FIT_XY
            imageVeiculo.tag = caminhoFoto
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("imagem", caminhoFoto!!)
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        val intent = intent
        val veiculo = intent.getSerializableExtra("editarVeiculo")
        val helper = HelperVeiculo(this)


        if (veiculo != null) {
            helper.preencheVeiculo(veiculo as Veiculo)
            if (veiculo.caminhoImagem != "") {
                floatRemover.visibility = View.VISIBLE
            }
        }

    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val opCamera = menu!!.add("Câmera")
        opCamera.setOnMenuItemClickListener {
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            caminhoFoto = getExternalFilesDir(null).toString() + "/" + System.currentTimeMillis() + ".jpg"
            val foto = File(caminhoFoto)
            val fotoURI =
                FileProvider.getUriForFile(this@FormVeiculoActivity, "com.guizaotech.provider", foto)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI)
            startActivityForResult(intentCamera, codigoCamera)
            false
        }

        val opGaleria = menu.add("Galeria")
        opGaleria.setOnMenuItemClickListener {
            if (ActivityCompat.checkSelfPermission(this@FormVeiculoActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this@FormVeiculoActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), codigoPermissaoMemoria
                )
                true
            } else {
                val abrirGaleria =
                    Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(Intent.createChooser(abrirGaleria, getString(R.string.selecione_um_aplicativo)), codigoGaleria)
                false
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val helper = HelperVeiculo(this)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == codigoCamera) {
                helper.carregaImagem(caminhoFoto)


            } else if (requestCode == codigoGaleria) {
                caminhoFoto = getImagePath(data?.data!!)
                helper.carregaImagem(caminhoFoto)

            }

            floatRemover.visibility = View.VISIBLE
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == codigoPermissaoMemoria) {
            val abrirGaleria = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(Intent.createChooser(abrirGaleria, getString(R.string.selecione_um_aplicativo)), codigoGaleria)
        }
    }

    fun getImagePath(contentUri: Uri): String {
        val campos = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, campos, null, null, null)
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }
}
