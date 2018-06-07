package cn.androidpi.common.crypto

import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

/**
 * Created by jastrelax on 2017/6/15.
 */

object HashUtils {

    private val HEX_DIGIT = "0123456789abcdef".toCharArray()

    /**
     * 字节数组转16进制字符串表示。
     *
     * @param data
     * @return
     */
    fun hexToString(data: ByteArray): String {
        val len = data.size
        val hexChars = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            val v = data[i].toInt() and 0xff
            hexChars[j++] = HEX_DIGIT[(v.ushr( 4) and 0x0F)]
            hexChars[j++] = HEX_DIGIT[v and 0x0F]
            i++
        }
        return String(hexChars)
    }

    /**
     * md5哈希函数。
     *
     * @param input
     * @return
     */
    fun md5(input: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            val result = md.digest(input.toByteArray())
            return hexToString(result)
        } catch (e: NoSuchAlgorithmException) {

        }

        return null
    }

    /**
     * sha256哈希函数。
     *
     * @param input
     * @return
     */
    fun sha256(input: String): String? {
        try {
            val sha = MessageDigest.getInstance("SHA-256")
            val result = sha.digest(input.toByteArray(charset("UTF-8")))
            return hexToString(result)
        } catch (e: NoSuchAlgorithmException) {

        } catch (e: UnsupportedEncodingException) {

        }

        return null
    }

    fun hmacSha256(key: String, input: String): String? {
        try {
            val algorithm = "HmacSHA256"
            val mac = Mac.getInstance(algorithm)
            val keySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), algorithm)
            mac.init(keySpec)
            val result = mac.doFinal(input.toByteArray(charset("UTF-8")))
            return hexToString(result)
        } catch (e: NoSuchAlgorithmException) {

        } catch (e: UnsupportedEncodingException) {

        } catch (e: InvalidKeyException) {

        }

        return null
    }
}
