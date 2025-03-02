package com.yuuki.betterbar.utils

class TimeConvert {
    fun timeToShiChen(hour: Int, minute: Int): String {
        // 时辰对照表
        val ctime = mapOf(
            23 to "子", 0 to "子",
            1 to "丑", 2 to "丑",
            3 to "寅", 4 to "寅",
            5 to "卯", 6 to "卯",
            7 to "辰", 8 to "辰",
            9 to "巳", 10 to "巳",
            11 to "午", 12 to "午",
            13 to "未", 14 to "未",
            15 to "申", 16 to "申",
            17 to "酉", 18 to "酉",
            19 to "戌", 20 to "戌",
            21 to "亥", 22 to "亥"
        )

        // 获取对应的时辰
        val shichen = ctime[hour] ?: "未知"

        // 确定是“初”还是“正”
        val chuZheng = if (hour % 2 == 0) "正" else "初"

        // 根据分钟确定刻数
        val ke = when {
            minute < 15 -> "一"
            minute in 15..29 -> "二"
            minute in 30..44 -> "三"
            else -> "四"
        }

        return shichen + chuZheng + ":" + ke + "刻"
    }
}