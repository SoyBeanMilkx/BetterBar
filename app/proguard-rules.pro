# 保持 Xposed API 和模块核心代码不被移除
-keep class de.robv.android.xposed.** { *; }
-dontwarn de.robv.android.xposed.**

-keep class com.yuuki.betterbar.** { *; } # 替换为你的包名
-keepclassmembers class com.yuuki.betterbar.** {
    *;
}

# 保持反射调用中的类和方法不被移除
-keepclassmembers class * {
    public <methods>;
    public <fields>;
}

# 保持枚举类型不被移除
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 SharedPreferences 相关类不被移除
-keep class android.content.SharedPreferences { *; }

# 移除所有日志（减少体积）
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# 禁用混淆（不考虑安全性）
-dontobfuscate

# 启用最高级别的优化
-optimizationpasses 5
-allowaccessmodification
-dontpreverify