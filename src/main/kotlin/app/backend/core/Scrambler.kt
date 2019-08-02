package app.backend.core

import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.Deflater
import java.util.zip.Inflater
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Scrambler {
  fun deflate(data: String): String {
    val minified = compress(data.toByteArray())

    val keyBytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    val ivBytes = byteArrayOf(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5)

    val key = SecretKeySpec(keyBytes, "AES")
    val ivSpec = IvParameterSpec(ivBytes)
    val cipher = Cipher.getInstance("AES/CTR/NoPadding")

    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
    val compressed = compress(cipher.doFinal(minified))
    return Base64.getEncoder().encodeToString(compressed)
  }

  fun inflate(data: String): String {
    val decodedBytes = Base64.getDecoder().decode(data.toByteArray())
    val inflated = decompress(decodedBytes)

    val keyBytes = byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    val ivBytes = byteArrayOf(5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5)

    val key = SecretKeySpec(keyBytes, "AES")
    val ivSpec = IvParameterSpec(ivBytes)
    val cipher = Cipher.getInstance("AES/CTR/NoPadding")

    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
    val bOut = ByteArrayOutputStream()
    val cOut = CipherOutputStream(bOut, cipher)
    cOut.write(inflated)
    cOut.close()
    val myBytes = bOut.toByteArray()
    val inflated2 = decompress(myBytes)
    return String(inflated2)
  }

  private fun decompress(compressed: ByteArray): ByteArray {
    val inflater = Inflater()
    inflater.setInput(compressed, 0, compressed.size)
    val result = ByteArray(compressed.size * 10)
    val len = inflater.inflate(result)
    inflater.end()
    return result.sliceArray(0 until len)
  }

  private fun compress(expanded: ByteArray): ByteArray {
    val deflater = Deflater(9, false)
    deflater.setInput(expanded, 0, expanded.size)
    deflater.finish()
    val output = ByteArray(expanded.size * 10)
    val len = deflater.deflate(output)
    deflater.end()
    return output.sliceArray(0 until len)
  }
}
