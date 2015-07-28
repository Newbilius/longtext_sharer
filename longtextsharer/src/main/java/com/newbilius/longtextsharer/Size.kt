package com.newbilius.longtextsharer

public data class Size(val width:Int,val height:Int);

fun Int.x(height:Int):Size{
    return Size(this,height);
}