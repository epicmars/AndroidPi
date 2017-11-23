package cn.androidpi.common.color

/**
 * Created by jastrelax on 2017/11/23.
 */
class ColorModel {

    companion object {

        fun hsvToRgb(hsv: HSV, alpha: Int) : Int {
            return hsv.toRGB(alpha).value()
        }
    }
}

/**
 * RGB
 * - Red: [0, 255]
 * - Green: [0, 255]
 * - Blue: [0, 255]
 */
class RGB {
    var alpha: Int = 0
    var red: Int = 0
    var green: Int = 0
    var blue: Int = 0

    fun value(): Int {
        return (alpha and 0xff shl 24) or (red and 0xff shl 16) or (green and 0xff shl 8) or (blue and 0xff)
    }
}

/**
 * HSV
 * - Hue: [0°, 360°]
 * - Saturation: [0, 1]
 * - Value: [0, 1]
 */
class HSV() {
    constructor(hue: Int, saturation: Float, value: Float): this() {
        assert(hue in 0 until 360)
        assert(saturation in 0f..1f)
        assert(value in 0f..1f)
        this.hue = hue
        this.saturation = saturation
        this.value = value
    }

    var hue: Int = 0
    var saturation: Float = 0f
    var value: Float = 0f

    fun set(hue: Int, saturation: Float, value: Float): HSV {
        assert(hue in 0 until 360)
        assert(saturation in 0f..1f)
        assert(value in 0f..1f)
        this.hue = hue
        this.saturation = saturation
        this.value = value

        return this
    }

    fun toRGB(alpha: Int): RGB {
        val chroma = value * saturation
        val h = hue/60 % 6
        val f = hue/60.0f - h
        val p = value - chroma
        val q = value - f * chroma
        val t = value - (1 - f) * chroma
        var v = when(h) {
            in 0 until 1 -> floatArrayOf( value,      t,     p)
            in 1 until 2 -> floatArrayOf(     q,  value,     p)
            in 2 until 3 -> floatArrayOf(     p,  value,     t)
            in 3 until 4 -> floatArrayOf(     p,      q, value)
            in 4 until 5 -> floatArrayOf(     t,      p, value)
            in 5 until 6 -> floatArrayOf( value,      p,     q)
            else -> floatArrayOf(0f, 0f, 0f)
        }
        val rgb = RGB()
        rgb.alpha = alpha
        rgb.red   = (v[0] * 255).toInt()
        rgb.green = (v[1] * 255).toInt()
        rgb.blue  = (v[2] * 255).toInt()
        return rgb
    }
}