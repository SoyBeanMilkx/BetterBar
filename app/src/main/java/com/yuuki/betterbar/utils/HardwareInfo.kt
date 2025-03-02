package com.yuuki.betterbar.utils

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class HardwareInfo{

    fun readTemperature(zone: Int): Float? {
        val path = "/sys/class/thermal/thermal_zone$zone/temp"
        return try {
            val temp = File(path).readText().trim().toFloat() / 1000.0f
            temp
        } catch (e: IOException) {
            null
        }
    }

    fun getCpuZones(): List<Int> {
        val thermalZonesPath = "/sys/class/thermal/"
        val thermalZonesDir = File(thermalZonesPath)
        val zones = mutableListOf<Int>()

        if (thermalZonesDir.exists() && thermalZonesDir.isDirectory) {
            try {
                thermalZonesDir.listFiles { file -> file.name.startsWith("thermal_zone") }?.forEach { file ->
                    val zoneIndex = file.name.removePrefix("thermal_zone").toIntOrNull()
                    if (zoneIndex != null) {
                        // 过滤出 CPU 热区，一般以 cpuss 前缀标识
                        val typeFile = File(file, "type")
                        if (typeFile.exists()) {
                            val type = typeFile.readText().trim()
                            if (type.startsWith("cpuss")) {
                                zones.add(zoneIndex)
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                // 处理读取文件时的异常
                e.printStackTrace()
            }
        }

        return zones
    }

    fun getCpuAverageTemperature(): String? {
        val cpuZones = getCpuZones()
        val temperatures = cpuZones.mapNotNull { readTemperature(it) }

        return if (temperatures.isNotEmpty()) {
            val averageTemp = temperatures.sum() / temperatures.size
            // 保留一位小数并添加单位℃
            String.format("%.1f℃", averageTemp)
        } else {
            null
        }
    }


    fun getBatteryCurrentNow(): String {
        return try {
            // 执行 'su' 命令以获得 root 权限，并读取电池电流
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "cat /sys/class/power_supply/battery/current_now"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val voltageNowString = reader.readLine()
            reader.close()

            // 检查进程退出码，确保命令执行成功
            if (process.waitFor() == 0 && voltageNowString != null) {
                val voltageNow = voltageNowString.toInt() / 1000.0f
                "$voltageNow mA"
            } else {
                "Error: Command failed or no voltage information available"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error: IOException occurred"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            "Error: NumberFormatException occurred"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: An unexpected error occurred"
        }
    }


    fun getBatteryVoltageNow(): String {
        return try {
            // 执行 'su' 命令以获得 root 权限，并读取电池电压
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "cat /sys/class/power_supply/battery/voltage_now"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val voltageNowString = reader.readLine()
            reader.close()

            // 检查进程退出码，确保命令执行成功
            if (process.waitFor() == 0 && voltageNowString != null) {
                val voltageNow = voltageNowString.toInt() / 1000.0f
                "$voltageNow mV"
            } else {
                "Error: Command failed or no voltage information available"
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error: IOException occurred"
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            "Error: NumberFormatException occurred"
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: An unexpected error occurred"
        }
    }


    // 获取电池功率
    fun getBatteryPowerNow(): String {
        return try {
            // 使用Root权限读取 uevent
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "cat /sys/class/power_supply/battery/uevent"))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            reader.close()

            if (process.waitFor() != 0) {
                return "Error: Command failed with exit code ${process.exitValue()}"
            }

            // 手动解析 uevent 输出
            val lines = output.split("\n")
            var currentNowString: String? = null
            var voltageNowString: String? = null

            for (line in lines) {
                if (line.startsWith("POWER_SUPPLY_CURRENT_NOW=")) {
                    currentNowString = line.removePrefix("POWER_SUPPLY_CURRENT_NOW=").trim()
                } else if (line.startsWith("POWER_SUPPLY_VOLTAGE_NOW=")) {
                    voltageNowString = line.removePrefix("POWER_SUPPLY_VOLTAGE_NOW=").trim()
                }
            }

            if (currentNowString != null && voltageNowString != null) {
                val currentNow = currentNowString.toFloat() / 1000000.0f // µA 转 A
                val voltageNow = voltageNowString.toFloat() / 1000000.0f // µV 转 V
                val power = currentNow * voltageNow
                String.format("%.2f W", power)
            } else {
                "N/A - current or voltage missing"
            }
        } catch (e: IOException) {
            "Error: IOException - ${e.message}"
        } catch (e: NumberFormatException) {
            "Error: NumberFormatException - ${e.message}"
        }
    }

}