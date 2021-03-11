package com.guizaotech.automorama.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface

class CarregaImagem {


    private fun abreImagem(caminhoFoto: String, angulo: Float, width: Int, height: Int): Bitmap {
        // Abre o bitmap a partir do caminho da foto
        val bitmap = BitmapFactory.decodeFile(caminhoFoto)
        val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, width, height, true)


        // Prepara a operação de rotação com o ângulo escolhido
        val matrix = Matrix()
        matrix.postRotate(angulo)

        // Cria um novo bitmap a partir do original já com a rotação aplicada
        return Bitmap.createBitmap(
            bitmapReduzido, 0, 0,
            bitmapReduzido.width,  bitmapReduzido.height,
            matrix, true
        )
    }

    fun  giraImagem(caminhoFoto: String, width: Int, height: Int): Bitmap? {
        val exif = ExifInterface(caminhoFoto);
        val orientacao = exif.getAttribute(ExifInterface.TAG_ORIENTATION)

        val codigoOrientacao = Integer.parseInt(orientacao)

        when (codigoOrientacao) {
            ExifInterface.ORIENTATION_NORMAL -> return abreImagem(caminhoFoto, 0F, width, height)
            ExifInterface.ORIENTATION_ROTATE_90 ->  return abreImagem(caminhoFoto, 90F, width, height)
            ExifInterface.ORIENTATION_ROTATE_180 -> return abreImagem(caminhoFoto, 180F, width, height)
            ExifInterface.ORIENTATION_ROTATE_270 -> return abreImagem(caminhoFoto, 270F, width, height)
        }

        return null
    }
}